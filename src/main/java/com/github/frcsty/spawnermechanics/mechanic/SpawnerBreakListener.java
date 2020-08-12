package com.github.frcsty.spawnermechanics.mechanic;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.util.HologramDisplay;
import com.github.frcsty.spawnermechanics.util.SpawnerItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.Optional;

public final class SpawnerBreakListener implements Listener {

    @EventHandler
    public void onSpawnerBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block broken = event.getBlock();

        if (broken.getType() != Material.MOB_SPAWNER) {
            return;
        }

        final Optional<Spawner> optionalSpawner = SpawnerMechanics.WRAPPER.getSpawner(broken.getLocation());
        if (!optionalSpawner.isPresent()) {
            Bukkit.broadcastMessage("Spawner not present");
            return;
        }

        event.setExpToDrop(0);
        final Spawner spawner = optionalSpawner.get();
        if (!player.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
            SpawnerMechanics.WRAPPER.removeSpawner(spawner);
            HologramDisplay.removeHologram(broken);
            return;
        }

        final boolean stack = player.isSneaking();
        final int amount = stack ? spawner.getStack() : 1;

        if (!stack && spawner.getStack() > 1) {
            event.setCancelled(true);
        }

        if (stack || spawner.getStack() == 1) {
            broken.setType(Material.AIR);
            SpawnerMechanics.WRAPPER.removeSpawner(spawner);
            HologramDisplay.removeHologram(broken);
        } else {
            SpawnerMechanics.WRAPPER.removeSpawner(spawner);
            spawner.removeAmount(1);
            SpawnerMechanics.WRAPPER.addSpawner(spawner);
            HologramDisplay.updateHologram(broken, spawner.getMobType(), spawner.getStack());
        }

        player.getInventory().addItem(SpawnerItem.getItemStack(spawner.getMobType(), spawner.getType(), amount));
    }
}
