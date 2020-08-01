package com.github.frcsty.spawnermechanics;

import com.github.frcsty.spawnermechanics.api.SpawnerWrapper;
import com.github.frcsty.spawnermechanics.mechanic.SpawnerEnableListener;
import com.github.frcsty.spawnermechanics.mechanic.SpawnerPlaceListener;
import com.github.frcsty.spawnermechanics.mechanic.SpawnerStackListener;
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

        registerListeners(
                new SpawnerEnableListener(),
                new SpawnerPlaceListener(),
                new SpawnerStackListener()
        );

        WRAPPER.getStorage().load();
        WRAPPER.getActivation().run();
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

        int amount = 0;
        for (final Entity entity : world.getEntities()) {
            if (entity instanceof Player) {
                continue;
            }

            entity.remove();
            amount++;
        }

        player.sendMessage("Removed " + amount + " entities!");
        return true;
    }
}
