package com.ucf.aigame;


/**
 * Created by Bryan on 2/10/2016.
 */
public class Debugger
{
    private GameWorld gameWorld;
    private boolean displayDebugger;

    private boolean wallToolStatus;
    private boolean entityToolStatus;

    private float[] wallSensorLengthArray;
    private PlayerEntity playerEntity;

    Debugger(GameWorld gameWorld)
    {
        // Instantiate objects; no  display by default
        this.gameWorld = gameWorld;
        playerEntity = gameWorld.getPlayerEntity();

        displayDebugger = false;
        wallToolStatus = false;
        entityToolStatus = false;

        wallSensorLengthArray = new float[playerEntity.getWallSensorLengthArray().length];
        for (int i = 0; i < wallSensorLengthArray.length; i++)
        {
            wallSensorLengthArray[i] = 0;
        }
    }

    public void update()
    {
        updateWallSensorLengthArray();
    }

    public void setDebugDisplayState(boolean setTo)
    {
        displayDebugger = setTo;
    }

    public boolean getDebugDisplayState()
    {
        return displayDebugger;
    }

    public float getWallSensorLengthOutput(int index)
    {
        return wallSensorLengthArray[index];
    }

    public void placeEntityTool(float x, float y)
    {
        gameWorld.newEntity(x, y);
    }

    public boolean getEntityToolStatus()
    {
        return entityToolStatus;
    }

    public void setEntityToolStatus(boolean status)
    {
        entityToolStatus = status;

        if (status)
            wallToolStatus = false;
    }

    public void placeWallTool(float x, float y)
    {
        gameWorld.newWall(x, y);
    }

    public void setWallToolStatus(boolean status)
    {
        wallToolStatus = status;

        if (status)
            entityToolStatus = false;
    }

    public boolean getWallToolStatus()
    {
        return wallToolStatus;
    }

    private void updateWallSensorLengthArray()
    {
        wallSensorLengthArray = playerEntity.getWallSensorLengthArray();
    }
}
