package com.github.frcsty.spawnermechanics.wrapper.hologram;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.object.SpawnerLocation;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public final class HologramDisplay {

    private final List<Hologram> holograms = new ArrayList<>();

    private void createHologram(final Hologram hologram) {
        holograms.add(hologram);
    }

    private Hologram findHologram(final Location location) {
        Hologram hologram = null;
        for (final Hologram holo : holograms) {
            if (holo.getLocation().equals(location)) {
                hologram = holo;
                break;
            }
        }

        return hologram;
    }

    private String getDisplayLine(final Spawner spawner) {
        return "&b" + spawner.getStack() + "x " + getDisplay(spawner.getMobType()) + " " + getSpawnerPronunciation(spawner.getStack());
    }

    public void createHologram(final SpawnerLocation location, final Spawner spawner) {
        createHologram(new Hologram(normalizeLocation(location.getLocation()), getDisplayLine(spawner)));
    }

    private void removeHologram(final Location location) {
        final Hologram hologram = findHologram(normalizeLocation(location));

        if (hologram == null) {
            return;
        }
        hologram.clearLines();
        holograms.remove(hologram);
    }

    public void updateHologram(final SpawnerLocation loc, final Spawner spawner, final boolean remove) {
        final Location location = normalizeLocation(loc.getLocation());

        if (remove) {
            removeHologram(location);
            return;
        }

        final Hologram hologram = findHologram(location);

        if (hologram == null) {
            createHologram(loc, spawner);
            return;
        }

        hologram.clearLines();
        hologram.appendLine(getDisplayLine(spawner));
    }

    private Location normalizeLocation(final Location location) {
        location.setX(location.getBlockX() + .5f);
        location.setZ(location.getBlockZ() + .5f);

        return location;
    }

    private String getSpawnerPronunciation(final int amount) {
        return amount == 1 ? "Spawner" : "Spawners";
    }

    private String getDisplay(final String identifier) {
        return SpawnerMechanics.getWrapper().getMobDisplay(identifier);
    }

    public void remove() {
        holograms.forEach(Hologram::clearLines);
        holograms.clear();
    }

    public void load() {
        SpawnerMechanics.getWrapper().getStorage().getSpawners().forEach(this::createHologram);
    }

}
