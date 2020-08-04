package com.github.frcsty.spawnermechanics.api.storage;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;

public final class PersistentStorage {

    private static final Set<Spawner> SPAWNERS = new HashSet<>();

    public void load() {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);
        final File file = new File(plugin.getDataFolder() + "/spawners", "locations.yml");
        final FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);
        final ConfigurationSection spawners = fileConfig.getConfigurationSection("spawners");

        if (spawners == null) {
            return;
        }

        for (final String localizedLocation : spawners.getKeys(false)) {
            final String[] argumentedLocation = localizedLocation.split(";");
            final World world = Bukkit.getWorld(argumentedLocation[0]);
            final Location location = new Location(
                    world,
                    Integer.valueOf(argumentedLocation[1]),
                    Integer.valueOf(argumentedLocation[2]),
                    Integer.valueOf(argumentedLocation[3])
            );
            final String[] argumentedParameters = fileConfig.getString("spawners." + localizedLocation).split(";");
            final int amount = Integer.valueOf(argumentedParameters[0]);
            final EntityType type = EntityType.valueOf(argumentedParameters[1]);

            SPAWNERS.add(new Spawner(location, argumentedParameters[2], type, amount));
        }
        if (!file.delete()) {
            plugin.getLogger().log(Level.WARNING, "Failed to delete 'locations.yml' file!");
        }
    }

    public void save() {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);
        final File file = new File(plugin.getDataFolder() + "/spawners", "locations.yml");
        final FileConfiguration fileConfig = YamlConfiguration.loadConfiguration(file);

        for (final Spawner spawner : SPAWNERS) {
            final Location location = spawner.getLocation();
            final String localizedLocation = location.getWorld().getName() + ";" + location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ();

            fileConfig.set("spawners." + localizedLocation, spawner.getStack() + ";" + spawner.getType() + ";" + spawner.getMobType());
        }

        try {
            fileConfig.save(file);
        } catch (final IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Failed to save spawner locations", ex);
        }
    }

    public final Set<Spawner> getSpawners() {
        return SPAWNERS;
    }
}
