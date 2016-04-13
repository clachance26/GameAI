package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * Agents are game objects that are able to move and are affected by the gravity of the black holes
 */
public abstract class Agent extends GameObject {

    // Velocity of the character
    protected Vector2 velocity;

    public Agent(String imageName, SpriteBatch batch, float x, float y, float angle, int width, int height) {

        super(imageName, batch, x, y, angle, width, height);

        // Set starting velocity to 0;
        velocity = new Vector2();
    }

    abstract void move(Vector2 vel, List<GameObject> objects);

    abstract void moveFromGravity(Vector2 velocity, List<GameObject> objects);

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}
