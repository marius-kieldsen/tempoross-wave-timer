package com.temporosswavetimer;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TemporossWaveTimerPluginTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TemporossWaveTimerPlugin.class);
		RuneLite.main(args);
	}
}