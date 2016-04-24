package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.GameAI;
import com.mygdx.game.application_mode.ApplicationModeEnum;
import com.mygdx.game.application_mode.ApplicationModeSingleton;
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
    public boolean alive = true;
    private GameObject collisionObject;
    private TextureRegion textureRegion;

    public Hunter(SpriteBatch batch, float x, float y, float ang){

        super(IMAGE_NAME, batch, x, y, ang, WIDTH, HEIGHT);
        textureRegion = new TextureRegion(texture);
    }

    @Override
    public void draw() {
        if (alive) {
            batch.draw(textureRegion, position.x, position.y, height / 2, width / 2, width, height, 1, 1, (-angle + 270 % 360));
        }
    }

    @Override
    public void move(Vector2 vel, List<GameObject> objects) {
        checkForCollisions(objects);
        this.position.add(vel);
        //If we are playing the game and we collide with a black hole or an alien, we lose
        if (ApplicationModeSingleton.getInstance().getApplicationMode().equals(ApplicationModeEnum.PLAY) &&
                (collisionObject instanceof BlackHole)){
            alive = false;
        }
    }

    @Override
    void moveFromGravity(Vector2 velocity, List<GameObject> objects) {
        move(velocity, objects);
    }

    /**
     * makes sure we do not go through agents and walls
     */
    private boolean checkForCollisions(List<GameObject> objects) {

        for(GameObject object : objects) {

            if (!(object instanceof Character)) {

                if (this.collidesWith(object)) {
                    collisionObject = object;
                    return true;
                }
            }
        }
        return false;
    }
}
