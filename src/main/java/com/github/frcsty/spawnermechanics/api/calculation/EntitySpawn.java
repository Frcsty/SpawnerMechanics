package com.github.frcsty.spawnermechanics.api.calculation;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

public final class EntitySpawn {

    private static final int BATCH_AMOUNT = 3;
    private static final int BATCH_SPAWN_AMOUNT = 4;
    private static final int DEVIATION_AMOUNT = 5;
    private static final SplittableRandom RANDOM = new SplittableRandom();
    private static final Map<Location, Integer> MOB_SPAWNS = new HashMap<>();
    private final Spawner spawner;

    public EntitySpawn(final Spawner spawner) {
        this.spawner = spawner;

        new BukkitRunnable() {
            @Override
            public void run() {
                calculateSpawns();
            }
        }.runTaskAsynchronously(JavaPlugin.getPlugin(SpawnerMechanics.class));
    }

    private void calculateSpawns() {
        final Location baseLocation = spawner.getLocation();
        final int batchAmount = RANDOM.nextInt(BATCH_AMOUNT);
        final int total = RANDOM.nextInt(spawner.getStack()) + RANDOM.nextInt(BATCH_SPAWN_AMOUNT);

        if (total <= 0) {
            return;
        }

        Bukkit.broadcastMessage("Total Spawn Amount: " + total);
        for (int i = 0; i < batchAmount; i++) {
            final int deviation = Integer.valueOf(getAddition() + RANDOM.nextInt(DEVIATION_AMOUNT));
            final int batch = (total / batchAmount) + deviation;

            if (batch <= 0) {
                continue;
            }

            final Location location = baseLocation.clone();
            location.setX(location.getBlockX() + getRandomX());
            location.setZ(location.getBlockZ() + getRandomZ());

            Bukkit.broadcastMessage(" - Batch Spawn Amount: " + batch);
            MOB_SPAWNS.put(location, batch);
        }
    }

    private String getAddition() {
        return RANDOM.nextInt(2) == 1 ? "+" : "-";
    }

    private double getRandomX() {
        return RANDOM.nextInt(2) == 1 ? -1.5 : +1.5;
    }

    private double getRandomZ() {
        return RANDOM.nextInt(2) == 1 ? -1.5 : +1.5;
    }

    public Map<Location, Integer> getMobSpawns() {
        return MOB_SPAWNS;
    }

}
