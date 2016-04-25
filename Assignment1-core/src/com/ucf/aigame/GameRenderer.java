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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.sun.prism.image.ViewPort;

/**
 * Created by Bryan on 1/21/2016.
 */
public class GameRenderer
{
	private static final int TILE_DIMENSIONS = 16;
	
    private GameWorld gameWorld;
    private OrthographicCamera camera;
    private Viewport viewport;
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

    // Treasure
    private static Texture treasureJewel;

    private float gameWidth;
    private float gameHeight;

    private float tempFloat = 0;

    public GameRenderer(GameWorld gameWorld, DungeonGenerator dungeonGenerator, OrthographicCamera camera, float gameWidth, float gameHeight)
    {
        this.gameWorld = gameWorld;
        //this.debugger = debugger;
        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        this.playerEntity = gameWorld.getPlayerEntity();
        this.camera = camera;

        dungeonMap = dungeonGenerator.getDungeonMap();
        floorMap = dungeonGenerator.getFloorMap();

        //false so y increases as it goes up instead of down, bottom left corner is coordinate (0,0)
        camera.setToOrtho( false, 128, 128 );
        
        camera.position.set( gameWorld.getPlayerEntity().getCurrentXPosition(), gameWorld.getPlayerEntity().getCurrentYPosition(), 0 );
        camera.update();
        
        //Telling SpriteBatch and shapeRenderer to use camera's coordinates when drawing Sprites
        batcher = new SpriteBatch();
        batcher.setProjectionMatrix( camera.combined );

        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix( camera.combined );

        bitmapFont = new BitmapFont();

        //viewport = new FitViewport( gameWidth, gameHeight, camera );
        //initializeGameAssets();
        initializeAssets();
    }

    public void render(float runTime)
    {
        //OpenGL graphics stuff
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
    	//viewport.update( (int)gameWidth, (int)gameHeight );
    	camera.position.set( playerEntity.getCurrentXPosition(), playerEntity.getCurrentYPosition(), 0 );
    	camera.update();
    	
        batcher.setProjectionMatrix(camera.combined);
        shapeRenderer.setProjectionMatrix(camera.combined);
    	
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

        renderTreasure();
        //renderGraphNodes();
        renderAdjacentAgentSensors();
        renderGameEntities();
        renderPlayerEntity();
        
        shapeRenderer.begin(ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1 );
        
        for ( int i = 0; i < gameWorld.getEntityList().size(); i++ )
        {
        	shapeRenderer.rect( gameWorld.getEntityList().get(i).getBoundingBox().getX(), gameWorld.getEntityList().get(i).getBoundingBox().getY(), TILE_DIMENSIONS, TILE_DIMENSIONS );
        }
        
        for ( int i = 0; i < gameWorld.getWallList().size(); i++ )
        {
        	shapeRenderer.rect( gameWorld.getWallList().get(i).getBoundingBox().getX(), gameWorld.getWallList().get(i).getBoundingBox().getY(), TILE_DIMENSIONS, TILE_DIMENSIONS );
        }
        

        shapeRenderer.rect( gameWorld.getPlayerEntity().getBoundingBox().getX(), gameWorld.getPlayerEntity().getBoundingBox().getY(), TILE_DIMENSIONS, TILE_DIMENSIONS );

        for (int i = 0; i < gameWorld.getTreasureArrayList().size(); i++)
        {
            shapeRenderer.rect( gameWorld.getTreasureArrayList().get(i).getPosition().x, gameWorld.getTreasureArrayList().get(i).getPosition().y, TILE_DIMENSIONS, TILE_DIMENSIONS);
        }

        shapeRenderer.end();

    }

    private void renderGameEntities() {
        batcher.begin();

        for (int i = 0; i < gameWorld.getEntityList().size(); i++)
        {
            GameEntity entity = gameWorld.getEntityList().get(i);

            batcher.draw(gameEntityTextureRegion, entity.getPositionVector().x, entity.getPositionVector().y,
                    entity.getOriginVector().x, entity.getOriginVector().y,
                    entity.getDimensionVector().x, entity.getDimensionVector().y,
                    1, 1, entity.getRotationAngle());
        }

        batcher.end();
    }

    private void renderPlayerEntity() {
        batcher.begin();

        //Drawing the playerEntityTexture
        batcher.draw(playerEntityTextureRegion, playerEntity.getCurrentXPosition(), playerEntity.getCurrentYPosition(),
                playerEntity.getXPlayerOrigin(), playerEntity.getYPlayerOrigin(), TILE_DIMENSIONS, TILE_DIMENSIONS,
                1, 1, playerEntity.getRotationAngle());

        batcher.end();
    }

    private void renderAdjacentAgentSensors() {
        // For AdjacentAgentSensor
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(1, 1, 0, 1 );


        // ------------------------------------------------------------------
        /*/ Draw AdjacentAgentSensor Circle
        shapeRenderer.circle(playerEntity.getAdjecentAgentSensor().x, playerEntity.getAdjecentAgentSensor().y,
                playerEntity.getAdjecentAgentSensor().radius);

        // Check all GameEntities
        for (int i = 0; i < gameWorld.getEntityList().size(); i++) {

            // Check if detected by AdjacentAgentSensor
            if ( gameWorld.getEntityList().get(i).isAlerted() ) {
                shapeRenderer.setColor(255, 0, 0, 0.5f);

                // Draw Circle
                shapeRenderer.circle(gameWorld.getEntityList().get(i).getEntityCenter().x,
                        gameWorld.getEntityList().get(i).getEntityCenter().y,
                        gameWorld.getEntityList().get(i).getWidth());

                // Draw Relative Heading
                shapeRenderer.rectLine(playerEntity.getvOrigin(), gameWorld.getEntityList().get(i).getEntityCenter(), 1);
            }
        } // */
        // -------------------------------------------------------------------

        for (int i = 0; i < gameWorld.getEntityList().size(); i++)
        {
            GameEntity entity = gameWorld.getEntityList().get(i);

            if ( entity.isAlerted() ) {
            	System.out.println("HELLO!!!!");
                shapeRenderer.setColor(Color.RED);
            }

            shapeRenderer.circle(entity.getAdjacentAgentSensor().x, entity.getAdjacentAgentSensor().y,
                    entity.getAdjacentAgentSensor().radius);

            if ( entity.isAlerted() ) {
                shapeRenderer.setColor(1, 1, 0, 1 );
            }
        }

        shapeRenderer.end();
    }

    private void renderGraphNodes() {

        /*
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
        // */

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.CYAN);

        for (int i = 0; i < gameWorld.getGraphNodeList().size(); i++)
        {
            GraphNode graphNode = gameWorld.getGraphNodeList().get(i);

            shapeRenderer.circle(graphNode.getCenteredPos().x, graphNode.getCenteredPos().y, 2);
        }

        shapeRenderer.end();

    }

    private void renderTreasure() {
        batcher.begin();

        // Go through uncollected Treasure
        for (int i=0; i<gameWorld.getTreasureArrayList().size(); i++) {
            Treasure treasure = gameWorld.getTreasureArrayList().get(i);

            // Draw Treasure
            batcher.draw(treasureJewel, treasure.getPosition().x, treasure.getPosition().y,
                    treasure.getDimensions().x, treasure.getDimensions().y);
        }

        batcher.end();
    }
    
    private void initializeAssets()
    {
        playerEntityTextureRegion = AssetLoader.playerEntityTextureRegion;
        gameEntityTextureRegion = AssetLoader.gameEntityTextureRegion;
        wallTileTexture = AssetLoader.wallTileTexture;
        floorTileTexture = AssetLoader.floorTileTexture;
        
        topMiddleCaveWall = AssetLoader.topMiddleCaveWall;
        dirtFloor3 = AssetLoader.dirtFloor3;

        treasureJewel = AssetLoader.treasureJewel;
    }
}
