package com.ucf.aigame;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Bryan on 2/7/2016.
 */
public class WallSensor
{
    private Vector2 wallSensorArray[];
    private float wallSensorRange;

    private boolean collisionArray[];
    private float lengthArray[];

    private static final int NUMBER_OF_RAYS = 5;

    WallSensor(float wallSensorRange)
    {
        this.wallSensorRange = wallSensorRange;

        wallSensorArray = new Vector2[NUMBER_OF_RAYS];
        collisionArray = new boolean[NUMBER_OF_RAYS];
        lengthArray = new float[NUMBER_OF_RAYS];

        for (int i = 0; i < NUMBER_OF_RAYS; i++)
        {
            wallSensorArray[i] = new Vector2();
            collisionArray[i] = false;
            lengthArray[i] = wallSensorRange;
        }
    }

    public void update(Vector2 currentHeading)
    {
        float rotateSensorDegrees = 60;

        for (int i = 0; i < NUMBER_OF_RAYS; i++)
        {

            wallSensorArray[i].set(currentHeading);
            wallSensorArray[i].rotate(rotateSensorDegrees);

            // If no collision
            if(!collisionArray[i] && !(lengthArray[i] > wallSensorRange))
            {
                lengthArray[i] += 2;

                if (lengthArray[i] >= wallSensorRange)
                {
                    lengthArray[i] = 0;
                }
            }

            wallSensorArray[i].setLength(lengthArray[i]);



            rotateSensorDegrees -= 30;
        }
    }

    public Vector2 getSensor(int i)
    {
        return wallSensorArray[i];
    }

    public Vector2[] getWallSensorArray()
    {
        return wallSensorArray;
    }

    public void setLength(float length, int sensorNumber)
    {
        lengthArray[sensorNumber] = length;
    }

    public float[] getLengthArray()
    {
        return lengthArray;
    }

    public boolean[] getCollisionArray()
    {
        return collisionArray;
    }

    public boolean getCollisionValue(int index)
    {
        return collisionArray[index];
    }

    public void setCollisionArrayIndex(int index, boolean value)
    {
        collisionArray[index] = value;
    }
}
