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

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

public class GameAI extends ApplicationAdapter {

	private static final int NUM_BLACK_HOLES = 3;

	Rectangle bounds = new Rectangle(0, 0, 900, 600);
	List<GameObject> gameObjects = new ArrayList<>();
	List<RenderedObject> renderedObjects = new ArrayList<>();
	SpriteBatch batch;
	Background background;
	BlackHole[] blackHoles = new BlackHole[NUM_BLACK_HOLES];
	int blackHoleIndex = 0;

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
		ApplicationModeSingleton.getInstance().setApplicationMode(ApplicationModeEnum.SETUP);

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

		//Initialize the game objects
		background = new Background(this);
		character = new Character(batch, 800, 100, 270);

		//Add all game components to the rendered object list to be drawn
		renderedObjects.add(background);
		renderedObjects.add(character);

		//Create the Navigation Graph
		navigationGraph = new NavigationGraph();
		AStar.setNavigationGraph(navigationGraph.getGraph());
		AStar.setGameObjects(gameObjects);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		//This will move the character based on the user input
		character.moveFromKeyboardControls(inputProcessor.getForward(), inputProcessor.getTurning(), bounds, gameObjects);

		//Perform actions based on current application mode
		switch (ApplicationModeSingleton.getInstance().getApplicationMode()) {

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
				//play code
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
}
