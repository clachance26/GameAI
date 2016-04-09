package com.mygdx.game.sensor_implementation;

/**
 * The output of the pie slice sensors
 * One sensor produces four of these objects (one for each quadrant)
 */
public class PieSliceResults {

    private int degreesMin;
    private int degreesMax;
    private int count;

    PieSliceResults(int degreesMin, int degreesMax, int count) {
        this.degreesMin = degreesMin;
        this.degreesMax = degreesMax;
        this.count = count;
    }

    public int getDegreesMin() {
        return degreesMin;
    }

    public int getDegreesMax() {
        return degreesMax;
    }

    public int getCount() {
        return count;
    }
}
