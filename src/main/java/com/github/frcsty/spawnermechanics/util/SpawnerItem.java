package com.github.frcsty.spawnermechanics.util;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class SpawnerItem {

    public static ItemStack getItemStack(final String mobType, final EntityType type, final int amount) {
        return getFormattedItemStack(mobType, type, amount);
    }

    private static ItemStack getFormattedItemStack(final String mobType, final EntityType type, final int amount) {
        final Block spawnerBlock = getBaseBlock();
        spawnerBlock.setType(Material.MOB_SPAWNER);
        final CreatureSpawner spawner = getCreatureSpawner(type);

        ItemStack item = new ItemStack(spawner.getType(), amount);
        final ItemMeta meta = item.getItemMeta();

        meta.setDisplayName(Color.colorize("&b" + getFormattedEntityType(mobType) + " Spawner"));

        item.setItemMeta(meta);
        item = ItemNBT.setNBTTag(item, "entityType", mobType.toUpperCase());

        return item;
    }

    private static Block getBaseBlock() {
        return Bukkit.getWorld("world").getBlockAt(0, 0, 0);
    }

    private static String getFormattedEntityType(final String type) {
        return StringUtils.capitalize(type.toLowerCase());
    }

    private static CreatureSpawner getCreatureSpawner(final EntityType type) {
        final Block spawnerBlock = getBaseBlock();
        spawnerBlock.setType(Material.MOB_SPAWNER);
        final CreatureSpawner spawner = (CreatureSpawner) spawnerBlock.getState();
        spawner.setSpawnedType(type);

        return spawner;
    }

}
