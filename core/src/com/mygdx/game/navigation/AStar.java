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

    private Boolean isDone = false;
    private NavigationGraph navigationGraph;
    private Deque<NavigationNode> shortestPath;
    private PriorityQueue<NavigationNode> searchQueue;
    private Integer searchQueueCounter = 0;
    private static List<GameObject> gameObjects;
    private NavigationNode start, destination, current, left, right, up, down;
    private static Debug debug;
    private int x, y;

    public Deque<NavigationNode> evaluateAStar(Point startPoint, Point destinationPoint) {

        shortestPath = new LinkedBlockingDeque<>();
        searchQueue = new PriorityQueue<>();
        int tempIndex1, tempIndex2;

        tempIndex1 = NavigationNode.getNodeIndexFromLocation(startPoint.x);
        tempIndex2 = NavigationNode.getNodeIndexFromLocation(startPoint.y);
        start = navigationGraph.graph[tempIndex1][tempIndex2];

        tempIndex1 = NavigationNode.getNodeIndexFromLocation(destinationPoint.x);
        tempIndex2 = NavigationNode.getNodeIndexFromLocation(destinationPoint.y);
        destination = navigationGraph.graph[tempIndex1][tempIndex2];

        //Initialize the start node
        start.setAsTheStartNode(true);
        start.setCostToThisNode(0);
        evaluateNode(start);
        start.setEvaluated(true);
        current = start;

        try {
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
//                debug.printAStarSearch(left.getLocation());
                }
                if (right != null && evaluateNode(right)) {
                    right.setaStarCounter(++searchQueueCounter);
                    searchQueue.add(right);
//                debug.printAStarSearch(right.getLocation());
                }
                if (up != null && evaluateNode(up)) {
                    up.setaStarCounter(++searchQueueCounter);
                    searchQueue.add(up);
//                debug.printAStarSearch(up.getLocation());
                }
                if (down != null && evaluateNode(down)) {
                    down.setaStarCounter(++searchQueueCounter);
                    searchQueue.add(down);
//                debug.printAStarSearch(down.getLocation());
                }

                if (searchQueue.isEmpty()) {
                    shortestPath.clear();
                    return shortestPath;
                }
                current = searchQueue.remove();
            }

        } catch (Exception e) {
            shortestPath.clear();
            return shortestPath;
        }

        traceBackShortestPath();

        System.out.println("");
        isDone = true;
        return shortestPath;
    }

    private boolean evaluateNode(NavigationNode nodeToEvaluate) {

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

    private void initializeAdjacentNodes() {

        if (x > 0) {
            left = navigationGraph.graph[x-1][y];
        } else {
            left = null;
        }

        if (x < navigationGraph.graph.length-1) {
            right = navigationGraph.graph[x+1][y];
        } else {
            right = null;
        }

        if (y < navigationGraph.graph[0].length-1) {
            up = navigationGraph.graph[x][y+1];
        } else {
            up = null;
        }

        if (y > 0) {
            down = navigationGraph.getGraph()[x][y-1];
        } else {
            down = null;
        }
    }

    private void traceBackShortestPath() {

        NavigationNode currentTraceNode = destination;
        while (!currentTraceNode.isStartNode()) {
            shortestPath.addFirst(currentTraceNode);
            currentTraceNode = currentTraceNode.getPreviousNode();
        }
        //Add the start node
        shortestPath.addFirst(currentTraceNode);
    }

    public  NavigationGraph getNavigationGraph() {
        return navigationGraph;
    }

    public void setNavigationGraph(NavigationGraph navigationGraph) {
        this.navigationGraph = navigationGraph;
    }

    public  Boolean getIsDone() {
        return isDone;
    }

    public void setIsDone(Boolean isDone) {
        this.isDone = isDone;
    }

    public Deque<NavigationNode> getShortestPath() {
        return shortestPath;
    }

    public static void setGameObjects(List<GameObject> gameObjects) {
        AStar.gameObjects = gameObjects;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }
}
