package com.mygdx.game.navigation;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.game_objects.*;
import com.mygdx.game.game_objects.Character;

import java.awt.*;
import java.util.List;

/**
 * The data structure to hold the graph of navigation nodes
 * Since we are using a dense mesh of navigation nodes, our graph is just going to be a 2d array
 */
public class NavigationGraph {

    //Note: to change these constants, you must edit the size of the texture for the nav node
    //We are using nav nodes evenly spaced at 20 pixels apart, which is why we are using a 45 by 30 graph
    private static final int GRAPH_WIDTH  = 45;
    private static final int GRAPH_HEIGHT = 30;
    //The size of the bounds used to determine if a node is valid (if it does not intersect with any game objects
    private static final int BUFFER_DISTANCE = 26;

    NavigationNode[][] graph;

    public NavigationGraph() {
        graph = new NavigationNode[GRAPH_WIDTH][GRAPH_HEIGHT];
        initializeNavigationGraph();
    }

    private void initializeNavigationGraph() {
        for (int i=0; i<GRAPH_WIDTH; i++) {
            for (int j=0; j<GRAPH_HEIGHT; j++) {
                graph[i][j] = new NavigationNode(new Point((i*20)+10, (j*20)+10));
            }
        }
    }

    public void update(List<GameObject> gameObjects, Rectangle gameBounds) {

        for (int i = 0; i < GRAPH_WIDTH; i++) {
            for (int j = 0; j < GRAPH_HEIGHT; j++) {
                graph[i][j].setIsValid(isNodeValid(graph[i][j], gameObjects, gameBounds));
            }
        }
    }

    public void drawGraph(SpriteBatch batch, Texture navNodeTexture) {

        for (int i = 0; i < GRAPH_WIDTH; i++) {
            for (int j = 0; j < GRAPH_HEIGHT; j++) {
                NavigationNode node = graph[i][j];
                if (node.getIsValid()) {
                    batch.draw(navNodeTexture, node.getLocation().x - navNodeTexture.getWidth()/2,
                                               node.getLocation().y - navNodeTexture.getHeight()/2);
                }
            }
        }
    }

    private boolean isNodeValid(NavigationNode node, List<GameObject> gameObjects, Rectangle gameBounds) {

        Rectangle nodeBounds = new Rectangle(node.getLocation().x - BUFFER_DISTANCE/2,
                                             node.getLocation().y - BUFFER_DISTANCE/2,
                                             BUFFER_DISTANCE, BUFFER_DISTANCE);

        for(GameObject object : gameObjects) {

            if (object instanceof Hunter || object instanceof Feeder || object instanceof FeederNest || object instanceof Character) {
                return true;
            }

            Rectangle objectBounds = new Rectangle((int) object.getPosition().x,
                                                   (int) object.getPosition().y,
                                                   object.getWidth(),
                                                   object.getHeight());
            //For black holes, the width and height represent the center of the black hole (so we can detect collisions
            //only at the center).  So we must use the texture width and height to navigate around the black holes
            //Some gravity will still be applied at this distance, but it should be plenty weak enough to navigate through
            if (object instanceof BlackHole) {
                //Use some simple math to get the x and y position of the bottom left point of the black hole texture
                //That way we can make the bounds the entire texture so we can properly avoid black holes
                int x = (int) object.getPosition().x;
                x -= object.getTexture().getWidth()/2 - object.getWidth()/2;
                int y = (int) object.getPosition().y;
                y -= object.getTexture().getHeight()/2 - object.getHeight()/2;

                objectBounds.setPosition(x,y);
                objectBounds.setWidth(object.getTexture().getWidth());
                objectBounds.setHeight(object.getTexture().getHeight());
            }

            //Make sure the node bounds are within the bounds of the game
            //If the node is close enough to the edge, the buffer distance my put it beyond the bounds of the game
            //In this case, the character would not be able to get its center on the node before colliding with the wall
            if (!withinGameBounds(nodeBounds, gameBounds)) {
                return false;
            }

            if(Intersector.overlaps(nodeBounds, objectBounds)) {
                return false;
            }
        }
        return true;
    }

    private boolean withinGameBounds(Rectangle nodeBounds, Rectangle gameBounds) {

        if (nodeBounds.getX() < gameBounds.getX() ||
                nodeBounds.getY() < gameBounds.getY() ||
                nodeBounds.getX() + nodeBounds.getWidth() > gameBounds.getX() + gameBounds.getWidth() ||
                nodeBounds.getY() + nodeBounds.getHeight() > gameBounds.getY() + gameBounds.getHeight()) {
            return false;
        }

        return true;
    }

    public NavigationNode[][] getGraph() {
        return graph;
    }
}
