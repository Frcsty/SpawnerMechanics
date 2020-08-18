package com.github.frcsty.spawnermechanics.wrapper.runnable;

import com.github.frcsty.spawnermechanics.Setting;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.SpawnerLocation;
import com.github.frcsty.spawnermechanics.wrapper.calculation.EntitySpawn;
import com.github.frcsty.spawnermechanics.object.CustomEntity;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public final class Activation {

    private static final long DELAY = 20L;
    private final Map<SpawnerLocation, Spawner> removalQueue = new ConcurrentHashMap<>();
    private final Map<SpawnerLocation, Spawner> activationQueue = new ConcurrentHashMap<>();

    public void run() {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (!Setting.ENABLED_SPAWNING) {
                    return;
                }

                activationQueue.forEach((loc, spawner) -> {
                    final EntitySpawn spawn = new EntitySpawn(loc.getLocation(), spawner);

                    for (final Location location : spawn.getMobSpawns().keySet()) {
                        final int batch = spawn.getMobSpawns().get(location);
                        final CustomEntity entity = new CustomEntity(spawner.getType(), batch, location, spawner.getMobType());

                        entity.spawn(true);
                    }

                    removalQueue.put(loc, spawner);
                });

                removalQueue.forEach((loc, spawner) -> {
                    activationQueue.remove(loc);

                    removalQueue.remove(loc);
                });
            }
        }.runTaskTimer(plugin, DELAY, DELAY);
    }

    public Map<SpawnerLocation, Spawner> getActivationQueue() {
        return this.activationQueue;
    }
}
