package com.github.frcsty.spawnermechanics.object;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class Drop {

    private final Map<ItemStack, Integer> drops = new HashMap<>();
    private static final SplittableRandom RANDOM = new SplittableRandom();

    public Drop withDrop(final ItemStack item, final int chance) {
        drops.put(item, chance);
        return this;
    }

    public Drop withDrop(final Material material, final int amount, final int chance) {
        drops.put(new ItemStack(material, amount), chance);
        return this;
    }

    public Drop withDrop(final ItemStack item) {
        drops.put(item, 100);
        return this;
    }

    public Drop withDrop(final Material material, final int amount) {
        drops.put(new ItemStack(material, amount), 100);
        return this;
    }

    public Map<ItemStack, Integer> getDrops() {
        return drops;
    }

    public List<ItemStack> getChanceSortedDrops(final boolean randomAmount) {
        final List<ItemStack> result = new ArrayList<>();

        for (final ItemStack item : drops.keySet()) {
            if (RANDOM.nextInt(100) <= drops.get(item)) {
                if (randomAmount) {
                    final int amount = RANDOM.nextInt(item.getAmount() + 1);
                    result.add(new ItemStack(item.getType(), amount <= 0 ? 1 : amount));
                    continue;
                }

                result.add(item);
            }
        }

        return result;
    }

}
