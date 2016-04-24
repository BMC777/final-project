package com.ucf.aigame;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Steven on 2/8/2016.
 */
public class AdjacentAgentSensor {


    private Vector2 center;
    private Circle circle;

    AdjacentAgentSensor(float sensorRange, Vector2 center, GameWorld gameWorld) {

        // Instantiate Vector pointing to center of circle and Circle itself
        circle = new Circle(center, sensorRange);
        this.center = center;
    }

    public void update(Vector2 newCenter) {

        // Update center of circle
        center.set(newCenter);

        // Recenter circle
        circle.setPosition(newCenter.x, newCenter.y);
        //System.out.println("new center: "+ newCenter);
    }

    public Vector2 getCenter() {
        return new Vector2(circle.x, circle.y);
    }

    public Circle getCircle() {
        return circle;
    }
}
