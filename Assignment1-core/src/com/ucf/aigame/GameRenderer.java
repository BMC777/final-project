package com.ucf.aigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Bryan on 1/21/2016.
 */
public class GameRenderer
{
	private static final int TILE_DIMENSIONS = 16;
	
    private GameWorld gameWorld;
    private OrthographicCamera camera;
    private SpriteBatch batcher;
    private ShapeRenderer shapeRenderer;
    private BitmapFont bitmapFont;
    private Debugger debugger;
    
    private boolean[][] dungeonMap;
    private boolean[][] floorMap;

    private TextureRegion playerEntityTextureRegion;
    private TextureRegion gameEntityTextureRegion;
    private Texture floorTileTexture;
    private Texture wallTileTexture;

    private PlayerEntity playerEntity;
    
    // Cave Walls
    private static TextureRegion topMiddleCaveWall;
    private static TextureRegion topLeftCaveWall;
    private static TextureRegion topRightCaveWall;
    private static TextureRegion middleLeftCaveWall;
    private static TextureRegion middleRightCaveWall;
    private static TextureRegion bottomMiddleCaveWall;
    private static TextureRegion bottomLeftCaveWall;
    private static TextureRegion bottomRightCaveWall;
    
    // Dirt Floors
    private static TextureRegion dirtFloor3;

    private float gameWidth;
    private float gameHeight;

    private float tempFloat = 0;

    public GameRenderer(GameWorld gameWorld, DungeonGenerator dungeonGenerator, Debugger debugger, float gameWidth, float gameHeight)
    {
        this.gameWorld = gameWorld;
        this.debugger = debugger;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        dungeonMap = dungeonGenerator.getDungeonMap();
        floorMap = dungeonGenerator.getFloorMap();

        //Sets Camera to Orthographic for 2D view of the screen.
        camera = new OrthographicCamera();
        //false so y increases as it goes up instead of down, bottom left corner is coordinate (0,0)
        camera.setToOrtho(false, gameWidth, gameHeight);

        //Telling SpriteBatch and shapeRenderer to use camera's coordinates when drawing Sprites
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix(camera.combined);

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(camera.combined);

        bitmapFont = new BitmapFont();

        //initializeGameAssets();
        initializeAssets();
    }

    public void render(float runTime)
    {
        //OpenGL graphics stuff
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batcher.begin();
        
        for ( int y = 0; y < gameHeight / TILE_DIMENSIONS; y++ )
        {
        	for ( int x = 0; x < gameWidth / TILE_DIMENSIONS; x++ )
        	{
        		if ( dungeonMap[y][x] == true )
        		{
            		batcher.draw( topMiddleCaveWall, x * TILE_DIMENSIONS, y * TILE_DIMENSIONS );
        		}
        		
        		if ( floorMap[y][x] == true )
        		{
        			batcher.draw( dirtFloor3, x * TILE_DIMENSIONS, y * TILE_DIMENSIONS );
        		}
        	}
        }
        
        batcher.end();
    }

        /*renderBackground();

        renderPlayerEntity();
        renderGameEntities();

        // If debugger is on, render sensor output
        if (debugger.getDebugDisplayState())
        {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            renderGraphNodes();

            renderWallSensor();
            renderAdjacentAgentSensors();
            renderPieSliceSensor();

            renderPlayerInformation();
            renderToolStates();


            Gdx.gl.glDisable(GL20.GL_BLEND);
        }

        renderDebugState();
    }

    private void renderBackground()
    {
        batcher.begin();

        //Fills the screen with floor tiles.
        for (int x = 0; x < gameWidth; x += TILE_DIMENSIONS)
        {
            for (int y = 0; y < gameHeight; y += TILE_DIMENSIONS)
            {
                    batcher.draw(floorTileTexture, x, y);
            }
        }

        for (int i = 0; i < gameWorld.getWallList().size(); i++)
        {
            batcher.draw(wallTileTexture, gameWorld.getWallList().get(i).getXWorldPosition(), gameWorld.getWallList().get(i).getYWorldPosition());
        }


        batcher.end();
    }

    private void renderPlayerEntity()
    {
        batcher.begin();

        //Drawing the playerEntityTexture
        batcher.draw(playerEntityTextureRegion, playerEntity.getCurrentXPosition(), playerEntity.getCurrentYPosition(),
                playerEntity.getXPlayerOrigin(), playerEntity.getYPlayerOrigin(), playerEntity.getWidth(), playerEntity.getHeight(),
                1, 1, playerEntity.getRotationAngle());

        batcher.end();
    }

    private void renderPlayerInformation()
    {
        batcher.begin();
        bitmapFont.setColor(255f, 255f, 255f, 1);

        bitmapFont.draw(batcher, "Heading: " + playerEntity.getCurrentHeading().toString(), playerEntity.getCurrentXPosition() - 16, playerEntity.getCurrentYPosition());
        bitmapFont.draw(batcher, "Position:  (" + Float.toString(playerEntity.getCurrentXPosition()) + ", " +
                Float.toString(playerEntity.getCurrentYPosition()) + ")", playerEntity.getCurrentXPosition() - 16, playerEntity.getCurrentYPosition() - 16);

        batcher.end();
    }

    private void renderGameEntities()
    {
        batcher.begin();

        for (int i = 0; i < gameWorld.getEntityList().size(); i++)
        {

            batcher.draw(gameEntityTextureRegion, gameWorld.getEntityList().get(i).getCurrentXPosition(),
                    gameWorld.getEntityList().get(i).getCurrentYPosition(),
                    gameWorld.getEntityList().get(i).getXEntityOrigin(), gameWorld.getEntityList().get(i).getYEntityOrigin(),
                    gameWorld.getEntityList().get(i).getWidth(), gameWorld.getEntityList().get(i).getHeight(),
                    1, 1, gameWorld.getEntityList().get(i).getRotationAngle());
        }

        batcher.end();
    }

    private void renderGraphNodes() {

        Color nodeColor = Color.CYAN;
        Color pathColor = Color.ORANGE;
        Color targetColor = Color.YELLOW;

        int circleSize = 2;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(nodeColor);

        for (int i = 0; i < gameWorld.getGraphNodeList().size(); i++) {

            // Regular node
            if ( !(gameWorld.getGraphNodeList().get(i).getxWorldPosition() == gameWorld.getPlayerEntity().getAStarSearch().getGoalLocation().x
                    && gameWorld.getGraphNodeList().get(i).getyWorldPosition() == gameWorld.getPlayerEntity().getAStarSearch().getGoalLocation().y) ) {

                if ( gameWorld.getGraphNodeList().get(i).isVisited() ) {
                    shapeRenderer.setColor(pathColor);
                    circleSize = 5;
                }
                else {
                    shapeRenderer.setColor(nodeColor);
                    circleSize  = 2;
                }

                shapeRenderer.circle(gameWorld.getGraphNodeList().get(i).getxWorldPosition(),
                        gameWorld.getGraphNodeList().get(i).getyWorldPosition(), circleSize);
            }
            // Node matches Target Node; show difference (defined by playerEntity, stored in aStarSearch)
            else {
                circleSize = 10;
                shapeRenderer.setColor(targetColor);

                shapeRenderer.circle(gameWorld.getGraphNodeList().get(i).getxWorldPosition(),
                        gameWorld.getGraphNodeList().get(i).getyWorldPosition(), circleSize);

            }
        }

        shapeRenderer.end();

    }

    private void renderWallSensor()
    {
        int lineHeightTextOffset = 0;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(gameWidth - 32 * 8, 32, 32 * 7, 32 * 3);
        shapeRenderer.setColor(0/255f, 34/255f, 255/255f, 0.5f);
        shapeRenderer.rect((gameWidth + 2) - 32 * 8,2 + 32, (32 * 7) - 4, (32 * 3) - 4);
        shapeRenderer.end();

        for (int i = 0; i < playerEntity.getWallSensorArray().length; i++)
        {
            // Draw sensors
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(255/255f, 180/255f, 0, 0.5f);

            shapeRenderer.rectLine(playerEntity.getWallSensorOriginX(), playerEntity.getWallSensorOriginY(),
                    playerEntity.getWallSensorEndpointX(i), playerEntity.getWallSensorEndpointY(i), 1);

            shapeRenderer.circle(playerEntity.getWallSensorEndpointX(i), playerEntity.getWallSensorEndpointY(i), 4);

            shapeRenderer.end();

            // Render sensor values
            batcher.begin();

            bitmapFont.setColor(255/255f, 180/255f, 0, 1);

            bitmapFont.draw(batcher, Integer.toString(i), playerEntity.getWallSensorEndpointX(i) + 8, playerEntity.getWallSensorEndpointY(i) + 8);
            bitmapFont.draw(batcher, "Wall Sensor " + Integer.toString(i) + " Length: "
                    + Float.toString(debugger.getWallSensorLengthOutput(i)), (gameWidth + 4) - 32 * 8, 32 * 3.5f + 6 - lineHeightTextOffset);


            lineHeightTextOffset += 16;
            batcher.end();
        }
    }

    private void renderAdjacentAgentSensors()
    {
        // For AdjacentAgentSensor
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // Draw AdjacentAgentSensor Circle
        shapeRenderer.circle(playerEntity.getAdjecentAgentSensor().x, playerEntity.getAdjecentAgentSensor().y,
                playerEntity.getAdjecentAgentSensor().radius);

        // Check all GameEntities
        for (int i = 0; i < gameWorld.getEntityList().size(); i++) {

            // Check if detected by AdjacentAgentSensor
            if ( gameWorld.getEntityList().get(i).isDetected() ) {
                shapeRenderer.setColor(255, 0, 0, 0.5f);

                // Draw Circle
                shapeRenderer.circle(gameWorld.getEntityList().get(i).getEntityCenter().x,
                        gameWorld.getEntityList().get(i).getEntityCenter().y,
                        gameWorld.getEntityList().get(i).getWidth());

                // Draw Relative Heading
                shapeRenderer.rectLine(playerEntity.getvOrigin(), gameWorld.getEntityList().get(i).getEntityCenter(), 1);
            }
        }

        shapeRenderer.end();
    }

    private void renderPieSliceSensor()
    {
        for (int i = 0; i < gameWorld.getEntityList().size(); i++) {

            // Check if detected by AdjacentAgentSensor
            if ( gameWorld.getEntityList().get(i).isDetected() ) {
                shapeRenderer.setColor(255, 0, 0, 0.5f);

                // For: PieSliceSensor (detected)
                // Identifies Quadrant and increments its Activation Level
                playerEntity.getPieSliceSensor().identifyQuadrant(
                        playerEntity.getCurrentHeading(),
                        gameWorld.getEntityList().get(i).getEntityCenter().sub(playerEntity.getvOrigin()));
            }
        }
        // Activation Levels have been tallied, Now get highest Activation Level and draw / color lines

        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);

        // FRONT RIGHT
        tempFloat = Math.max(playerEntity.getPieSliceSensor().getActivationLevelFRONT(), playerEntity.getPieSliceSensor().getActivationLevelRIGHT());
        if (tempFloat < 1) { // Green
            shapeRenderer.setColor(0, 255, 0, 0.5f);
        }
        else if (tempFloat == 1) { // Yellow
            shapeRenderer.setColor(255, 255, 0, 0.5f);
        }
        else { // Red
            shapeRenderer.setColor(255, 0, 0, 0.5f);
        }
        shapeRenderer.rectLine(playerEntity.getvOrigin(),
                playerEntity.getPieSliceSensor().getFrontRight().add(playerEntity.getvOrigin()), 0.5f);

        // FRONT LEFT
        tempFloat = Math.max(playerEntity.getPieSliceSensor().getActivationLevelFRONT(), playerEntity.getPieSliceSensor().getActivationLevelLEFT());
        if (tempFloat < 1) { // Green
            shapeRenderer.setColor(0, 255, 0, 1);
        }
        else if (tempFloat == 1) { // Yellow
            shapeRenderer.setColor(255, 255, 0, 1);
        }
        else { // Red
            shapeRenderer.setColor(255, 0, 0, 1);
        }
        shapeRenderer.rectLine(playerEntity.getvOrigin(),
                playerEntity.getPieSliceSensor().getFrontLeft().add(playerEntity.getvOrigin()), 0.5f);

        // BACK LEFT
        tempFloat = Math.max(playerEntity.getPieSliceSensor().getActivationLevelBACK(), playerEntity.getPieSliceSensor().getActivationLevelLEFT());
        if (tempFloat < 1) { // Green
            shapeRenderer.setColor(0, 255, 0, 0.5f);
        }
        else if (tempFloat == 1) { // Yellow
            shapeRenderer.setColor(255, 255, 0, 0.5f);
        }
        else { // Red
            shapeRenderer.setColor(255, 0, 0, 0.5f);
        }
        shapeRenderer.rectLine(playerEntity.getvOrigin(),
                playerEntity.getPieSliceSensor().getBackLeft().add(playerEntity.getvOrigin()), 0.5f);

        // BACK RIGHT
        tempFloat = Math.max(playerEntity.getPieSliceSensor().getActivationLevelBACK(), playerEntity.getPieSliceSensor().getActivationLevelRIGHT());
        if (tempFloat < 1) { // Green
            shapeRenderer.setColor(0, 255, 0, 0.5f);
        }
        else if (tempFloat == 1) { // Yellow
            shapeRenderer.setColor(255, 255, 0, 0.5f);
        }
        else { // Red
            shapeRenderer.setColor(255, 0, 0, 0.5f);
        }
        shapeRenderer.rectLine(playerEntity.getvOrigin(),
                playerEntity.getPieSliceSensor().getBackRight().add(playerEntity.getvOrigin()), 0.5f);


        // Reset Color to White and Reset PieSliceSensors
        shapeRenderer.setColor(255, 255, 255, 0.5f);
        playerEntity.getPieSliceSensor().resetActivationLevels();

        shapeRenderer.end();
    }

    private void renderToolStates()
    {
        // Render box as font background to debug text info
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(128, 0, 132, 24);
        shapeRenderer.rect(258, 0, 132, 24);
        shapeRenderer.setColor(0/255f, 34/255f, 255/255f, 0.5f);
        shapeRenderer.rect(130, 2, 128, 20);
        shapeRenderer.rect(260, 2, 128, 20);

        shapeRenderer.end();

        // Render debug text info
        batcher.begin();

        if (debugger.getEntityToolStatus())
        {
            bitmapFont.setColor(0/255f, 255/255f, 43/255f, 1);
            bitmapFont.draw(batcher, "[O] Entity Tool ON", 133, 18);
        }
        else
        {
            bitmapFont.setColor(255/255f, 0/255f, 0/255f, 1);
            bitmapFont.draw(batcher, "[O] Entity Tool OFF", 133, 18);
        }

        if (debugger.getWallToolStatus())
        {
            bitmapFont.setColor(0/255f, 255/255f, 43/255f, 1);
            bitmapFont.draw(batcher, "[P] Wall Tool ON", 263, 18);
        }
        else
        {
            bitmapFont.setColor(255/255f, 0/255f, 0/255f, 1);
            bitmapFont.draw(batcher, "[P] Wall Tool OFF", 263, 18);
        }

        batcher.end();
    }

    private void renderDebugState()
    {
        // Render box as font background to debug text info
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

        shapeRenderer.setColor(0, 0, 0, 0.5f);
        shapeRenderer.rect(0, 0, 128, 24);
        shapeRenderer.setColor(0/255f, 34/255f, 255/255f, 0.5f);
        shapeRenderer.rect(2, 2, 124, 20);

        shapeRenderer.end();

        // Render debug text info
        batcher.begin();

        if (debugger.getDebugDisplayState())
        {
            bitmapFont.setColor(0/255f, 255/255f, 43/255f, 1);
            bitmapFont.draw(batcher, "[V] Debugger ON", 6, 18);
        }
        else
        {
            bitmapFont.setColor(255/255f, 0/255f, 0/255f, 1);
            bitmapFont.draw(batcher, "[V] Debugger OFF", 6, 18);
        }

        batcher.end();
    }

    private void initializeGameAssets()
    {
        playerEntity = gameWorld.getPlayerEntity();
    }*/

    private void initializeAssets()
    {
        playerEntityTextureRegion = AssetLoader.playerEntityTextureRegion;
        gameEntityTextureRegion = AssetLoader.gameEntityTextureRegion;
        wallTileTexture = AssetLoader.wallTileTexture;
        floorTileTexture = AssetLoader.floorTileTexture;
        
        topMiddleCaveWall = AssetLoader.topMiddleCaveWall;
        dirtFloor3 = AssetLoader.dirtFloor3;
    }
}
