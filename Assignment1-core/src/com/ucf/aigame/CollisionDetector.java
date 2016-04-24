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
    	
    	// Checking player entity for collisions
    	collidingEntity = entityCollisionCheck( playerEntity.getBoundingBox() );
		if ( collidingEntity != null )
		{
			//System.out.println( "Player is colliding with Entity #" + entityList.indexOf( collidingEntity ) + " in list!");
		}
		
		collidingWall = wallCollisionCheck( playerEntity.getBoundingBox() );
    	if ( collidingWall != null )
    	{
    		//System.out.println("Player is colliding with Wall #" + wallList.indexOf( collidingWall ) + " in list!");
    	}
    	
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
}
