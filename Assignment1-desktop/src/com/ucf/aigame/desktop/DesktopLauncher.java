package com.ucf.aigame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.ucf.aigame.GameMain;

public class DesktopLauncher
{
	public static void main (String[] arg)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Assignment1";
		config.width = 640*2;
		config.height = 640;
		new LwjglApplication(new GameMain(), config);
	}
}
