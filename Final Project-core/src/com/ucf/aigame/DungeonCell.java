package com.ucf.aigame;

import java.util.LinkedList;
import java.util.Random;

/**
 * DungeonCell objects are partitioned spaces within the entire dungeon, wherever possible, rooms will be inserted
 * within a DungeonCell, otherwise the cell will be partitioned.
 *
 * Created by Bryan on 3/28/2016.
 */
public class DungeonCell
{
    // The closer these values are to the minimum room dimensions, the less variation there will be in room placement within a cell
    private static final int MINIMUM_CELL_WIDTH = 35;
    private static final int MINIMUM_CELL_HEIGHT = 35;

    // The closer these values are to minimum cell dimensions, the less variation there will be in room sizes
    public static final int MINIMUM_ROOM_WIDTH = 16;
    public static final int MINIMUM_ROOM_HEIGHT = 16;

    // The larger the value, the greater the possible room sizes and rooms placement options
    public static final int MAXIMUM_CELL_WIDTH = 75;
    public static final int MAXIMUM_CELL_HEIGHT = 75;

    private Rectangle boundingBox;  // Bounds the dimensions of this cell
    private Rectangle dungeonRoom;  // If this cell is a leaf cell in the BSP, it will contain a room

    private LinkedList<Rectangle> roomsInCell;  // List of all rooms within the dimensions of this cell
    private DungeonCell parentCell;     // This cell's parent cell
    private DungeonCell childCell1;     // Will always be top cell or right cell
    private DungeonCell childCell2;     // Will always be bottom cell or left cell

    private int partitionType;          // 0 = vertical, 1 = horizontal

    private Random random = new Random();   // Random number generator

    // Constructor
    public DungeonCell( Rectangle boundingBox )
    {
        this.boundingBox = boundingBox;
        roomsInCell = new LinkedList<Rectangle>();
    }

    // Attempts to insert a room within this cell, if the cell is too large, partitions the cell and tries again on the new cells
    public boolean insertDungeonRoom( )
    {
        // Checks if cell can be partitioned, if it can it will be.
        if ( partitionCell() )
        {
            childCell1.setParentCell( getMe() );
            childCell2.setParentCell( getMe() );

            return false;
        }

        // Inserts a room into this cell, ensuring at least 2 spaces as a buffer zone between the room and edges of the cell
        int dungeonRoomOffsetX = random.nextInt( Math.round( boundingBox.getWidth() / 2 ) - 2 ) + 2;
        int dungeonRoomOffsetY = random.nextInt( Math.round( boundingBox.getHeight() / 2 ) - 2 ) + 2;

        int dungeonRoomWidth = random.nextInt( boundingBox.getWidth() - dungeonRoomOffsetX );
        int dungeonRoomHeight = random.nextInt( boundingBox.getHeight() - dungeonRoomOffsetY );

        if ( dungeonRoomWidth < MINIMUM_ROOM_WIDTH )
        {
            dungeonRoomOffsetX = 1;
            dungeonRoomWidth = MINIMUM_ROOM_WIDTH;
        }

        if ( dungeonRoomHeight < MINIMUM_ROOM_HEIGHT )
        {
            dungeonRoomOffsetY = 1;
            dungeonRoomHeight = MINIMUM_ROOM_HEIGHT;
        }

        dungeonRoom = new Rectangle( boundingBox.getX() + dungeonRoomOffsetX, boundingBox.getY() - dungeonRoomOffsetY, dungeonRoomWidth, dungeonRoomHeight );
        roomsInCell.add( dungeonRoom );

        return true;
    }

    //Getters and Setters for various private variables
    public Rectangle getRoom()
    {
        return dungeonRoom;
    }

    public DungeonCell getChildCell1()
    {
        return childCell1;
    }

    public DungeonCell getChildCell2()
    {
        return childCell2;
    }

    public Rectangle getBoundingBox()
    {
        return boundingBox;
    }

    public DungeonCell getParentCell()
    {
        return parentCell;
    }

    public DungeonCell getMe()
    {
        return this;
    }

    public LinkedList<Rectangle> getRoomsInCell()
    {
        return roomsInCell;
    }

    public void setRoomsInCell( LinkedList<Rectangle> roomsInCell )
    {
        ( this.roomsInCell ).addAll( roomsInCell );
    }

    public void setParentCell( DungeonCell dungeonCell)
    {
        parentCell = dungeonCell;
    }

    public int getPartitionType()
    {
        return partitionType;
    }

    //If able, partitions this cell
    private boolean partitionCell()
    {
        Random random = new Random();

        //If cell is smaller than maximum cell dimensions, don't partition it.
        if ( boundingBox.getWidth() <  MAXIMUM_CELL_WIDTH  && boundingBox.getHeight() < MAXIMUM_CELL_HEIGHT )
        {
            return false;
        }

        // Determines which partition type is valid, if both are, chooses one randomly
        if ( boundingBox.getWidth() < MAXIMUM_CELL_WIDTH )
        {
            partitionType = 1;
        }
        else if ( boundingBox.getHeight() < MAXIMUM_CELL_HEIGHT )
        {
            partitionType = 0;
        }
        else
        {
            partitionType = random.nextInt( 2 );        // 0 = vertical, 1 = horizontal
        }

        // Do the partition
        if ( partitionType == 0 )
        {
            partitionVertically();
        }

        if ( partitionType == 1 )
        {
            partitionHorizontally();
        }

        return true;
    }

    // Vertically partitions this cell
    private void partitionVertically()
    {
        //partitionDistance is the distance from the Upper-Left corner of the cell;
        int partitionDistance = random.nextInt( boundingBox.getWidth() - ( MINIMUM_CELL_WIDTH * 2 ) ) + MINIMUM_CELL_WIDTH;

        //Split this cell vertically partitionDistance units away from the Upper-Left corner of the cell.
        childCell1 = new DungeonCell( new Rectangle( boundingBox.getX() + partitionDistance, boundingBox.getY(), boundingBox.getWidth() - partitionDistance, boundingBox.getHeight() ) );
        childCell2 = new DungeonCell( new Rectangle( boundingBox.getX(), boundingBox.getY(), partitionDistance, boundingBox.getHeight() ) );

    }

    //Horizontally partitions this cell.
    private void partitionHorizontally()
    {
        //partitionDistance is the distance from the Upper-Left corner of the cell;
        int partitionDistance = random.nextInt( boundingBox.getHeight() - ( MINIMUM_CELL_HEIGHT * 2 ) ) + MINIMUM_CELL_HEIGHT;

        //Split this cell horizontally partitionDistance units away from the Upper-Left corner of the cell.
        childCell1 = new DungeonCell( new Rectangle( boundingBox.getX(), boundingBox.getY(), boundingBox.getWidth(), partitionDistance ) );
        childCell2 = new DungeonCell( new Rectangle( boundingBox.getX(), boundingBox.getY() - partitionDistance, boundingBox.getWidth(), boundingBox.getHeight() - partitionDistance ) );
    }
}
