package com.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.application_mode.ApplicationModeEnum;
import com.mygdx.game.application_mode.ApplicationModeSingleton;
import com.mygdx.game.debug.Debug;
import com.mygdx.game.game_objects.*;
import com.mygdx.game.game_objects.Character;
import com.mygdx.game.navigation.AStar;
import com.mygdx.game.navigation.NavigationGraph;
import com.mygdx.game.navigation.NavigationNode;
import com.mygdx.game.rendered_objects.*;
import com.mygdx.game.rendered_objects.SplashScreen;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameAI extends ApplicationAdapter {

	private static final int NUM_BLACK_HOLES = 3;
	private static final int NEST_PLACEMENT_BUFFER_DISTANCE = 50;
	private static final Vector2 DIFFICULTY_DRAW_POSITION = new Vector2(182, 59);
	private static final String NEST_IMAGE_NAME_A = "tempNestA.png";
	private static final String NEST_IMAGE_NAME_B = "tempNestB.png";
	private static final String NEST_IMAGE_NAME_C = "tempNestC.png";

	Rectangle bounds = new Rectangle(0, 0, 900, 600);
	List<GameObject> gameObjects = new ArrayList<>();
	List<RenderedObject> renderedObjects = new ArrayList<>();
	SpriteBatch batch;
	BitmapFont font;
	SplashScreen splashScreen;
	GameOverScreen gameOverScreen;
	Background background;
	BlackHole[] blackHoles;
	List<FeederNest> feederNests;
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
		font = new BitmapFont();

		ApplicationModeSingleton.getInstance().setGameAI(this);
		ApplicationModeSingleton.getInstance().setApplicationMode(ApplicationModeEnum.SPLASH_SCREEN);
		ApplicationModeSingleton.getInstance().setGameDifficulty(Difficulty.EASY);

		//Create the debug object
		debug = new Debug(this);
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
					gameOverScreenInitialized = false;
					resetGame();
				}
				break;
		}

		//Draw all of the game components
		renderedObjects.forEach(RenderedObject::draw);

		if (ApplicationModeSingleton.getInstance().getApplicationMode().equals(ApplicationModeEnum.SPLASH_SCREEN)) {
			drawDifficultyText(ApplicationModeSingleton.getInstance().getGameDifficulty());
		}

		//Handle sensor output periodically
//		if (ApplicationModeSingleton.getInstance().getApplicationMode().equals(ApplicationModeEnum.EVALUATE_SENSORS)
//				&& renderCount % outputUpdateRate == 0) {
//			debug.printSensorOutput();
//		}

		batch.end();
		renderCount++;
	}

	/**
	 * This function creates all of the necessary game components to start the game
	 * It is called immediately after the user navigates away from the splash screen or when we reset the game after a game over
	 */
	private void initializeGame() {

		//Initialize the game objects
		background = new Background(this);
		character = new Character(batch, 800, 100, 270);

		gameObjects.add(character);

		//Add all game components to the rendered object list to be drawn
		renderedObjects.add(background);
		renderedObjects.add(character);

		initializeFeederNests();

		//Create the Navigation Graph
		navigationGraph = new NavigationGraph();
		AStar.setNavigationGraph(navigationGraph.getGraph());
		AStar.setGameObjects(gameObjects);

		blackHoles = new BlackHole[NUM_BLACK_HOLES];
		blackHoleIndex = 0;
		inputProcessor.setNewClickToProcess(false);
	}

	//Todo: finish all of this logic
	private void resetGame() {
		ApplicationModeSingleton.getInstance().setApplicationMode(ApplicationModeEnum.SETUP);
		renderedObjects = new ArrayList<>();
		gameObjects = new ArrayList<>();
		initializeGame();
	}

	/**
	 * Helper function to spawn the feeder nests
	 * We will do this at the start of the game
	 * Nests are placed randomly within some bounds
	 */
	private void initializeFeederNests() {

		feederNests = new ArrayList<>();

		switch (ApplicationModeSingleton.getInstance().getGameDifficulty()) {
			//The number of nests in the game is going to be based on the difficulty
			case EASY:
				feederNests.add(createNewNest(Feeder.FeederBreedEnum.BREED_A));
				break;
			case MEDIUM:
				feederNests.add(createNewNest(Feeder.FeederBreedEnum.BREED_A));
				feederNests.add(createNewNest(Feeder.FeederBreedEnum.BREED_B));
				break;
			case HARD:
				feederNests.add(createNewNest(Feeder.FeederBreedEnum.BREED_A));
				feederNests.add(createNewNest(Feeder.FeederBreedEnum.BREED_B));
				break;
			case BRUTAL:
				feederNests.add(createNewNest(Feeder.FeederBreedEnum.BREED_A));
				feederNests.add(createNewNest(Feeder.FeederBreedEnum.BREED_B));
				feederNests.add(createNewNest(Feeder.FeederBreedEnum.BREED_C));
				break;
		}
	}

	/**
	 * Private function to create a feeder nest within a given bounds
	 * We then have to look at where we placed it and make sure that is not too close to other game objects
	 * @return the created feeder nest
	 */
	private FeederNest createNewNest(Feeder.FeederBreedEnum breed) {

		FeederNest nest = null;
		Random random = new Random();
		int x, y;
		boolean validPlacement = false;
		String imageName = "";

		switch (breed) {
			case BREED_A:
				imageName = NEST_IMAGE_NAME_A;
				break;
			case BREED_B:
				imageName = NEST_IMAGE_NAME_B;
				break;
			case BREED_C:
				imageName = NEST_IMAGE_NAME_C;
				break;
		}

		while (!validPlacement) {
			//Now we need to come up with some coordinates
			//We will pick a random coordinate within the buffer distance of the game bounds
			x = random.nextInt((int) bounds.getWidth() - (2 * NEST_PLACEMENT_BUFFER_DISTANCE) - FeederNest.getWIDTH());
			x += NEST_PLACEMENT_BUFFER_DISTANCE;
			y = random.nextInt((int) bounds.getHeight() - (2 * NEST_PLACEMENT_BUFFER_DISTANCE) - FeederNest.getHEIGHT());
			y += NEST_PLACEMENT_BUFFER_DISTANCE;

			nest = new FeederNest(imageName, batch, x, y, 270, breed);

			validPlacement = nest.isValidPlacement(gameObjects);
		}

		//We are using the game objects list to check if the placement of a nest is valid
		//For this reason, we must add this new object to the game objects list here
		renderedObjects.add(nest);
		gameObjects.add(nest);
		return nest;
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

		//Make sure that the black hole is within the appropriate bounds
		//The position that we found is the bottom left of the center of the black hole (the region that we use to detect
		//collisions for determining if an entity has been consumed by the black hole)
		//We need to find the bottom left of the actual black hole texture to see if the placement is within the correct bounds
		Vector2 texturePosition = new Vector2();
		texturePosition.set(blackHole.getPosition().x - ((blackHole.getTexture().getWidth()/2) - (blackHole.getWidth()/2)),
				            blackHole.getPosition().y - ((blackHole.getTexture().getHeight()/2) - (blackHole.getHeight()/2)));
		if (texturePosition.x < 0 || texturePosition.y < 0) {
			return false;
		}
		if (texturePosition.x > bounds.getWidth() || texturePosition.y > bounds.getHeight()) {
			return false;
		}
		if (!blackHole.isValidPlacement(gameObjects)) {
			return false;
		}

		//Add it to be rendered before the character
		renderedObjects.add(renderedObjects.indexOf(character), blackHole);
		gameObjects.add(blackHole);
		blackHoles[blackHoleIndex] = blackHole;
		return true;
	}

	public void drawDifficultyText(Difficulty difficulty) {
		String difficultyString = "";

		switch (difficulty) {
			case EASY:
				font.setColor(Color.GREEN);
				difficultyString = "Easy";
				break;
			case MEDIUM:
				font.setColor(Color.YELLOW);
				difficultyString = "Medium";
				break;
			case HARD:
				font.setColor(Color.RED);
				difficultyString = "Hard";
				break;
			case BRUTAL:
				font.setColor(Color.BLACK);
				difficultyString = "Brutal";
				break;
		}

		//Only able to change difficulty in splash screen, so only draw it in the splash screen
		if (ApplicationModeSingleton.getInstance().getApplicationMode().equals(ApplicationModeEnum.SPLASH_SCREEN)) {
			font.draw(batch, difficultyString, DIFFICULTY_DRAW_POSITION.x, DIFFICULTY_DRAW_POSITION.y);
		}
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
