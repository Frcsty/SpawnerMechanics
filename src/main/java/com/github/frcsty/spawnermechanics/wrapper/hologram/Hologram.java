package com.github.frcsty.spawnermechanics.wrapper.hologram;

import com.github.frcsty.spawnermechanics.util.Color;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class Hologram {

    private final Location location;
    private final List<HologramLine> lines = new ArrayList<>();

    Hologram(final Location location, final String... lines) {
        this.location = location;
        Arrays.stream(lines).forEach(line -> this.lines.add(new HologramLine(line, location)));
    }

    void appendLine(final String text) {
        lines.add(new HologramLine(text, location));
    }

    public Location getLocation() {
        return this.location;
    }

    void clearLines() {
        lines.forEach(line -> line.getStand().remove());
    }

    private class HologramLine {
        private final ArmorStand stand;

        HologramLine(final String text, final Location location) {
            this.stand = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);

            stand.setGravity(false);
            stand.setVisible(false);
            stand.setCustomNameVisible(true);
            stand.setCustomName(Color.colorize(text));
        }

        ArmorStand getStand() {
            return this.stand;
        }

    }

}
