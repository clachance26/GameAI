package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

/**
 * The abstract class that all game objects extend
 * This is used to store the basic fields that all game objects have in common (such as dimensions, position, and velocity)
 */
public abstract class GameObject {

    // Size of object.
    protected int height;
    protected int width;

    protected SpriteBatch batch;
    protected Texture texture;

    // Position of the object
    protected Vector2 position;
    // Current angle of the object
    protected float angle;

    public GameObject(String imageName, SpriteBatch batch, float x, float y, float angle, int width, int height) {

        //Get the sprite batch so we can draw this object later
        this.batch = batch;
        //Create the texture for this object
        texture = new Texture(imageName);

        // Set the position of this character
        this.position = new Vector2(x,y);
        this.angle = angle;
        this.height = height;
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public Texture getTexture() {
        return texture;
    }

    public void setTexture(Texture texture) {
        this.texture = texture;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
    }
}
