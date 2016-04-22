package com.ucf.aigame;

import java.io.*;
import java.util.LinkedList;

public class DungeonGenerator
{
	private int dungeonWidth;	// Change these to vary overall dungeon size dungeon
	private int dungeonHeight;

    private static boolean[][] dungeonMap;   // True if wall, false if no wall, index is: [y-coordinate][x-coordinate]
    private static boolean[][] floorMap;	// Map of all floor spaces
    
    private static LinkedList<Rectangle> roomList;   // Holds all rooms within the BSP, used to set dungeonMap values.
    private static LinkedList<CartesianPoint> doorList;  // When paths are drawn, holds the coordinate value of the end of each path, used to cut a door into a wall
    private static LinkedList<CartesianPoint> inOrderCellList;   // Holds the in-order BSP tree traversal top-left corner co-ordinate values of all cells (nodes) within the BSP

    private static DungeonCell rootCell;    //Root of the BSP

    public DungeonGenerator( int dungeonWidth, int dungeonHeight ) throws IOException
    {
    	this.dungeonWidth = dungeonWidth;
    	this.dungeonHeight = dungeonHeight;
    	
        PrintWriter fileOut = new PrintWriter( "DungeonMap.txt" );  // Creates a DungeonMap.txt output file within the Assignment's folder

        // Initialize private variables
        dungeonMap = new boolean[ dungeonHeight ][ dungeonWidth ];
        floorMap = new boolean[ dungeonHeight ][ dungeonWidth ];
        
        roomList = new LinkedList<Rectangle>();
        doorList = new LinkedList<CartesianPoint>();
        inOrderCellList = new LinkedList<CartesianPoint>();

        // Make sure all spaces within the map are initially set to false (0).
        for ( int y = 0; y < dungeonHeight; y++ )
        {
            for ( int x = 0; x < dungeonWidth; x++ )
            {
                dungeonMap[y][x] = false;
                floorMap[y][x] = false;
            }
        }

        // Initialize the BSP root cell, then insert it into the BSP
        rootCell = new DungeonCell( new Rectangle( 0, dungeonHeight, dungeonWidth, dungeonHeight ) );
        insertRoom( rootCell ); // This will completely fill the BSP

        // Iterate over filled roomList and set all coordinates of walls of rooms to true.
        for ( int i = 0; i < roomList.size(); i++ )
        {
            for ( int x = 0; x < roomList.get( i ).getWidth() + 1; x++ )
            {
                dungeonMap[roomList.get( i ).getY()][roomList.get( i ).getX() + x] = true;
                dungeonMap[roomList.get( i ).getY() - roomList.get( i ).getHeight()][roomList.get( i ).getX() + x] = true;
            }

            for ( int y = 0; y < roomList.get( i ).getHeight() + 1; y++ )
            {
                dungeonMap[roomList.get( i ).getY() - y][roomList.get( i ).getX()] = true;
                dungeonMap[roomList.get( i ).getY() - y][roomList.get( i ).getX() + roomList.get( i ).getWidth()] = true;
            }
            
            for ( int y = 1; y < roomList.get( i ).getHeight(); y++ )
            {
            	for ( int x = 1; x < roomList.get( i ).getWidth(); x++ )
            	{
            		floorMap[roomList.get( i ).getY() - y][roomList.get( i ).getX() + x] = true;
            	}
            }
            
            /*int interiorX = roomList.get( i ).getX() + 1;
            int interiorY = roomList.get( i ).getY() - 1;
            
            for ( ; interiorX < interiorX + roomList.get( i ).getWidth() - 1; interiorX++ )
            {
            	for ( ; interiorY > interiorY - roomList.get( i ).getHeight() + 1; interiorY-- )
            	{
            		dungeonMap[interiorY][interiorX] = true;
            	}
            }*/
        }

        traverseByLevel();  // This will traverse the tree from bottom level to top, drawing paths between rooms along the way
        inOrderTraversal( rootCell );   // Traverse the tree in-order and fill inOrderCellList

        // Prints BSP cell co-ordinate values In-Order ( These are not the room co-ordinates )
        fileOut.print("In-Order Tree Traversal: ");
        for ( int i = 0; i < inOrderCellList.size(); i++ )
        {
            fileOut.print("(" + inOrderCellList.get( i ).getX() + "," + inOrderCellList.get( i ).getY() + "), ");

            if ( i % 5 == 0)
            {
                fileOut.println();
            }
        }
        fileOut.println();
        fileOut.println();

        // Create a door leading into whatever wall the drawn paths end at.
        for ( int i = 0; i < doorList.size(); i++ )
        {
            dungeonMap[doorList.get(i).getY()][doorList.get(i).getX()] = false;
        }

        // Print the dungeonMap to file output.
        for ( int y = dungeonHeight - 1; y >= 0; y-- )
        {
            for ( int x = 0; x < dungeonWidth; x++ )
            {
                if ( dungeonMap[y][x] )
                {
                    fileOut.print( "1" );
                }
                else
                {
                    fileOut.print( "0" );
                }
            }

            fileOut.println( );
        }

        fileOut.close();
    }


    // Inserts cells into the BSP, then rooms at leaf cells
    public static void insertRoom( DungeonCell dungeonCell )
    {
        if ( dungeonCell.insertDungeonRoom( ) )
        {
            roomList.add( dungeonCell.getRoom() );
        }

        if ( dungeonCell.getChildCell1() != null )
        {
            insertRoom( dungeonCell.getChildCell1() );
        }

        if ( dungeonCell.getChildCell2() != null )
        {
            insertRoom( dungeonCell.getChildCell2() );
        }
    }

    //In-Order Traversal of the BSP
    public static void inOrderTraversal( DungeonCell dungeonCell )
    {
        if ( dungeonCell == null )
        {
            return;
        }

        inOrderTraversal( dungeonCell.getChildCell1() );
        inOrderCellList.add( new CartesianPoint( dungeonCell.getBoundingBox().getX(), dungeonCell.getBoundingBox().getY() ) );
        inOrderTraversal( dungeonCell.getChildCell2() );
    }

    //Reverse-level traversal of the BSP
    public static void traverseByLevel()
    {
        int height = getHeight( rootCell );

        for ( int i = height; i > 0; i-- )
        {
            insertPathsByLevel( rootCell, i );
        }
    }

    //Inserts a path to connect rooms between two children of the same level
    public static void insertPathsByLevel( DungeonCell dungeonCell, int level )
    {
        if ( dungeonCell == null )
        {
            return;
        }

        if ( level == 1 )
        {
            if ( dungeonCell.getParentCell() != null )
            {
                dungeonCell.getParentCell().setRoomsInCell( dungeonCell.getRoomsInCell() );
                insertPath( dungeonCell.getParentCell().getChildCell1().getRoomsInCell(), dungeonCell.getParentCell().getChildCell2().getRoomsInCell(), dungeonCell.getParentCell().getPartitionType() );
            }
        }
        else if ( level > 1 )
        {
            insertPathsByLevel( dungeonCell.getChildCell1(), level - 1 );
            insertPathsByLevel( dungeonCell.getChildCell2(), level - 1 );
        }
    }

    //Returns height of the BSP containing DungeonCell nodes.
    private static int getHeight( DungeonCell dungeonCell )
    {
        if ( dungeonCell == null )
        {
            return 0;
        }

        int firstHeight = getHeight( dungeonCell.getChildCell1() );
        int secondHeight = getHeight( dungeonCell.getChildCell2() );

        if ( firstHeight > secondHeight )
        {
            return ( firstHeight + 1 );
        }
        else
        {
            return ( secondHeight + 1 );
        }
    }

    // Does the actual path-drawing. roomLists contain all rooms within one child of the subtree
    public static void insertPath( LinkedList<Rectangle> roomList1, LinkedList<Rectangle> roomList2, int partitionType )
    {
        // Variables used to represent current position of the path-drawer
        int pathDrawerX = 0;
        int pathDrawerY = 0;

        boolean hasTurned = false;  // Flag to indicate when a path changes direction, used to make directional transitions look nicer

        // If the partition of the parent cell was vertical, the path will initially be drawn towards the right.
        if ( partitionType == 0 )
        {
            // Retrieve the two closest rooms between both children
            Rectangle leftMostRoom = roomList1.getFirst();
            Rectangle rightMostRoom = roomList2.getFirst();

            for ( int i = 0; i < roomList1.size(); i++ )
            {
                if ( roomList1.get(i).getX() < leftMostRoom.getX() )
                {
                    leftMostRoom = roomList1.get(i);
                }
            }

            for (int i = 0; i < roomList2.size(); i++)
            {
                if ( roomList2.get(i).getX() > rightMostRoom.getX() )
                {
                    rightMostRoom = roomList2.get(i);
                }
            }

            // Set the pathDrawer to the center of the right wall of the right-most room in the first child.
            pathDrawerX = rightMostRoom.getX() + rightMostRoom.getWidth();
            pathDrawerY = rightMostRoom.getY() - Math.round(rightMostRoom.getHeight() / 2);


            // Accounting for situation where path would end in the top left corner of the next room.
            if ( pathDrawerY == leftMostRoom.getY() )
            {
                pathDrawerY--;
            }

            // Accounting for situation where path would end in the bottom left corner of the next room.
            if ( pathDrawerY == leftMostRoom.getY() - leftMostRoom.getHeight() )
            {
                pathDrawerY++;
            }

            // Place a door in the wall before path drawing
            dungeonMap[pathDrawerY][pathDrawerX] = false;

            // Path drawing will stop if a wall is encountered
            while ( !dungeonMap[pathDrawerY][pathDrawerX] )
            {
            	floorMap[pathDrawerY][pathDrawerX] = true;
                // Draws path to the right until path hits a wall or is at the same y-value of the center of the target room
                if (pathDrawerX < leftMostRoom.getX() + Math.round(leftMostRoom.getWidth() / 2))
                {
                    dungeonMap[pathDrawerY + 1][pathDrawerX] = true;
                    dungeonMap[pathDrawerY - 1][pathDrawerX] = true;
                    pathDrawerX++;
                }
                else
                {
                    // If target room has not yet been hit, path drawing continues up or down as needed
                    dungeonMap[pathDrawerY][pathDrawerX + 1] = true;
                    dungeonMap[pathDrawerY][pathDrawerX - 1] = true;

                    if (leftMostRoom.getY() > rightMostRoom.getY())
                    {

                        //Cleans up hallway corners when turning upwards
                        if (!hasTurned)
                        {
                            dungeonMap[pathDrawerY - 1][pathDrawerX] = true;
                            dungeonMap[pathDrawerY - 1][pathDrawerX + 1] = true;
                            dungeonMap[pathDrawerY][pathDrawerX - 1] = true;

                            hasTurned = true;
                        }

                        pathDrawerY++;
                    }
                    else
                    {
                        // Cleans up hallway corners when turning downwards
                        if (!hasTurned)
                        {
                            dungeonMap[pathDrawerY + 1][pathDrawerX] = true;
                            dungeonMap[pathDrawerY + 1][pathDrawerX + 1] = true;
                            dungeonMap[pathDrawerY][pathDrawerX - 1] = false;

                            hasTurned = true;
                        }

                        pathDrawerY--;
                    }
                }
            }
        }

        // If the partition of the parent cell was horizontal, the path will initially be drawn upwards.
        if ( partitionType == 1 )
        {
            // Retrieve the two closest rooms between both children
            Rectangle downMostRoom = roomList1.getFirst();
            Rectangle upMostRoom = roomList2.getFirst();

            for (int i = 0; i < roomList1.size(); i++)
            {
                if (roomList1.get(i).getY() < downMostRoom.getY())
                {
                    downMostRoom = roomList1.get(i);
                }
            }

            for (int i = 0; i < roomList2.size(); i++)
            {
                if (roomList2.get(i).getY() > upMostRoom.getY())
                {
                    upMostRoom = roomList2.get(i);
                }
            }

            // Set the pathDrawer to the center of the top wall of the top-most room in the first child.
            pathDrawerX = upMostRoom.getX() + Math.round(upMostRoom.getWidth() / 2);
            pathDrawerY = upMostRoom.getY();

            //Accounting for situation where path would end in the bottom left corner of the next room.
            if ( pathDrawerX == downMostRoom.getX() )
            {
                pathDrawerX++;
            }

            //Accounting for situation where path would end in the bottom right corner of the next room.
            if ( pathDrawerX == downMostRoom.getX() + downMostRoom.getWidth() )
            {
                pathDrawerX--;
            }

            // Place a door in the wall before path drawing
            dungeonMap[pathDrawerY][pathDrawerX] = false;

            // Path drawing will stop if a wall is encountered
            while (!dungeonMap[pathDrawerY][pathDrawerX])
            {
            	floorMap[pathDrawerY][pathDrawerX] = true;
                // Draws path upwards until path hits a wall or is at the same x-value of the center of the target room
                if (pathDrawerY < downMostRoom.getY() - Math.round(downMostRoom.getHeight() / 2))
                {
                    dungeonMap[pathDrawerY][pathDrawerX + 1] = true;
                    dungeonMap[pathDrawerY][pathDrawerX - 1] = true;
                    pathDrawerY++;
                }
                else
                {
                    // If target room has not yet been hit, path drawing continues left or right as needed
                    dungeonMap[pathDrawerY + 1][pathDrawerX] = true;
                    dungeonMap[pathDrawerY - 1][pathDrawerX] = true;

                    if (downMostRoom.getX() > upMostRoom.getX())
                    {
                        //Cleans up hallway corners when turning right
                        if ( !hasTurned )
                        {
                            dungeonMap[pathDrawerY][pathDrawerX - 1] = true;
                            dungeonMap[pathDrawerY + 1][pathDrawerX - 1] = true;
                            dungeonMap[pathDrawerY - 1][pathDrawerX] = false;

                            hasTurned = true;
                        }

                        pathDrawerX++;
                    }
                    else
                    {
                        //Cleans up hallway corners when turning left
                        if (!hasTurned)
                        {
                            dungeonMap[pathDrawerY][pathDrawerX + 1] = true;
                            dungeonMap[pathDrawerY + 1][pathDrawerX + 1] = true;
                            dungeonMap[pathDrawerY - 1][pathDrawerX] = false;

                            hasTurned = true;
                        }

                        pathDrawerX--;
                    }
                }
            }
        }

        //Store coordinates of room entrance path drawing ended at.
        doorList.add( new CartesianPoint( pathDrawerX, pathDrawerY) );
        floorMap[pathDrawerY][pathDrawerX] = true;
    }
    
    public boolean[][] getDungeonMap()
    {
    	return dungeonMap;
    }
    
    public boolean[][] getFloorMap()
    {
    	return floorMap;
    }

    public LinkedList<CartesianPoint> getDoorList()
    {
        return doorList;
    }
}
