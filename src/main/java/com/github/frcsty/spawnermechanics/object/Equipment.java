package com.github.frcsty.spawnermechanics.object;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.HashMap;
import java.util.Map;

public final class Equipment {

    private final Map<Enchantment, Integer> enchantments = new HashMap<>();
    private Material material = Material.STONE;
    private short data = 0;

    public void setMaterial(final String material) {
        this.material = Material.getMaterial(Integer.valueOf(material));
    }

    public void setData(final int data) {
        this.data = (short) data;
    }

    public void addEnchantment(final String enchantment, final int level) {
        this.enchantments.put(Enchantment.getByName(enchantment), level);
    }

    public Material getMaterial() {
        return this.material;
    }

    public short getData() {
        return this.data;
    }

    public Map<Enchantment, Integer> getEnchantments() {
        return this.enchantments;
    }
}
