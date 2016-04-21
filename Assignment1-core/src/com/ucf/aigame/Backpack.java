package com.ucf.aigame;

import java.util.ArrayList;

/**
 * Created by Steven on 4/11/2016.
 */
public class Backpack {

    // shall keep track of total weight
    // shall add and store new Treasure objects

    private ArrayList<Treasure> treasure;
    private float weight;
    private float value;

    public Backpack() {

        this.treasure = new ArrayList<Treasure>();
        this.weight = 0;
        this.value = 0;
    }

    public void add(Treasure booty) {
        this.treasure.add( booty );
        this.weight += ( booty.getWeight() );
        this.value += ( booty.getValue() );
    }

    public void remove(Treasure booty) {
        this.weight -= ( booty.getWeight() );
        this.value -= ( booty.getValue() );
        this.treasure.remove( booty );

    }
}
