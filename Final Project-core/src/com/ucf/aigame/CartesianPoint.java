package com.ucf.aigame;

/**
 * CartesianPoint objects hold an x and y integer representing a point on a
 * 2D coordinate plane.
 * Created by Bryan on 3/28/2016.
 */
public class CartesianPoint
{
    public int x;
    public int y;

    public CartesianPoint( int x, int y )
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public int getY()
    {
        return y;
    }
}
