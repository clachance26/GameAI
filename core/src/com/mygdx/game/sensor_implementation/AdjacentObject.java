package com.mygdx.game.sensor_implementation;

public class AdjacentObject {

    double degrees;
    double dist;

    public AdjacentObject(double degrees, double dist) {
        this.degrees = degrees;
        this.dist = dist;
    }

    public double getDegrees() {
        return degrees;
    }

    public double getDist() {
        return dist;
    }
}
