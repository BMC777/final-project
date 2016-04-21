package com.ucf.aigame;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Steven on 2/9/2016.
 */
public class PieSliceSensor {

    // Consider generalizing class to have n pie slices
    private Vector2 frontLeft;
    private Vector2 frontRight;
    private Vector2 backLeft;
    private Vector2 backRight;

    private float sensorRange;


    private int activationLevelRIGHT = 0; // Quadrant 0
    private int activationLevelFRONT = 0; // Quadrant 1
    private int activationLevelLEFT = 0; // Quadrant 2
    private int activationLevelBACK = 0; // Quadrant 3

    public PieSliceSensor(Vector2 currentHeading, float sensorRange) {

        // Create new Vector2 objects and position relative to current entity heading
        frontLeft = new Vector2( currentHeading );
        frontRight = new Vector2( currentHeading );
        backLeft = new Vector2( currentHeading );
        backRight = new Vector2( currentHeading );

        frontLeft.rotate(45);
        frontRight.rotate(315);
        backLeft.rotate(135);
        backRight.rotate(255);

        // Scale vectors
        this.sensorRange = sensorRange;
        frontLeft.scl(sensorRange);
        frontRight.scl(sensorRange);
        backLeft.scl(sensorRange);
        backRight.scl(sensorRange);

    }

    public void update(Vector2 currentHeading) {

        // Adjust Pie Slices
        frontLeft.set( currentHeading ).rotate(45);
        frontRight.set( currentHeading ).rotate(315);
        backLeft.set( currentHeading ).rotate(135);
        backRight.set( currentHeading ).rotate(225);

        // Scale vectors
        frontLeft.scl(sensorRange);
        frontRight.scl(sensorRange);
        backLeft.scl(sensorRange);
        backRight.scl(sensorRange);
    }

    public void incrementActivationLevel(int quadrant) {
        if (quadrant == 0) {
            activationLevelRIGHT++;
        }
        if (quadrant == 1) {
            activationLevelFRONT++;
        }
        if (quadrant == 2) {
            activationLevelLEFT++;
        }
        if (quadrant == 3) {
            activationLevelBACK++;
        }
    }

    public void identifyQuadrant(Vector2 currentHeading, Vector2 relativeHeading) {

        float angleDifference = currentHeading.angle(relativeHeading);


        // RIGHT
        if ( angleDifference < -45 && angleDifference > -135 ) {
            incrementActivationLevel( 0 );
        }
        // FRONT
        if ( angleDifference <= 45 && angleDifference >= -45 ) {
            incrementActivationLevel(1);
        }
        // LEFT
        if ( angleDifference < 135 && angleDifference > 45 ) {
            incrementActivationLevel( 2 );
        }
        // BACK
        if ( angleDifference >= 135 || angleDifference <= -135 ) {
            incrementActivationLevel( 3 );
        }
    }

    public void resetActivationLevels() {
        activationLevelRIGHT = 0;
        activationLevelFRONT = 0;
        activationLevelLEFT = 0;
        activationLevelBACK = 0;
    }

    public int getActivationLevelRIGHT() {
        return activationLevelRIGHT;
    }

    public int getActivationLevelFRONT() {
        return activationLevelFRONT;
    }

    public int getActivationLevelLEFT() {
        return activationLevelLEFT;
    }

    public int getActivationLevelBACK() {
        return activationLevelBACK;
    }

    public Vector2 getFrontLeft() {
        return frontLeft;
    }

    public Vector2 getFrontRight() {
        return frontRight;
    }

    public Vector2 getBackLeft() {
        return backLeft;
    }

    public Vector2 getBackRight() {
        return backRight;
    }
}
