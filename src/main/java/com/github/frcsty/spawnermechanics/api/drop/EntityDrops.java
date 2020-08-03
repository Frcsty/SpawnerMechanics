package com.github.frcsty.spawnermechanics.api.drop;

import com.github.frcsty.spawnermechanics.api.drop.entity.PigDrops;
import com.github.frcsty.spawnermechanics.api.drop.entity.ZombieDrops;
import com.github.frcsty.spawnermechanics.object.Drop;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public final class EntityDrops {

    private static final Map<String, EntityDrop> ENTITY_DROPS = new HashMap<>();

    public void loadDefault() {
        ENTITY_DROPS.put("ZOMBIE", new ZombieDrops(
                new Drop()
                    .withDrop(new ItemStack(Material.ROTTEN_FLESH, 3), 60)
                    .withDrop(new ItemStack(Material.IRON_INGOT, 1), 30)
        ));
        ENTITY_DROPS.put("PIG", new PigDrops(
                new Drop()
                    .withDrop(new ItemStack(Material.PORK, 2), 100)
                    .withDrop(new ItemStack(Material.SUGAR, 1), 35)
        ));
    }

    public static EntityDrop getEntityDrop(final String identifier) {
        return ENTITY_DROPS.get(identifier);
    }

    public static void setEntityDrop(final String identifier, final EntityDrop drop) {
        ENTITY_DROPS.put(identifier, drop);
    }
}
