package com.ucf.aigame;

import java.util.ArrayList;

/**
 * Created by Bryan on 2/8/2016.
 */
public class CollisionDetector
{
    private PlayerEntity playerEntity;
    private ArrayList<WallObject> wallList;
    private ArrayList<GameEntity> entityList;

    CollisionDetector ( GameWorld gameWorld )
    {
    	playerEntity = gameWorld.getPlayerEntity();
    	wallList = gameWorld.getWallList();
    	entityList = gameWorld.getEntityList();
    }
    
    public void update( GameWorld gameWorld )
    {
    	entityList = gameWorld.getEntityList();
    	playerEntity = gameWorld.getPlayerEntity();
    	
    	GameEntity collidingEntity;
    	WallObject collidingWall;
    	
    	boolean collisionLeft = false;
    	boolean collisionRight = false;
    	boolean collisionUp = false;
    	boolean collisionDown = false;
    	
    	// Checking player entity for collisions
    	collidingEntity = entityCollisionCheck( playerEntity.getBoundingBox() );
		if ( collidingEntity != null )
		{
			// Debug:
			// System.out.println( "Player is colliding with Entity #" + entityList.indexOf( collidingEntity ) + " in list!");		
		}
		
    	collisionLeft = false;
    	collisionRight = false;
    	collisionUp = false;
    	collisionDown = false;
		
		collidingWall = wallCollisionCheck( playerEntity.getBoundingBox() );
    	if ( collidingWall != null )
    	{
    		// Debug:
    		// System.out.println("Player is colliding with Wall #" + wallList.indexOf( collidingWall ) + " in list!");
    		
    		if ( leftCollision( playerEntity.getBoundingBox(), collidingWall.getBoundingBox() ) )
    		{
    			collisionLeft = true;
    		}
    		
    		if ( rightCollision( playerEntity.getBoundingBox(), collidingWall.getBoundingBox() ) )
    		{
    			collisionRight = true;
    		}
    		
    		if ( topCollision( playerEntity.getBoundingBox(), collidingWall.getBoundingBox() ) )
    		{
    			collisionUp = true;
    		}
    		
    		if ( bottomCollision( playerEntity.getBoundingBox(), collidingWall.getBoundingBox() ) )
    		{
    			collisionDown = true;
    		}
    	}
    	
    	playerEntity.setCollisionDetection( collisionUp, collisionDown, collisionLeft, collisionRight );
    	
    	// Checking each entity for collisions
    	for ( int i = 0; i < entityList.size(); i++ )
    	{
    		collidingEntity = entityCollisionCheck( entityList.get(i).getBoundingBox() );
    		if ( collidingEntity != null )
    		{
    			//System.out.println( "Entity #" + i + " is colliding with Entity #" + entityList.indexOf( collidingEntity ) + " in list!");
    		}
    		
    		collidingWall = wallCollisionCheck( entityList.get(i).getBoundingBox() );
        	if ( collidingWall != null )
        	{
    			//System.out.println( "Entity #" + i + " is colliding with Wall #" + wallList.indexOf( collidingWall ) + " in list!");
        	}

			if ( entityList.get(i).getAdjacentAgentSensor().contains( playerEntity.getCenter() ) )
			{
				entityList.get(i).setAlertion(true);
			}
    	}
    }
    
    private GameEntity entityCollisionCheck( BoundingBox boundingBox )
    {
    	for ( int i = 0; i < entityList.size(); i++ )
    	{
    		if ( boundingBox.isBoundingBoxIntersecting( entityList.get(i).getBoundingBox() ) && !boundingBox.equals( entityList.get( i ).getBoundingBox() ) )
    		{
    			return entityList.get( i );
    		}
    	}
    	
    	return null;
    }

	private GameEntity entitySensoryCheck( BoundingBox boundingBox )
	{
		for ( int i = 0; i < entityList.size(); i++ )
		{
			if ( entityList.get(i).getAdjacentAgentSensor().contains( playerEntity.getCenter() ) )
			{
				return entityList.get( i );
			}
		}

		return null;
	}
    
    private WallObject wallCollisionCheck( BoundingBox boundingBox )
    {
    	for ( int i = 0; i < wallList.size(); i++ )
    	{
    		if ( boundingBox.isBoundingBoxIntersecting( wallList.get( i ).getBoundingBox() ) )
    		{
    			return wallList.get( i );
    		}
    	}
    	
    	return null;
    }
    
    private boolean leftCollision( BoundingBox thisBoundingBox, BoundingBox otherBoundingBox )
    {
    	if ( ( thisBoundingBox.getX() < otherBoundingBox.getX() + otherBoundingBox.getWidth() ) && 
    			( thisBoundingBox.getX() + thisBoundingBox.getWidth() > otherBoundingBox.getX() + otherBoundingBox.getWidth() ) )
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean rightCollision( BoundingBox thisBoundingBox, BoundingBox otherBoundingBox )
    {
    	if ( ( thisBoundingBox.getX() < otherBoundingBox.getX() ) && thisBoundingBox.getX() + thisBoundingBox.getWidth() > otherBoundingBox.getX() )
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean topCollision( BoundingBox thisBoundingBox, BoundingBox otherBoundingBox )
    {
    	if ( ( thisBoundingBox.getY() < otherBoundingBox.getY() ) && ( thisBoundingBox.getY() > otherBoundingBox.getY() - otherBoundingBox.getHeight() ) )
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    private boolean bottomCollision( BoundingBox thisBoundingBox, BoundingBox otherBoundingBox )
    {
    	if ( ( thisBoundingBox.getY() - thisBoundingBox.getHeight() < otherBoundingBox.getY() ) && ( thisBoundingBox.getY() > otherBoundingBox.getY() ) )
    	{
    		return true;
    	}
    	
    	return false;
    }
}
