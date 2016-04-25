package com.ucf.aigame;

public class BoundingBox 
{
	private float x;
	private float y;
	
	private int width;
	private int height;
	
	public BoundingBox( float x, float y, int width, int height )
	{
		this.x = x;
		this.y = y;
		
		this.width = width;
		this.height = height;
	}
	
	public float getX()
	{
		return x;
	}
	
	public float getY()
	{
		return y;
	}
	
	public int getWidth()
	{
		return width;
	}
	
	public int getHeight()
	{
		return height;
	}
	
	public void setPosition( float x, float y )
	{
		this.x = x;
		this.y = y;
	}
	
	public boolean isBoundingBoxIntersecting( BoundingBox boundingBox )
	{
		if ( x < boundingBox.getX() + boundingBox.getWidth() - 1 &&
			 x + width - 1 > boundingBox.getX() &&
			 y - height + 1 < boundingBox.getY() &&
			 y > boundingBox.getY() - boundingBox.getHeight() + 1 )
		{
			return true;
		}
		
		return false;
	}
	


}
