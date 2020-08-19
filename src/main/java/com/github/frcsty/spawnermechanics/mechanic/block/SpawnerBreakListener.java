package com.github.frcsty.spawnermechanics.mechanic.block;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.object.SpawnerLocation;
import com.github.frcsty.spawnermechanics.util.SpawnerItem;
import com.github.frcsty.spawnermechanics.wrapper.SpawnerWrapper;
import com.github.frcsty.spawnermechanics.wrapper.economy.TaxHolder;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public final class SpawnerBreakListener implements Listener {

    private final SpawnerWrapper wrapper = SpawnerMechanics.getWrapper();

    @EventHandler
    public void onSpawnerBreak(final BlockBreakEvent event) {
        final Player player = event.getPlayer();
        final Block broken = event.getBlock();
        final SpawnerLocation location = new SpawnerLocation(broken.getWorld(), broken.getX(), broken.getY(), broken.getZ());
        final Spawner spawner = handleSpawner(broken, location);
        if (spawner == null) {
            return;
        }

        if (handlePlayer(event, player, spawner, location)) {
            return;
        }

        final int amount = handleAmount(broken, player, spawner, event, location);
        if (amount == 0) {
            return;
        }
        player.getInventory().addItem(SpawnerItem.getItemStack(spawner.getMobType(), spawner.getType(), amount));
    }

    private Spawner handleSpawner(final Block broken, final SpawnerLocation location) {
        if (broken.getType() != Material.MOB_SPAWNER) {
            return null;
        }

        final Spawner spawner = wrapper.getSpawner(location);
        if (spawner == null) {
            wrapper.removeSpawner(location);
            return null;
        }

        return spawner;
    }

    private boolean handlePlayer(final BlockBreakEvent event, final Player player, final Spawner spawner, final SpawnerLocation location) {
        event.setExpToDrop(0);
        if (!player.getItemInHand().containsEnchantment(Enchantment.SILK_TOUCH)) {
            wrapper.removeSpawner(location);
            wrapper.updateHologram(location, spawner, true);
            return true;
        }

        return false;
    }

    private int handleAmount(final Block broken, final Player player, final Spawner spawner, final BlockBreakEvent event, final SpawnerLocation location) {
        final TaxHolder holder = wrapper.getTaxHandler().handle(player, spawner);

        int amount;
        if (holder != null) {
            amount = holder.getAmount();

            if (amount == 0) {
                player.sendMessage("You can not afford to pick-up a spawner.");
                event.setCancelled(true);
                return 0;
            }
        } else {
            amount = 1;
        }

        if (spawner.getStack() == 1) {
            broken.setType(Material.AIR);
            wrapper.removeSpawner(location);
            spawner.setAmount(-1);
        } else {
            event.setCancelled(true);
            wrapper.removeSpawner(location);
            spawner.removeAmount(1);
            wrapper.setSpawner(location, spawner);
        }
        wrapper.updateHologram(location, spawner, spawner.getStack() - 1 < 0);
        return amount;
    }
}
