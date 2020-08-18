package com.github.frcsty.spawnermechanics.mechanic.block;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.object.SpawnerLocation;
import com.github.frcsty.spawnermechanics.util.ItemNBT;
import com.github.frcsty.spawnermechanics.wrapper.SpawnerWrapper;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public final class SpawnerPlaceListener implements Listener {

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onSpawnerPlace(final BlockPlaceEvent event) {
        final SpawnerWrapper wrapper = SpawnerMechanics.getWrapper();
        final Block placedAgainst = event.getBlockAgainst();
        final ItemStack item = event.getItemInHand();
        final Block block = event.getBlockPlaced();

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

        final SpawnerLocation location = new SpawnerLocation(block.getWorld(), block.getX(), block.getY(), block.getZ());
        Spawner spawner = wrapper.getSpawner(location);
        final EntityType entityType = wrapper.getSpawnerType(type);
        if (spawner == null) {
            spawner = new Spawner(type, entityType, 1);
        }

        final CreatureSpawner creatureSpawner = (CreatureSpawner) block.getState();
        creatureSpawner.setSpawnedType(entityType);

        wrapper.setSpawner(location, spawner);
        wrapper.createHologram(location, spawner);
    }

}
