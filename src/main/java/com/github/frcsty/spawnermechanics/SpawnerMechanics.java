package com.github.frcsty.spawnermechanics;

import com.github.frcsty.spawnermechanics.api.SpawnerWrapper;
import com.github.frcsty.spawnermechanics.mechanic.*;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class SpawnerMechanics extends JavaPlugin implements CommandExecutor {

    public static final SpawnerWrapper WRAPPER = new SpawnerWrapper();

    @Override
    public void onEnable() {
        getCommand("mobs-clear").setExecutor(this);
        getCommand("mobs-clear-cache").setExecutor(this);

        registerListeners(
                new SpawnerEnableListener(),
                new SpawnerPlaceListener(),
                new SpawnerStackListener(),

                new MobSpawnListener(this),
                new MobDeathListener(this)
        );

        WRAPPER.getStorage().load();
        WRAPPER.getActivation().run();
        WRAPPER.getEntityDrops().loadDefault();
    }

    @Override
    public void onDisable() {
        WRAPPER.getStorage().save();
    }

    private void registerListeners(final Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        final Player player = (Player) sender;
        final World world = player.getWorld();
        final long current = System.currentTimeMillis();

        if (command.getName().equalsIgnoreCase("mobs-clear")) {
            int amount = 0;
            for (final Entity entity : world.getEntities()) {
                if (entity instanceof Player) {
                    continue;
                }

                entity.remove();
                amount++;
            }

            player.sendMessage("Removed " + amount + " entities! (Took: " + (System.currentTimeMillis() - current) + "ms)");
            return true;
        }
        if (command.getName().equalsIgnoreCase("mobs-clear-cache")) {
            WRAPPER.getStorage().getSpawners().clear();
            player.sendMessage("Cleared spawner storage cache! (Took: " + (System.currentTimeMillis() - current) + "ms)");
            return true;
        }

        return true;
    }

}
