package com.github.frcsty.spawnermechanics.api;

import com.github.frcsty.spawnermechanics.api.attribute.EntityAttributes;
import com.github.frcsty.spawnermechanics.api.drop.EntityDrop;
import com.github.frcsty.spawnermechanics.api.drop.EntityDrops;
import com.github.frcsty.spawnermechanics.api.runnable.Activation;
import com.github.frcsty.spawnermechanics.api.storage.PersistentStorage;
import com.github.frcsty.spawnermechanics.api.type.SpawnerTypes;
import com.github.frcsty.spawnermechanics.object.MobType;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;

import java.util.Optional;

public final class SpawnerWrapper {

    private final PersistentStorage storage = new PersistentStorage();
    private final Activation activation = new Activation();
    private final EntityDrops entityDrops = new EntityDrops();
    private final SpawnerTypes spawnerTypes = new SpawnerTypes();
    private final EntityAttributes entityAttributes = new EntityAttributes();

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

    public EntityType getSpawnerType(final String identifier) {
        final MobType type = this.spawnerTypes.getTypes().get(identifier.toUpperCase());

        return type.getEntityType();
    }

    public String getMobDisplay(final String identifier) {
        final MobType type = this.spawnerTypes.getTypes().get(identifier.toUpperCase());

        return type.getName();
    }

    public void addSpawnerActivation(final Spawner spawner) {
        this.activation.getActivationQueue().add(spawner);
    }

    public PersistentStorage getStorage() {
        return this.storage;
    }

    public Activation getActivation() {
        return this.activation;
    }

    public EntityDrops getEntityDrops() {
        return this.entityDrops;
    }

    public SpawnerTypes getSpawnerTypes() {
        return this.spawnerTypes;
    }

    public EntityAttributes getEntityAttributes() {
        return this.entityAttributes;
    }
}
