package com.mygdx.game.game_objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.GameAI;
import com.mygdx.game.Seek;
import com.mygdx.game.application_mode.ApplicationModeEnum;
import com.mygdx.game.application_mode.ApplicationModeSingleton;
import com.mygdx.game.navigation.AStar;
import com.mygdx.game.navigation.NavigationGraph;
import com.mygdx.game.navigation.NavigationNode;
import com.mygdx.game.rendered_objects.RenderedObject;
import com.mygdx.game.sensor_implementation.AdjacentAgentSensor;

import java.awt.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.Deque;
import java.util.List;

/**
 * This is a type of alien called a feeder
 * There are 3 breeds of feeders, each with a distinct hive
 * A feeder has these properties:
 *      - spawn at a fixed spawn point on the map (each breed has its own spawn point)
 *      - navigate using A* from their spawn point to their nest
 *      - will attack the character if it gets within a certain range
 */
public class Feeder extends Agent implements RenderedObject {

    //Image used for Feeder texture
    private static final String IMAGE_NAME = "feeder.png";
    private static final int WIDTH  = 25;
    private static final int HEIGHT  = 25;
    public boolean triggered = false;
    private GameObject collisionObject;
    private TextureRegion textureRegion;
    private FeederBreedEnum breed;
    private AdjacentAgentSensor aaSensor;
    private Character characterToChase;
    private Deque<NavigationNode> aStarShortestPath;
    private BlackHole[] blackHoles;
    private Seek seek;
    private AStar aStar;
    private NavigationGraph navigationGraph;

    public Feeder(SpriteBatch batch, float x, float y, float ang, FeederBreedEnum breed, Character characterToChase,
                  List<GameObject> gameObjects, Point end, BlackHole[] blackHoles){
        super(IMAGE_NAME, batch, x, y, ang, WIDTH, HEIGHT);
        aaSensor = new AdjacentAgentSensor(this, 200);
        textureRegion = new TextureRegion(texture);
        this.breed = breed;
        this.characterToChase = characterToChase;
        this.blackHoles = blackHoles;
        this.seek = new Seek();
        this.aStar = new AStar();
        this.navigationGraph = new NavigationGraph();
        this.navigationGraph.update(gameObjects, new Rectangle(0,0,900,600));

        if(!aStar.getIsDone()) {
            Point feederPosition = new Point((int) (position.x + WIDTH / 2),
                    (int) (position.y + HEIGHT / 2));
            feederPosition = NavigationNode.findClosestNavNode(feederPosition);
            end = NavigationNode.findClosestNavNode(end);
            AStar.setNavigationGraph(navigationGraph);
            aStarShortestPath = aStar.evaluateAStar(feederPosition, end);
//                                aStarShortestPath = AStar.evaluateAStar(feederPosition, end);
        }

    }

    @Override
    public void draw() {
            batch.draw(textureRegion, position.x, position.y, height / 2, width / 2, width, height, 1, 1, (-angle + 270 % 360));

    }

    @Override
    public void move(Vector2 vel, List<GameObject> objects) {
        checkForCollisions(objects);
        position.add(vel);
    }

    @Override
    public void move(List<GameObject> objects) {
        checkForCollisions(objects);
        aaSensor.detect(Collections.singletonList(characterToChase));
        //If we are playing the game and we collide with a black hole or an alien, we lose
        if (ApplicationModeSingleton.getInstance().getApplicationMode().equals(ApplicationModeEnum.PLAY) &&
                aaSensor.getAdjacentObjects().size() > 0){
            triggered = true;
        }

        if (aStar.getIsDone()) {
//            if (!seek.isDone()) {
                Vector2 seekToVector = new Vector2(aStarShortestPath.peek().getLocation().x,
                        aStarShortestPath.peek().getLocation().y);
                seek.seek(this, seekToVector, Arrays.asList(blackHoles));
//            }
//            else
//            {
                if (aStarShortestPath.size() > 3)
                {
                    aStarShortestPath.remove();
                    seek.setDone(false);
                }
//                else if (aStarShortestPath.size() < 2)
//                {
//                    seek.setDone(true);
//                }
//            }
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

    public enum FeederBreedEnum{
        BREED_A, BREED_B, BREED_C;
    }

}





