package com.ucf.aigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;

/**
 * Created by Bryan on 1/21/2016.
 */
public class GameScreen implements Screen
{
    private GameWorld gameWorld;
    private GameRenderer gameRenderer;
    private CollisionDetector collisionDetector;
    private Debugger debugger;

    private float runTime;

    public GameScreen()
    {
        // Set dimensions
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        int midPointX = (int)(screenWidth / 2);
        int midPointY = (int)(screenHeight / 2);

        // Instantiate universal objects
        gameWorld = new GameWorld(midPointX, midPointY, screenWidth, screenHeight);
        collisionDetector = new CollisionDetector(gameWorld);
        debugger = new Debugger(gameWorld);
        gameRenderer = new GameRenderer(gameWorld, debugger, screenWidth, screenHeight);

        PlayerEntity playerEntity = gameWorld.getPlayerEntity();

        // For player input
        InputHandler inputHandler = new InputHandler(playerEntity, debugger);
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
