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
        wallObjectArrayList = new ArrayList<WallObject>();
        graphNodeArrayList = new ArrayList<GraphNode>();

        for ( int y = 0; y < gameHeight / TILE_DIMENSIONS; y++ )
        {
        	for ( int x = 0; x < gameWidth / TILE_DIMENSIONS; x++ )
        	{
        		// Inserting walls into the gameWorld
        		if ( dungeonMap[y][x] )
        		{
            		wallObjectArrayList.add( new WallObject( new BoundingBox( TILE_DIMENSIONS * x, TILE_DIMENSIONS * y, TILE_DIMENSIONS, TILE_DIMENSIONS ) ) );
        		}

                if ( floorMap[y][x] && !summonedPlayer )
                {
                    playerEntity = new PlayerEntity(x * TILE_DIMENSIONS, y * TILE_DIMENSIONS, this);
                    summonedPlayer = true;
                }

                spawnEntity(x, y);
                generateGraphNode(x, y);
        	}
        }

        generateGraphNodeAdjacencyList();
    }

    public void update(float delta)
    {
        playerEntity.update(delta);

        for (int i = 0; i < gameEntityArrayList.size(); i++)
        {
            gameEntityArrayList.get(i).update(delta);
        }
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

    private void spawnEntity(int x, int y) {
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

                if ( Math.abs( currentCoords.x + currentCoords.y ) <= nearBoundary )
                {
                    return;
                }


                gameEntityArrayList.add( new GameEntity(new Vector2(x*TILE_DIMENSIONS, y*TILE_DIMENSIONS),
                        new Vector2(TILE_DIMENSIONS, TILE_DIMENSIONS)) );
                monsterDelay++;
                monsterDelayCounter = 1;
            }

            else
            {
                monsterDelayCounter++;
            }
        }
    }

    private void generateGraphNode(int x, int y)
    {
        if ( floorMap[y][x] )
        {
            // If adjacent walls are on opposite sides, it's a hallway; do not generate
            if ( (dungeonMap[y+1][x] && dungeonMap[y-1][x]) || (dungeonMap[y][x+1] && dungeonMap[y][x-1])
                    || (dungeonMap[y+1][x+1] && dungeonMap[y-1][x-1]) && dungeonMap[y+1][x-1] && dungeonMap[y-1][x+1] )
            {
                return;
            }
            else
            {
                graphNodeArrayList.add(new GraphNode( new Vector2(x*TILE_DIMENSIONS+(TILE_DIMENSIONS/2), y*TILE_DIMENSIONS+(TILE_DIMENSIONS/2)) ));
            }
        }
    }

    private void generateGraphNodeAdjacencyList()
    {
        int unitLength = TILE_DIMENSIONS;
        Vector2 tempVect;
        Vector2 tempVectRight;
        Vector2 tempVectLeft;
        Vector2 tempVectUp;
        Vector2 tempVectDown;

        for(int i=0; i<graphNodeArrayList.size(); i++)
        {
            GraphNode currentNode = graphNodeArrayList.get(i);

            tempVect = currentNode.getCenteredPos().cpy();
            tempVectUp = tempVect.cpy().add(0, unitLength);
            tempVectDown = tempVect.cpy().sub(0, unitLength);
            tempVectLeft = tempVect.cpy().sub(unitLength, 0);
            tempVectRight = tempVect.cpy().add(unitLength, 0);

            for(int j=0; j<graphNodeArrayList.size(); j++)
            {
                GraphNode possibleNeighbor = graphNodeArrayList.get(j);

                // If node is adjacent (right, left, down, up) add to neighbors list
                if ( possibleNeighbor.getCenteredPos().x == tempVectRight.x
                        && possibleNeighbor.getCenteredPos().y == tempVectRight.y
                        || possibleNeighbor.getCenteredPos().x == tempVectLeft.x
                        && possibleNeighbor.getCenteredPos().y == tempVectLeft.y
                        || possibleNeighbor.getCenteredPos().x == tempVectDown.x
                        && possibleNeighbor.getCenteredPos().y == tempVectDown.y
                        || possibleNeighbor.getCenteredPos().x == tempVectUp.x
                        && possibleNeighbor.getCenteredPos().y == tempVectUp.y )
                {
                    currentNode.getNeighbors().add( possibleNeighbor );
                }
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
