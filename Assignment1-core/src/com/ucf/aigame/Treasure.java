package com.ucf.aigame;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Steven on 4/6/2016.
 */
public class Treasure {

    private BoundingBox boundingBox;

    private float value;
    private float weight;

    private Vector2 position;
    private Vector2 dimensions;
    private Vector2 center;

    Treasure(Vector2 position, Vector2 dimensions, float value, float weight) {
        this.position = position;
        this.dimensions = dimensions;
        this.center = new Vector2(position.x+dimensions.x, position.y+dimensions.y);

        this.value = value;
        this.weight = weight;

        this.boundingBox = new BoundingBox( position.x, position.y, (int)dimensions.x, (int)dimensions.y );
    }

    public float getValue() {
        return value;
    }

    public float getWeight() {
        return weight;
    }

    public Vector2 getPosition() {
        return position;
    }

    public void setPosition(Vector2 position) {
        this.position = position;
        this.boundingBox.setPosition(position.x, position.y);
    }

    public Vector2 getDimensions() {
        return dimensions;
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }

    public BoundingBox getBoundingBox() {
        return boundingBox;
    }
}
