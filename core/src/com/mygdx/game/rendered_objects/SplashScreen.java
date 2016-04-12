package com.mygdx.game.rendered_objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Christopher on 4/12/2016.
 */
public class SplashScreen implements RenderedObject {

    private Texture splashScreenTexture;
    private SpriteBatch batch;

    public SplashScreen(SpriteBatch batch) {
        splashScreenTexture = new Texture("SplashScreen.png");
        this.batch = batch;
    }

    @Override
    public void draw() {
        batch.draw(splashScreenTexture, 0, 0);
    }
}
