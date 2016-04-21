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

    private enum Mode {
        STAND, WANDER, PURSUIT
    }
    private Mode currentMode;

    private enum State {
        IDLE, CALCULATE, NEXT, SEEK, RESET, ERROR
    }
    private State currentState;

    public Movement(Vector2 goal) {

        this.start = new Vector2(0,0);
        this.target = goal;
        this.waypoints = new ArrayList<Vector2>();
        this.currentMode = Mode.STAND;
        this.currentState = State.IDLE;
    }

    public void update() {

        switch( this.currentMode ) {
            case STAND:
                break;

            case WANDER:
                wander();
                break;

            case PURSUIT:
                pursuit();
                break;

            default:
                break;
        }
    }

    private void wander() {
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

    private void pursuit() {
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

    public Vector2 getStart() {
        return start;
    }

    public Vector2 getTarget() {
        return target;
    }
}
