package com.ucf.aigame;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Steven on 4/6/2016.
 */
public class BaseEntity {

    private Vector2 position;
    private Vector2 center;
    private Vector2 heading;
    private Vector2 nextHeading;
    private Vector2 velocity;
    private Vector2 nextVelocity;

    private float width;
    private float height;

    private static final float BASE_VELOCITY = 125;
    private static final Vector2 REFERENCE_VECTOR = new Vector2(1, 0);
    private static final float MAX_SPEED = 250;

    // Constructor for NPCs
    BaseEntity() {

    }

    // Constructor for Player
    BaseEntity(Vector2 worldPosition) {

    }

    public void update() {

    }

    // Create movement for entity
    // Do we need inputX and inputY?
    public void moveRight() {}

    public void moveLeft() {}

    public void moveUp() {}

    public void moveDown() {}

    public void stop() {}

    public float getHeight() {
        return height;
    }

    public float getWidth() {
        return width;
    }

    public Vector2 getPosition() {
        return position;
    }

    public Vector2 getCenter() {
        return center;
    }

    public Vector2 getHeading() {
        return heading;
    }

    public void setNextHeading(Vector2 nextHeading) {
        this.nextHeading = nextHeading;
        this.nextHeading.nor();
    }
}
