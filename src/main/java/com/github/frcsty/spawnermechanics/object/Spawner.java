package com.github.frcsty.spawnermechanics.object;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;

public final class Spawner {

    private final Location location;
    private final String mobType;
    private final EntityType type;
    private int stack;

    public Spawner(final Location location, final String mobType, final EntityType type, final int stack) {
        this.location = location;
        this.mobType = mobType;
        this.stack = stack;
        this.type = type;
    }

    public void addAmount(final int amount) {
        this.stack += amount;
    }

    public void removeAmount(final int amount) {
        this.stack -= amount;
    }

    public Location getLocation() {
        return location;
    }

    public String getMobType() {
        return mobType;
    }

    public EntityType getType() {
        return type;
    }

    public int getStack() {
        return stack == 0 ? 1 : stack;
    }
}
