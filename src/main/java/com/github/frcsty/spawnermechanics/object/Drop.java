package com.github.frcsty.spawnermechanics.object;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class Drop {

    private static final Map<ItemStack, Integer> DROPS = new HashMap<>();
    private static final SplittableRandom RANDOM = new SplittableRandom();

    public Drop withDrop(final ItemStack item, final int chance) {
        DROPS.put(item, chance);
        return this;
    }

    public Drop withDrop(final Material material, final int amount, final int chance) {
        DROPS.put(new ItemStack(material, amount), chance);
        return this;
    }

    public Drop withDrop(final ItemStack item) {
        DROPS.put(item, 100);
        return this;
    }

    public Drop withDrop(final Material material, final int amount) {
        DROPS.put(new ItemStack(material, amount), 100);
        return this;
    }

    public Map<ItemStack, Integer> getDrops() {
        return DROPS;
    }

    public List<ItemStack> getChanceSortedDrops() {
        final List<ItemStack> result = new ArrayList<>();

        for (final ItemStack item : DROPS.keySet()) {
            if (RANDOM.nextInt(100) <= DROPS.get(item)) {
                result.add(item);
            }
        }

        return result;
    }

}
