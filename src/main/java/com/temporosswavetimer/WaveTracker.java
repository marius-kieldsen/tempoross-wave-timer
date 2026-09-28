package com.temporosswavetimer;

public class WaveTracker {
    private static final int TICKS_UNTIL_IMPACT = 12;
    private static final int TETHER_ACTION_TICKS = 1;
    private static final int TILES_PER_TICK_RUNNING = 2;

    private Integer waveStartTick = null; // null = no active wave

    public void startWaveTimer(int currentTick) {
        waveStartTick = currentTick;
    }

    // to expire an active wave event
    public void tick(int currentTick) {
        if (waveStartTick != null && currentTick - waveStartTick >= TICKS_UNTIL_IMPACT) {
            waveStartTick = null;
        }
    }

    public boolean isActive() {
        return waveStartTick != null;
    }

    public Integer ticksRemaining(int currentTick, int distanceToNearestTether) {
        if (waveStartTick == null) return null;
        int elapsed = currentTick - waveStartTick;
        int travelTicks = (distanceToNearestTether + TILES_PER_TICK_RUNNING - 1) / TILES_PER_TICK_RUNNING;
        return (TICKS_UNTIL_IMPACT - TETHER_ACTION_TICKS) - elapsed - travelTicks;
    }

}
