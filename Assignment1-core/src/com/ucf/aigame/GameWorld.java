package com.ucf.aigame;

import com.badlogic.gdx.math.Vector2;
import java.util.ArrayList;
import java.util.LinkedList;

public class GameWorld
{
    private PlayerEntity playerEntity;

    private ArrayList<WallObject> wallObjectArrayList;
    private ArrayList<GameEntity> gameEntityArrayList;
    private ArrayList<GraphNode> graphNodeArrayList;
    
    private boolean[][] dungeonMap;
    private boolean[][] floorMap;
    private LinkedList<CartesianPoint> doorList;

    private float gameWidth;
    private float gameHeight;

    private static final int TILE_DIMENSIONS = 16;
    public Vector2 goalLocation;

    private int monsterDelay = 6;
    private int monsterDelayCounter = 0;

    public GameWorld( float midPointX, float midPointY, float gameWidth, float gameHeight, DungeonGenerator dungeonGenerator )
    {
        // Instantiate and deploy entities
        //playerEntity = new PlayerEntity(midPointX, midPointY, TILE_DIMENSIONS, TILE_DIMENSIONS, this);

        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        dungeonMap = dungeonGenerator.getDungeonMap();
        floorMap = dungeonGenerator.getFloorMap();
        doorList = dungeonGenerator.getDoorList();

        boolean summonedPlayer = false;

        // Create array of non-player entities and walls
        gameEntityArrayList = new ArrayList<GameEntity>();
        //gameEntityArrayList.add(new GameEntity(64, 544, TILE_DIMENSIONS, TILE_DIMENSIONS));
        //gameEntityArrayList.add(new GameEntity(544, 544, TILE_DIMENSIONS, TILE_DIMENSIONS));

        wallObjectArrayList = new ArrayList<WallObject>();

        graphNodeArrayList = new ArrayList<GraphNode>();

        for ( int y = 0; y < gameHeight / TILE_DIMENSIONS; y++ )
        {
        	for ( int x = 0; x < gameWidth / TILE_DIMENSIONS; x++ )
        	{
        		if ( dungeonMap[y][x] )
        		{
            		wallObjectArrayList.add( new WallObject( new Rectangle( TILE_DIMENSIONS * x, TILE_DIMENSIONS * y, TILE_DIMENSIONS, TILE_DIMENSIONS ) ) );
        		}

                if ( floorMap[y][x] && !summonedPlayer )
                {
                    playerEntity = new PlayerEntity(x*TILE_DIMENSIONS, y*TILE_DIMENSIONS, TILE_DIMENSIONS, TILE_DIMENSIONS, this);
                    System.out.println("Player summoned at "+playerEntity.getTiledPositionVector());
                    summonedPlayer = true;
                }

                spawnEntities(x, y);

        	}
        }
    }
        
        
        // ------------------------------------------------------------------
        // CHANGE MAP AND GOAL
        // ------------------------------------------------------------------

        /*int min, max, stable, map = 1, goal = 1;
        float x = 1, y = 1;

        if ( goal == 0 ) { x = 38; y = 18; }
        if ( goal == 1 ) { x = 18; y = 18; }
        if ( goal == 2 ) { x = 4; y = 10; }

        this.goalLocation = new Vector2(( x )*32 -16, ( y )*32 -16);*/



        // Create any other walls you'd like
        // 0 < x < 40 , 0 < y < 20
        //ArrayList<Vector2> innerWalls = new ArrayList<Vector2>();

        /*if ( map == 0 ) {

            // VERTICAL
            min = 4; max = 15; stable = 30;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(stable, i));
            }

            // HORIZONTAL
            min = 14; max = 29; stable = 15;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(i, stable));
            }

            // VERTICAL
            min = 3; max = 18; stable = 11;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(stable, i));
            }

            // HORIZONTAL
            min = 12; max = 25; stable = 11;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(i, stable));
            }

            // HORIZONTAL
            min = 25; max = 36; stable = 5;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(i, stable));
            }

            // HORIZONTAL
            min = 3; max = 10; stable = 4;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(i, stable));
            }
        }

        if ( map == 1 ) {

            // VERTICAL
            min = 4; max = 15; stable = 10;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(stable, i));
            }

            // HORIZONTAL
            min = 10; max = 37; stable = 15;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(i, stable));
            }

            // VERTICAL
            min = 4; max = 15; stable = 30;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(stable, i));
            }

            // HORIZONTAL
            min = 11; max = 18; stable = 6;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(i, stable));
            }

            // VERTICAL
            min = 1; max = 10; stable = 25;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(stable, i));
            }

            // HORIZONTAL
            min = 34; max = 38; stable = 6;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(i, stable));
            }

            // HORIZONTAL
            min = 5; max = 9; stable = 9;

            for (int i = min; i <= max; i++) {
                innerWalls.add(new Vector2(i, stable));
            }
        }

        // ------------------------------------------------------------------

        for (int i = 0; i < innerWalls.size(); i++) {
            newWall(innerWalls.get(i).x , innerWalls.get(i).y );
        }



        boolean createNode;
        // For every (x,y) check if there's a wall. If not, add new GraphNode
        for (x = 0; x < gameWidth; x += TILE_DIMENSIONS)
        {
            for (y = 0; y < gameHeight; y += TILE_DIMENSIONS)
            {
                createNode = true;
                for (int i = 0; i < wallObjectArrayList.size(); i++) {

                    if ( x == wallObjectArrayList.get(i).getXWorldPosition() && y == wallObjectArrayList.get(i).getYWorldPosition() ) {

                        createNode = false;
                        break;
                    }
                }

                // We do not remove nodes sharing location with other
                // entities, initially, since they would be moving

                if ( createNode )
                    graphNodeArrayList.add(new GraphNode(x+(TILE_DIMENSIONS/2), y+(TILE_DIMENSIONS/2) ));
            }
        }

        // Instantiate and deploy entities
        playerEntity = new PlayerEntity(midPointX, midPointY, TILE_DIMENSIONS, TILE_DIMENSIONS, this);


    } */

    public void update(float delta)
    {
        //playerEntity.update(delta);

        /*for (int i = 0; i < gameEntityArrayList.size(); i++)
        {
            gameEntityArrayList.get(i).update(delta);
        }*/
    }

    /*public void newWall(float x, float y)
    {
        wallObjectArrayList.add(new WallObject(x * TILE_DIMENSIONS, y * TILE_DIMENSIONS,
                TILE_DIMENSIONS, TILE_DIMENSIONS));
    }

    public void newEntity(float x, float y)
    {
        gameEntityArrayList.add(new GameEntity(x*TILE_DIMENSIONS - 16, y*TILE_DIMENSIONS - 16,
                TILE_DIMENSIONS, TILE_DIMENSIONS));
    }*/

    private void spawnEntities(int x, int y) {
        // if adjacent walls are on opposite side, it's a hallway, don't spawn
        // if 'near' 4+ other entities, don't spawn
        // Try different methods to determine random spawning (x%y >= 4 ?)

        Vector2 currentCoords = new Vector2(x, y);
        float nearBoundary = 4;

        if ( floorMap[y][x] )
        {
            if ( (monsterDelay == monsterDelayCounter) )
            {
                // If adjacent walls are on opposite sides, it's a hallway; do not spawn
                if ( (dungeonMap[y+1][x] && dungeonMap[y-1][x]) || (dungeonMap[y][x+1] && dungeonMap[y][x-1])
                        || (dungeonMap[y+1][x+1] && dungeonMap[y-1][x-1]) || dungeonMap[y+1][x-1] && dungeonMap[y-1][x+1] )
                {
                    return;
                }


                // If entity is 'near' the player, do not spawn
                currentCoords.x -= playerEntity.getTiledPositionVector().x;
                currentCoords.y -= playerEntity.getTiledPositionVector().y;
                //System.out.print("dist: "+Math.abs( currentCoords.x + currentCoords.y )+" from ");
                //System.out.println( currentCoords );
                if ( Math.abs( currentCoords.x + currentCoords.y ) <= nearBoundary )
                {
                    System.out.println("Too close at "+(int)Math.abs( currentCoords.x + currentCoords.y )+" spaces!");
                    return;
                }


                gameEntityArrayList.add( new GameEntity(new Vector2(x*TILE_DIMENSIONS, y*TILE_DIMENSIONS),
                        new Vector2(TILE_DIMENSIONS, TILE_DIMENSIONS)) );
                //System.out.println("Monster spawned at: " + x + " : " + y);
                monsterDelay++;
                monsterDelayCounter = 1;
            }

            else
            {
                //System.out.println(monsterDelay+" : "+monsterDelayCounter);
                monsterDelayCounter++;
            }
        }
    }

    public float getGameWidth() {
        return gameWidth;
    }

    public float getGameHeight() {
        return gameHeight;
    }

    public PlayerEntity getPlayerEntity()
    {
        return playerEntity;
    }

    public ArrayList<WallObject> getWallList()
    {
        return wallObjectArrayList;
    }

    public ArrayList<GameEntity> getEntityList()
    {
        return gameEntityArrayList;
    }

    public ArrayList<GraphNode> getGraphNodeList() {
        return graphNodeArrayList;
    }

    public int getTileDimensions()
    {
        return TILE_DIMENSIONS;
    }
}
