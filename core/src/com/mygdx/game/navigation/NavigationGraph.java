package com.mygdx.game.navigation;

import java.awt.*;

/**
 * The data structure to hold the graph of navigation nodes
 * Since we are using a dense mesh of navigation nodes, our graph is just going to be a 2d array
 */
public class NavigationGraph {

    NavigationNode[][] graph;

    public NavigationGraph() {
        graph = new NavigationNode[45][30];
        initializeNavigationGraph();
    }

    private void initializeNavigationGraph() {
        for (int i=0; i<45; i++) {
            for (int j=0; j<30; j++) {
                graph[i][j] = new NavigationNode(new Point((i*20)+10, (j*20)+10));
            }
        }
    }

    public NavigationNode[][] getGraph() {
        return graph;
    }
}
