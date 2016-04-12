package com.mygdx.game.sensor_implementation;

import com.mygdx.game.game_objects.GameObject;

public class AdjacentObject {

    GameObject detectedObject;
    double degrees;
    double dist;

    public AdjacentObject(GameObject detectedObject, double degrees, double dist) {
        this.detectedObject = detectedObject;
        this.degrees = degrees;
        this.dist = dist;
    }

    public double getDegrees() {
        return degrees;
    }

    public double getDist() {
        return dist;
    }

    public GameObject getDetectedObject() {
        return detectedObject;
    }
}
