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

    public TaxHolder handle(final Player player, final Spawner spawner) {
        final int balance = (int) economy.getBalance(player);
        final int value = getSpawnerValue(spawner);
        if (value <= 0) return null;

        final TaxHolder holder = new TaxHolder();
        if (hasBypass(player) || balance >= value) {
            holder.setAmount(1);
            if (!hasBypass(player)) {
                economy.withdrawPlayer(player, value);
            }
        } else {
            holder.setAmount(0);
        }

        return holder;
    }

    public int getSpawnerValue(final Spawner spawner) {
        return this.taxes.get(spawner.getMobType().toUpperCase()) == null ? 0 : this.taxes.get(spawner.getMobType().toUpperCase());
    }

    private boolean hasBypass(final Player player) {
        return player.hasPermission("districtspawners.tax.bypass");
    }
}
