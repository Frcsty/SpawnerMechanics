package com.github.frcsty.spawnermechanics.api.drop;

import com.github.frcsty.spawnermechanics.object.Drop;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class EntityDrops {

    private static final Map<String, EntityDrop> ENTITY_DROPS = new HashMap<>();

    public void loadDefault() {
        ENTITY_DROPS.put("ZOMBIE", new EntityDrop(
                new Drop().withDrop(new ItemStack(Material.ROTTEN_FLESH, 3), 60).withDrop(new ItemStack(Material.IRON_INGOT, 1), 30)
        ));

        ENTITY_DROPS.put("PIG", new EntityDrop(
                new Drop().withDrop(new ItemStack(Material.PORK, 2), 100).withDrop(new ItemStack(Material.SUGAR, 1), 35)
        ));

        ENTITY_DROPS.put("SKELETON", new EntityDrop(
                new Drop().withDrop(new ItemStack(Material.ARROW, 2), 60)
        ));
    }

    public Map<String, EntityDrop> getEntityDrops() {
        return ENTITY_DROPS;
    }
}
