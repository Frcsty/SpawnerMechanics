package com.github.frcsty.spawnermechanics.api.runnable;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.api.calculation.EntitySpawn;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.ListIterator;

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

                        entity.setMetadata(Identifier.MOB_TYPE, new FixedMetadataValue(plugin, spawner.getType().name()));
                        entity.setMetadata(Identifier.MOB_AMOUNT, new FixedMetadataValue(plugin, batch));
                        // TODO: Custom Entity
                        entity.setCustomName(batch + "x " + StringUtils.capitalize(spawner.getType().name().toLowerCase()));
                    }

                    REMOVAL_QUEUE.add(spawner);
                });

                final ListIterator<Spawner> iterator = REMOVAL_QUEUE.listIterator();
                if (iterator.hasNext()) {
                    final Spawner spawner = iterator.next();

                    SpawnerMechanics.WRAPPER.removeSpawner(spawner);
                    REMOVAL_QUEUE.removeFirst();
                }
            }
        }.runTaskTimer(plugin, DELAY, DELAY);
    }
}
