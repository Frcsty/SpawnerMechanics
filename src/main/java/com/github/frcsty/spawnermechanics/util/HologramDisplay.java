package com.github.frcsty.spawnermechanics.util;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.plugin.java.JavaPlugin;

public final class HologramDisplay {

    public static void createHologram(final Block origin, final String type, final int amount) {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);

        final Location location = normalizeLocation(origin.getLocation());
        final Hologram hologram = HologramsAPI.createHologram(plugin, location);

        hologram.appendTextLine(Color.colorize("&b1x " + SpawnerMechanics.WRAPPER.getMobDisplay(type) + " " + getSpawnerPlural(amount)));
    }

    public static void removeHologram(final Block origin) {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);

        final Location location = normalizeLocation(origin.getLocation());
        HologramsAPI.getHolograms(plugin).forEach(hologram -> {
            if (hologram.getLocation().equals(location)) {
                hologram.delete();
            }
        });
    }

    public static void updateHologram(final Block origin, final String type, final int amount) {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);

        if (amount == 0) {
            removeHologram(origin);
            return;
        }

        final Location location = normalizeLocation(origin.getLocation());

        HologramsAPI.getHolograms(plugin).forEach(hologram -> {
            if (hologram.getLocation().equals(location)) {
                hologram.clearLines();

                hologram.appendTextLine(Color.colorize("&b" + amount + "x " + SpawnerMechanics.WRAPPER.getMobDisplay(type) + " " + getSpawnerPlural(amount)));
            }
        });
    }

    public static void loadHolograms() {
        SpawnerMechanics.WRAPPER.getStorage().getSpawners().forEach(spawner ->
            createHologram(spawner.getLocation().getBlock(), spawner.getMobType(), spawner.getStack())
        );
    }

    private static Location normalizeLocation(final Location location) {
        location.setX(location.getBlockX() + .5f);
        location.setY(location.getBlockY() + 1.7f);
        location.setZ(location.getBlockZ() + .5f);

        return location;
    }

    private static String getSpawnerPlural(final int amount) {
        return amount == 1 ? "Spawner" : "Spawners";
    }
}
