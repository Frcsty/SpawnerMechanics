package com.github.frcsty.spawnermechanics;

import com.github.frcsty.spawnermechanics.command.SpawnerGiveCommand;
import com.github.frcsty.spawnermechanics.command.temp.MobsClearCommand;
import com.github.frcsty.spawnermechanics.command.temp.SpawnerCacheClearCommand;
import com.github.frcsty.spawnermechanics.command.temp.ToggleSpawnerActivation;
import com.github.frcsty.spawnermechanics.mechanic.block.*;
import com.github.frcsty.spawnermechanics.mechanic.entity.MobDeathListener;
import com.github.frcsty.spawnermechanics.mechanic.entity.MobSpawnListener;
import com.github.frcsty.spawnermechanics.mechanic.entity.mob.AttributeListener;
import com.github.frcsty.spawnermechanics.wrapper.SpawnerWrapper;
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

    private static final SpawnerWrapper WRAPPER = new SpawnerWrapper();

    public static SpawnerWrapper getWrapper() {
        return WRAPPER;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();

        saveResources(
                "types.json",
                "economy.json",

                "attributes/blaze.json",
                "attributes/iron_golem.json",

                "drops/pig.json",
                "drops/skeleton.json",
                "drops/zombie.json",
                "drops/frozen_snowman.json",
                "drops/blaze.json",

                "equipment/zombie.json"
        );

        registerCommands(
                new MobsClearCommand(),
                new SpawnerCacheClearCommand(),
                new ToggleSpawnerActivation(),

                new SpawnerGiveCommand()
        );

        registerListeners(
                new SpawnerEnableListener(),
                new SpawnerPlaceListener(),
                new SpawnerStackListener(),
                new SpawnerBreakListener(),
                new SpawnerInteractListener(this),

                new MobSpawnListener(this),
                new MobDeathListener(this),

                new AttributeListener()
        );

        WRAPPER.getStorage().load(this);

        WRAPPER.getActivation().run();
        WRAPPER.getSpawnerTypes().load();
        WRAPPER.getEntityDrops().load();
        WRAPPER.getEntityAttributes().load();
        WRAPPER.getEntityEquipment().load();
        WRAPPER.getHologramDisplay().load();
        WRAPPER.getTaxHandler().load();
    }

    @Override
    public void onDisable() {
        reloadConfig();
        WRAPPER.getStorage().save(this);
        WRAPPER.getHologramDisplay().remove();

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

    private void saveResources(final String... resources) {
        Arrays.stream(resources).forEach(resource -> saveResource(resource, false));
    }

}
