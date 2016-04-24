package com.ucf.aigame;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Bryan on 2/7/2016.
 */
public class GameEntity
{
	private static final int TILE_DIMENSIONS = 16;
	
    private BoundingBox boundingBox;
    private boolean alerted;

    private AdjacentAgentSensor adjacentAgentSensor;
    private Movement movement;
    private GameWorld gameWorld;

    private Vector2 currentEntityHeading;   //Direction entity is facing (Should always be Normalized)
    private Vector2 nextEntityHeading;
    private Vector2 currentEntityVelocity;
    private Vector2 nextEntityVelocity;

    private int inputX;
    private int inputY;

    private Vector2 dimensions;
    private Vector2 origin;
    private Vector2 position;
    private Vector2 centerOfEntity;
    private float rotationAngle; //Angle between current and next Heading

    private static final Vector2 REFERENCE_VECTOR = new Vector2(1, 0);  //Normalized Vector pointing to 0 degrees
    private static final float BASE_VELOCITY = 20;
    private static final float MAX_SPEED = 60;

    GameEntity(Vector2 position, Vector2 dimensions, GameWorld gameWorld)
    {
        //Player Sprite dimensions
        this.dimensions = dimensions;

        //Center of the Sprite
        this.origin = new Vector2(dimensions.x/2, dimensions.y/2); //new Vector2(position.x/2, position.y/2);

        //Spawn Position
        this.position = position;

        this.centerOfEntity = new Vector2(position.x+dimensions.x/2, position.y+dimensions.y/2);

        // Headings
        currentEntityHeading = new Vector2( REFERENCE_VECTOR );       //Player always spawns facing 'East'
        nextEntityHeading = new Vector2( currentEntityHeading );
        currentEntityVelocity = new Vector2();
        nextEntityVelocity = new Vector2( currentEntityVelocity );

        // Collisions
        boundingBox = new BoundingBox( position.x, position.y, TILE_DIMENSIONS, TILE_DIMENSIONS );
        alerted = false;

        // Sensors
        adjacentAgentSensor = new AdjacentAgentSensor(dimensions.x*2, centerOfEntity, gameWorld);

        //this.gameWorld = gameWorld;
        this.movement = new Movement(this, gameWorld);
    }

    public void test()
    {
        //movement.wander( centerOfEntity );
    }

    public void update(float timeSinceLastUpdate)
    {
        currentEntityHeading.set(nextEntityHeading);    //Update to new calculated heading
        rotationAngle = currentEntityHeading.angle();   //Angle new heading was rotated by.

        // Transform this to be GameEntity compatible
        nextEntityVelocity.set(inputX, inputY);         // Velocity initialized to basic input velocities

        if (inputX != 0 && inputY != 0)
        {
            nextEntityVelocity.scl(0.5f);               // Diagonal movement should not be faster
        }

        nextEntityVelocity.scl(BASE_VELOCITY);        // Applying the velocity magnitude
        nextEntityVelocity.rotate(rotationAngle - 90);  // Rotating the vector to match the Sprite
        nextEntityVelocity.limit(MAX_SPEED);
        currentEntityVelocity.set(nextEntityVelocity);  // Update the current velocity

        position.x += currentEntityVelocity.x*timeSinceLastUpdate;
        position.y += currentEntityVelocity.y*timeSinceLastUpdate;
        centerOfEntity.set(position.x+dimensions.x/2, position.y+dimensions.y/2);

        adjacentAgentSensor.update(centerOfEntity);

        if ( isAlerted() ) {
            movement.pursuit();
        }
        else {
            movement.wander();
        }

        boundingBox.setPosition(position.x, position.y);
    }

    public void rotateToFaceMouse(float xCurrentMousePosition, float yCurrentMousePosition)
    {
        //Determine the new heading vector offset by entityOrigin to align heading with center of sprite
        nextEntityHeading.x = xCurrentMousePosition - (position.x + origin.x);
        nextEntityHeading.y = yCurrentMousePosition - (position.y + origin.y);

        //Normalize the heading vector
        nextEntityHeading.nor();

        boundingBox.setPosition( position.x, position.y );
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

    public void setNextEntityHeading(Vector2 newHeading) {
        nextEntityHeading = newHeading;
        nextEntityHeading.nor();
    }

    public float getWallSensorOriginY()
    {
        return position.y + origin.y;
    }

    public BoundingBox getBoundingBox()
    {
        return boundingBox;
    }

    public void setAlertion(boolean newValue) {
        alerted = newValue;
    }

    public boolean isAlerted() {
        return alerted;
    }

    // Used by Movement Class
    public void moveLeft()
    {
        inputX -= 1;
    }

    public void moveRight()
    {
        inputX += 1;
    }

    public void moveUp()
    {
        inputY += 1;
    }

    public void moveDown()
    {
        inputY -= 1;
    }

    public void stop() {
        inputX = inputY = 0;
    }
}
