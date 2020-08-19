package com.github.frcsty.spawnermechanics.wrapper;

import com.github.frcsty.spawnermechanics.object.Attribute;
import com.github.frcsty.spawnermechanics.object.MobType;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.object.SpawnerLocation;
import com.github.frcsty.spawnermechanics.wrapper.attribute.EntityAttributes;
import com.github.frcsty.spawnermechanics.wrapper.drop.EntityDrop;
import com.github.frcsty.spawnermechanics.wrapper.drop.EntityDrops;
import com.github.frcsty.spawnermechanics.wrapper.economy.TaxHandler;
import com.github.frcsty.spawnermechanics.wrapper.equipment.EntityEquipment;
import com.github.frcsty.spawnermechanics.wrapper.equipment.EquipmentSet;
import com.github.frcsty.spawnermechanics.wrapper.hologram.HologramDisplay;
import com.github.frcsty.spawnermechanics.wrapper.runnable.Activation;
import com.github.frcsty.spawnermechanics.wrapper.storage.PersistentStorage;
import com.github.frcsty.spawnermechanics.wrapper.type.SpawnerTypes;
import org.bukkit.entity.EntityType;

public final class SpawnerWrapper {

    private final PersistentStorage storage = new PersistentStorage();
    private final Activation activation = new Activation();
    private final EntityDrops entityDrops = new EntityDrops();
    private final SpawnerTypes spawnerTypes = new SpawnerTypes();
    private final EntityAttributes entityAttributes = new EntityAttributes();
    private final EntityEquipment entityEquipment = new EntityEquipment();
    private final HologramDisplay hologramDisplay = new HologramDisplay();
    private final TaxHandler taxHandler = new TaxHandler();

    public void setSpawner(final SpawnerLocation location, final Spawner spawner) {
        storage.getSpawners().put(location, spawner);
    }

    public Spawner getSpawner(final SpawnerLocation input) {
        return storage.getSpawners().get(input);
    }

    public void removeSpawner(final SpawnerLocation input) {
        storage.getSpawners().remove(input);
    }

    public EntityDrop getEntityDrop(final String identifier) {
        return entityDrops.getEntityDrops().get(identifier.toUpperCase());
    }

    public EntityType getSpawnerType(final String identifier) {
        final MobType type = this.spawnerTypes.getTypes().get(identifier.toUpperCase());

        return type.getEntityType();
    }

    public String getMobDisplay(final String identifier) {
        final MobType type = this.spawnerTypes.getTypes().get(identifier.toUpperCase());

        return type.getName();
    }

    public EquipmentSet getEquipment(final String identifier) {
        return this.entityEquipment.getEntityEquipment().get(identifier.toUpperCase());
    }

    public Attribute getEntityAttribute(final String identifier) {
        return this.getEntityAttributes().getAttributes().get(identifier.toUpperCase());
    }

    public int getSpawnerValue(final Spawner spawner) {
        return this.taxHandler.getSpawnerValue(spawner);
    }

    public void addSpawnerActivation(final SpawnerLocation location, final Spawner spawner) {
        this.activation.getActivationQueue().put(location, spawner);
    }

    public void createHologram(final SpawnerLocation location, final Spawner spawner) {
        this.hologramDisplay.createHologram(location, spawner);
    }

    public void updateHologram(final SpawnerLocation location, final Spawner spawner, final boolean remove) {
        this.hologramDisplay.updateHologram(location, spawner, remove);
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

    public EntityEquipment getEntityEquipment() {
        return this.entityEquipment;
    }

    public HologramDisplay getHologramDisplay() {
        return this.hologramDisplay;
    }

    public TaxHandler getTaxHandler() {
        return this.taxHandler;
    }
}
