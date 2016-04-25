package com.ucf.aigame;

import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class GameWorld
{
    private PlayerEntity playerEntity;

    private ArrayList<WallObject> wallObjectArrayList;
    private ArrayList<GameEntity> gameEntityArrayList;
    private ArrayList<GraphNode> graphNodeArrayList;
    private ArrayList<Treasure> treasureArrayList;
    
    private boolean[][] dungeonMap;
    private boolean[][] floorMap;

    private float gameWidth;
    private float gameHeight;

    private static final int TILE_DIMENSIONS = 16;
    public Vector2 goalLocation;

    // Spawn Limiters
    private int monsterDelay = 40;
    private int monsterDelayCounter = 0;
    private int treasureDelay = 50;
    private int treasureDelayCounter = 0;

    public GameWorld( float midPointX, float midPointY, float gameWidth, float gameHeight, DungeonGenerator dungeonGenerator )
    {
        // Instantiate and deploy entities
        //playerEntity = new PlayerEntity(midPointX, midPointY, TILE_DIMENSIONS, TILE_DIMENSIONS, this);

        this.gameWidth = gameWidth;
        this.gameHeight = gameHeight;
        dungeonMap = dungeonGenerator.getDungeonMap();
        floorMap = dungeonGenerator.getFloorMap();

        boolean summonedPlayer = false;

        // Create array of non-player entities and walls
        gameEntityArrayList = new ArrayList<GameEntity>();
        wallObjectArrayList = new ArrayList<WallObject>();
        graphNodeArrayList = new ArrayList<GraphNode>();
        treasureArrayList = new ArrayList<Treasure>();

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
                generateTreasure(x, y);
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

    private void spawnEntity(int x, int y) {

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

                gameEntityArrayList.add( new GameEntity(new Vector2(x*TILE_DIMENSIONS, y*TILE_DIMENSIONS), new Vector2(TILE_DIMENSIONS, TILE_DIMENSIONS), this) );
                monsterDelay++;
                monsterDelayCounter = 1;
            }

            else
            {
                monsterDelayCounter++;
            }
        }
    }

    private void generateTreasure(int x, int y) {
        Vector2 currentCoords = new Vector2(x, y);
        float nearBoundary = 1;

        if ( floorMap[y][x] )
        {
            if ( (treasureDelay == treasureDelayCounter) )
            {
                /*/ Treasure should be able to be found in hallways
                // If adjacent walls are on opposite sides, it's a hallway; do not spawn
                if ( (dungeonMap[y+1][x] && dungeonMap[y-1][x]) || (dungeonMap[y][x+1] && dungeonMap[y][x-1])
                        || (dungeonMap[y+1][x+1] && dungeonMap[y-1][x-1]) || dungeonMap[y+1][x-1] && dungeonMap[y-1][x+1] )
                {
                    return;
                } // */

                // If entity is 'near' the player, do not spawn
                currentCoords.x -= playerEntity.getTiledPositionVector().x;
                currentCoords.y -= playerEntity.getTiledPositionVector().y;

                if ( Math.abs( currentCoords.x + currentCoords.y ) <= nearBoundary )
                {
                    return;
                }

                // Randomly generate value and weight of treasure
                Random treasureValueGenerator = new Random();
                int value = treasureValueGenerator.nextInt(91)+10; // Range: 10 - 100
                //int weight = treasureValueGenerator.nextInt(10)+1; // Range: 1 - 10

                treasureArrayList.add(new Treasure(new Vector2(x*TILE_DIMENSIONS, y*TILE_DIMENSIONS), new Vector2(TILE_DIMENSIONS, TILE_DIMENSIONS), value, value/10));
                treasureDelay++;
                treasureDelayCounter = 1;
            }

            else
            {
                treasureDelayCounter++;
            }
        }
    }

    private float getDistance(Vector2 p, Vector2 q) {
        float distance, x, y;

        x = q.x - p.x;
        y = q.y - p.y;
        distance = (float) Math.sqrt(x*x + y*y);

        return distance;
    }

    public void dropTreasure() {

        ArrayList<Vector2> dropZoneStack = new ArrayList<Vector2>();
        Vector2 closestFloorTile = new Vector2(-1, -1);
        Vector2 dropZone, heading;
        Treasure droppedTreasure;
        float curDist = 0, minDist = 1000000;
        int x = 0, y = 0;

        dropZone = playerEntity.getCenter();
        heading = playerEntity.getCurrentHeading();

        // Looking top-right, drop bottom-left
        if (heading.x > 0 && heading.y > 0) {
            dropZone.add((-1.5f) * TILE_DIMENSIONS, (-1.5f) * TILE_DIMENSIONS);
        }
        // Looking top-left, drop bottom-right
        if (heading.x < 0 && heading.y > 0) {
            dropZone.add((1.5f) * TILE_DIMENSIONS, (-1.5f) * TILE_DIMENSIONS);
        }
        // Looking bottom-left, drop top-right
        if (heading.x < 0 && heading.y < 0) {
            dropZone.add((1.5f) * TILE_DIMENSIONS, (1.5f) * TILE_DIMENSIONS);
        }
        // Looking bottom-right, drop top-left
        if (heading.x > 0 && heading.y < 0) {
            dropZone.add((-1.5f) * TILE_DIMENSIONS, (1.5f) * TILE_DIMENSIONS);
        }


        for (y=0; y<gameHeight / TILE_DIMENSIONS; y++) {

            for (x=0; x<gameWidth / TILE_DIMENSIONS; x++) {

                if (floorMap[y][x]) {
                    // Check if closest floor tile
                    curDist = getDistance(new Vector2(x*TILE_DIMENSIONS, y*TILE_DIMENSIONS), dropZone);

                    if ( curDist < minDist ) {
                        minDist = curDist;
                        closestFloorTile = new Vector2(x*TILE_DIMENSIONS, y*TILE_DIMENSIONS);
                        dropZoneStack.add(0, closestFloorTile);
                    }
                }
            }
        }

        /*/
        System.out.println();
        System.out.println("---------------------");
        System.out.println("Dropping Treasure");
        System.out.println("-Player position: "+playerEntity.getCenter());
        System.out.println("-Floor position: "+closestFloorTile);
        System.out.println("-DropZoneQueue: " + dropZoneStack.size());
        //System.out.println("-Player heading: "+playerEntity.getCurrentHeading());
        System.out.println();
        // */

        // Use the first tile that is NOT too close to the player
        for (int i=0; i<dropZoneStack.size(); i++) {
            if ( getDistance(dropZoneStack.get(i), playerEntity.getCenter()) > TILE_DIMENSIONS ) {
                //System.out.println("Tile #"+i+" from stack chosen!");
                closestFloorTile = dropZoneStack.get(i);
                break;
            }
        }

        droppedTreasure = playerEntity.getBackpack().remove();
        if ( droppedTreasure != null ) {
            droppedTreasure.setPosition( closestFloorTile );
            treasureArrayList.add( droppedTreasure );
        }
        else {
            System.out.println("No Treasure to drop!");
        }
    }

    private void generateGraphNode(int x, int y) {
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

    private void generateGraphNodeAdjacencyList() {
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
                    currentNode.getNeighbors().add(possibleNeighbor);
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

    public ArrayList<Treasure> getTreasureArrayList() {
        return treasureArrayList;
    }

    public int getTileDimensions()
    {
        return TILE_DIMENSIONS;
    }

}
