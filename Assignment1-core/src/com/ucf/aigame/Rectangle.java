package com.ucf.aigame;

/**
 * Rectangle objects stores x,y integer coordinate values of the top-left corner of a rectangle,
 * along with it's width and height.
 * Created by Bryan on 3/28/2016.
 */
public class Rectangle
{
    private int width;
    private int height;

    //Upper-left point.
    private int x;
    private int y;

    public Rectangle()
    {
        x = 0;
        y = 0;

        width = 0;
        height = 0;
    }

    public Rectangle( int width, int height )
    {
        x = 0;
        y = 0;

        this.width = width;
        this.height = height;
    }

    public Rectangle( int x, int y, int width, int height )
    {
        this.x = x;
        this.y = y;

        this.width = width;
        this.height = height;
    }

    public Rectangle( CartesianPoint point, int width, int height )
    {
        this.x = point.getX();
        this.y = point.getY();

        this.width = width;
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
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
