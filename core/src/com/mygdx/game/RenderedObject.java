package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;

/**
 * Created by Christopher on 3/8/2016.
 * An interface for creating a rendered object
 * This is for all of the objects in the game that we want to be able to render to the screem
 * Draw is the function used to render the object
 */
public interface RenderedObject {

    void draw();
}
