package com.ucf.aigame;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Steven on 4/11/2016.
 */
public class Movement {

    private GameWorld gameWorld;
    private GameEntity gameEntity;
    private Vector2 start;
    private Vector2 target;
    private ArrayList<Vector2> waypoints;

    // Defines current action
    private enum Mode { STAND, WANDER, PURSUIT }
    private Mode currentMode;

    // Defines current state of current action
    private enum State { CALCULATE, NEXT, SEEK, RESET }
    private State currentState;

    // For wander()
    private float wanderRadius = 10;
    private float wanderDistance = 10;
    private float wanderJitter = 2;

    public Movement(GameEntity gameEntity, GameWorld gameWorld) {

        this.gameWorld = gameWorld;
        this.gameEntity = gameEntity;
        this.start = new Vector2();
        this.target = new Vector2();
        this.waypoints = new ArrayList<Vector2>();
        this.currentMode = Mode.STAND;
        this.currentState = State.CALCULATE;
    }

    public void update(Vector2 start) {

        switch( currentMode ) {
            case STAND:
                break;

            case WANDER:
                wander();
                break;

            case PURSUIT:
                pursuit();
                break;

            default:
                System.out.println("**Case defaulted in Movement.update()!!**");
                break;
        }
    }

    public void wander() {
        switch( currentState ) {
            case CALCULATE:

                target = findClosestPathNode( gameEntity.getEntityCenter() );
                waypoints = calculateWanderPath(target);

                /*/
                System.out.println();
                System.out.println("---------------");
                //System.out.println("target: "+target);
                //
                for (int i=0; i<waypoints.size(); i++){
                    System.out.println( waypoints.get(i) );
                } // */

                currentState = State.SEEK;
                break;

            case NEXT:

                if ( waypoints.size() > 0 ) {
                    target = waypoints.remove(0);
                    currentState = State.SEEK;
                }
                else {
                    currentState = State.CALCULATE;
                }
                break;

            case SEEK:
                seek(target);
                break;

            case RESET:
                currentState = State.CALCULATE;
                gameEntity.stop();
                break;

            default:
                System.out.println("ERROR: default hit for currentState in wander()");
                gameEntity.stop();
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

            default:
                break;
        }
    }

    private Vector2 findClosestPathNode(Vector2 currentPosition)  {
        Vector2 currentPos = currentPosition;
        Vector2 closestNode = new Vector2();
        float minDist = 1000000;
        float curDist, x, y;

        // Find GraphNode with shortest Euclidean distance to entity
        for (int i=0; i<gameWorld.getGraphNodeList().size(); i++) {
            GraphNode node = gameWorld.getGraphNodeList().get(i);

            x = currentPos.x - node.getCenteredPos().x;
            y = currentPos.y - node.getCenteredPos().y;

            curDist = (float) Math.sqrt(x*x + y*y);

            if( curDist < minDist ) {
                closestNode.x = node.getCenteredPos().x;
                closestNode.y = node.getCenteredPos().y;
                minDist = curDist;
            }
        }

        return closestNode;
    }


    private ArrayList<Vector2> calculateWanderPath(Vector2 start) {

        ArrayList<Vector2> waypoints = new ArrayList<Vector2>();
        GraphNode tempNode = new GraphNode(start);
        int waypointCounter = 6;

        //System.out.println();
        //System.out.println("---------------");

        while (waypointCounter-- != 0)
        {
            // Find the GraphNode that corresponds to the first position
            for (int i=0; i<gameWorld.getGraphNodeList().size(); i++)
            {
                GraphNode node = gameWorld.getGraphNodeList().get(i);

                //System.out.println();
                //System.out.println("Comparing:");
                //System.out.println(tempNode.getCenteredPos()+" and "+node.getCenteredPos());

                if (tempNode.getCenteredPos().x == node.getCenteredPos().x
                        && tempNode.getCenteredPos().y == node.getCenteredPos().y)
                {
                    tempNode = node;
                    break;
                }
            }

            // Add current node to waypoints
            waypoints.add(tempNode.getCenteredPos());

            // Randomly select neighbor from current node
            if ( tempNode.getNeighbors().size() <= 0 ) {
                System.out.println("ERROR: no neighbors found!");
                return waypoints;
            }

            Random rand = new Random();
            int n = rand.nextInt( tempNode.getNeighbors().size() );

            // Set neighbor as new current node
            tempNode = tempNode.getNeighbors().get(n);

            waypoints.add(tempNode.getCenteredPos());
        }

        return waypoints;
    }

    private void seek(Vector2 target) {
        Vector2 headingToTarget = new Vector2();
        headingToTarget.x = target.x - ( gameEntity.getPositionVector().x + gameEntity.getOriginVector().x );
        headingToTarget.y = target.y - ( gameEntity.getPositionVector().y + gameEntity.getOriginVector().y );

        float angle = gameEntity.getEntityHeading().angle(headingToTarget);
        float rotationSpeed = 1.3f;

        // Determine when is "close enough" when facing / seeking target
        float rotationError = rotationSpeed;
        float spacialError = gameEntity.getDimensionVector().x*0.1f;

        // Rotate in direction closest to target
        if (angle < 0) {
            rotationSpeed *= (-1);
        }

        /*/
        System.out.println("-----------");
        System.out.println("target: "+this.target);
        System.out.println("curPos: "+ gameEntity.getEntityCenter());
        for (int i=0; i<waypoints.size(); i++){
            System.out.println( waypoints.get(i) );
        } // */

        // Stop if reached target
        if ( (Math.abs(headingToTarget.x) <= spacialError && Math.abs(headingToTarget.y) <= spacialError) ) {
            currentState = State.NEXT;
            gameEntity.stop();
        }
        else {
            // Rotate to face target
            if ( Math.abs(angle) > rotationError ) {
                gameEntity.stop();
                gameEntity.setNextEntityHeading(gameEntity.getEntityHeading().rotate(rotationSpeed));
            }
            // Move forward toward target
            else {
                gameEntity.moveUp();
            }
        }

    }

}
