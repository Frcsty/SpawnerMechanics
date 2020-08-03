package com.github.frcsty.spawnermechanics.api.drop;

import com.github.frcsty.spawnermechanics.object.Drop;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public interface EntityDrop {

    List<Drop> getDrops();

    void addDrops(Drop... drops);

    void clearDrops();
}
