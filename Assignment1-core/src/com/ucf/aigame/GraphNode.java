package com.ucf.aigame;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Created by Steven on 3/10/2016.
 */
public class GraphNode {

    private Vector2 position;
    private boolean visited;
    private ArrayList<GraphNode> neighbors;
    private GraphNode parent;
    private float movementCost;
    private float heuristicCost;

    public GraphNode (Vector2 worldPosition) {

        this.position = worldPosition;

        this.visited = false;
        this.neighbors = new ArrayList<GraphNode>();
        this.parent = null;
        this.movementCost = 0;
        this.heuristicCost = -1;
    }

    public float getxWorldPosition() {
        return position.x;
    }

    public float getyWorldPosition() {
        return position.y;
    }

    public Vector2 getCenteredPos() {
        return position;
    }

    public Vector2 getTiledPosition() {
        return new Vector2(position.x-8, position.y-8);
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

    public ArrayList<GraphNode> getNeighbors() {
        return this.neighbors;
    }


}
