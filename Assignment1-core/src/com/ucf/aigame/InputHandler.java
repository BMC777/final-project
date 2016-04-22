package com.ucf.aigame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;

/**
 * Created by Bryan on 1/21/2016.
 */
public class InputHandler implements InputProcessor
{
    private PlayerEntity playerEntity;

    public InputHandler( PlayerEntity playerEntity )
    {
        this.playerEntity = playerEntity;
        //playerEntity.setController(this);
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
    	playerEntity.rotateToFaceMouse(screenX, 640 - screenY);
    	
        return true;
    }

    @Override
    public boolean scrolled(int amount)
    {
        return false;
    }
}
