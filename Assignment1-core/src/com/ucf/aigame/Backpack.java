package com.ucf.aigame;

import java.util.ArrayList;

/**
 * Created by Steven on 4/11/2016.
 */
public class Backpack {

    // shall keep track of total weight
    // shall add and store new Treasure objects

    private ArrayList<Treasure> treasureCollected;
    private float weight;
    private float value;

    public Backpack() {

        this.treasureCollected = new ArrayList<Treasure>();
        this.weight = 0;
        this.value = 0;
    }

    public void add(Treasure booty) {
        treasureCollected.add(booty);
        weight += ( booty.getWeight() );
        value += ( booty.getValue() );

        /*/
        System.out.println("-----------");
        System.out.println("Treasure Collected!");
        System.out.println("at "+booty.getPosition());
        System.out.println("value: "+booty.getValue());
        System.out.println("weight: "+booty.getWeight());
        System.out.println("Total Value: "+value);
        System.out.println("Total Weight: "+weight);
        // */
    }

    public Treasure remove() {
        if ( treasureCollected.size() <= 0 ) {
            return null;
        }

        Treasure droppedTreasure = treasureCollected.get(treasureCollected.size()-1);
        weight -= ( droppedTreasure.getWeight() );
        value -= ( droppedTreasure.getValue() );
        treasureCollected.remove( droppedTreasure );

        return droppedTreasure;
    }

    public float getValue() {
        return value;
    }

    public float getWeight() {
        return weight;
    }
}
