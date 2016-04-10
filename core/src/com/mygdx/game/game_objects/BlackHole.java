package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.RenderedObject;

/**
 * The black hole object that your character spawns
 * To make it so that you do not "collide" with the black hole until you hit the center, we will specify the width and
 * height to be smaller than the actual .png file
 */
public class BlackHole extends GameObject implements RenderedObject{

    //Image used for black hole texture
    private static final String IMAGE_NAME = "blackHoleTemp.png";
    //Bounds for the center of the black hole (where if you hit it you will die)
    private static final int WIDTH   = 25;
    private static final int HEIGHT  = 25;
    //Bounds for the texture of the black hole (used to draw it)
    private static final int TEXTURE_WIDTH   = 100;
    private static final int TEXTURE_HEIGHT  = 76;

    public BlackHole(SpriteBatch batch, float x, float y, float ang){

        super(IMAGE_NAME, batch, x, y, ang, WIDTH, HEIGHT);
    }

    @Override
    public void draw() {
        batch.draw(texture, position.x - ((TEXTURE_WIDTH/2)  - (WIDTH/2)),
                            position.y - ((TEXTURE_HEIGHT/2) - (HEIGHT/2)));
    }
}
