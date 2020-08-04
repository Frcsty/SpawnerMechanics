package com.github.frcsty.spawnermechanics.mechanic;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.Setting;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import org.apache.commons.lang.StringUtils;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.Optional;

public final class MobSpawnListener implements Listener {

    private final SpawnerMechanics plugin;

    public MobSpawnListener(final SpawnerMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobSpawn(final EntitySpawnEvent event) {
        final Entity spawnedEntity = event.getEntity();

        if (spawnedEntity instanceof Player) {
            return;
        }

        if (!(spawnedEntity instanceof Animals) && !(spawnedEntity instanceof Monster)) {
            return;
        }

        final List<MetadataValue> startingData = spawnedEntity.getMetadata(Identifier.MOB_TYPE);
        if (startingData.size() == 0) {
            spawnedEntity.setMetadata(Identifier.MOB_TYPE, new FixedMetadataValue(plugin, spawnedEntity.getType().name()));
        }

        final List<MetadataValue> typeData = spawnedEntity.getMetadata(Identifier.MOB_TYPE);
        final EntityType mobType = EntityType.valueOf(typeData.get(0).asString().toUpperCase());
        final List<MetadataValue> amountData = spawnedEntity.getMetadata(Identifier.MOB_AMOUNT);
        final int amount = amountData.size() == 0 ? 1 : amountData.get(0).asInt();
        final Optional<Entity> stackEntity = spawnedEntity.getNearbyEntities(
                Setting.DISTANCE_X,
                Setting.DISTANCE_Y,
                Setting.DISTANCE_Z
        ).stream().filter(entity -> {
            if (entity.equals(spawnedEntity) || entity instanceof Player) {
                return false;
            }

            final List<MetadataValue> type = entity.getMetadata(Identifier.MOB_TYPE);
            if (type.size() == 0) {
                return false;
            }

            final EntityType entityType = EntityType.valueOf(type.get(0).asString().toUpperCase());
            if (entityType == mobType) {
                final List<MetadataValue> batchAmount = entity.getMetadata(Identifier.MOB_AMOUNT);
                final int rawBatch = batchAmount.size() == 0 ? 1 : batchAmount.get(0).asInt();
                int batch = rawBatch + amount;

                if (rawBatch >= Setting.MAX_MOB_STACK) {
                    return false;
                }

                if (batch >= Setting.MAX_MOB_STACK) {
                    batch = Setting.MAX_MOB_STACK;
                }
                entity.setMetadata(Identifier.MOB_AMOUNT, new FixedMetadataValue(plugin, batch));
                entity.setCustomName(batch + "x " + StringUtils.capitalize(entityType.name().toLowerCase()));
                return true;
            }
            return false;
        }).findAny();

        if (!stackEntity.isPresent()) {
            return;
        }

        spawnedEntity.remove();
    }
}
