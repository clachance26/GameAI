package com.mygdx.game;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.application_mode.ApplicationModeEnum;
import com.mygdx.game.application_mode.ApplicationModeSingleton;
import com.mygdx.game.debug.Debug;
import com.mygdx.game.navigation.AStar;
import com.mygdx.game.navigation.NavigationNode;

import java.awt.*;
import java.util.List;


/**
 * Created by Christopher on 3/8/2016.
 */
public class MyInputProcessor implements InputProcessor {

    // Forward speed
    private float forward;
    // Turning speed
    private float turning;
    //The locations of mouse clicks
    private Point click = new Point();
    private Debug debug;
    private boolean clicked = false;
    private boolean newNavigationClick = false;
    //Whether we want to display the navigation nodes on the screen
    private boolean showNavigationNodes = false;

    public MyInputProcessor(Debug debug) {
        this.debug = debug;
    }

    @Override
    public boolean keyDown(int keycode) {

        switch (keycode) {

            case Input.Keys.UP:
                if (forward == -1) {
                    forward = 0;
                }
                else {
                    forward = 1;
                }
                return true;

            case Input.Keys.DOWN:
                if (forward == 1) {
                    forward = 0;
                }
                else {
                    forward = -1;
                }
                return true;

            case Input.Keys.LEFT:
                if (turning == -1) {
                    turning = 0;
                }
                else {
                    turning = 1;
                }
                return true;

            case Input.Keys.RIGHT:
                if (turning == 1) {
                    turning = 0;
                }
                else {
                    turning = -1;
                }
                return true;

            case Input.Keys.N:
                showNavigationNodes = !showNavigationNodes;
                return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {

        switch (keycode) {

            case Input.Keys.UP:
                if (forward == 0) {
                    forward = -1;
                }
                else {
                    forward = 0;
                }
                return true;

            case Input.Keys.DOWN:
                if (forward == 0) {
                    forward = 1;
                }
                else {
                    forward = 0;
                }
                return true;

            case Input.Keys.LEFT:
                if (turning == 0) {
                    turning = -1;
                }
                else {
                    turning = 0;
                }
                return true;

            case Input.Keys.RIGHT:
                if (turning == 0) {
                    turning = 1;
                }
                else {
                    turning = 0;
                }
                return true;
        }


        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        ApplicationModeEnum appMode = ApplicationModeSingleton.getInstance().getApplicationMode();

        //This listener has the game world origin at the top left while the LibGDX spritebatch draw function has it in the bottom left
        //So we must convert the y values to start at the bottom (using the height of the game as 600)
        screenY = 600 - screenY;

        //The current application mode will determine how we handle a click
        switch (appMode) {

            case SETUP:
                click.setLocation(screenX, screenY);
                break;
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }

    public boolean isShowNavigationNodes() {
        return showNavigationNodes;
    }

    public float getForward() {
        return forward;
    }

    public float getTurning() {
        return turning;
    }

    public Point getClick() {
        return click;
    }

    public boolean isClicked() {
        return clicked;
    }

    public boolean isNewNavigationClick() {
        return newNavigationClick;
    }

    public void setNewNavigationClick(boolean newNavigationClick) {
        this.newNavigationClick = newNavigationClick;
    }
}