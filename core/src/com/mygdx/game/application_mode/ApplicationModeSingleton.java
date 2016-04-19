package com.mygdx.game.application_mode;


import com.mygdx.game.Difficulty;
import com.mygdx.game.GameAI;
import com.mygdx.game.debug.Debug;

/**
 * Created by Christopher on 3/8/2016.
 * A singleton used to store the mode of this application
 * The modes are splash screen, setup, play, game over
 */
public class ApplicationModeSingleton {

    private ApplicationModeEnum applicationMode;
    private Difficulty gameDifficulty;
    private Debug debug;
    private GameAI gameAI;


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
    }

    public Difficulty getGameDifficulty() {
        return gameDifficulty;
    }

    public void setGameDifficulty(Difficulty gameDifficulty) { this.gameDifficulty = gameDifficulty; }

    public Debug getDebug() {
        return debug;
    }

    public void setDebug(Debug debug) {
        this.debug = debug;
    }

    public GameAI getGameAI() {
        return gameAI;
    }

    public void setGameAI(GameAI gameAI) {
        this.gameAI = gameAI;
    }
}
