package com.mygdx.game.sensor_implementation;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.game_objects.Character;
import com.mygdx.game.game_objects.GameObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Senses all surroundings for agents (up to the specified range)
 */
public class AdjacentAgentSensor {

    private GameObject sensingObject;
    private Vector2 adjacent = new Vector2();
    private Vector2 characterCenter = new Vector2();
    private List<AdjacentObject> adjacentObjects;
    private int range;

    public AdjacentAgentSensor(GameObject sensingObject, int range){
        this.sensingObject = sensingObject;
        this.range = range;
    }

    /**
     * called by the sensingObject when it wants to scan surroundings
     * @param objects list of object in the game
     */
    public void detect(List<GameObject> objects) {

        adjacentObjects = new ArrayList<>();

        for(GameObject object : objects)
        {
//            if(object.getIsAgent())
//            {
                adjacent.set(object.getPosition().x + (object.getWidth()/2), object.getPosition().y + (object.getHeight()/2));
                characterCenter.set(sensingObject.getPosition().x + (sensingObject.getWidth()/2), sensingObject.getPosition().y + (sensingObject.getHeight()/2));
                if(adjacent.dst(sensingObject.getPosition()) < range)
                {
                    double degrees = Math.atan2(
                            characterCenter.y - adjacent.y,
                            characterCenter.x - adjacent.x
                    ) * 180.0d / Math.PI;
                    degrees += 180;
                    degrees += sensingObject.getAngle();
                    if(degrees < 0)
                    {
                        degrees += 360;
                    }
                    degrees = degrees % 360;

                    adjacentObjects.add(new AdjacentObject(object, degrees, sensingObject.getPosition().dst(adjacent)));
//                    System.out.println("Adjacent Agent detected at " + degrees + " degrees "
//                            + sensingObject.getPosition().dst(adjacent) + " pixels away.");
                }
//            }
        }
    }

    public List<AdjacentObject> getAdjacentObjects() {
        return adjacentObjects;
    }
}
