package com.mygdx.game.rendered_objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Christopher on 4/12/2016.
 */
public class GameOverScreen implements RenderedObject{

    private Texture gameOverScreenTexture;
    private SpriteBatch batch;

    public GameOverScreen(SpriteBatch batch) {
        gameOverScreenTexture = new Texture("gameOver.png");
        this.batch = batch;
    }

    @Override
    public void draw() {
        batch.draw(gameOverScreenTexture, 0, 0);
    }
}
