package com.github.frcsty.spawnermechanics.command.temp;

import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.Default;
import me.mattstudios.mf.base.CommandBase;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

@Command("mobs-clear")
public class MobsClearCommand extends CommandBase {

    @Default
    public void onCommand(final Player player) {
        final World world = player.getWorld();
        final long start = System.currentTimeMillis();

        int amount = 0;
        for (final Entity entity : world.getEntities()) {
            if (entity instanceof Player) {
                continue;
            }

            entity.remove();
            amount++;
        }

        player.sendMessage("Removed " + amount + " entities! (Took: " + (System.currentTimeMillis() - start) + "ms)");
    }
}
