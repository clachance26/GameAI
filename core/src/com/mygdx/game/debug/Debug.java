package com.mygdx.game.debug;

import com.mygdx.game.GameAI;
import com.mygdx.game.Seek;
import com.mygdx.game.application_mode.ApplicationModeEnum;
import com.mygdx.game.application_mode.ApplicationModeSingleton;
import com.mygdx.game.game_objects.Character;
import com.mygdx.game.sensor_implementation.AdjacentObject;
import com.mygdx.game.sensor_implementation.PieSliceResults;
import com.mygdx.game.sensor_implementation.WallSensor;

import java.awt.*;

/**
 * Prints the output for the application
 */
public class Debug {

    private GameAI gameAI;

    public Debug(GameAI gameAI) {
        this.gameAI = gameAI;

    }

    public void printApplicationHeader() {

        StringBuilder sb = new StringBuilder();
        sb.append("Welcome to Overrun\n");
        sb.append("Begin by placing your 3 black holes\n");
        System.out.println(sb);
    }

    public void printSeekOutput(Point seekPoint) {
        System.out.println("Seek to: (" + seekPoint.x + ", " + seekPoint.y + ")\n");
    }

    public void printNavigateOutput(Point source, Point destination) {
        System.out.println("Navigate using A*\nSource: (" + source.x + ", " + source.y +
                ")\tDestination: (" +  destination.x + ", " + destination.y + ")");
    }

    public void printDisplayOutput(Point source, Point destination) {
        System.out.println("Display A* Shortest Path\nSource: (" + source.x + ", " + source.y +
                ")\tDestination: (" +  destination.x + ", " + destination.y + ")");
    }

    public void printAStarSearch(Point searchPoint) {
        System.out.println("\t- Search: (" + searchPoint.x + ", " + searchPoint.y + ")");
    }
}
