package com.github.frcsty.spawnermechanics.api.drop;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Drop;
import org.bukkit.Material;
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

public final class EntityDrops {

    private final Map<String, EntityDrop> entityDrops = new HashMap<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void load() {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);
        final File dropsDir = new File(plugin.getDataFolder() + "/drops");
        if (!dropsDir.exists()) {
            dropsDir.mkdir();
        }
        if (dropsDir.length() == 0) {
            return;
        }

        final File[] files = dropsDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (final File dropFile : files) {
            if (!dropFile.getName().contains(".json")) {
                continue;
            }

            final EntityDrop entityDrop = new EntityDrop();
            final JSONParser parser = new JSONParser();
            try {
                final Object parsed = parser.parse(new FileReader(dropFile.getPath()));
                final JSONObject json = (JSONObject) parsed;
                final JSONObject drops = (JSONObject) json.get("drops");

                for (final Object dropObject : drops.keySet()) {
                    final JSONObject object = (JSONObject) drops.get(dropObject);
                    Drop drop;
                    if (object.get("command") != null) {
                        drop = new Drop().withCommandDrop(
                                (String) object.get("command"),
                                Integer.valueOf(object.get("chance").toString())
                        );
                    } else {
                        drop = new Drop().withDrop(
                                Material.matchMaterial((String) object.get("material")),
                                Integer.valueOf(object.get("amount").toString()),
                                Integer.valueOf(object.get("chance").toString())
                        );
                    }

                    entityDrop.addDrops(drop);
                }
            } catch (ParseException | IOException ex) {
                plugin.getLogger().log(Level.WARNING, "Failed to parse file " + dropFile.getName() + "!", ex);
            }

            entityDrops.put(dropFile.getName().split("\\.")[0].toUpperCase(), entityDrop);
        }
    }

    public Map<String, EntityDrop> getEntityDrops() {
        return this.entityDrops;
    }
}
