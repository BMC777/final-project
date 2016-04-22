package com.ucf.aigame;
/**
 * Created by Bryan on 2/8/2016.
 */


public class WallObject
{
	private Rectangle boundingBox;
	
	public WallObject( Rectangle boundingBox )
	{
		this.boundingBox = boundingBox;
	}
	
	public Rectangle getBoundingBox()
	{
		return boundingBox;
	}
}
/*import com.badlogic.gdx.math.Rectangle;


public class WallObject
{
    private Rectangle collisionBox;

    private float xWorldPosition;
    private float yWorldPosition;
    private float width;
    private float height;

    WallObject (float xWorldPosition, float yWorldPosition, float width, float height)
    {
        this.xWorldPosition = xWorldPosition;
        this.yWorldPosition = yWorldPosition;
        this.width = width;
        this.height = height;

        collisionBox = new Rectangle(xWorldPosition, yWorldPosition, width + 2, height + 2);
    }

    public float getXWorldPosition()
    {
        return xWorldPosition;
    }

    public float getYWorldPosition()
    {
        return yWorldPosition;
    }

    public float getWidth()
    {
        return width;
    }

    public float getHeight()
    {
        return height;
    }

    public Rectangle getCollisionBox()
    {
        return collisionBox;
    }
}*/
