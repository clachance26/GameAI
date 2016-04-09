package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.RenderedObject;

/**
 * This is a type of alien called a feeder
 * There are 3 breeds of feeders, each with a distinct hive
 * A feeder has these properties:
 *      - spawn at a fixed spawn point on the map (each breed has its own spawn point)
 *      - navigate using A* from their spawn point to their nest
 *      - will attack the character if it gets within a certain range
 */
public class Feeder extends GameObject implements RenderedObject {

    private FeederBreedEnum breed;

    public Feeder(String imageName, SpriteBatch batch, float x, float y, float ang, int width, int height, FeederBreedEnum breed) {

        super(imageName, batch, x, y, ang, width, height);
        this.breed = breed;
    }

    @Override
    public void draw() {
        batch.draw(texture, position.x, position.y);

    }

    public enum FeederBreedEnum{
        BREED_A, BREED_B, BREED_C;
    }

}





