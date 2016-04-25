package com.ucf.aigame;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by Bryan on 1/21/2016.
 */
public class InputHandler implements InputProcessor
{
    private PlayerEntity playerEntity;
    private GameEntity gameEntity;
    private GameWorld gameWorld;
    private OrthographicCamera camera;

    public InputHandler( PlayerEntity playerEntity, GameWorld gameWorld, OrthographicCamera camera )
    {
        this.playerEntity = playerEntity;
        this.gameWorld = gameWorld;
        this.camera = camera;
    }

    @Override
    public boolean keyDown(int keycode)
    {
        switch(keycode)
        {
            case Keys.W:
                playerEntity.moveUp();
                return true;

            case Keys.A:
                playerEntity.moveLeft();
                return true;

            case Keys.S:
                playerEntity.moveDown();
                return true;

            case Keys.D:
                playerEntity.moveRight();
                return true;

            case Keys.X:
                gameWorld.dropTreasure();

                return true;

            case Keys.T:
                for (int i=0; i<gameWorld.getEntityList().size(); i++)
                {
                    gameWorld.getEntityList().get(i).test();
                }
                return true;
        }

        return false;
    }

    @Override
    public boolean keyUp(int keycode)
    {
        switch(keycode)
        {
            case Keys.W:
                playerEntity.moveDown();
                return true;

            case Keys.A:
                playerEntity.moveRight();
                return true;

            case Keys.S:
                playerEntity.moveUp();
                return true;

            case Keys.D:
                playerEntity.moveLeft();
                return true;
        }

        return false;
    }

    @Override
    public boolean keyTyped(char character)
    {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button)
    {
    	/*
        if (button == Input.Buttons.LEFT && debugger.getDebugDisplayState() && debugger.getWallToolStatus())
        {
            debugger.placeWallTool(screenX, 640 - screenY);
            return true;
        }

        if (button == Input.Buttons.LEFT && debugger.getDebugDisplayState() && debugger.getEntityToolStatus())
        {
            debugger.placeEntityTool(screenX, 640 - screenY);
            return true;
        }*/

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button)
    {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer)
    {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY)
    {
    	float x = screenX;
    	float y = screenY;
    	float z = 0;
    	
    	Vector3 mouseVector = new Vector3( x, y, z );
    	camera.unproject( mouseVector );
    	
    	playerEntity.rotateToFaceMouse( mouseVector.x, mouseVector.y );
    	
        return true;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
