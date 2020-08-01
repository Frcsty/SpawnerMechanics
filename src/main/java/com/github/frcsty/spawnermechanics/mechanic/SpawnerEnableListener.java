package com.github.frcsty.spawnermechanics.mechanic;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.SpawnerSpawnEvent;

public final class SpawnerEnableListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSpawnerSpawn(final SpawnerSpawnEvent event) {
        final CreatureSpawner spawner = event.getSpawner();

        if (!SpawnerMechanics.WRAPPER.getSpawner(spawner.getLocation()).isPresent()) {
            SpawnerMechanics.WRAPPER.addSpawner(new Spawner(spawner.getLocation(), spawner.getSpawnedType(), 1));
        }

        Bukkit.broadcastMessage("Spawner Spawn Event Triggered!");
        event.setCancelled(true);
    }
}
