package com.github.frcsty.spawnermechanics.mechanic;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.util.ItemNBT;
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

import java.util.Optional;

public final class SpawnerStackListener implements Listener {

    @EventHandler
    public void onSpawnerStack(final PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = event.getItem();
        final Action action = event.getAction();
        final Block clicked = event.getClickedBlock();

        if (item == null || clicked == null) {
            return;
        }

        if (clicked.getType() != Material.MOB_SPAWNER) {
            return;
        }

        if (item.getType() != Material.MOB_SPAWNER) {
            return;
        }

        if (action != Action.RIGHT_CLICK_BLOCK) {
            return;
        }

        final String type = ItemNBT.getNBTTag(item, Identifier.ENTITY_TYPE);
        if (type.length() == 0) {
            return;
        }

        final EntityType entityType = SpawnerMechanics.WRAPPER.getSpawnerType(type.toUpperCase());
        final CreatureSpawner creatureSpawner = (CreatureSpawner) clicked.getState();
        if (creatureSpawner.getSpawnedType() != entityType) {
            return;
        }

        int amount = 1;
        if (player.isSneaking()) {
            amount = item.getAmount();
        }

        final Optional<Spawner> spawner = SpawnerMechanics.WRAPPER.getSpawner(clicked.getLocation());
        if (!spawner.isPresent()) {
            SpawnerMechanics.WRAPPER.addSpawner(new Spawner(clicked.getLocation(),type, entityType, amount));
        } else {
            SpawnerMechanics.WRAPPER.removeSpawner(spawner.get());
            spawner.get().addAmount(amount);
            SpawnerMechanics.WRAPPER.addSpawner(spawner.get());
        }

        item.setAmount(item.getAmount() - amount);
        player.setItemInHand(item);
    }
}
