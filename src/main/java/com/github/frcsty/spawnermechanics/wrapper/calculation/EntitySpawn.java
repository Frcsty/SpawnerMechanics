package com.github.frcsty.spawnermechanics.wrapper.calculation;

import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

public final class EntitySpawn {

    private static final int BATCH_AMOUNT = 4;
    private static final int BATCH_SPAWN_AMOUNT = 4;
    private static final SplittableRandom RANDOM = new SplittableRandom();
    private final Map<Location, Integer> mobSpawns = new HashMap<>();
    private final Spawner spawner;
    private final Location location;

    public EntitySpawn(final Location location, final Spawner spawner) {
        this.location = location;
        this.spawner = spawner;

        calculateSpawns();
    }

    private void calculateSpawns() {
        final Location baseLocation = location;
        final int batchAmount = getBatchAmount();
        final int total = getTotalSpawnAmount(spawner.getStack());

        if (total <= 0) {
            return;
        }

        final int remain = total % batchAmount;
        Bukkit.broadcastMessage("Total Spawn Amount: " + total + " (" + remain + ")");
        for (int i = 0; i < batchAmount; i++) {
            int batch = (total / batchAmount);
            if (i == 0 && total != 1) {
                batch += remain;
            }
            if (batch <= 0) {
                continue;
            }

            Location location = getRandomLocation(baseLocation.clone());
            Bukkit.broadcastMessage(" - Batch Spawn Amount: " + batch);
            if (mobSpawns.get(location) == null) {
                mobSpawns.put(location, batch);
            } else {
                location = getRandomLocation(baseLocation.clone());

                mobSpawns.put(location, batch);
            }
        }
    }

    private int getBatchAmount() {
        return RANDOM.nextInt(1, BATCH_AMOUNT);
    }

    private int getTotalSpawnAmount(final int stack) {
        return RANDOM.nextInt(1, stack + RANDOM.nextInt(1, BATCH_SPAWN_AMOUNT + 1));
    }

    private Location getRandomLocation(Location location) {
        location.setX(location.getBlockX() + getRandomPosition());
        location.setZ(location.getBlockZ() + getRandomPosition());

        if (location.getWorld().getBlockAt(location).getType() != Material.AIR) {
            location = getRandomLocation(location);
        }

        return location;
    }

    private double getRandomPosition() {
        final int random = RANDOM.nextInt(1, 4);

        double deviation = 0;
        switch (random) {
            case 1:
                deviation = 1.5;
                break;
            case 2:
                deviation = 2;
                break;
            case 3:
                deviation = 2.5;
        }

        final int position = RANDOM.nextInt(1, 3);
        return position == 1 ? -deviation : +deviation;
    }

    public Map<Location, Integer> getMobSpawns() {
        return this.mobSpawns;
    }

}
