package com.ucf.aigame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by Bryan on 1/31/2016.
 */
public class AssetLoader
{
    public static Texture floorTileTexture;
    public static Texture wallTileTexture;
    public static Texture gameEntityTexture;
    public static Texture playerEntityTexture;

    public static TextureRegion playerEntityTextureRegion;
    public static TextureRegion gameEntityTextureRegion;

    public static void load()
    {
        // Retrieving Textures from assets folder
        floorTileTexture = new Texture(Gdx.files.internal("Floor Tile.png"));
        wallTileTexture =  new Texture(Gdx.files.internal("Wall Tile.png"));
        gameEntityTexture = new Texture(Gdx.files.internal("Game Entity.png"));
        playerEntityTexture = new Texture(Gdx.files.internal("Player.png"));

        // Instantiate TextureRegions
        playerEntityTextureRegion = new TextureRegion(playerEntityTexture);
        gameEntityTextureRegion = new TextureRegion(gameEntityTexture);
    }

    public static void dispose()
    {
        floorTileTexture.dispose();
        wallTileTexture.dispose();
    }
}
