package com.github.frcsty.spawnermechanics.mechanic.event;

import com.github.frcsty.spawnermechanics.object.CustomEntity;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public final class CustomMobSpawnEvent extends Event {

    private static HandlerList handlerList = new HandlerList();
    private final CustomEntity entity;

    public CustomMobSpawnEvent(final CustomEntity entity) {
        this.entity = entity;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public CustomEntity getCustomEntity() {
        return this.entity;
    }
}
