package com.mygdx.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.game.application_mode.ApplicationModeSingleton;
import com.mygdx.game.navigation.AStar;
import com.mygdx.game.navigation.NavigationNode;

import java.awt.*;
import java.util.Deque;


/**
 * Created by Christopher on 3/8/2016.
 */
public class Background implements RenderedObject {

    private Texture backgroundTexture;
    private Texture navNodeTexture;
    private Texture pathNodeTexture;
    private Texture destinationNodeTexture;
    private SpriteBatch batch;
    private MyInputProcessor inputProcessor;
    //The actual game AI object is needed to get real time updates of the a star shortest path
    //and if the a star algorithm is done
//    private GameAI gameAI;

    public Background(GameAI gameAI) {

        backgroundTexture = new Texture("background.png");
        navNodeTexture = new Texture("navNode.png");
        pathNodeTexture = new Texture("pathNode.png");
        destinationNodeTexture = new Texture("destinationNode.png");
        this.batch = gameAI.batch;
        this.inputProcessor = gameAI.inputProcessor;
    }

    @Override
    public void draw() {

        batch.draw(backgroundTexture, 0, 0);

        if (inputProcessor.isShowNavigationNodes()) {
            //Draw each node on the background
            for (int i=0; i<backgroundTexture.getWidth(); i+=navNodeTexture.getWidth()) {
                for (int j=0; j<backgroundTexture.getHeight(); j+=navNodeTexture.getHeight()) {

                    if (AStar.isNodeReachable(new NavigationNode(new Point(i+navNodeTexture.getWidth()/2,
                                                                           j+navNodeTexture.getHeight()/2)))) {

                        batch.draw(navNodeTexture, i, j);
                    }
                }
            }
        }

//        Point drawCoordinates;
        //Draw over any animations for the nav nodes
//        switch (ApplicationModeSingleton.getInstance().getApplicationMode()) {
//
//            case SEEK:
//                if (inputProcessor.isClicked1() && !Seek.isDone()) {
//                    drawCoordinates = calculateNodeDrawCoordinates(inputProcessor.getClick1());
//                    batch.draw(destinationNodeTexture, drawCoordinates.x, drawCoordinates.y);
//                }
//                break;
//
//            case DISPLAY_A_STAR:
//                if (inputProcessor.isClicked1()) {
//                    drawCoordinates = calculateNodeDrawCoordinates(inputProcessor.getClick1());
//                    batch.draw(pathNodeTexture, drawCoordinates.x, drawCoordinates.y);
//                }
//                if (inputProcessor.isClicked2()) {
//                    drawCoordinates = calculateNodeDrawCoordinates(inputProcessor.getClick2());
//                    batch.draw(destinationNodeTexture, drawCoordinates.x, drawCoordinates.y);
//
//                    if (AStar.getIsDone()) {
//                        drawAStarPath();
//                    }
//
//                }
//                break;
//
//            case NAVIGATE_A_STAR:
//                if (inputProcessor.isClicked1() && AStar.getIsDone()) {
//                    drawAStarPath();
//                }
//                break;
//        }

    }

    private Point calculateNodeDrawCoordinates(Point nodeCenter) {

        Point drawCoordinates = new Point();

        drawCoordinates.x = nodeCenter.x - (navNodeTexture.getWidth()/2);
        drawCoordinates.y = nodeCenter.y - (navNodeTexture.getHeight()/2);

        return drawCoordinates;
    }

    private void drawAStarPath() {

        Point drawCoordinates;
        Deque<NavigationNode> aStarShortestPath = AStar.getShortestPath();

        for (NavigationNode pathNode : aStarShortestPath) {
            drawCoordinates = calculateNodeDrawCoordinates(pathNode.getLocation());
            batch.draw(destinationNodeTexture, drawCoordinates.x, drawCoordinates.y);
        }
    }
}
