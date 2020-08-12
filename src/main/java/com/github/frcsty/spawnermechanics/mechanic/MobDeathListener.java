package com.github.frcsty.spawnermechanics.mechanic;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.api.drop.EntityDrop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;

public final class MobDeathListener implements Listener {

    private final SpawnerMechanics plugin;

    public MobDeathListener(final SpawnerMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onEntityDeath(final EntityDeathEvent event) {
        final Entity entity = event.getEntity();
        final List<MetadataValue> type = entity.getMetadata(Identifier.MOB_TYPE);

        if (type.size() == 0) {
            return;
        }

        event.getDrops().clear();
        event.setDroppedExp(0);

        handleDrops(type.get(0).asString(), entity, null);
    }

    @EventHandler
    public void onFakeEntityDeathByEntity(final EntityDamageByEntityEvent event) {
        if (handle(event.getFinalDamage(), event.getEntity(), event.getDamager())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFakeEntityDeathByBlock(final EntityDamageByBlockEvent event) {
        if (handle(event.getFinalDamage(), event.getEntity(), null)) {
            event.setCancelled(true);
        }
    }

    private boolean handle(final double finalDamage, final Entity normalEntity, final Entity damager) {
        if (!(normalEntity instanceof LivingEntity)) {
            return false;
        }
        final LivingEntity entity = (LivingEntity) normalEntity;
        final List<MetadataValue> type = entity.getMetadata(Identifier.MOB_TYPE);
        final List<MetadataValue> data = entity.getMetadata(Identifier.MOB_AMOUNT);

        if (data.size() == 0) {
            return false;
        }

        if (finalDamage < entity.getHealth()) {
            return false;
        }

        final int batch = data.get(0).asInt();
        entity.setHealth(entity.getMaxHealth());
        entity.setMetadata(Identifier.MOB_AMOUNT, new FixedMetadataValue(plugin, batch - 1));

        final String typeString = type.get(0).asString();
        handleDrops(typeString, entity, damager);
        entity.setCustomName(batch + "x " + SpawnerMechanics.WRAPPER.getMobDisplay(typeString));
        return batch > 1;
    }

    private void handleDrops(final String type, final Entity entity, final Entity damager) {
        final EntityDrop drop = SpawnerMechanics.WRAPPER.getEntityDrop(type);
        if (drop == null) {
            return;
        }

        drop.getDrops().forEach(itemDrop -> {
            itemDrop.getChanceSortedDrops(true).forEach(loot ->
                    entity.getWorld().dropItemNaturally(entity.getLocation(), loot));

            if (damager == null) {
                return;
            }
            itemDrop.getChanceSortedCommands().forEach(command -> {
                if (damager instanceof Player) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", damager.getName()));
                }
            });
        });
    }
}
