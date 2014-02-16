package com.github.hoqhuuep.islandcraft.digitalclock;

import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class DigitalClockManager {
    private static final long ONE_DAY = 24000;
    private static final long HOURS_PER_DAY = 24;
    private static final long MINUTES_PER_HOUR = 60;
    private static final long ONE_HOUR = ONE_DAY / HOURS_PER_DAY;
    private static final long OFFSET = 6 * ONE_HOUR;
    private final ConfigurationSection config;

    public DigitalClockManager(final ConfigurationSection config) {
        this.config = config;
    }

    public void displayTime(final Player to) {
        final World world = to.getWorld();
        if (world.getEnvironment() == World.Environment.NORMAL) {
            final long time = to.getWorld().getTime();
            final String message = formatTime(time);
            to.sendMessage(message);
        } else {
            to.sendMessage(String.format(config.getString("message.clock-error")));
        }
    }

    private String formatTime(final long time) {
        final long hour = ((time + OFFSET) % ONE_DAY) / ONE_HOUR;
        final long minute = (((time + OFFSET) % ONE_HOUR) * MINUTES_PER_HOUR) / ONE_HOUR;
        return String.format(config.getString("message.clock"), hour, minute);
    }
}
