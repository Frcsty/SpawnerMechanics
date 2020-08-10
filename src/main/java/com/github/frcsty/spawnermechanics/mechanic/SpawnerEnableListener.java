package com.github.frcsty.spawnermechanics.mechanic;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

import java.util.Optional;

public final class SpawnerEnableListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSpawnerSpawn(final SpawnerSpawnEvent event) {
        final CreatureSpawner creatureSpawner = event.getSpawner();
        Optional<Spawner> spawner = SpawnerMechanics.WRAPPER.getSpawner(creatureSpawner.getLocation());

        if (!spawner.isPresent()) {
            final String type = creatureSpawner.getSpawnedType().name();
            spawner = Optional.of(new Spawner(creatureSpawner.getLocation(), type, SpawnerMechanics.WRAPPER.getSpawnerType(type.toUpperCase()), 1));

            SpawnerMechanics.WRAPPER.addSpawner(spawner.get());
        }

        SpawnerMechanics.WRAPPER.addSpawnerActivation(spawner.get());
        event.setCancelled(true);
    }
}
