package com.ucf.aigame;

import java.io.IOException;

import com.badlogic.gdx.Game;

public class GameMain extends Game
{
	@Override
	public void create ()
	{
		//Load Textures/Sounds/etc. into game.
		AssetLoader.load();		
		//Creates a screen to display the game on.
		setScreen(new GameScreen());

	}

	@Override
	public void render ()
	{
		super.render();
		super.dispose();
	}
}
