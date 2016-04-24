package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.rendered_objects.RenderedObject;
import com.mygdx.game.sensor_implementation.AdjacentAgentSensor;
import com.mygdx.game.sensor_implementation.AdjacentObject;

import java.util.List;

/**
 * The nest that the feeders will want to migrate to (to feed)
 * These nests are randomly placed at the start of the map
 * There is one nest for each breed of feeder
 * The number of breeds of feeders is dependent on the game difficulty
 */
public class FeederNest extends GameObject implements RenderedObject {

    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final int MIN_DISTANCE_BETWEEN_NESTS = 150;

    public Feeder.FeederBreedEnum breed;
    AdjacentAgentSensor nestPlacementSensor;

    public FeederNest(String imageName, SpriteBatch batch, float x, float y, float angle, Feeder.FeederBreedEnum breed) {
        super(imageName, batch, x, y, angle, WIDTH, HEIGHT);

        this.breed = breed;
        nestPlacementSensor = new AdjacentAgentSensor(this, MIN_DISTANCE_BETWEEN_NESTS);
    }

    @Override
    public void draw() {
        batch.draw(texture, getPosition().x, getPosition().y, width, height);
    }

    /**
     * Checks to see if we have placed our nest in a valid position
     * If the nest is too close to another nest, or it is on top of the character, that will be invalid
     * @param objects game objects
     * @return if the placement is valid
     */
    public boolean isValidPlacement(List<GameObject> objects) {

        List<AdjacentObject> adjacentObjects;
        nestPlacementSensor.detect(objects);
        adjacentObjects = nestPlacementSensor.getAdjacentObjects();

        for (AdjacentObject adjacentObject : adjacentObjects) {
            if (adjacentObject.getDetectedObject() instanceof FeederNest) {
                return false;
            }
            if (adjacentObject.getDetectedObject() instanceof Character) {
                if (this.collidesWith(adjacentObject.getDetectedObject())) {
                    return false;
                }
            }
        }

        return true;
    }

    public static int getWIDTH() {
        return WIDTH;
    }

    public static int getHEIGHT() {
        return HEIGHT;
    }
}
