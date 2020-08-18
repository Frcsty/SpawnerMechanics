package com.github.frcsty.spawnermechanics.wrapper.economy;

import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Spawner;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

public final class TaxHandler {

    private final Map<String, Integer> taxes = new HashMap<>();
    private Economy economy;

    public void load() {
        final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);
        final RegisteredServiceProvider<Economy> provider = plugin.getServer().getServicesManager().getRegistration(Economy.class);
        if (provider == null) {
            plugin.getLogger().log(Level.WARNING, "Failed to find economy provider!");
            plugin.getPluginLoader().disablePlugin(plugin);
            return;
        }

        this.economy = provider.getProvider();

        final File economyFile = new File(plugin.getDataFolder(), "economy.json");
        final JSONParser parser = new JSONParser();
        try {
            final Object parsed = parser.parse(new FileReader(economyFile.getPath()));
            final JSONObject json = (JSONObject) parsed;
            final JSONObject taxes = (JSONObject) json.get("taxes");

            for (final Object tax : taxes.keySet()) {
                this.taxes.put(tax.toString().toUpperCase(), Integer.valueOf(taxes.get(tax).toString()));
            }
        } catch (final ParseException | IOException ex) {
            plugin.getLogger().log(Level.WARNING, "Failed to parse file " + economyFile.getName() + "!", ex);
        }
    }

    public TaxHolder handle(final Player player, final Spawner spawner, final boolean stack) {
        final int balance = (int) economy.getBalance(player);
        final int value = this.taxes.get(spawner.getMobType().toUpperCase());
        if (value <= 0) return null;

        final TaxHolder holder = new TaxHolder();
        final int required = stack ? spawner.getStack() * value : value;

        if (balance >= required) {
            holder.setAmount(stack ? spawner.getStack() : 1);
            economy.withdrawPlayer(player, required);
        } else {
            final int affordable = balance / value;
            final int remaining = spawner.getStack() - affordable;

            System.out.println(affordable + ", " + remaining + ", " + (required - (remaining * value)));
            holder.setAmount(affordable);
            holder.setRemaining(remaining);
            economy.withdrawPlayer(player, required - (remaining * value));
        }

        return holder;
    }
}
