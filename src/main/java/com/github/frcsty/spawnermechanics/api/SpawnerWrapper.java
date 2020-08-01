package com.github.frcsty.spawnermechanics.api;

import com.github.frcsty.spawnermechanics.api.runnable.Activation;
import com.github.frcsty.spawnermechanics.api.storage.PersistentStorage;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Location;

import java.util.Optional;

public final class SpawnerWrapper {

    private static final PersistentStorage STORAGE = new PersistentStorage();
    private static final Activation ACTIVATION = new Activation();

    public void addSpawner(final Spawner spawner) {
        STORAGE.getSpawners().add(spawner);
    }

    public Optional<Spawner> getSpawner(final Location location) {
        return STORAGE.getSpawners().stream().filter(spawner -> spawner.getLocation().equals(location)).findAny();
    }

    public void removeSpawner(final Spawner spawner) {
        STORAGE.getSpawners().remove(spawner);
    }

    public PersistentStorage getStorage() {
        return STORAGE;
    }

    public Activation getActivation() {
        return ACTIVATION;
    }
}
