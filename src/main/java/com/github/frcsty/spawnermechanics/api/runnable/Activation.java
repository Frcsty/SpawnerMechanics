package com.github.frcsty.spawnermechanics.api.runnable;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.api.calculation.EntitySpawn;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;

public final class Activation {

    private static final LinkedList<Spawner> REMOVAL_QUEUE = new LinkedList<>();
    private static final long DELAY = 20L;

    public void run() {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);

        new BukkitRunnable() {
            @Override
            public void run() {
                SpawnerMechanics.WRAPPER.getStorage().getSpawners().forEach(spawner -> {
                    final EntitySpawn spawn = new EntitySpawn(spawner);

                    for (final Location location : spawn.getMobSpawns().keySet()) {
                        final int batch = spawn.getMobSpawns().get(location);
                        final Entity entity = location.getWorld().spawnEntity(location, spawner.getType());

                        entity.setMetadata("mob-type", new FixedMetadataValue(plugin, spawner.getType().name()));
                        entity.setMetadata("mob-amount", new FixedMetadataValue(plugin, batch));

                        //entity.setFireTicks(10);
                        // TODO: Custom Entity
                        entity.setCustomName("Stack Amount: " + batch);
                    }

                    REMOVAL_QUEUE.add(spawner);
                });

                for (final Spawner spawner : REMOVAL_QUEUE) {
                    SpawnerMechanics.WRAPPER.removeSpawner(spawner);

                    REMOVAL_QUEUE.removeFirst();
                }
            }
        }.runTaskTimer(plugin, DELAY, DELAY);
    }
}
