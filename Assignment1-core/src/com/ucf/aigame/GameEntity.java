package com.ucf.aigame;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Bryan on 2/7/2016.
 */
public class GameEntity
{
    private Rectangle collisionBox;
    private boolean detected;

    private AdjacentAgentSensor adjacentAgentSensor;

    private Vector2 currentEntityHeading;   //Direction entity is facing (Should always be Normalized)
    private Vector2 nextEntityHeading;

    private int inputX;
    private int inputY;

    private Vector2 dimensions;
    private Vector2 origin;
    private Vector2 position;
    private Vector2 centerOfEntity;
    private float rotationAngle; //Angle between current and next Heading

    private static final Vector2 REFERENCE_VECTOR = new Vector2(1, 0);  //Normalized Vector pointing to 0 degrees

    GameEntity(Vector2 position, Vector2 dimensions)
    {
        // For sake of committing

        //Player Sprite dimensions
        this.dimensions = dimensions;

        //Center of the Sprite
        this.origin = new Vector2(position.x/2, position.y/2);

        //Spawn Position
        this.position = position;

        this.centerOfEntity = new Vector2(position.x+dimensions.x/2, position.y+dimensions.y/2);

        // Headings
        currentEntityHeading = new Vector2(REFERENCE_VECTOR);       //Player always spawns facing 'East'
        nextEntityHeading = new Vector2(currentEntityHeading);

        // Collisions
        collisionBox = new Rectangle(this.position.x, this.position.y, this.dimensions.x, this.dimensions.y);
        detected = false;

        // Sensors
        adjacentAgentSensor = new AdjacentAgentSensor(dimensions.x*2, centerOfEntity);
    }

    public void update(float timeSinceLastUpdate)
    {
        currentEntityHeading.set(nextEntityHeading);    //Update to new calculated heading
        rotationAngle = currentEntityHeading.angle();   //Angle new heading was rotated by.

        collisionBox.setPosition(position.x, position.y);
    }

    public Vector2 getPositionVector()
    {
        return this.position;
    }

    public float getRotationAngle()
    {
        return rotationAngle;
    }

    public Vector2 getDimensionVector()
    {
        return dimensions;
    }

    public Vector2 getOriginVector()
    {
        return origin;
    }

    public Vector2 getEntityCenter()
    {
        return new Vector2(position.x + origin.x, position.y + origin.y);
    }

    public Circle getAdjacentAgentSensor()
    {
        return adjacentAgentSensor.getCircle();
    }

    public Vector2 getEntityHeading()
    {
        return currentEntityHeading;
    }

    public float getWallSensorOriginY()
    {
        return position.y + origin.y;
    }

    public Rectangle getCollisionBox()
    {
        return collisionBox;
    }

    public void setDetection(boolean newValue) {
        detected = newValue;
    }

    public boolean isDetected() {
        return detected;
    }
}
