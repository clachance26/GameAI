package com.mygdx.game.sensor_implementation;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.game_objects.Character;
import com.mygdx.game.game_objects.Feeder;

import java.util.ArrayList;
import java.util.List;

/**
 * Senses all surroundings for agents (up to the specified range)
 */
public class AdjacentAgentSensor {

    private static final int RANGE = 200;
    private Character character;
    private Vector2 adjacent = new Vector2();
    private Vector2 characterCenter = new Vector2();
    private List<AdjacentObject> adjacentObjects;

    public AdjacentAgentSensor(Character character){
        this.character = character;
    }

    /**
     * called by the character when it wants to scan surroundings
     * @param objects list of object in the game
     */
    public void detect(List<Feeder> objects) {

        adjacentObjects = new ArrayList<>();

        for(Feeder object : objects)
        {
//            if(object.getIsAgent())
//            {
                adjacent.set(object.getPosition().x + (object.getWidth()/2), object.getPosition().y + (object.getHeight()/2));
                characterCenter.set(character.getPosition().x + (character.getSize()/2), character.getPosition().y + (character.getSize()/2));
                if(adjacent.dst(character.getPosition()) < RANGE)
                {
                    double degrees = Math.atan2(
                            characterCenter.y - adjacent.y,
                            characterCenter.x - adjacent.x
                    ) * 180.0d / Math.PI;
                    degrees += 180;
                    degrees += character.getAngle();
                    if(degrees < 0)
                    {
                        degrees += 360;
                    }
                    degrees = degrees % 360;

                    adjacentObjects.add(new AdjacentObject(degrees, character.getPosition().dst(adjacent)));
//                    System.out.println("Adjacent Agent detected at " + degrees + " degrees "
//                            + character.getPosition().dst(adjacent) + " pixels away.");
                }
//            }
        }
    }

    public List<AdjacentObject> getAdjacentObjects() {
        return adjacentObjects;
    }
}
