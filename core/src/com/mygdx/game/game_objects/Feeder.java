package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.rendered_objects.RenderedObject;

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

    private FeederBreedEnum breed;

    public Feeder(SpriteBatch batch, float x, float y, float ang, FeederBreedEnum breed) {

        super(IMAGE_NAME, batch, x, y, ang, WIDTH, HEIGHT);
        this.breed = breed;
    }

    @Override
    public void draw() {
        batch.draw(texture, position.x, position.y);
    }

    @Override
    void move(Vector2 vel, List<GameObject> objects) {

    }

    @Override
    void moveFromGravity(Vector2 velocity, List<GameObject> objects) {

    }

    public enum FeederBreedEnum{
        BREED_A, BREED_B, BREED_C;
    }

}





