package com.ucf.aigame;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Steven on 3/7/2016.
 */
public class AStarSearch {

    private PlayerEntity playerEntity;
    private InputHandler inputHandler;
    private ArrayList<GraphNode> adjacencyList;

    private Vector2 targetLocation;
    private Vector2 goalLocation;
    private ArrayList<Vector2> shortestPath;
    private boolean searchStatus;
    private String aiStatus;
    private boolean debug;
    private int aiState;

    /**
     * Constructor Function
     * */
    public AStarSearch(PlayerEntity entity, Vector2 targetLocation, GameWorld gameWorld) {

        // Control Player's entity (movement)
        this.playerEntity = entity;

        // Goal Location
        this.goalLocation = targetLocation;

        // Track AI progress when on 'debug' for console output
        this.aiStatus = "";
        this.debug = true;

        // Determines state of AI (calculating, seeking, etc)
        this.aiState = 0;

        // Create adjacency list from list of nodes in Game World
        this.adjacencyList = gameWorld.getGraphNodeList();
        createAdjacencyList( this.adjacencyList );

        // Initialize list of (x, y) representing shortest path
        this.shortestPath = new ArrayList<Vector2>();

    }

    /**
     * Controls State of AI
     * */

    public void update(){

        // Only run if A* path finding is turned on
        if ( searchStatus ) {

            // CALCULATE
            if ( aiState == 0 ) {
                // Function: Find closest path node
                targetLocation = findClosestPathNode();

                // Calculate Shortest Path
                shortestPath = calculateShortestPath(adjacencyList, targetLocation, goalLocation);

                displayAIStatus("Seeking Goal...");
                aiState = 1; // Goes to SEEK
            }

            // NEXT
            else if ( aiState == 1 ) {

                // Seek "way points" to Goal State
                if ( shortestPath.size() > 0 ) {
                    targetLocation = shortestPath.remove(0);
                    aiState = 2; // Goes to SEEK
                }
                else {
                    displayAIStatus("Done");
                    aiState = 3; // Goes to DONE
                }
            }

            // SEEK
            else if ( aiState == 2 ) {
                // Seek Closest Path Node
                seek(targetLocation.x, targetLocation.y); // Goes to NEXT (within function)
            }

            // DONE
            else if ( aiState == 3 ) {

                // Disable A* Search
                searchStatus = false;

                // Reset Nodes
                for (int i = 0; i < adjacencyList.size(); i++) {
                    adjacencyList.get(i).setVisited( false );
                    adjacencyList.get(i).setHeuristicCost(0);
                    adjacencyList.get(i).setMovementCost(0);
                    adjacencyList.get(i).setParent( null );
                }

                // Ready to search again
                aiState = 0; // Will goes to CALCULATE
            }

            // ERROR
            else {
                // Something went wrong, disable A* Search
                displayAIStatus("Action not recognized");
                searchStatus = false;
            }

        }

    }

    /**
     * Rotates and moves entity to given target (x, y) position
     * */

    private void seek(float targetX, float targetY) {

        Vector2 headingToTarget = getTargetHeading(targetX, targetY);
        float angle = playerEntity.getCurrentHeading().angle(headingToTarget);
        float rotationSpeed = 1.3f;

        // Determine when is "close enough" when facing / seeking target
        float rotationError = rotationSpeed;
        float spacialError = 16*0.1f; //playerEntity.getWidth()*0.1f;

        // Rotate in direction closest to target
        if ( angle < 0 ){
            rotationSpeed = rotationSpeed*(-1);
        }

        // Stop if reached target
        if ( (Math.abs(headingToTarget.x) <= spacialError && Math.abs(headingToTarget.y) <= spacialError) ) {
            aiState = 1; // Goes to NEXT ( in update() )
            stop();
        }
        else {
            // Rotate to face target
            if ( Math.abs(angle) > rotationError ) {
                stop();
                playerEntity.setNextPlayerHeading(playerEntity.getCurrentHeading().rotate(rotationSpeed));
            }
            // Move forward toward target
            else {
                moveUp();
            }
        }

    }

    /**
     * Returns GraphNode if graph contains a node at target (x, y) position; Otherwise, null
     * */

    private GraphNode isMatch(ArrayList<GraphNode> list, float xPosition, float yPosition) {

        // Go through list of nodes and checks for (x, y) match
         for (int i = 0; i < list.size(); i++) {

             if ( list.get(i).getxWorldPosition() == xPosition
                     && list.get(i).getyWorldPosition() == yPosition ) {

                 return ( list.get(i) );
             }
         }

        // No match found
        return ( null );
    }

    /**
     * Goes through list of GraphNodes and adds existing neighbors to its adjacency list
     * */

    private void createAdjacencyList(ArrayList<GraphNode> list) {

        GraphNode match;
        float unitLength = 16; //playerEntity.getWidth();
        float addX, addY;

        // For each GraphNode, check adjacent tiles for existing neighbors
        for (int i = 0; i < list.size(); i++ ) {

            // Check North
            addX = 0;
            addY = unitLength;
            match = isMatch(list, list.get(i).getxWorldPosition()+addX, list.get(i).getyWorldPosition()+addY);
            // If match, add that GraphNode
            if ( match != null ) {
                list.get(i).getNeighbors().add( match );
            }

            // Check South
            addX = 0;
            addY = -unitLength;
            match = isMatch(list, list.get(i).getxWorldPosition()+addX, list.get(i).getyWorldPosition()+addY);
            // If match, add that GraphNode
            if ( match != null ) {
                list.get(i).getNeighbors().add( match );
            }

            // Check East
            addX = unitLength;
            addY = 0;
            match = isMatch(list, list.get(i).getxWorldPosition()+addX, list.get(i).getyWorldPosition()+addY);
            // If match, add that GraphNode
            if ( match != null ) {
                list.get(i).getNeighbors().add( match );
            }

            // Check West
            addX = -unitLength;
            addY = 0;
            match = isMatch(list, list.get(i).getxWorldPosition()+addX, list.get(i).getyWorldPosition()+addY);
            // If match, add that GraphNode
            if ( match != null ) {
                list.get(i).getNeighbors().add( match );
            }

        }

    }

    /**
     * Returns (x, y) position of GraphNode closest to entity
     * */

    private Vector2 findClosestPathNode() {

        Vector2 currentPosition = playerEntity.getCenter();
        Vector2 closestNode = new Vector2();
        float minDistance = 1000000;
        float curDistance, x, y;

        // Finds GraphNode with shortest Euclidean distance to entity
        for (int i = 0; i < adjacencyList.size(); i++) {
            x = currentPosition.x - adjacencyList.get(i).getxWorldPosition();
            y = currentPosition.y - adjacencyList.get(i).getyWorldPosition();

            curDistance = (float) Math.sqrt( x*x + y*y );

            if ( curDistance < minDistance ) {
                closestNode.x = adjacencyList.get(i).getxWorldPosition();
                closestNode.y = adjacencyList.get(i).getyWorldPosition();
                minDistance = curDistance;
            }
        }

        return closestNode;
    }

    private float calculateHeuristic(GraphNode current, GraphNode goal) {

        // Heuristic is Manhattan Distance (divided by the tile size: 32)
        float x = Math.abs( current.getxWorldPosition() - goal.getxWorldPosition() );
        float y = Math.abs( current.getyWorldPosition() - goal.getyWorldPosition() );
        return ( (x + y)/32 ) ;
    }

    /**
     * A* Search Algorithm. Returns list of (x, y) coordinates from start to goal
     * */

    private ArrayList<Vector2> calculateShortestPath(ArrayList<GraphNode> list, Vector2 start, Vector2 goal) {

        ArrayList<Vector2> shortestPath = new ArrayList<Vector2>();

        ArrayList<GraphNode> unvisitedNodes = new ArrayList<GraphNode>();
        ArrayList<GraphNode> visitedNodes = new ArrayList<GraphNode>();

        GraphNode currentNode = new GraphNode(start);
        GraphNode goalNode = new GraphNode(goal);

        // Temporary variables used for readability throughout function
        GraphNode tempNode;
        Vector2 tempVect = new Vector2(start.x, start.y);

        // DEBUGGING -> Parameters
        displayAIStatus("  ");
        displayAIStatus("Start: "+(start.x/32-0.5)+" : "+start.y/32);
        displayAIStatus("Goal: "+(goal.x/32-0.5)+" : "+goal.y/32);
        displayAIStatus("------------------------");
        // */

        // Find GraphNode with start (x, y) position in list
        for (int i = 0; i < list.size(); i++) {
            if ( start.x == list.get(i).getxWorldPosition() && start.y == list.get(i).getyWorldPosition() ) {
                currentNode = list.get(i);
            }
        }

        // Return if failed to find GraphNode with start (x, y) position
        if ( currentNode.getNeighbors().size() == 0) {
            displayAIStatus("Error: startNode not found or has zero edges");
            return ( null );
        }
        // Start GraphNode found, initialize values
        else {
            currentNode.setParent(null);
            currentNode.setMovementCost( 0 );
            currentNode.setHeuristicCost(calculateHeuristic(currentNode, goalNode) );
        }

        // Search until GraphNode with goal (x, y) position found
        while ( !tempVect.equals( goal ) ) {

            // Add node to 'visited'
            visitedNodes.add( currentNode );

            // DEBUGGING -> Current Node: ( x ) : ( y ) -> ( TotalCost )
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
                displayAIStatus("No more nodes to visit, no path found");
                return ( null );
            }
            // Else, use first GraphNode as base of comparison
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

    public void setInputHandler(InputHandler ctrl) {
        inputHandler = ctrl;
    }

    public void setSearchStatus(boolean newStatus){
        searchStatus = newStatus;
        //System.out.println("Search : " + searchStatus);
        displayAIStatus("Search : " + searchStatus);

        // if Search is OFF, stop movement, ready for player input
        if ( !searchStatus ) {
            stop();
        }
    }

    public boolean getSearchStatus(){
        return searchStatus;
    }

    private void moveUp(){
        inputHandler.keyDown(Input.Keys.W);
    }

    private void stop(){
        playerEntity.stop();
    }

    public Vector2 getTargetLocation() {
        return targetLocation;
    }

    public Vector2 getGoalLocation() {
        return goalLocation;
    }

    public Vector2 getTargetHeading(float targetX, float targetY) {
        float x = targetX - ( playerEntity.getCurrentXPosition()+playerEntity.getXPlayerOrigin() );
        float y = targetY - ( playerEntity.getCurrentYPosition()+playerEntity.getYPlayerOrigin() );
        return new Vector2(x, y);
    }

    private void displayAIStatus(String msg) {
        if ( !aiStatus.equalsIgnoreCase(msg) && debug ) {
            System.out.println(msg);
            aiStatus = msg;
        }
    }
}
