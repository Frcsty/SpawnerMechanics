package com.github.frcsty.spawnermechanics.command.temp;

import com.github.frcsty.spawnermechanics.Setting;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.command.CommandSender;

@Command("toggle-spawners")
public class ToggleSpawnerActivation extends CommandBase {

    @Default
    public void toggleCommand(final CommandSender sender) {
        Setting.ENABLED_SPAWNING = !Setting.ENABLED_SPAWNING;
        sender.sendMessage("Toggled spawning, status: " + Setting.ENABLED_SPAWNING);
    }
}
