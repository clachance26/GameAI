package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Christopher on 4/8/2016.
 */
public abstract class GameObject {

    // Size of object.
    protected int height;
    protected int width;

    protected SpriteBatch batch;
    protected Texture texture;

    // Position of the object
    protected Vector2 position;
    // Velocity of the character
    protected Vector2 velocity;
    // Current angle of the object
    protected float ang;

    public GameObject(String imageName, SpriteBatch batch, float x, float y, float ang, int width, int height) {

        //Get the sprite batch so we can draw this object later
        this.batch = batch;
        //Create the texture for this object
        texture = new Texture(imageName);

        // Set the position of this character
        this.position = new Vector2(x,y);
        this.ang = ang;
        this.height = height;
        this.width = width;

        // Set starting velocity to 0;
        velocity = new Vector2();
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

    public float getAng() {
        return ang;
    }

    public void setAng(float ang) {
        this.ang = ang;
    }

    public Vector2 getVelocity() {
        return velocity;
    }

    public void setVelocity(Vector2 velocity) {
        this.velocity = velocity;
    }
}
