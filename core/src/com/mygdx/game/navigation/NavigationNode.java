package com.mygdx.game.navigation;
import java.awt.Point;
import java.sql.Timestamp;
import java.util.Comparator;

/**
 * Created by Christopher on 3/9/2016.
 */
public class NavigationNode implements Comparator<NavigationNode>, Comparable<NavigationNode>{

    private Point location;
    private Integer costToThisNode;     //G
    private Integer heuristic;          //H - manhattan distance
    private Integer total;              //F
    private NavigationNode previousNode;
    //Needed in the A Star algorithm to break ties based on most recently added
    private Integer aStarCounter;
    private Boolean startNode = false;
    private Boolean isReachable = true;
    private Boolean evaluated = false;

    public NavigationNode(Point location) {
        this.location = location;
    }

    public static Point findClosestNavNode(Point source){

        Point closestNavNode = new Point();
        int tempX = source.x / 10;
        int tempY = source.y / 10;

        if (tempX % 2 == 1) {
            closestNavNode.x = tempX * 10;
        }
        else {
            closestNavNode.x = tempX * 10 + 10;
        }

        if (tempY % 2 == 1) {
            closestNavNode.y = tempY * 10;
        }
        else {
            closestNavNode.y = tempY * 10 + 10;
        }

        return closestNavNode;
    }

    public static int getNodeIndexFromLocation(int coordinate) {
        return (coordinate - 10) / 20;
    }

    public Point getLocation() {
        return location;
    }

    public NavigationNode getPreviousNode() {
        return previousNode;
    }

    public void setPreviousNode(NavigationNode previousNode) {
        this.previousNode = previousNode;
    }

    public Integer getCostToThisNode() {
        return costToThisNode;
    }

    public void setCostToThisNode(Integer costToThisNode) {
        this.costToThisNode = costToThisNode;
    }

    public Integer getHeuristic() {
        return heuristic;
    }

    public void setHeuristic(Integer heuristic) {
        this.heuristic = heuristic;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Boolean isStartNode() {
        return startNode;
    }

    public void setAsTheStartNode(Boolean startNode) {
        this.startNode = startNode;
    }

    public Boolean getIsReachable() {
        return isReachable;
    }

    public void setIsReachable(Boolean isReachable) {
        this.isReachable = isReachable;
    }

    public Boolean isEvaluated() {
        return evaluated;
    }

    public void setEvaluated(Boolean evaluated) {
        this.evaluated = evaluated;
    }

    public void setaStarCounter(Integer aStarCounter) {
        this.aStarCounter = aStarCounter;
    }

    public Integer getaStarCounter() {
        return aStarCounter;
    }

    @Override
    public int compareTo(NavigationNode navigationNode) {
        if (!total.equals(navigationNode.getTotal())) {
            return total.compareTo(navigationNode.getTotal());
        }
        else {
            return -aStarCounter.compareTo(navigationNode.getaStarCounter());
        }
    }

    @Override
    public int compare(NavigationNode n1, NavigationNode n2) {
        return n1.compareTo(n2);
    }
}
