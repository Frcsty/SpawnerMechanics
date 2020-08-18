package com.github.frcsty.spawnermechanics.wrapper.equipment;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Equipment;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class EntityEquipment {

    private final Map<String, EquipmentSet> entityEquipment = new HashMap<>();

    public void load() {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);
        final File equipmentDir = new File(plugin.getDataFolder() + "/equipment");
        if (!equipmentDir.exists()) {
            equipmentDir.mkdir();
        }
        if (equipmentDir.length() == 0) {
            return;
        }

        final File[] files = equipmentDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (final File equipmentFile : files) {
            if (!equipmentFile.getName().contains(".json")) {
                continue;
            }

            final EquipmentSet equipmentSet = new EquipmentSet();
            final JSONParser parser = new JSONParser();
            try {
                final Object parsed = parser.parse(new FileReader(equipmentFile.getPath()));
                final JSONObject json = (JSONObject) parsed;
                final JSONObject jsonEquipment = (JSONObject) json.get("equipment");
                final Equipment equipment = new Equipment();

                for (final Object equipmentObject : jsonEquipment.keySet()) {
                    final JSONObject object = (JSONObject) jsonEquipment.get(equipmentObject);

                    equipment.setMaterial(object.get("material"));
                    equipment.setData(Integer.valueOf(object.get("data").toString()));

                    final JSONObject enchantments = (JSONObject) object.get("enchantments");
                    if (enchantments != null) {
                        for (final Object enchantment : enchantments.keySet()) {
                            final JSONObject enchantmentObject = (JSONObject) enchantments.get(enchantment);

                            equipment.addEnchantment(enchantmentObject.get("enchantment").toString(), Integer.valueOf(enchantmentObject.get("level").toString()));
                        }
                    }

                    equipmentSet.setEquipment(object.get("slot").toString(), equipment);
                }
            } catch (final ParseException | IOException ex) {
                plugin.getLogger().log(Level.WARNING, "Failed to parse file " + equipmentFile.getName() + "!", ex);
            }

            entityEquipment.put(equipmentFile.getName().split("\\.")[0].toUpperCase(), equipmentSet);
        }
    }

    public Map<String, EquipmentSet> getEntityEquipment() {
        return this.entityEquipment;
    }
}
