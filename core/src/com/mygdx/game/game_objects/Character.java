package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.mygdx.game.RenderedObject;
import com.mygdx.game.sensor_implementation.AdjacentAgentSensor;
import com.mygdx.game.sensor_implementation.PieSliceSensor;
import com.mygdx.game.sensor_implementation.WallSensor;

import java.util.ArrayList;
import java.util.List;

/**
 * The character is the movable agent that is controlled by the user
 */
public class Character extends GameObject implements RenderedObject{

    // Size of character.
    private static final int SIZE  = 25;
    // Speed factor.
    private static final float SPEED_FACTOR = 0.1f;
    //Max speed
    private static final float SPEED_LIMIT = 15;
    // Decay speed.
    private static final float FORWARD_DECAY = 0.6f;
    private TextureRegion textureRegion;

    //Sensors
    private PieSliceSensor psSensor = new PieSliceSensor(this, 0, 0);
    private AdjacentAgentSensor aaSensor;
    private List<WallSensor> wallSensors;

    /**
     * creates the character and initializes its sensors
     * @param x position
     * @param y position
     * @param ang its angle heading
     */
    public Character(String imageName, SpriteBatch batch, float x, float y, float ang) {

        super(imageName, batch, x, y, ang, SIZE, SIZE);
        textureRegion = new TextureRegion(texture);

        //Initialize sensors
        aaSensor = new AdjacentAgentSensor(this);
        wallSensors = new ArrayList<>();
        wallSensors.add(new WallSensor(0, this, "front"));
        wallSensors.add((new WallSensor(90, this, "right")));
        wallSensors.add(new WallSensor(270, this, "left"));
    }

    /**
     * Draws the character to the screen
     */
    @Override
    public void draw() {
        batch.draw(textureRegion, position.x, position.y, SIZE / 2, SIZE / 2, SIZE, SIZE, 1, 1, (-ang + 270 % 360));
    }

    /**
     * Moves the character based on the forward and turn parameters passed
     * Also handles collisions using the bounds of the world and the other game objects
     * @param forward whether or not to move forward or backward
     * @param turn whether we should turn
     * @param bounds bounds of the game world (edges of the world)
     * @param objects game objects
     */
    public void moveFromKeyboardControls(float forward, float turn, Rectangle bounds, List objects){
        processTurn(turn);
        if (forward != 0.0f) {
            // Forward key pressed. increase speed.
            velocity.add(forward * (float)Math.cos(Math.toRadians (ang)) * SPEED_FACTOR,
                    forward * (float)-Math.sin (Math.toRadians (ang)) * SPEED_FACTOR);
            if (velocity.x > SPEED_LIMIT) {
                velocity.x = SPEED_LIMIT;
            }
            if (velocity.x < -SPEED_LIMIT) {
                velocity.x = -SPEED_LIMIT;
            }
            if (velocity.y > SPEED_LIMIT) {
                velocity.y = SPEED_LIMIT;
            }
            if (velocity.y < -SPEED_LIMIT) {
                velocity.y = -SPEED_LIMIT;
            }
        } else {
            // Slow the character down over time.
            velocity.scl(FORWARD_DECAY);
        }

        if (ang > 360)
            ang -= 360;
        if (ang < 0)
            ang += 360;

        move(velocity, objects);
        adjustToBounds(bounds);
    }

    public void move(Vector2 vel, List objects) {

        // Move the character
        if(!checkForCollisions(objects)) {
            this.position.add(vel);
        }
        else{
            vel.x = vel.x*-2;
            vel.y = vel.y*-2;
            if (vel.x > SPEED_LIMIT) {
                vel.x = SPEED_LIMIT;
            }
            if (vel.x < -SPEED_LIMIT) {
                vel.x = -SPEED_LIMIT;
            }
            if (vel.y > SPEED_LIMIT) {
                vel.y = SPEED_LIMIT;
            }
            if (vel.y < -SPEED_LIMIT) {
                vel.y = -SPEED_LIMIT;
            }

            this.position.add(vel);
        }
    }

    public void evaluateSensors(List objects) {

        evaluateAASensor(objects);

        psSensor.resetSensorResults();
        evaluatePieSliceSensor(objects, 0, 90);
        evaluatePieSliceSensor(objects, 90, 180);
        evaluatePieSliceSensor(objects, 180, 270);
        evaluatePieSliceSensor(objects, 270, 360);

        for (int i=0; i<wallSensors.size(); i++) {
            evaluateWallSensor(wallSensors.get(i), objects);
        }
    }

    private void processTurn(float turn) {
		this.ang -= turn*5;
    }

    private void adjustToBounds(Rectangle bounds) {
        if (position.x <= bounds.x) {
            velocity.x = 0;
            position.x = bounds.x;
        } else if (position.x + SIZE >= bounds.width) {
            velocity.x = 0;
            position.x = bounds.width - SIZE - 1.0f;
        }

        if (position.y <= bounds.y) {
            velocity.y = 0;
            position.y = bounds.y;
        } else if (position.y + SIZE >= bounds.height) {
            velocity.y = 0;
            position.y = bounds.height - SIZE - 1.0f;
        }
    }

    /**
     * makes sure we do not go through agents and walls
     */
    private boolean checkForCollisions(List<GameObject> objects) {
        for(GameObject object : objects)
        {
            Rectangle characterBounds = new Rectangle(this.getPosition().x, this.getPosition().y, this.getSize(), this.getSize());
            Rectangle objectBounds = new Rectangle(object.getPosition().x, object.getPosition().y, object.getWidth(), object.getHeight());
            if(Intersector.overlaps(characterBounds, objectBounds))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * performs a scan with the adjacent agent sensor
     */
    private void evaluateAASensor(List<Feeder> objects) {aaSensor.detect(objects);
    }

    /**
     * performs a scan with the wall sensor
     */
    private void evaluateWallSensor(WallSensor wallSensor, List<Feeder> objects) {
        wallSensor.Sense(objects);
    }

    /**
     * performs a scan with the pie slice sensor
     */
    private void evaluatePieSliceSensor(List<GameObject> objects, int min, int max){
        psSensor.setDegreesMin(min);
        psSensor.setDegreesMax(max);
        psSensor.detect(objects);
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 value) {
        position.set(value);
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 value) {
        velocity.set(value);
    }

    public float getAngle() {
        return ang;
    }

    public void setAngle(float value) {
        ang = value;
    }

    public float getSize() {
        return SIZE;
    }

    public List<WallSensor> getWallSensors() {
        return wallSensors;
    }

    public AdjacentAgentSensor getAaSensor() {
        return aaSensor;
    }

    public PieSliceSensor getPsSensor() {
        return psSensor;
    }

    public static int getSIZE() {
        return SIZE;
    }
}
