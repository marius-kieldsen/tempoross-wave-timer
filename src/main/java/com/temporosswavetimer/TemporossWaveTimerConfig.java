package com.temporosswavetimer;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

import java.awt.Color;

@ConfigGroup("temporosswavetimer")
public interface TemporossWaveTimerConfig extends Config
{

	@ConfigItem(
			keyName = "overlayMode",
			name = "Overlay position",
			description = "Where to show the countdown",
			position = 0
	)
	default OverlayMode overlayMode() { return OverlayMode.PANEL; }

	@ConfigItem(
			keyName = "runThreshold",
			name = "Run threshold (ticks)",
			description = "Sets a higher tick threshold for a bigger margin of error",
			position = 1
	)
	default int runThreshold() { return 0; }

	@ConfigItem(
			keyName = "showTicksAsSeconds",
			name = "Show seconds instead of ticks",
			description = "Display remaining time as seconds instead of ticks",
			position = 2
	)
	default boolean showTicksAsSeconds() { return false; }

	@ConfigItem(
			keyName = "safeColor",
			name = "Safe color",
			description = "Color used for the SAFE status",
			position = 3
	)
	default Color safeColor() { return Color.GREEN; }

	@ConfigItem(
			keyName = "runColor",
			name = "Run color",
			description = "Color used for the RUN status",
			position = 4
	)
	default Color runColor() { return Color.YELLOW; }

	@ConfigItem(
			keyName = "lateColor",
			name = "Late color",
			description = "Color used for the LATE status",
			position = 5
	)
	default Color lateColor() { return Color.RED; }
}
