package com.ucf.aigame;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by Bryan on 1/21/2016.
 */
public class GameScreen implements Screen
{
	private static final int TILE_DIMENSIONS = 16;
	
    private GameWorld gameWorld;
    private DungeonGenerator dungeonGenerator;
    private GameRenderer gameRenderer;
    private CollisionDetector collisionDetector;
    private Debugger debugger;

    private float runTime;

    public GameScreen() throws IOException
    {
        // Set dimensions
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        int midPointX = (int)(screenWidth / 2);
        int midPointY = (int)(screenHeight / 2);

        // Instantiate universal objects
        gameWorld = new GameWorld(midPointX, midPointY, screenWidth, screenHeight);
        dungeonGenerator = new DungeonGenerator((int)( screenWidth / TILE_DIMENSIONS ), (int)( screenHeight / TILE_DIMENSIONS ));
        collisionDetector = new CollisionDetector( gameWorld );
        debugger = new Debugger( gameWorld );
        gameRenderer = new GameRenderer( gameWorld, dungeonGenerator, debugger, screenWidth, screenHeight );

        PlayerEntity playerEntity = gameWorld.getPlayerEntity();

        // For player input
        InputHandler inputHandler = new InputHandler( playerEntity, debugger );
        Gdx.input.setInputProcessor(inputHandler);
    }

    @Override
    public void show()
    {

    }

    @Override
    public void render(float delta)
    {
        runTime += delta;
        gameWorld.update(delta);
        collisionDetector.checkCollisions();
        gameRenderer.render(runTime);
        debugger.update();
    }

    @Override
    public void resize(int width, int height)
    {

    }

    @Override
    public void pause()
    {

    }

    @Override
    public void resume()
    {

    }

    @Override
    public void hide()
    {

    }

    @Override
    public void dispose()
    {

    }
}
