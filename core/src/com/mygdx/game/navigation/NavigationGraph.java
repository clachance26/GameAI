package com.mygdx.game.navigation;

/**
 * The data structure to hold the graph of navigation nodes
 * Since we are using a dense mesh of navigation nodes, our graph is just going to be a 2d array
 */
public class NavigationGraph {

    NavigationNode[][] graph;

    public NavigationGraph() {

    }

    public NavigationNode[][] getGraph() {
        return graph;
    }
}
