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
        if ( currentMode != Mode.WANDER ) {
            waypoints = new ArrayList<Vector2>();
            currentState = State.CALCULATE;
            currentMode = Mode.WANDER;
        }

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
        if ( currentMode != Mode.PURSUIT ) {
            waypoints = new ArrayList<Vector2>();
            currentState = State.CALCULATE;
            currentMode = Mode.PURSUIT;
        }

        switch( this.currentState ) {
            case CALCULATE:
                // Saves player's location from initial calculation
                start = gameWorld.getPlayerEntity().getCenter();

                // If player leaves room OR goes to far from entity, do not pursue
                target = findClosestPathNode( start );
                if ( getDistance(target, gameWorld.getPlayerEntity().getCenter()) > gameWorld.getTileDimensions()
                        || getDistance(gameEntity.getEntityCenter(), gameWorld.getPlayerEntity().getCenter()) > gameWorld.getTileDimensions()*10) {
                    gameEntity.setAlertion( false );
                    break;
                }

                target = findClosestPathNode( gameEntity.getEntityCenter() );
                waypoints = calculateShortestPath(gameWorld.getGraphNodeList(), target, gameWorld.getPlayerEntity().getCenter());

                /*/
                System.out.println();
                System.out.println("---------------");
                System.out.print("Shortest Path Found:");
                for (int i=0; waypoints != null && i<waypoints.size(); i++){
                    System.out.println( waypoints.get(i) );
                } // */

                currentState = State.SEEK;
                break;

            case NEXT:
                // If player moves father that a tile-length away, recalculate shortest path
                if ( getDistance(start, gameWorld.getPlayerEntity().getCenter()) > gameWorld.getTileDimensions() ) {
                    currentState = State.CALCULATE;
                    break;
                }
                // Otherwise proceed as normal
                else {
                    if ( waypoints != null && waypoints.size() > 0 ) {
                        target = waypoints.remove(0);
                        currentState = State.SEEK;
                    }
                    else {
                        currentState = State.CALCULATE;
                    }
                }
                break;

            case SEEK:
                seek(target);
                break;

            case RESET:
                break;

            default:
                break;
        }
    }

    private float getDistance(Vector2 p, Vector2 q) {
        float distance, x, y;

        x = q.x - p.x;
        y = q.y - p.y;
        distance = (float) Math.sqrt(x*x + y*y);

        return distance;
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

    private float calculateHeuristic(GraphNode current, GraphNode goal) {

        // Heuristic is Manhattan Distance (divided by the tile size: 32)
        float x = Math.abs( current.getxWorldPosition() - goal.getxWorldPosition() );
        float y = Math.abs( current.getyWorldPosition() - goal.getyWorldPosition() );
        return ( (x + y)/gameWorld.getTileDimensions() ) ;
    }

    private ArrayList<Vector2> calculateShortestPath(ArrayList<GraphNode> list, Vector2 start, Vector2 goal) {

        ArrayList<Vector2> shortestPath = new ArrayList<Vector2>();

        ArrayList<GraphNode> unvisitedNodes = new ArrayList<GraphNode>();
        ArrayList<GraphNode> visitedNodes = new ArrayList<GraphNode>();

        GraphNode currentNode = new GraphNode(start);
        GraphNode goalNode = new GraphNode(goal);

        // Temporary variables used for readability throughout function
        GraphNode tempNode;
        Vector2 tempVect = new Vector2(start.x, start.y);

        /*/ DEBUGGING -> Parameters
        System.out.println("  ");
        System.out.println("Start: " + (start.x) + " : " + start.y);
        System.out.println("Goal: " + (goal.x) + " : " + goal.y);
        System.out.println("------------------------");
        // */

        // Find GraphNode with start (x, y) position in list
        for (int i = 0; i < list.size(); i++) {
            if ( start.x == list.get(i).getCenteredPos().x && start.y == list.get(i).getCenteredPos().y ) {
                currentNode = list.get(i);
                break;
            }
        }

        // Return if failed to find GraphNode with start (x, y) position
        if ( currentNode.getNeighbors().size() == 0) {
            System.out.println("Error: startNode not found or has no neighbors");
            return ( null );
        }
        // GraphNode found, initialize values
        else {
            currentNode.setParent(null);
            currentNode.setMovementCost( 0 );
            currentNode.setHeuristicCost(calculateHeuristic(currentNode, goalNode) );
        }

        // Search until GraphNode with goal (x, y) position found
        while ( !tempVect.equals( goal ) ) {

            // Add node to 'visited'
            visitedNodes.add( currentNode );

            /*/ DEBUGGING -> Current Node: ( x ) : ( y ) -> ( TotalCost )
            displayAIStatus((currentNode.getxWorldPosition()/32-0.5)
                    +" : "+(currentNode.getyWorldPosition()/32-0.5)+" -> "+currentNode.getTotalCost());
            // */

            // Go through neighbors of current node
            for (int i = 0; i < currentNode.getNeighbors().size(); i++) {

                tempNode = currentNode.getNeighbors().get(i);

                // Skip neighbor node if already visited
                if ( visitedNodes.contains( tempNode ) ) {
                    continue;
                }
                // Check if current neighbor node is in 'unvisited' list
                if ( !unvisitedNodes.contains( tempNode ) ) {

                    tempNode.setParent( currentNode );
                    tempNode.setHeuristicCost(calculateHeuristic(tempNode, goalNode));
                    tempNode.setMovementCost(currentNode.getMovementCost() + 1);

                    unvisitedNodes.add( tempNode );
                }
                else {
                    /*
                    * Normally, if current neighbor node were already in Unvisited List
                    * then check if current path to neighbor node is cheaper.
                    * If so, replace parent of neighbor node.
                    *
                    * However, all movement cost are equal and heuristic won't change
                    * so if node is already in Unvisited List then it's already the cheapest
                    * path to that node
                    * */

                    continue;
                }
            }

            /*/ DEBUGGING -> List Unvisited Nodes
            displayAIStatus("Unvisited: ");
            for (int i = 0; i < unvisitedNodes.size(); i++) {
                displayAIStatus(unvisitedNodes.get(i).getxWorldPosition()/32+
                        " : "+unvisitedNodes.get(i).getyWorldPosition()/32+
                        " -> "+unvisitedNodes.get(i).getTotalCost());
            }
            displayAIStatus("  ");
            // */

            // Return if all nodes have been visited
            if ( unvisitedNodes.isEmpty() ) {
                System.out.println("No more nodes to visit, no path found");
                gameEntity.setAlertion(false); // Player is out of the room
                return ( null );
            }
            // Use first GraphNode as base of comparison
            else {
                tempNode = unvisitedNodes.get(0);
            }

            // Get graphNode in Unvisited List with lowest cost
            for (int i = 1; i < unvisitedNodes.size(); i++) {
                if (unvisitedNodes.get(i).getTotalCost() < tempNode.getTotalCost()) {
                    tempNode = unvisitedNodes.get(i);
                }
            }

            // Remove that node from Unvisited List
            unvisitedNodes.remove( tempNode );

            // Set lowest-cost GraphNode as new current node and repeat
            currentNode = tempNode;
            tempVect.x = currentNode.getxWorldPosition();
            tempVect.y = currentNode.getyWorldPosition();

            // If goal (player) is less than 1 tile-length away,
            // then its not on a GraphNode; move directly to player
            if ( getDistance(tempVect, gameWorld.getPlayerEntity().getCenter()) < gameWorld.getTileDimensions() ) {
                shortestPath.add(0, gameWorld.getPlayerEntity().getCenter() );
                break;
            }
        }

        // Go backwards from goal using Parents
        while ( !tempVect.equals( start ) ) {

            // Add each new (x, y) at the beginning of list to set path from Start to Goal
            shortestPath.add(0,  tempVect );

            // Set that GraphNode as 'Visited' (for graphical debugging purposes)
            currentNode.setVisited( true );
            currentNode = currentNode.getParent();

            // Extract (x, y) position of next parent GraphNode
            tempVect = new Vector2(currentNode.getxWorldPosition(), currentNode.getyWorldPosition());

        }

        return ( shortestPath );
    }
}
