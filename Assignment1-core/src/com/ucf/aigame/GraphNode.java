package com.ucf.aigame;

import java.util.ArrayList;

/**
 * Created by Steven on 3/10/2016.
 */
public class GraphNode {

    private float xWorldPosition;
    private float yWorldPosition;
    private boolean visited;
    private ArrayList<GraphNode> edges;
    private GraphNode parent;
    private float movementCost;
    private float heuristicCost;

    public GraphNode (float xWorldPosition, float yWorldPosition) {

        this.xWorldPosition = xWorldPosition;
        this.yWorldPosition = yWorldPosition;

        this.visited = false;
        this.edges = new ArrayList<GraphNode>();
        this.parent = null;
        this.movementCost = 0;
        this.heuristicCost = -1;
    }

    public float getxWorldPosition() {
        return this.xWorldPosition;
    }

    public float getyWorldPosition() {
        return this.yWorldPosition;
    }

    public boolean isVisited() {
        return this.visited;
    }

    public void setVisited(boolean val) {
        this.visited = val;
    }

    public void setMovementCost(float newCost) {
        this.movementCost = newCost;
    }

    public float getMovementCost() {
        return ( this.movementCost );
    }

    public void setHeuristicCost(float newCost) {
        this.heuristicCost = newCost;
    }

    public float getHeuristicCost() {
        return ( this.heuristicCost );
    }

    public float getTotalCost() {
        return ( this.movementCost + this.heuristicCost);
    }

    public void setParent(GraphNode parent) {
        this.parent = parent;
    }

    public GraphNode getParent() {
        return ( this.parent );
    }

    public ArrayList<GraphNode> getEdges() {
        return this.edges;
    }


}
