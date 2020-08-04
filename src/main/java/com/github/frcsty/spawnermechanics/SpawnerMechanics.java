package com.github.frcsty.spawnermechanics;

import com.github.frcsty.spawnermechanics.api.SpawnerWrapper;
import com.github.frcsty.spawnermechanics.command.SpawnerGiveCommand;
import com.github.frcsty.spawnermechanics.command.temp.MobsClearCommand;
import com.github.frcsty.spawnermechanics.command.temp.SpawnerCacheClearCommand;
import com.github.frcsty.spawnermechanics.mechanic.*;
import me.mattstudios.mf.base.CommandBase;
import me.mattstudios.mf.base.CommandManager;
import org.bukkit.Bukkit;
import org.bukkit.Warning;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class SpawnerMechanics extends JavaPlugin {

    public static final SpawnerWrapper WRAPPER = new SpawnerWrapper();

    @Override
    public void onEnable() {
        registerCommands(
                new MobsClearCommand(),
                new SpawnerCacheClearCommand(),

                new SpawnerGiveCommand()
        );

        registerListeners(
                new SpawnerEnableListener(),
                new SpawnerPlaceListener(),
                new SpawnerStackListener(),
                new SpawnerBreakListener(),

                new MobSpawnListener(this),
                new MobDeathListener(this)
        );

        WRAPPER.getStorage().load();
        WRAPPER.getActivation().run();
        WRAPPER.getEntityDrops().loadDefault();
        WRAPPER.getSpawnerTypes().loadDefaults();
    }

    @Override
    public void onDisable() {
        WRAPPER.getStorage().save();

        for (final World world : Bukkit.getWorlds()) {
            for (final Entity entity : world.getEntities()) {
                if (entity instanceof Player) {
                    continue;
                }

                entity.remove();
            }
        }
    }

    private void registerListeners(final Listener... listeners) {
        Arrays.stream(listeners).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));
    }

    @Warning(reason = "Only ever generate one command manager!")
    private void registerCommands(final CommandBase... commands) {
        final CommandManager manager = new CommandManager(this);

        Arrays.stream(commands).forEach(manager::register);
    }

}
