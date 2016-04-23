package com.ucf.aigame;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Steven on 2/8/2016.
 */
public class AdjacentAgentSensor {

    private Vector2 vOrigin;
    private Circle sensor;

    public AdjacentAgentSensor(float sensorRange, float xOrigin, float yOrigin) {

        // Instantiate Vector pointing to center of circle and Circle itself
        vOrigin = new Vector2(xOrigin, yOrigin);
        sensor = new Circle(vOrigin, sensorRange);
    }

    public void update(float xOrigin, float yOrigin) {

        // Update center of circle
        vOrigin.set(xOrigin, yOrigin);

        // Recenter circle
        sensor.setPosition(vOrigin);
    }

    public Vector2 getvOrigin() {
        return vOrigin;
    }

    public Circle getSensor() {
        return sensor;
    }
}
