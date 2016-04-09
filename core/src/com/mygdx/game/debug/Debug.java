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
        sb.append("Application States:\n");
        sb.append("Evaluate Sensors (e)\n");
        sb.append("Seek (s)\n");
        sb.append("Display A* Shortest Path (d)\n");
        sb.append("Navigate A* Shortest Path (a)\n");
        sb.append("\nCurrent Mode: " + ApplicationModeSingleton.getInstance().getApplicationMode() + "\n\n");
        sb.append("Press \"n\" to show navigation nodes\n");
        System.out.println(sb);
    }

    public void printAppModeChange() {
        System.out.println("\nCurrent Mode: " + ApplicationModeSingleton.getInstance().getApplicationMode() + "\n");
    }

    /**
     * Prints the sensor output
     */
    public void printSensorOutput() {

        Character character = gameAI.getCharacter();
        StringBuilder sb = new StringBuilder();
        sb.append("\n\n");
        sb.append("Subject\t\tPosition: " + character.getPosition() + "\tHeading: " + character.getAngle() + "\n");
        sb.append("-----------------------------------------------------------------------------\n");

        sb.append("Wall Sensors\n");
        for (WallSensor wallSensor : character.getWallSensors()) {
            sb.append(wallSensor.getName() + "\t\t");
            if (wallSensor.getDistToClosestObject() >= 0) {
                sb.append(String.format("%.4f", wallSensor.getDistToClosestObject()) + "\n");
            }
            else {
                sb.append("-\n");
            }
        }

        sb.append("\n");
        sb.append("Adjacent Agents\n");
        int count = 1;
        for (AdjacentObject adjacentObject: character.getAaSensor().getAdjacentObjects()) {
            sb.append(count + ") Distance: " + String.format("%.4f", adjacentObject.getDist()) + "\t\tHeading: "
                    + String.format("%.4f",adjacentObject.getDegrees()) + "\n");
            count++;
        }

        sb.append("\n");
        sb.append("Pie Slice Sensor\n");
        for (PieSliceResults results : character.getPsSensor().getResults()) {
            sb.append("Range: " + String.format("%3d", results.getDegreesMin()) + "  -  " + String.format("%3d", results.getDegreesMax()) +
                      "\t\tActivation Level: " + results.getCount() + "\n");
        }

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
