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
import com.mygdx.game.game_objects.Character;
import com.mygdx.game.game_objects.GameObject;
import com.mygdx.game.navigation.AStar;
import com.mygdx.game.navigation.NavigationNode;

import java.awt.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GameAI extends ApplicationAdapter {

	Rectangle bounds = new Rectangle(0, 0, 900, 600);
	List<GameObject> agentList = new ArrayList<>();
	List<RenderedObject> renderedObjects = new ArrayList<>();
	SpriteBatch batch;
	Background background;

	Character character;

	MyInputProcessor inputProcessor;
	NavigationNode[][] navigationGraph;
	Deque<NavigationNode> aStarShortestPath;
	Boolean runAStar = true;

	Debug debug;
	//Specifies the number of renders in between each output update
	int outputUpdateRate = 60;
	//Keeps track of how often we need to output
	int renderCount = 0;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		ApplicationModeSingleton.getInstance().setApplicationMode(ApplicationModeEnum.SETUP);

		//Scan input file for agent locations
//		Scanner scanner;
//		try {
//			scanner = new Scanner(new File("agentLocations.txt"));
//			a1x = scanner.nextInt();
//			a1y = scanner.nextInt();
//			a2x = scanner.nextInt();
//			a2y = scanner.nextInt();
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		}

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
		character = new Character("character.png", batch, 800, 100, 270);


//		horizontalWall = new Feeder("horizontalWall.png", 400, 400, 0, 400, 50, false, batch);
//		horizontalWall2 = new Feeder("horizontalWall.png", 400, 500, 0 , 400, 50, false, batch);
//		verticalWall = new Feeder("verticalWall.png", 100, 100, 0, 100, 450, false, batch);
//		agent1 = new Feeder("agent1.png", a1x, a1y, 0, 50, 50, true, batch);
//		agent2 = new Feeder("agent2.png", a2x, a2y, 0, 50, 50, true, batch);

		//Add game objects to the agent list (for sensor detection and collision detection)
//		agentList.add(agent1);
//		agentList.add(agent2);
//		agentList.add(horizontalWall);
//		agentList.add(horizontalWall2);
//		agentList.add(verticalWall);

		//Add all game components to the rendered object list to be drawn
		renderedObjects.add(background);
		renderedObjects.add(character);
//		renderedObjects.add(horizontalWall);
//		renderedObjects.add(horizontalWall2);
//		renderedObjects.add(verticalWall);
//		renderedObjects.add(agent1);
//		renderedObjects.add(agent2);

		//Create the Navigation Graph
		//Each navigation node is separated by 20 pixels, so we have 44 nodes wide and 29 nodes tall
		navigationGraph = new NavigationNode[44][29];
		AStar.setNavigationGraph(navigationGraph);
		AStar.setAgentList(agentList);
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		//This will move the character based on the user input
		character.moveFromKeyboardControls(inputProcessor.getForward(), inputProcessor.getTurning(), bounds, agentList);

		//Perform actions based on current application mode
		switch (ApplicationModeSingleton.getInstance().getApplicationMode()) {

			case SETUP:
				//setup code
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

	public List<GameObject> getAgentList() {
		return agentList;
	}

	public Character getCharacter() {
		return character;
	}
}
