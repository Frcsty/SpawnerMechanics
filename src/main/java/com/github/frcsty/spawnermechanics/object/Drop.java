package com.github.frcsty.spawnermechanics.object;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.regex.Pattern;

public final class Drop {

    private final Map<ItemStack, Integer> drops = new HashMap<>();
    private final Map<String, Integer> commands = new HashMap<>();
    private static final int CHANCE_TRESHOLD = 1000;
    private static final SplittableRandom RANDOM = new SplittableRandom();
    private static final Pattern BRACKET_PATTERN = Pattern.compile("\\{", Pattern.CASE_INSENSITIVE);

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
        drops.put(new ItemStack(material, amount), CHANCE_TRESHOLD);
        return this;
    }

    public Drop withCommandDrop(final String command, final int chance) {
        commands.put(command, chance);
        return this;
    }

    public Drop withCommandDrop(final String command) {
        commands.put(command, CHANCE_TRESHOLD);
        return this;
    }

    public Map<ItemStack, Integer> getDrops() {
        return drops;
    }

    public List<ItemStack> getChanceSortedDrops(final boolean randomAmount) {
        final List<ItemStack> result = new ArrayList<>();

        for (final ItemStack item : drops.keySet()) {
            if (RANDOM.nextInt(CHANCE_TRESHOLD) <= drops.get(item)) {
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

    public List<String> getChanceSortedCommands() {
        final List<String> result = new ArrayList<>();

        for (String command : commands.keySet()) {
            if (RANDOM.nextInt(CHANCE_TRESHOLD) < commands.get(command)) {
                final String[] args = BRACKET_PATTERN.split(command);

                if (args.length == 1) {
                    result.add(command);
                    continue;
                }
                String range = args[1];
                range = range.replace("{", "");
                range = range.replace("}", "");

                final String[] rangeAmount = range.split(":")[1].split("-");
                final int amount = RANDOM.nextInt(Integer.valueOf(rangeAmount[0]), Integer.valueOf(rangeAmount[1]));

                command = command.replace(args[1], String.valueOf(amount));
                command = command.replace("{", "");
                result.add(command);
            }
        }

        return result;
    }

}
