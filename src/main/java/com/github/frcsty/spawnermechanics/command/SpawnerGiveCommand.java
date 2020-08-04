package com.github.frcsty.spawnermechanics.command;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.util.SpawnerItem;
import me.mattstudios.mf.annotations.Command;
import me.mattstudios.mf.annotations.SubCommand;
import me.mattstudios.mf.base.CommandBase;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

@Command("spawner")
public class SpawnerGiveCommand extends CommandBase {

    @SubCommand("give")
    public void onSpawnerGive(final CommandSender sender, final String player, final String typeString, final Integer amount) {
        final Player target = Bukkit.getPlayerExact(player);
        if (target == null || !target.isOnline()) {
            sender.sendMessage("Invalid Target Player.");
            return;
        }

        final EntityType type = SpawnerMechanics.WRAPPER.getSpawnerType(typeString.toUpperCase());
        if (type == null) {
            sender.sendMessage("Invalid Spawner Type.");
            return;
        }

        if (amount <= 0) {
            sender.sendMessage("Amount can not be 0 or negative.");
            return;
        }

        if (target.getInventory().firstEmpty() == -1) {
            target.getWorld().dropItemNaturally(target.getLocation(), SpawnerItem.getItemStack(typeString, type, amount));
        } else {
            target.getInventory().addItem(SpawnerItem.getItemStack(typeString, type, amount));
        }
        target.sendMessage("You were given " + amount + "x " + StringUtils.capitalize(typeString.toLowerCase()) + " Spawners");
    }

}
