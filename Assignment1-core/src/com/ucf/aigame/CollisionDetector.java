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
	private ArrayList<Treasure> treasureList;

    CollisionDetector ( GameWorld gameWorld )
    {
    	playerEntity = gameWorld.getPlayerEntity();
    	wallList = gameWorld.getWallList();
    	entityList = gameWorld.getEntityList();
		treasureList = gameWorld.getTreasureArrayList();
    }
    
    public void update( GameWorld gameWorld )
    {
    	entityList = gameWorld.getEntityList();
    	playerEntity = gameWorld.getPlayerEntity();
		treasureList = gameWorld.getTreasureArrayList();
    	
    	GameEntity collidingEntity;
    	WallObject collidingWall;
		Treasure collidingTreasure;
    	
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

		collidingTreasure = treasureCollisionCheck(playerEntity.getBoundingBox());
		if ( collidingTreasure != null )
		{
			//System.out.println("Player is colliding with Treasure #" + treasureList.indexOf( collidingTreasure ) + " in list!");
			System.out.println("Treasure Collected!");
			playerEntity.getBackpack().add( collidingTreasure );
			treasureList.remove(collidingTreasure);
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

	private Treasure treasureCollisionCheck( BoundingBox boundingBox )
	{
		for ( int i = 0; i < treasureList.size(); i++ )
		{
			if ( boundingBox.isBoundingBoxIntersecting( treasureList.get(i).getBoundingBox() ) )
			{
				return treasureList.get( i );
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
