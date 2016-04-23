package com.ucf.aigame;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Bryan on 2/7/2016.
 */
public class GameEntity
{
	private static final int TILE_DIMENSIONS = 16;
	
    private BoundingBox boundingBox;
    private boolean detected;

    private Vector2 currentEntityHeading;   //Direction entity is facing (Should always be Normalized)
    private Vector2 nextEntityHeading;

    private int inputX;
    private int inputY;

    private float entityWidth;
    private float entityHeight;
    private float xEntityOrigin;
    private float yEntityOrigin;
    private float xCurrentWorldPosition;
    private float yCurrentWorldPosition;
    private float rotationAngle; //Angle between current and next Heading

    private static final Vector2 REFERENCE_VECTOR = new Vector2(1, 0);  //Normalized Vector pointing to 0 degrees

    GameEntity(float xCurrentWorldPosition, float yCurrentWorldPosition, float entityWidth, float entityHeight)
    {
        //Player Sprite dimensions
        this.entityWidth = entityWidth;
        this.entityHeight = entityHeight;

        //Center of the Sprite
        this.xEntityOrigin = entityWidth / 2;
        this.yEntityOrigin = entityHeight / 2;

        //Spawn Position
        this.xCurrentWorldPosition = xCurrentWorldPosition;
        this.yCurrentWorldPosition = yCurrentWorldPosition;

        currentEntityHeading = new Vector2(REFERENCE_VECTOR);       //Player always spawns facing 'East'
        nextEntityHeading = new Vector2(currentEntityHeading);

        boundingBox = new BoundingBox(xCurrentWorldPosition, yCurrentWorldPosition, TILE_DIMENSIONS, TILE_DIMENSIONS);

        detected = false;
    }

    public void update(float timeSinceLastUpdate)
    {
        currentEntityHeading.set(nextEntityHeading);    //Update to new calculated heading
        rotationAngle = currentEntityHeading.angle();   //Angle new heading was rotated by.

        boundingBox.setPosition(xCurrentWorldPosition, yCurrentWorldPosition);
    }

    public void rotateToFaceMouse(float xCurrentMousePosition, float yCurrentMousePosition)
    {
        //Determine the new heading vector offset by entityOrigin to align heading with center of sprite
        nextEntityHeading.x = xCurrentMousePosition - (xCurrentWorldPosition + xEntityOrigin);
        nextEntityHeading.y = yCurrentMousePosition - (yCurrentWorldPosition + yEntityOrigin);

        //Normalize the heading vector
        nextEntityHeading.nor();
    }

    public float getCurrentXPosition()
    {
        return xCurrentWorldPosition;
    }

    public float getCurrentYPosition()
    {
        return yCurrentWorldPosition;
    }

    public float getRotationAngle()
    {
        return rotationAngle;
    }

    public float getWidth()
    {
        return entityWidth;
    }

    public float getHeight()
    {
        return entityHeight;
    }

    public float getXEntityOrigin()
    {
        return xEntityOrigin;
    }

    public float getYEntityOrigin()
    {
        return yEntityOrigin;
    }

    public Vector2 getEntityCenter()
    {
        return new Vector2(xCurrentWorldPosition + xEntityOrigin, yCurrentWorldPosition + yEntityOrigin);
    }

    public Vector2 getEntityHeading()
    {
        return currentEntityHeading;
    }

    public float getWallSensorOriginY()
    {
        return yCurrentWorldPosition + yEntityOrigin;
    }

    public BoundingBox getBoundingBox()
    {
        return boundingBox;
    }

    public void setDetection(boolean newValue) {
        detected = newValue;
    }

    public boolean isDetected() {
        return detected;
    }
}
