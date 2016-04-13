package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.application_mode.ApplicationModeEnum;
import com.mygdx.game.application_mode.ApplicationModeSingleton;
import com.mygdx.game.debug.Debug;
import com.mygdx.game.game_objects.BlackHole;
import com.mygdx.game.game_objects.Character;
import com.mygdx.game.game_objects.GameObject;
import com.mygdx.game.navigation.AStar;
import com.mygdx.game.navigation.NavigationGraph;
import com.mygdx.game.navigation.NavigationNode;
import com.mygdx.game.rendered_objects.*;
import com.mygdx.game.rendered_objects.SplashScreen;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class GameAI extends ApplicationAdapter {

	private static final int NUM_BLACK_HOLES = 3;
	private static final

	Rectangle bounds = new Rectangle(0, 0, 900, 600);
	List<GameObject> gameObjects = new ArrayList<>();
	List<RenderedObject> renderedObjects = new ArrayList<>();
	SpriteBatch batch;
	SplashScreen splashScreen;
	GameOverScreen gameOverScreen;
	Background background;
	BlackHole[] blackHoles;
	int blackHoleIndex;
	boolean gameOverScreenInitialized = false;

	Character character;

	MyInputProcessor inputProcessor;
	NavigationGraph navigationGraph;
	Deque<NavigationNode> aStarShortestPath;

	Debug debug;
	//Specifies the number of renders in between each output update
	int outputUpdateRate = 60;
	//Keeps track of how often we need to output
	int renderCount = 0;
	//Keeps track of which black hole we are placing in the game setup
	int blackHolePlacementCount = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		ApplicationModeSingleton.getInstance().setApplicationMode(ApplicationModeEnum.SPLASH_SCREEN);
		ApplicationModeSingleton.getInstance().setGameDifficulty(Difficulty.EASY);

		//Create the debug object
		debug = new Debug(this);
		debug.printApplicationHeader();
		ApplicationModeSingleton.getInstance().setDebug(debug);
		AStar.setDebug(debug);

		//Handle the input using an event handler
		//Every time a key is pressed or the mouse is clicked, an event will fire
		//Depending on the key pressed, fields in the processor object will be updated
		//These fields (forward, turning, click1, ect) will be polled each time tick to be processed
		inputProcessor = new MyInputProcessor(debug);
		Gdx.input.setInputProcessor(inputProcessor);

		splashScreen = new SplashScreen(batch);
		renderedObjects.add(splashScreen);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		//Some core logic that we want to implement in all game modes other than the splash screen
		if (!ApplicationModeSingleton.getInstance().getApplicationMode().equals(ApplicationModeEnum.SPLASH_SCREEN)) {

			//Move the character based on the user input and update the navigation graph
			character.moveFromKeyboardControls(inputProcessor.getForward(), inputProcessor.getTurning(), bounds, gameObjects);
			navigationGraph.update(gameObjects, bounds);
		}

		//Perform actions based on current application mode
		switch (ApplicationModeSingleton.getInstance().getApplicationMode()) {

			case SPLASH_SCREEN:
				//Transition to the setup mode after the user presses any key while in the splash screen mode
				if (inputProcessor.isSplashScreenContinue()) {
					inputProcessor.setSplashScreenContinue(false);
					initializeGame();
					ApplicationModeSingleton.getInstance().setApplicationMode(ApplicationModeEnum.SETUP);
				}
				break;

			case SETUP:
				if (inputProcessor.isNewClickToProcess()) {
					inputProcessor.setNewClickToProcess(false);
					if (placeBlackHole(inputProcessor.getClick())) {
						blackHoleIndex++;
					}
					if (blackHoleIndex >= NUM_BLACK_HOLES) {
						//We have placed all of our black holes, so transition into the play state and add the black
						//holes to our game objects
						ApplicationModeSingleton.getInstance().setApplicationMode(ApplicationModeEnum.PLAY);
						gameObjects.addAll(Arrays.asList(blackHoles));
					}
				}
				break;

			case PLAY:
				for (int i=0; i<NUM_BLACK_HOLES; i++) {
					blackHoles[i].performGravitationalPull(gameObjects);
				}
				break;

			case GAME_OVER:
				//Only need to initialize the screen once
				if (!gameOverScreenInitialized) {
					gameOverScreen = new GameOverScreen(batch);
					renderedObjects.add(gameOverScreen);
					gameOverScreenInitialized = true;
				}

				if (inputProcessor.isGameOverScreenContinue()) {
					inputProcessor.setGameOverScreenContinue(false);
					resetGame();
					ApplicationModeSingleton.getInstance().setApplicationMode(ApplicationModeEnum.SETUP);
				}
				break;
		}

		//Draw all of the game components
		renderedObjects.forEach(RenderedObject::draw);

		//Handle sensor output periodically
//		if (ApplicationModeSingleton.getInstance().getApplicationMode().equals(ApplicationModeEnum.EVALUATE_SENSORS)
//				&& renderCount % outputUpdateRate == 0) {
//			debug.printSensorOutput();
//		}

		batch.end();
		renderCount++;
	}

	/**
	 * This function is called immediately after the user navigates away from the splash screen
	 * Here we create all of the necessary game components to start the game
	 */
	public void initializeGame() {

		//Initialize the game objects
		background = new Background(this);
		character = new Character(batch, 800, 100, 270);

		gameObjects.add(character);

		//Add all game components to the rendered object list to be drawn
		renderedObjects.add(background);
		renderedObjects.add(character);

		//Create the Navigation Graph
		navigationGraph = new NavigationGraph();
		AStar.setNavigationGraph(navigationGraph.getGraph());
		AStar.setGameObjects(gameObjects);

		blackHoles = new BlackHole[NUM_BLACK_HOLES];
		blackHoleIndex = 0;
	}

	//Todo: finish all of this logic
	private void resetGame() {
		renderedObjects = new ArrayList<>();
		gameObjects = new ArrayList<>();
		initializeGame();
	}

	/**
	 * Helper function to place the black holes when in setup mode
	 * This contains the logic to check if the user click is valid
	 * @param click the point where the user clicked on the screen which will be the center of the black hole to place
	 * @return if the black hole was able to be placed
	 */
	private boolean placeBlackHole(Point click) {

		//Create the black hole based on the given click
		//Then adjust its position to make the user click be the center of the black hole
		//Note: This does not actually place it in the game yet
		BlackHole blackHole = new BlackHole(batch, click.x, click.y, 270);
		blackHole.setPosition(new Vector2(blackHole.getPosition().x - blackHole.getWidth()/2,
				                          blackHole.getPosition().y - blackHole.getHeight()/2));
		//Make sure that the click is within the appropriate bounds
		//if (click.x < )

		//gameObjects.add(blackHole);
		//Add it to be rendered before the character
		renderedObjects.add(renderedObjects.indexOf(character), blackHole);
		blackHoles[blackHoleIndex] = blackHole;
		return true;
	}

	public Rectangle getBounds() {
		return bounds;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public Character getCharacter() {
		return character;
	}

	public SpriteBatch getBatch() {
		return batch;
	}

	public MyInputProcessor getInputProcessor() {
		return inputProcessor;
	}

	public NavigationGraph getNavigationGraph() {
		return navigationGraph;
	}
}
