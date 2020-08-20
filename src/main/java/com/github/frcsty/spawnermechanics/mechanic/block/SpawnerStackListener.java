package com.github.frcsty.spawnermechanics.mechanic.block;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.mechanic.event.SpawnerInteractEvent;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.object.SpawnerLocation;
import com.github.frcsty.spawnermechanics.util.ItemNBT;
import com.github.frcsty.spawnermechanics.wrapper.SpawnerWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public final class SpawnerStackListener implements Listener {

    @EventHandler
    public void onSpawnerStack(final PlayerInteractEvent event) {
        final SpawnerWrapper wrapper = SpawnerMechanics.getWrapper();
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();
        final Action action = event.getAction();
        final Block clicked = event.getClickedBlock();

        if (clicked == null) {
            return;
        }

        if (clicked.getType() != Material.MOB_SPAWNER) {
            return;
        }

        final SpawnerLocation location = new SpawnerLocation(clicked.getWorld(), clicked.getX(), clicked.getY(), clicked.getZ());
        final Spawner spawner = SpawnerMechanics.getWrapper().getSpawner(location);

        if (action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        if (item == null || item.getType() != Material.MOB_SPAWNER) {
            Bukkit.getPluginManager().callEvent(new SpawnerInteractEvent(spawner, location, item, player));
            return;
        }

        final String type = ItemNBT.getNBTTag(item, Identifier.ENTITY_TYPE);
        if (type.length() == 0) {
            return;
        }

        final EntityType entityType = wrapper.getSpawnerType(type.toUpperCase());
        final CreatureSpawner creatureSpawner = (CreatureSpawner) clicked.getState();
        if (creatureSpawner.getSpawnedType() != entityType) {
            return;
        }

        int amount = 1;
        if (player.isSneaking()) {
            amount = item.getAmount();
        }

        if (spawner == null) {
            wrapper.setSpawner(location, new Spawner(type, entityType, amount));
        } else {
            wrapper.removeSpawner(location);
            spawner.addAmount(amount);
            wrapper.setSpawner(location, spawner);
        }

        wrapper.updateHologram(location, spawner, false);
        item.setAmount(item.getAmount() - amount);
        player.setItemInHand(item);
    }
}
