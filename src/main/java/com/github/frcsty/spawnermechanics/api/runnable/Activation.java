package com.github.frcsty.spawnermechanics.api.runnable;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.api.calculation.EntitySpawn;
import com.github.frcsty.spawnermechanics.object.CustomEntity;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.ListIterator;

public final class Activation {

    private static final long DELAY = 20L;
    private final LinkedList<Spawner> removalQueue = new LinkedList<>();
    private final LinkedList<Spawner> activationQueue = new LinkedList<>();

    public void run() {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);

        new BukkitRunnable() {
            @Override
            public void run() {
                activationQueue.forEach(spawner -> {
                    final EntitySpawn spawn = new EntitySpawn(spawner);

                    for (final Location location : spawn.getMobSpawns().keySet()) {
                        final int batch = spawn.getMobSpawns().get(location);
                        final CustomEntity entity = new CustomEntity(spawner.getType(), batch, location, spawner.getMobType());

                        entity.spawn(true);
                    }

                    removalQueue.add(spawner);
                });

                final ListIterator<Spawner> iterator = removalQueue.listIterator();
                if (iterator.hasNext()) {
                    final Spawner spawner = iterator.next();

                    activationQueue.remove(spawner);
                    removalQueue.removeFirst();
                }
            }
        }.runTaskTimer(plugin, DELAY, DELAY);
    }

    public LinkedList<Spawner> getActivationQueue() {
        return this.activationQueue;
    }
}
