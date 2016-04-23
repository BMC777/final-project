package com.ucf.aigame;

import com.badlogic.gdx.math.Intersector;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Bryan on 2/8/2016.
 */
public class CollisionDetector
{
    private Intersector intersector;

    private PlayerEntity playerEntity;

    private ArrayList<WallObject> wallList;
    private ArrayList<GameEntity> entityList;

    private Rectangle currentPlayerCollisionBox;
    private Rectangle currentWallCollisionBox;
    private Rectangle currentEntityCollisionBox;

    private Vector2 leftPoint;
    private Vector2 rightPoint;
    private Vector2 upPoint;
    private Vector2 downPoint;
    private Vector2 centerPoint;
    private Vector2 scalingSensor;

    private boolean[] collisionDetectionArray;

    CollisionDetector ( GameWorld gameWorld, DungeonGenerator dungeonGenerator )
    {
        /*// Get all GameWorld Objects
        this.playerEntity = gameWorld.getPlayerEntity();
        this.wallList = gameWorld.getWallList();
        this.entityList = gameWorld.getEntityList();

        leftPoint = new Vector2();
        rightPoint = new Vector2();
        upPoint = new Vector2();
        downPoint = new Vector2();
        centerPoint = new Vector2();
        scalingSensor = new Vector2();

        intersector = new Intersector();
        collisionDetectionArray = new boolean[4];*/
    }

    /*public void checkCollisions()
    {
        // Reset all collision detections
        for (int x = 0; x < 4; x++)
        {
            collisionDetectionArray[x] = false;
        }

        currentPlayerCollisionBox = playerEntity.getCollisionBox();

        for (int i = 0; i < wallList.size(); i++)
        {
            currentWallCollisionBox = wallList.get(i).getCollisionBox();

            if (i < entityList.size())
            {
                currentEntityCollisionBox = entityList.get(i).getCollisionBox();
            }

            //Looking for player collision with walls
            if (intersector.overlaps(currentPlayerCollisionBox, currentWallCollisionBox) || intersector.overlaps(currentPlayerCollisionBox, currentEntityCollisionBox))
            {
                playerEntity.setCollisionNotification(true);

                if (intersector.overlaps(currentPlayerCollisionBox, currentWallCollisionBox))
                {
                    currentWallCollisionBox.getCenter(centerPoint);
                    upPoint.set(centerPoint).add(0, currentWallCollisionBox.getHeight() / 2);
                    downPoint.set(centerPoint).sub(0, currentWallCollisionBox.getHeight() / 2);
                    leftPoint.set(centerPoint).sub(currentWallCollisionBox.getWidth() / 2, 0);
                    rightPoint.set(centerPoint).add(currentWallCollisionBox.getWidth() / 2, 0);
                }
                else
                {
                    currentEntityCollisionBox.getCenter(centerPoint);
                    upPoint.set(centerPoint).add(0, currentEntityCollisionBox.getHeight() / 2);
                    downPoint.set(centerPoint).sub(0, currentEntityCollisionBox.getHeight() / 2);
                    leftPoint.set(centerPoint).sub(currentEntityCollisionBox.getWidth() / 2, 0);
                    rightPoint.set(centerPoint).add(currentEntityCollisionBox.getWidth() / 2, 0);
                }

                if (currentPlayerCollisionBox.contains(rightPoint))
                {
                    collisionDetectionArray[2] = true;
                }

                if (currentPlayerCollisionBox.contains(leftPoint))
                {
                    collisionDetectionArray[3] = true;
                }

                if (currentPlayerCollisionBox.contains(upPoint))
                {
                    collisionDetectionArray[1] = true;
                }

                if (currentPlayerCollisionBox.contains(downPoint))
                {
                    collisionDetectionArray[0] = true;
                }

                playerEntity.setCollisionDetection(collisionDetectionArray[0], collisionDetectionArray[1],
                        collisionDetectionArray[2], collisionDetectionArray[3]);
            }

            // Player wall sensors 'feeling' for walls in the distance
            for (int j = 0; j < playerEntity.getWallSensorArray().length; j++)
            {
                scalingSensor.set(playerEntity.getWallSensorArray()[j]);
                scalingSensor.nor();
                Vector2 tempSensorOrigin = new Vector2(playerEntity.getWallSensorOriginX(), playerEntity.getWallSensorOriginY());
                Vector2 tempSensorEndpoints = new Vector2(playerEntity.getWallSensorEndpointX(j), playerEntity.getWallSensorEndpointY(j));
                Vector2 length = new Vector2();
                boolean changedSensor = false;


                while (currentWallCollisionBox.contains(tempSensorEndpoints))
                {
                    tempSensorEndpoints.sub(scalingSensor);
                    changedSensor = true;
                }

                length.set(tempSensorEndpoints.sub(tempSensorOrigin));

                if (changedSensor)
                {
                    playerEntity.setWallSensorLength(length.len(), j);
                }

                playerEntity.setWallSensorCollisionIndex(j, changedSensor);
            }

        }*/

    	/*
        // Cycle through all non-player entities in the game
        for (int i = 0; i < entityList.size(); i++) {
            // Check collision between AdjacentAgentSensor and GameEntity
            if (intersector.overlaps(playerEntity.getAdjecentAgentSensor(), entityList.get(i).getCollisionBox())) {
                entityList.get(i).setDetection(true);
            }
            else {
                entityList.get(i).setDetection(false);
            }
        }
    }*/
}
