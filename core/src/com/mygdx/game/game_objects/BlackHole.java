package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.RenderedObject;

/**
 * The black hole object that your character spawns
 */
public class BlackHole extends GameObject implements RenderedObject{

    //Image used for black hole texture
    private static final String IMAGE_NAME = "blackHoleTemp.png";
    private static final int WIDTH  = 100;
    private static final int HEIGHT  = 76;

    public BlackHole(SpriteBatch batch, float x, float y, float ang){

        super(IMAGE_NAME, batch, x, y, ang, WIDTH, HEIGHT);
    }

    @Override
    public void draw() {
        batch.draw(texture, position.x, position.y);
    }
}
