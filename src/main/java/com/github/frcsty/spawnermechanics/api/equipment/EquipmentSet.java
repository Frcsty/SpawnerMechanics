package com.github.frcsty.spawnermechanics.api.equipment;

import com.github.frcsty.spawnermechanics.object.Equipment;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Map;

public final class EquipmentSet {

    private final Map<String, Equipment> equipment = new HashMap<>();

    public void setEquipment(final String key, final Equipment value) {
        this.equipment.put(key.toUpperCase(), value);
    }

    public Map<String, Equipment> getEquipment() {
        return this.equipment;
    }
}
