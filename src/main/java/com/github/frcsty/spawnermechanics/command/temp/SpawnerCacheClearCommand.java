package com.github.frcsty.spawnermechanics.command.temp;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.entity.Player;

@Command("spawner-cache-clear")
public class SpawnerCacheClearCommand extends CommandBase {

    @Default
    public void onCommand(final Player player) {
        final long start = System.currentTimeMillis();
        SpawnerMechanics.WRAPPER.getStorage().getSpawners().clear();
        player.sendMessage("Cleared spawner storage cache! (Took: " + (System.currentTimeMillis() - start) + "ms)");
    }
}
