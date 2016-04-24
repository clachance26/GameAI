package com.mygdx.game.navigation;

import com.mygdx.game.debug.Debug;
import com.mygdx.game.game_objects.GameObject;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Created by Christopher on 3/9/2016.
 */
public class AStar {

    private static Boolean isDone = false;
    private static NavigationNode[][] navigationGraph;
    private static Deque<NavigationNode> shortestPath;
    private static PriorityQueue<NavigationNode> searchQueue;
    private static Integer searchQueueCounter = 0;
    private static List<GameObject> gameObjects;
    private static NavigationNode start, destination, current, left, right, up, down;
    private static Debug debug;
    private static int x, y;

    public static Deque<NavigationNode> evaluateAStar(Point startPoint, Point destinationPoint) {

        shortestPath = new LinkedBlockingDeque<>();
        searchQueue = new PriorityQueue<>();
        int tempIndex1, tempIndex2;

        tempIndex1 = NavigationNode.getNodeIndexFromLocation(startPoint.x);
        tempIndex2 = NavigationNode.getNodeIndexFromLocation(startPoint.y);
        start = navigationGraph[tempIndex1][tempIndex2];

        tempIndex1 = NavigationNode.getNodeIndexFromLocation(destinationPoint.x);
        tempIndex2 = NavigationNode.getNodeIndexFromLocation(destinationPoint.y);
        destination = navigationGraph[tempIndex1][tempIndex2];

        //Initialize the start node
        start.setAsTheStartNode(true);
        start.setCostToThisNode(0);
        evaluateNode(start);
        start.setEvaluated(true);
        current = start;
        current.setHeuristic(200);

        //When the heuristic of the current node reaches 0, we have reached the destination node
        while (current.getHeuristic() > 0) {

            x = NavigationNode.getNodeIndexFromLocation(current.getLocation().x);
            y = NavigationNode.getNodeIndexFromLocation(current.getLocation().y);

            //Initialize the adjacent nodes
            initializeAdjacentNodes();

            //Evaluate all of the adjacent nodes and add them to the search queue to be checked later (if they are valid)
            if (left != null && evaluateNode(left)) {
                left.setaStarCounter(++searchQueueCounter);
                searchQueue.add(left);
                debug.printAStarSearch(left.getLocation());
            }
            if (right != null && evaluateNode(right)) {
                right.setaStarCounter(++searchQueueCounter);
                searchQueue.add(right);
                debug.printAStarSearch(right.getLocation());
            }
            if (up != null && evaluateNode(up)) {
                up.setaStarCounter(++searchQueueCounter);
                searchQueue.add(up);
                debug.printAStarSearch(up.getLocation());
            }
            if (down != null && evaluateNode(down)) {
                down.setaStarCounter(++searchQueueCounter);
                searchQueue.add(down);
                debug.printAStarSearch(down.getLocation());
            }

            current = searchQueue.remove();
        }

        traceBackShortestPath();

        System.out.println("");
        isDone = true;
        return shortestPath;
    }

    private static boolean evaluateNode(NavigationNode nodeToEvaluate) {

        if (nodeToEvaluate.isEvaluated()) {
            return false;
        }

        if (!nodeToEvaluate.getIsValid()) {
            nodeToEvaluate.setEvaluated(true);
            return false;
        }
        nodeToEvaluate.setIsValid(true);

        //This node is being evaluated by the current node from the while loop back in evaluateAStar
        //If this node has not already been evaluated by a different node and is reachable, we set its previous node
        //It is being evaluated for the first time by current, so if there is a path through this node, it will be from current
        nodeToEvaluate.setPreviousNode(current);

        //If it is not the start node, use the previous node to evaluate the cost to this node
        if (!nodeToEvaluate.isStartNode()) {
            nodeToEvaluate.setCostToThisNode(nodeToEvaluate.getPreviousNode().getCostToThisNode() + 1);
        }

        //Next, evaluate the heuristic of the this node (the manhattan distance to this node)
        //Start with the total number of nodes to the destination in the x direction
        //Then add the number of nodes in the y direction
        //The nodes are spaced 20 pixels apart, so to get the number of nodes to the destination, divide the raw distance by 20
        int heuristic = Math.abs(destination.getLocation().x - nodeToEvaluate.getLocation().x) / 20;
        heuristic += Math.abs(destination.getLocation().y - nodeToEvaluate.getLocation().y) / 20;
        nodeToEvaluate.setHeuristic(heuristic);

        //The total is simply the cost to this node plus the heuristic distance
        nodeToEvaluate.setTotal(nodeToEvaluate.getCostToThisNode() + nodeToEvaluate.getHeuristic());

        nodeToEvaluate.setEvaluated(true);
        return true;
    }

    private static void initializeAdjacentNodes() {

        if (x > 0) {
            left = navigationGraph[x-1][y];
        } else {
            left = null;
        }

        if (x < navigationGraph.length-1) {
            right = navigationGraph[x+1][y];
        } else {
            right = null;
        }

        if (y < navigationGraph[0].length-1) {
            up = navigationGraph[x][y+1];
        } else {
            up = null;
        }

        if (y > 0) {
            down = navigationGraph[x][y-1];
        } else {
            down = null;
        }
    }

    private static void traceBackShortestPath() {

        NavigationNode currentTraceNode = destination;
        while (!currentTraceNode.isStartNode()) {
            shortestPath.addFirst(currentTraceNode);
            currentTraceNode = currentTraceNode.getPreviousNode();
        }
        //Add the start node
        shortestPath.addFirst(currentTraceNode);
    }

    public static NavigationNode[][] getNavigationGraph() {
        return navigationGraph;
    }

    public static void setNavigationGraph(NavigationNode[][] navigationGraph) {
        AStar.navigationGraph = navigationGraph;
    }

    public static Boolean getIsDone() {
        return isDone;
    }

    public static void setIsDone(Boolean isDone) {
        AStar.isDone = isDone;
    }

    public static Deque<NavigationNode> getShortestPath() {
        return shortestPath;
    }

    public static void setGameObjects(List<GameObject> gameObjects) {
        AStar.gameObjects = gameObjects;
    }

    public static void setDebug(Debug debug) {
        AStar.debug = debug;
    }
}
