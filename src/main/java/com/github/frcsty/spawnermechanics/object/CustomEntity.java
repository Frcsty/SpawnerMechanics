package com.github.frcsty.spawnermechanics.object;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.mechanic.event.CustomMobSpawnEvent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomEntity {

    private final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);
    private final EntityType type;
    private final int batch;
    private final Location location;
    private final String mobType;
    private Entity entity;

    public CustomEntity(final EntityType type, final int batch, final Location location, final String mobType) {
        this.type = type;
        this.batch = batch;
        this.location = location;
        this.mobType = mobType;
    }

    public void spawn(final boolean call) {
        final LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, type);
        this.entity = entity;

        entity.setMetadata(Identifier.MOB_TYPE, new FixedMetadataValue(plugin, mobType.toUpperCase()));
        entity.setMetadata(Identifier.MOB_AMOUNT, new FixedMetadataValue(plugin, batch));

        entity.setCustomName(batch + "x " + StringUtils.capitalize(mobType.toLowerCase()));
        SpawnerMechanics.WRAPPER.getEntityAttributes().applyEntityAttributes(entity, mobType.toUpperCase());

        if (call) {
            Bukkit.getPluginManager().callEvent(new CustomMobSpawnEvent(this));
        }
    }

    public String getMobType() {
        return mobType;
    }

    public EntityType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public int getBatch() {
        return batch;
    }

    public Entity getEntity() {
        return entity;
    }
}
