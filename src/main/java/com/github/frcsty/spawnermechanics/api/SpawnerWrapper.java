package com.github.frcsty.spawnermechanics.api;

import com.github.frcsty.spawnermechanics.api.drop.EntityDrop;
import com.github.frcsty.spawnermechanics.api.drop.EntityDrops;
import com.github.frcsty.spawnermechanics.api.runnable.Activation;
import com.github.frcsty.spawnermechanics.api.storage.PersistentStorage;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Location;

import java.util.Optional;

public final class SpawnerWrapper {

    private final PersistentStorage storage = new PersistentStorage();
    private final Activation activation = new Activation();
    private final EntityDrops entityDrops = new EntityDrops();

    public void addSpawner(final Spawner spawner) {
        storage.getSpawners().add(spawner);
    }

    public Optional<Spawner> getSpawner(final Location location) {
        return storage.getSpawners().stream().filter(spawner -> spawner.getLocation().equals(location)).findAny();
    }

    public void removeSpawner(final Spawner spawner) {
        storage.getSpawners().remove(spawner);
    }

    public EntityDrop getEntityDrop(final String identifier) {
        return entityDrops.getEntityDrops().get(identifier);
    }

    public void setEntityDrop(final String identifier, final EntityDrop drop) {
        entityDrops.getEntityDrops().put(identifier, drop);
    }

    public PersistentStorage getStorage() {
        return storage;
    }

    public Activation getActivation() {
        return activation;
    }

    public EntityDrops getEntityDrops() {
        return entityDrops;
    }
}
