package com.github.frcsty.spawnermechanics.object;

import org.bukkit.entity.EntityType;

public final class MobType {

    private final EntityType entityType;
    private final String name;

    public MobType(final EntityType entityType, final String name) {
        this.entityType = entityType;
        this.name = name;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public String getName() {
        return this.name;
    }
}
