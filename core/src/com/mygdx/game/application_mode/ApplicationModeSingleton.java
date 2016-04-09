package com.mygdx.game.application_mode;


import com.mygdx.game.debug.Debug;

/**
 * Created by Christopher on 3/8/2016.
 * A single used to store the mode of this application
 * The modes are seek, display A*, and navigate A*
 *      evaluate sensors    - performs the sensor computations defined in assignment 1
 *      seek                - moves the character to the position clicked
 *      display A*          - shows the A* shortest path between a start node and a destination node (clicked by the user)
 *      navigate A*         - moves the character to a destination node using the A* shortest path
 */
public class ApplicationModeSingleton {

    private ApplicationModeEnum applicationMode;
    private Debug debug;


    private static ApplicationModeSingleton ourInstance = new ApplicationModeSingleton();

    public static ApplicationModeSingleton getInstance() {
        return ourInstance;
    }

    private ApplicationModeSingleton() {
    }

    public ApplicationModeEnum getApplicationMode() {
        return applicationMode;
    }

    public void setApplicationMode(ApplicationModeEnum applicationMode) {

        this.applicationMode = applicationMode;
        if (debug != null) {
            debug.printAppModeChange();
        }
    }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }
}
