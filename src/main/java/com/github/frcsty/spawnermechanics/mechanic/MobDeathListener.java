package com.github.frcsty.spawnermechanics.mechanic;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.api.drop.EntityDrop;
import org.apache.commons.lang.StringUtils;
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
        if (handle(event.getFinalDamage(), event.getEntity())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onFakeEntityDeathByBlock(final EntityDamageByBlockEvent event) {
        if (handle(event.getFinalDamage(), event.getEntity())) {
            event.setCancelled(true);
        }
    }

    private boolean handle(final double finalDamage, final Entity normalEntity) {
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
        final EntityDrop drop = SpawnerMechanics.WRAPPER.getEntityDrop(typeString);
        if (drop != null) {
            drop.getDrops().forEach(itemDrop -> itemDrop.getChanceSortedDrops(true).forEach(loot ->
                    entity.getWorld().dropItemNaturally(entity.getLocation(), loot)));
        }
        entity.setCustomName(batch + "x " + StringUtils.capitalize(typeString.toLowerCase()));
        return batch > 1;
    }
}
