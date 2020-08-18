package com.github.frcsty.spawnermechanics.wrapper.attribute;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Attribute;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.LivingEntity;
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

public final class EntityAttributes {

    private final Map<String, Attribute> attributes = new HashMap<>();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void load() {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);
        final File attributesDir = new File(plugin.getDataFolder() + "/attributes");
        if (!attributesDir.exists()) {
            attributesDir.mkdir();
        }
        if (attributesDir.length() == 0) {
            return;
        }

        final File[] files = attributesDir.listFiles();
        if (files == null || files.length == 0) {
            return;
        }

        for (final File attributeFile : files) {
            if (!attributeFile.getName().contains(".json")) {
                continue;
            }

            final Attribute attribute = new Attribute();
            final JSONParser parser = new JSONParser();
            try {
                final Object parsed = parser.parse(new FileReader(attributeFile.getPath()));
                final JSONObject json = (JSONObject) parsed;
                final JSONObject attributes = (JSONObject) json.get("attributes");

                for (final Object key : attributes.keySet()) {
                    final Object value = attributes.get(key);
                    attribute.setAttribute((String) key, value);
                }
            } catch (final ParseException | IOException ex) {
                plugin.getLogger().log(Level.WARNING, "Failed to parse file " + attributeFile.getName() + "!", ex);
            }

            attributes.put(attributeFile.getName().split("\\.")[0].toUpperCase(), attribute);
        }
    }

    public Map<String, Attribute> getAttributes() {
        return this.attributes;
    }

    public void applyEntityAttributes(final LivingEntity entity, final String entityType) {
        final Attribute attribute = attributes.get(entityType);

        if (attribute == null) {
            return;
        }
        final net.minecraft.server.v1_8_R3.Entity nmsEntity = ((CraftEntity) entity).getHandle();
        final NBTTagCompound compound = new NBTTagCompound();

        nmsEntity.c(compound);
        compound.setByte("NoAI", (byte) 1);
        nmsEntity.f(compound);
        nmsEntity.b(true);

        entity.setHealth((long) attribute.getAttribute("health"));

        if ((boolean) attribute.getAttribute("fire_tick")) {
            entity.setFireTicks(Integer.MAX_VALUE);
        }
    }
}
