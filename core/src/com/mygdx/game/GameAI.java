package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.mygdx.game.application_mode.ApplicationModeEnum;
import com.mygdx.game.application_mode.ApplicationModeSingleton;
import com.mygdx.game.debug.Debug;
import com.mygdx.game.game_objects.BlackHole;
import com.mygdx.game.game_objects.Character;
import com.mygdx.game.game_objects.GameObject;
import com.mygdx.game.navigation.AStar;
import com.mygdx.game.navigation.NavigationGraph;
import com.mygdx.game.navigation.NavigationNode;

import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GameAI extends ApplicationAdapter {

	Rectangle bounds = new Rectangle(0, 0, 900, 600);
	List<GameObject> gameObjects = new ArrayList<>();
	List<RenderedObject> renderedObjects = new ArrayList<>();
	SpriteBatch batch;
	Background background;

	Character character;

	MyInputProcessor inputProcessor;
	NavigationGraph navigationGraph;
	Deque<NavigationNode> aStarShortestPath;

	Debug debug;
	//Specifies the number of renders in between each output update
	int outputUpdateRate = 60;
	//Keeps track of how often we need to output
	int renderCount = 0;
	
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
