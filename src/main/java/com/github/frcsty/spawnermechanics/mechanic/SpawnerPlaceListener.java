package com.github.frcsty.spawnermechanics.mechanic;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.util.HologramDisplay;
import com.github.frcsty.spawnermechanics.util.ItemNBT;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public final class SpawnerPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSpawnerPlace(final BlockPlaceEvent event) {
        final Block block = event.getBlockPlaced();
        final Block placedAgainst = event.getBlockAgainst();
        final ItemStack item = event.getItemInHand();

        if (block.getType() != Material.MOB_SPAWNER) {
            return;
        }

        if (placedAgainst.getType() == Material.MOB_SPAWNER) {
            event.setCancelled(true);
            return;
        }

        final String type = ItemNBT.getNBTTag(item, Identifier.ENTITY_TYPE);
        if (type.length() == 0) {
            return;
        }

        Optional<Spawner> spawner = SpawnerMechanics.WRAPPER.getSpawner(block.getLocation());
        final EntityType entityType = SpawnerMechanics.WRAPPER.getSpawnerType(type.toUpperCase());
        if (!spawner.isPresent()) {
            spawner = Optional.of(new Spawner(block.getLocation(), type, entityType, 1));
        }

        final CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
        creatureSpawner.setSpawnedType(entityType);
        SpawnerMechanics.WRAPPER.addSpawner(spawner.get());
        HologramDisplay.createHologram(block, type, 1);
    }

}
