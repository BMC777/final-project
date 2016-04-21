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
		try {
			setScreen(new GameScreen());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void render ()
	{
		super.render();
		super.dispose();
	}
}
