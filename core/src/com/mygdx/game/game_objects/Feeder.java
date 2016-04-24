package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameAI;
import com.mygdx.game.application_mode.ApplicationModeEnum;
import com.mygdx.game.application_mode.ApplicationModeSingleton;
import com.mygdx.game.rendered_objects.RenderedObject;
import com.mygdx.game.sensor_implementation.AdjacentAgentSensor;

import java.util.Collections;
import java.util.List;

/**
 * This is a type of alien called a feeder
 * There are 3 breeds of feeders, each with a distinct hive
 * A feeder has these properties:
 *      - spawn at a fixed spawn point on the map (each breed has its own spawn point)
 *      - navigate using A* from their spawn point to their nest
 *      - will attack the character if it gets within a certain range
 */
public class Feeder extends Agent implements RenderedObject {

    //Image used for Feeder texture
    private static final String IMAGE_NAME = "feeder.png";
    private static final int WIDTH  = 25;
    private static final int HEIGHT  = 25;
    public boolean triggered = false;
    private GameObject collisionObject;
    private TextureRegion textureRegion;
    private FeederBreedEnum breed;
    private AdjacentAgentSensor aaSensor;
    private Character characterToChase;

    public Feeder(SpriteBatch batch, float x, float y, float ang, FeederBreedEnum breed, Character characterToChase){
        super(IMAGE_NAME, batch, x, y, ang, WIDTH, HEIGHT);
        aaSensor = new AdjacentAgentSensor(this, 200);
        textureRegion = new TextureRegion(texture);
        this.breed = breed;
        this.characterToChase = characterToChase;
    }

    @Override
    public void draw() {
            batch.draw(textureRegion, position.x, position.y, height / 2, width / 2, width, height, 1, 1, (-angle + 270 % 360));

    }

    @Override
    public void move(Vector2 vel, List<GameObject> objects) {
        checkForCollisions(objects);
        this.position.add(vel);
        aaSensor.detect(Collections.singletonList(characterToChase));
        //If we are playing the game and we collide with a black hole or an alien, we lose
        if (ApplicationModeSingleton.getInstance().getApplicationMode().equals(ApplicationModeEnum.PLAY) &&
                aaSensor.getAdjacentObjects().size() > 0){
            triggered = true;
        }
    }

    @Override
    void moveFromGravity(Vector2 velocity, List<GameObject> objects) {
        move(velocity, objects);
    }

    /**
     * makes sure we do not go through agents and walls
     */
    private boolean checkForCollisions(List<GameObject> objects) {

        for(GameObject object : objects) {

            if (!(object instanceof Character)) {

                if (this.collidesWith(object)) {
                    collisionObject = object;
                    return true;
                }
            }
        }
        return false;
    }

    public enum FeederBreedEnum{
        BREED_A, BREED_B, BREED_C;
    }

}





