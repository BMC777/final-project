package com.ucf.aigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Bryan on 1/31/2016.
 */
public class AssetLoader
{
	//private static final int SPRITE_SHEET_ROWS = 18;
	//private static final int SPRITE_SHEET_COLUMNS = 29;
	
	private static final int TILE_DIMENSIONS = 16; //Tiles are each 16x16 bits
	
    public static Texture floorTileTexture;
    public static Texture wallTileTexture;
    public static Texture gameEntityTexture;
    public static Texture playerEntityTexture;
    public static Texture spriteSheet; // Each sprite is 16x16 bits, with 1 bit spacing between sprites.

    public static TextureRegion playerEntityTextureRegion;
    public static TextureRegion gameEntityTextureRegion;
    
    // Cave Walls
    public static TextureRegion topMiddleCaveWall;
    public static TextureRegion topLeftCaveWall;
    public static TextureRegion topRightCaveWall;
    public static TextureRegion middleLeftCaveWall;
    public static TextureRegion middleRightCaveWall;
    public static TextureRegion bottomMiddleCaveWall;
    public static TextureRegion bottomLeftCaveWall;
    public static TextureRegion bottomRightCaveWall;
    
    // Dirt Floors
    public static TextureRegion dirtFloor3;

    // Treasure
    public static Texture treasureJewel;
    
    public static void load()
    {
        // Retrieving Textures from assets folder
        floorTileTexture = new Texture(Gdx.files.internal("floor_tile"));
        wallTileTexture =  new Texture(Gdx.files.internal("wall_tile"));
        gameEntityTexture = new Texture(Gdx.files.internal("game_entity"));
        playerEntityTexture = new Texture(Gdx.files.internal("player"));
        spriteSheet = new Texture( Gdx.files.internal( "sprite_sheet"));
        treasureJewel = new Texture(Gdx.files.internal("jewel.png"));

        // Initialize TextureRegions
        playerEntityTextureRegion = new TextureRegion(playerEntityTexture);
        gameEntityTextureRegion = new TextureRegion(gameEntityTexture);
        
        // CaveWall initialization
        topMiddleCaveWall = new TextureRegion( spriteSheet, 11 + ( TILE_DIMENSIONS * 10), 0, TILE_DIMENSIONS, TILE_DIMENSIONS );
        dirtFloor3 = new TextureRegion( spriteSheet, 18 + ( TILE_DIMENSIONS * 17 ), 13 + ( TILE_DIMENSIONS * 12 ), TILE_DIMENSIONS, TILE_DIMENSIONS );
    }

    public static void dispose()
    {
        floorTileTexture.dispose();
        wallTileTexture.dispose();
    }
}
