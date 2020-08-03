package com.github.frcsty.spawnermechanics.mechanic;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.Setting;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.api.drop.EntityDrop;
import com.github.frcsty.spawnermechanics.api.drop.EntityDrops;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
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
    }

    @EventHandler
    public void onFakeEntityDeathByEntity(final EntityDamageByEntityEvent event) {
        final LivingEntity entity = (LivingEntity) event.getEntity();
        final List<MetadataValue> type = entity.getMetadata(Identifier.MOB_TYPE);
        final List<MetadataValue> data = entity.getMetadata(Identifier.MOB_AMOUNT);

        if (data.size() == 0) {
            return;
        }

        if (event.getFinalDamage() < entity.getHealth()) {
            return;
        }

        final int batch = data.get(0).asInt();
        if (batch <= 1) {
            return;
        }

        entity.setHealth(Setting.MAX_HEALTH);
        event.setCancelled(true);
        entity.setMetadata(Identifier.MOB_AMOUNT, new FixedMetadataValue(plugin, batch - 1));

        final EntityDrop drop = EntityDrops.getEntityDrop(type.get(0).asString());
        Bukkit.broadcastMessage(type.get(0).asString());
        if (drop == null) {
            return;
        }
        drop.getDrops().forEach(itemDrop -> itemDrop.getChanceSortedDrops().forEach(loot -> entity.getWorld().dropItemNaturally(entity.getLocation(), loot)));
        entity.setCustomName(batch + "x " + StringUtils.capitalize(type.get(0).asString().toLowerCase()));
    }

    @EventHandler
    public void onFakeEntityDeathByBlock(final EntityDamageByBlockEvent event) {
        final LivingEntity entity = (LivingEntity) event.getEntity();
        final List<MetadataValue> type = entity.getMetadata(Identifier.MOB_TYPE);
        final List<MetadataValue> data = entity.getMetadata(Identifier.MOB_AMOUNT);

        if (data.size() == 0) {
            return;
        }

        if (event.getFinalDamage() < entity.getHealth()) {
            return;
        }

        final int batch = data.get(0).asInt();
        if (batch <= 1) {
            return;
        }

        entity.setHealth(Setting.MAX_HEALTH);
        event.setCancelled(true);
        entity.setMetadata(Identifier.MOB_AMOUNT, new FixedMetadataValue(plugin, batch - 1));

        final EntityDrop drop = EntityDrops.getEntityDrop(type.get(0).asString());
        if (drop == null) {
            return;
        }
        drop.getDrops().forEach(itemDrop -> itemDrop.getChanceSortedDrops().forEach(loot -> entity.getWorld().dropItemNaturally(entity.getLocation(), loot)));
        entity.setCustomName(batch + "x " + StringUtils.capitalize(type.get(0).asString().toLowerCase()));
    }
}
