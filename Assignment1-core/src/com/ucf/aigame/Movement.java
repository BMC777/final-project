package com.ucf.aigame;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Steven on 4/11/2016.
 */
public class Movement {

    private Vector2 start;
    private Vector2 target;
    private ArrayList<Vector2> waypoints;

    // Defines current action
    private enum Mode { STAND, WANDER, PURSUIT }
    private Mode currentMode;

    // Defines current state of current action
    private enum State { IDLE, CALCULATE, NEXT, SEEK, RESET, ERROR }
    private State currentState;

    // For wander()
    private float wanderRadius = 10;
    private float wanderDistance = 10;
    private float wanderJitter = 2;

    public Movement() {

        this.start = new Vector2();
        this.target = new Vector2();
        this.waypoints = new ArrayList<Vector2>();
        this.currentMode = Mode.STAND;
        this.currentState = State.IDLE;
    }

    public void update(Vector2 start) {

        switch( currentMode ) {
            case STAND:
                break;

            case WANDER:
                wander(start);
                break;

            case PURSUIT:
                pursuit();
                break;

            default:
                System.out.println("**Case defaulted in Movement.update()!!**");
                break;
        }
    }

    public void wander(Vector2 start) {
        switch( currentState ) {
            case CALCULATE:
                break;

            case NEXT:
                break;

            case SEEK:
                break;

            case RESET:
                break;

            case ERROR:
                break;

            default:
                break;
        }
    }

    public void pursuit() {
        switch( this.currentState ) {
            case CALCULATE:
                break;

            case NEXT:
                break;

            case SEEK:
                break;

            case RESET:
                break;

            case ERROR:
                break;

            default:
                break;
        }
    }

    private void calculateWander(Vector2 start)
    {

    }

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getTarget() {
        return target;
    }
}
