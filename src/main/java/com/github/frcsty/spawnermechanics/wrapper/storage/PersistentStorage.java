package com.github.frcsty.spawnermechanics.wrapper.storage;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.object.SpawnerLocation;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class PersistentStorage {

    private final Map<SpawnerLocation, Spawner> spawners = new HashMap<>();

    public void load(final SpawnerMechanics plugin) {
        final File file = new File(plugin.getDataFolder() + "/spawners", "locations.yml");
        final FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
        final ConfigurationSection spawners = fileConfig.getConfigurationSection("spawners");

        if (spawners == null) {
            return;
        }

        for (final String localizedLocation : spawners.getKeys(false)) {
            final String[] argumentedLocation = localizedLocation.split(";");
            final World world = Bukkit.getWorld(argumentedLocation[0]);
            final SpawnerLocation location = new SpawnerLocation(
                    world,
                    Integer.valueOf(argumentedLocation[1]),
                    Integer.valueOf(argumentedLocation[2]),
                    Integer.valueOf(argumentedLocation[3])
            );
            final String[] argumentedParameters = fileConfig.getString("spawners." + localizedLocation).split(";");
            final int amount = Integer.valueOf(argumentedParameters[0]);
            final EntityType type = EntityType.valueOf(argumentedParameters[1]);

            this.spawners.put(location, new Spawner(argumentedParameters[2], type, amount));
        }
        if (!file.delete()) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete 'locations.yml' file!");
        }
    }

    public void save(final SpawnerMechanics plugin) {
        final File directory = new File(plugin.getDataFolder() + "/spawners");
        if (!directory.exists()) {
            directory.mkdir();
        }

        final File file = new File(plugin.getDataFolder() + "/spawners", "locations.yml");
        final FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);

        for (final SpawnerLocation location : this.spawners.keySet()) {
            final Spawner spawner = spawners.get(location);
            final String localizedLocation = location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ();

            if (spawner == null) {
                continue;
            }
            fileConfig.set("spawners." + localizedLocation, spawner.getStack() + ";" + spawner.getType() + ";" + spawner.getMobType());
        }

        try {
            fileConfig.save(file);
        } catch (final IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Failed to save spawner locations", ex);
        }
    }

    public final Map<SpawnerLocation, Spawner> getSpawners() {
        return this.spawners;
    }
}
