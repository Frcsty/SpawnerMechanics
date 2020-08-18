package com.github.frcsty.spawnermechanics.wrapper.type;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.MobType;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.entity.EntityType;
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

public final class SpawnerTypes {

    private final Map<String, MobType> types = new HashMap<>();

    public void load() {
        for (final EntityType type : EntityType.values()) {
            final String[] name = type.name().split("_");

            String display = String.join(" ", name);
            types.put(type.name().toUpperCase(), new MobType(type, StringUtils.capitalize(display.toLowerCase())));
        }

        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);
        final File typesFile = new File(plugin.getDataFolder(), "types.json");
        final JSONParser parser = new JSONParser();
        try {
            final Object parsed = parser.parse(new FileReader(typesFile.getPath()));
            final JSONObject json = (JSONObject) parsed;
            final JSONObject types = (JSONObject) json.get("types");

            for (final Object type : types.keySet()) {
                final JSONObject object = (JSONObject) types.get(type);

                this.types.put(((String) type).toUpperCase(),
                        new MobType(
                                EntityType.valueOf(((String) object.get("type")).toUpperCase()),
                                (String) object.get("name")
                        )
                );
            }
        } catch (final ParseException | IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Failed to parse file " + typesFile.getName() + "!", ex);
        }
    }

    public Map<String, MobType> getTypes() {
        return this.types;
    }
}
