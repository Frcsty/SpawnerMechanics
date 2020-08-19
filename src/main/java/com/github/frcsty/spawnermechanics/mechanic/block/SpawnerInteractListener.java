package com.github.frcsty.spawnermechanics.mechanic.block;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.mechanic.event.SpawnerInteractEvent;
import com.github.frcsty.spawnermechanics.object.Drop;
import com.github.frcsty.spawnermechanics.object.Spawner;
import com.github.frcsty.spawnermechanics.util.Color;
import com.github.frcsty.spawnermechanics.util.Replace;
import com.github.frcsty.spawnermechanics.wrapper.drop.EntityDrop;
import me.mattstudios.mfgui.gui.guis.Gui;
import me.mattstudios.mfgui.gui.guis.GuiItem;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

public final class SpawnerInteractListener implements Listener {

    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\{", Pattern.CASE_INSENSITIVE);
    private final DecimalFormat format = new DecimalFormat("#,###");
    private final SpawnerMechanics plugin;
    private final List<String> tools = new ArrayList<>(Arrays.asList(
            "PICKAXE", "SHOVEL", "AXE", "HOE"
    ));

    public SpawnerInteractListener(final SpawnerMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSpawnerInteract(final SpawnerInteractEvent event) {
        final Spawner spawner = event.getSpawner();
        final Player player = event.getPlayer();
        final ItemStack item = event.getItemInHand();

        boolean applicable = false;
        if (item == null || isUsable(item)) {
            applicable = true;
        }

        if (!applicable) {
            return;
        }

        if (spawner == null) {
            return;
        }

        final Gui gui = getMainUI(spawner);

        if (gui == null) {
            plugin.getLogger().log(Level.WARNING, "User " + player.getName() + " failed to open spawner UI!");
            return;
        }
        gui.open(player);
    }

    private boolean isUsable(final ItemStack item) {
        boolean result = false;
        for (final String tool : tools) {
            if (item.getType().name().contains(tool)) {
                result = true;
                break;
            }
        }

        return result;
    }

    private Gui getMainUI(final Spawner spawner) {
        final ConfigurationSection section = plugin.getConfig().getConfigurationSection("spawner-information");
        final Gui gui = new Gui(section.getInt("size"), Color.colorize(section.getString("title")));
        gui.setDefaultClickAction(event -> event.setCancelled(true));

        for (final String key : plugin.getConfig().getConfigurationSection("spawner-information.items").getKeys(false)) {
            final ItemStack item = new ItemStack(Material.getMaterial(section.getInt("items." + key + ".material")));
            final ItemMeta meta = item.getItemMeta();

            meta.setDisplayName(Color.colorize(Replace.replaceString(section.getString("items." + key + ".display"),
                    "{spawner-type}", SpawnerMechanics.getWrapper().getMobDisplay(spawner.getMobType()))));
            meta.setLore(Color.colorize(Replace.replaceList(section.getStringList("items." + key + ".lore"),
                    "{stack-value}", formatAmount(getSpawnerStackValue(spawner)),
                    "{stack-amount}", String.valueOf(spawner.getStack()))));

            meta.setLore(getFormattedLore(spawner, meta.getLore()));

            item.setItemMeta(meta);

            gui.setItem(section.getInt("items." + key + ".slot"), new GuiItem(item));
        }

        return gui;
    }

    private String getSpawnerStackValue(final Spawner spawner) {
        return String.valueOf(SpawnerMechanics.getWrapper().getSpawnerValue(spawner) * spawner.getStack());
    }

    private List<String> getFormattedLore(final Spawner spawner, final List<String> lore) {
        final List<String> result = new ArrayList<>();

        for (final String line : lore) {
            if (line.contains("{spawner-drop}")) {
                result.addAll(getStringDrops(spawner));
            } else {
                result.add(line);
            }
        }

        return result;
    }

    private List<String> getStringDrops(final Spawner spawner) {
        final EntityDrop drops = SpawnerMechanics.getWrapper().getEntityDrop(spawner.getMobType());
        final List<String> result = new ArrayList<>();

        if (drops == null) {
            return Color.colorize(new ArrayList<>(Collections.singletonList(" &8- &fNo Drops.")));
        }

        for (final Drop drop : drops.getDrops()) {
            for (final String command : drop.getCommandDrops().keySet()) {
                result.add(" &8- &f" + prettifyCommand(command));
            }

            for (final ItemStack item : drop.getDrops().keySet()) {
                result.add(" &8- &f" + WordUtils.capitalizeFully(item.getType().name().replace("_", " ").toLowerCase()));
            }
        }

        return Color.colorize(result);
    }

    private String prettifyCommand(final String command) {
        final String[] args = BRACKET_PATTERN.split(command);

        if (args.length >= 2) {
            String range = args[1];
            range = range.replace("{", "");
            range = range.replace("}", "");

            final String[] rangeAmount = range.split(":")[1].split("-");
            for (final String key : plugin.getConfig().getStringList("word-variables")) {
                final String[] value = key.split(";");
                if (command.contains(value[0])) {
                    String result = value[1];
                    result = result.replace("{num1}", formatAmount(rangeAmount[0]));
                    result = result.replace("{num2}", formatAmount(rangeAmount[1]));

                    return result;
                }
            }
        }

        return command;
    }

    private String formatAmount(final String amount) {
        return format.format(Integer.valueOf(amount));
    }
}
