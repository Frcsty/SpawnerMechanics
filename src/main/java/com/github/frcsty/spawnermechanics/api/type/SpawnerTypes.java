package com.github.frcsty.spawnermechanics.api.type;

import org.bukkit.entity.EntityType;

import java.util.HashMap;
import java.util.Map;

public final class SpawnerTypes {

    private final Map<String, EntityType> types = new HashMap<>();

    public void loadDefaults() {
        for (final EntityType type : EntityType.values()) {
            types.put(type.name().toUpperCase(), EntityType.valueOf(type.name().toUpperCase()));
        }
    }

    public Map<String, EntityType> getTypes() {
        return this.types;
    }
}
