package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.rendered_objects.RenderedObject;
import com.mygdx.game.sensor_implementation.AdjacentAgentSensor;
import com.mygdx.game.sensor_implementation.AdjacentObject;

import java.util.List;

/**
 * The black hole object that your character spawns
 * To make it so that you do not "collide" with the black hole until you hit the center, we will specify the width and
 * height to be smaller than the actual .png file
 */
public class BlackHole extends GameObject implements RenderedObject{

    //Image used for black hole texture
    private static final String IMAGE_NAME = "blackHoleTemp.png";
    //Bounds for the center of the black hole (where if you hit it you will die)
    private static final int WIDTH   = 10;
    private static final int HEIGHT  = 10;
    private static final int VALID_PLACEMENT_RANGE = 100;

    //How strong the gravitational pull of the black hole is
    private float gravitationalPullFactor = 35;
    private int gravitationalRange = 150;

    //Used for detecting nearby objects to add gravitational pull to
    private AdjacentAgentSensor gravitySensor;
    private AdjacentAgentSensor placementSensor;

    public BlackHole(SpriteBatch batch, float x, float y, float ang){

        super(IMAGE_NAME, batch, x, y, ang, WIDTH, HEIGHT);
        gravitySensor = new AdjacentAgentSensor(this, gravitationalRange);
        placementSensor = new AdjacentAgentSensor(this, VALID_PLACEMENT_RANGE);
    }

    @Override
    public void draw() {
        batch.draw(texture, position.x - ((texture.getWidth() / 2) - (WIDTH / 2)),
                position.y - ((texture.getHeight() / 2) - (HEIGHT / 2)));
    }

    public void performGravitationalPull(List<GameObject> gameObjects) {

        gravitySensor.detect(gameObjects);
        List<AdjacentObject> adjacentObjects = gravitySensor.getAdjacentObjects();

        for (AdjacentObject adjacentObject : adjacentObjects) {

            if (adjacentObject.getDetectedObject() instanceof Agent) {

                Agent detectedObject = (Agent) adjacentObject.getDetectedObject();
                Vector2 blackHolePosition = new Vector2(this.getPosition());
                Vector2 objectPosition = new Vector2(detectedObject.getPosition());
                float gravitationalPullStrength;

                objectPosition.add(detectedObject.getWidth() / 2, detectedObject.getHeight() / 2);
                blackHolePosition.add(this.getWidth() / 2, this.getHeight() / 2);

                gravitationalPullStrength = gravitationalPullFactor / (float) (adjacentObject.getDist());

                Vector2 gravitationalPull = new Vector2(blackHolePosition);
                gravitationalPull.sub(objectPosition).nor();
                gravitationalPull.scl(gravitationalPullStrength);
                //detectedObject.setVelocity(detectedObject.getVelocity().add(gravitationalPull));
                detectedObject.moveFromGravity(gravitationalPull, gameObjects);
            }
        }
    }

    public boolean isValidPlacement(List<GameObject> objects) {

        List<AdjacentObject> adjacentObjects;
        placementSensor.detect(objects);
        adjacentObjects = placementSensor.getAdjacentObjects();

        if (this.position.x < 100 || this.position.x > 800 || this.position.y < 100 || this.position.y > 500) {
            return false;
        }

        for (AdjacentObject adjacentObject : adjacentObjects) {

            GameObject detectedObject = adjacentObject.getDetectedObject();
            if (detectedObject instanceof FeederNest || detectedObject instanceof BlackHole) {
                return false;
            }
            if (detectedObject instanceof Character) {
                if (this.collidesWith(detectedObject)) {
                    return false;
                }
            }
        }

        return true;
    }
}
