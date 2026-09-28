package com.temporosswavetimer;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.GameObject;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.*;
import net.runelite.api.gameval.ObjectID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@PluginDescriptor(
		name = "Tempoross Wave Timer",
		description = "Countdown to the last safe tick to tether during Tempoross' colossal wave",
		tags = {"tempoross", "wave", "tether", "overlay"}
)

public class TemporossWaveTimerPlugin extends Plugin {
	@Inject private Client client;
	@Inject private OverlayManager overlayManager;
	@Inject private TemporossWaveTimerOverlay overlay;
	@Inject private TemporossWaveTimerConfig config;

	private final WaveTracker waveTracker = new WaveTracker();
	private final List<WorldPoint> tetherPoints = new ArrayList<>();

	private static final Set<Integer> TETHER_OBJECT_IDS = Set.of(
			ObjectID.TEMPOROSS_MAST_BOTTOM_WEST,
			ObjectID.TEMPOROSS_MAST_BOTTOM_EAST,
			ObjectID.TEMPOROSS_TOTEM_NORTH,
			ObjectID.TEMPOROSS_TOTEM_SOUTH
	);


	@Override
	protected void startUp() {
		overlayManager.add(overlay);
		overlay.updatePosition();
	}

	@Override
	protected void shutDown() {
		overlayManager.remove(overlay);
		tetherPoints.clear();
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event) {
		GameObject object = event.getGameObject();
		if (TETHER_OBJECT_IDS.contains(object.getId())) {
			tetherPoints.add(object.getWorldLocation());
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event) {
		if (event.getType() == ChatMessageType.GAMEMESSAGE && event.getMessage().contains("A colossal wave closes in...")) {
			waveTracker.startWaveTimer(client.getTickCount());
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event) {
		if (event.getGroup().equals("temporosswavetimer") && event.getKey().equals("overlayMode")) {
			overlay.updatePosition();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event) {
		waveTracker.tick(client.getTickCount());

		if (!waveTracker.isActive()) {
			overlay.setTicksRemaining(null);
			return;
		}

		WorldPoint player = client.getLocalPlayer().getWorldLocation();
		int nearestTetherPoint = tetherPoints.stream()
				.mapToInt(player::distanceTo)
				.min()
				.orElse(Integer.MAX_VALUE);
		overlay.setTicksRemaining(waveTracker.ticksRemaining(client.getTickCount(), nearestTetherPoint));
	}


	@Provides
	TemporossWaveTimerConfig provideConfig(ConfigManager configManager) {
		return configManager.getConfig(TemporossWaveTimerConfig.class);
	}
}
