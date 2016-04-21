package com.ucf.aigame;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Steven on 4/6/2016.
 */
public class Treasure {

    private float value;
    private float weight;

    private Vector2 position;
    private Vector2 center; // Necessary ?

    Treasure(Vector2 position, float value, float weight) {
        this.position = position;
        this.value = value;
        this.weight = weight;
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
    }

    public Vector2 getCenter() {
        return center;
    }

    public void setCenter(Vector2 center) {
        this.center = center;
    }
}
