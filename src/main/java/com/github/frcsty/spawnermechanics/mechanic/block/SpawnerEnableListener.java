package com.github.frcsty.spawnermechanics.mechanic.block;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.object.SpawnerLocation;
import com.github.frcsty.spawnermechanics.wrapper.SpawnerWrapper;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public final class SpawnerEnableListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSpawnerSpawn(final SpawnerSpawnEvent event) {
        final SpawnerWrapper wrapper = SpawnerMechanics.getWrapper();
        final CreatureSpawner creatureSpawner = event.getSpawner();
        final SpawnerLocation location = new SpawnerLocation(creatureSpawner.getWorld(), creatureSpawner.getX(), creatureSpawner.getY(), creatureSpawner.getZ());
        Spawner spawner = wrapper.getSpawner(location);

        if (spawner == null) {
            final String type = creatureSpawner.getSpawnedType().name();
            spawner = new Spawner(type, wrapper.getSpawnerType(type.toUpperCase()), 1);

            wrapper.setSpawner(location, spawner);
        }

        wrapper.addSpawnerActivation(location, spawner);
        event.setCancelled(true);
    }
}
