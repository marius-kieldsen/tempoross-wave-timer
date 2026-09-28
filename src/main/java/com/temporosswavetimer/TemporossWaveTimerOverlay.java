package com.temporosswavetimer;

import lombok.Getter;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.Perspective;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.client.ui.overlay.OverlayPanel;
import net.runelite.client.ui.overlay.OverlayPosition;
import net.runelite.client.ui.overlay.OverlayUtil;
import net.runelite.client.ui.overlay.components.LineComponent;

import javax.inject.Inject;
import java.awt.*;

public class TemporossWaveTimerOverlay extends OverlayPanel {

    @Inject private Client client;
    @Inject private TemporossWaveTimerConfig config;

    @Setter @Getter private WorldPoint nearestTetherPoint = null;
    @Setter private Integer ticksRemaining = null;

    private static final Color SAFE_COLOR = Color.GREEN;
    private static final Color RUN_COLOR = Color.YELLOW;
    private static final Color LATE_COLOR = Color.RED;

    private String statusText() {
        if (ticksRemaining == null) return "Waiting on colossal wave";
        if (ticksRemaining < 0) return "Late";
        if (ticksRemaining <= config.runThreshold()) return "RUN";
        return "SAFE";
    }

    private Color statusColor() {
        if (ticksRemaining == null) return Color.WHITE;
        if (ticksRemaining < 0) return LATE_COLOR;
        if (ticksRemaining <= config.runThreshold()) return RUN_COLOR;
        return SAFE_COLOR;
    }

    public void updatePosition() {
        setPosition(config.overlayMode() == OverlayMode.PANEL ? OverlayPosition.TOP_LEFT : OverlayPosition.DYNAMIC);
    }

    @Override
    public Dimension render(Graphics2D graphics) {
        if (config.overlayMode() == OverlayMode.PANEL) {
            panelComponent.getChildren().clear();
            panelComponent.getChildren().add(LineComponent.builder()
                    .left(statusText()).leftColor(statusColor())
                    .build());
            if (ticksRemaining != null) {
                panelComponent.getChildren().add(LineComponent.builder()
                        .left("Tether in:").right(ticksRemaining + " ticks")
                        .build());
            }
            return panelComponent.render(graphics);
        }

        WorldPoint target = config.overlayMode() == OverlayMode.ABOVE_PLAYER ? client.getLocalPlayer().getWorldLocation() : nearestTetherPoint;
        if (target == null || ticksRemaining == null) return null;

        LocalPoint local = LocalPoint.fromWorld(client, target);
        if (local == null)  return null;

        Point canvasPoint = Perspective.getCanvasTextLocation(client, graphics, local, statusText(), 40);
        if (canvasPoint != null) {
            OverlayUtil.renderTextLocation(graphics, canvasPoint, statusText() + " " + ticksRemaining, statusColor());
        }
        return null;
    }

}
