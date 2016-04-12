package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.rendered_objects.RenderedObject;

import java.util.List;

/**
 * This is a type of alien called a hunter
 * A hunter will spawn at a location on the edge of the map and will always seek towards the character and attack
 */
public class Hunter extends Agent implements RenderedObject{

    //Image used for Hunter texture
    private static final String IMAGE_NAME = "hunter.png";
    private static final int WIDTH  = 25;
    private static final int HEIGHT  = 25;

    public Hunter(SpriteBatch batch, float x, float y, float ang){

        super(IMAGE_NAME, batch, x, y, ang, WIDTH, HEIGHT);
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
}
