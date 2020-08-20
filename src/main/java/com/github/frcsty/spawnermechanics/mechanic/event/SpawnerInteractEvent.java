package com.github.frcsty.spawnermechanics.mechanic.event;

import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.object.SpawnerLocation;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public final class SpawnerInteractEvent extends Event {

    private static HandlerList handlerList = new HandlerList();

    private final Player player;
    private final ItemStack item;
    private final Spawner spawner;
    private final SpawnerLocation location;

    public SpawnerInteractEvent(final Spawner spawner, final SpawnerLocation location, final ItemStack item, final Player player) {
        this.spawner = spawner;
        this.location = location;
        this.item = item;
        this.player = player;
    }

    public static HandlerList getHandlerList() {
        return handlerList;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }

    public Spawner getSpawner() {
        return this.spawner;
    }

    public ItemStack getItemInHand() {
        return this.item;
    }

    public Player getPlayer() {
        return this.player;
    }

    public SpawnerLocation getLocation() {
        return this.location;
    }
}
