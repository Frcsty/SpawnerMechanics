package com.github.frcsty.spawnermechanics.wrapper.drop;

import com.github.frcsty.spawnermechanics.object.Drop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class EntityDrop {

    private final List<Drop> drops = new ArrayList<>();

    public EntityDrop(final Drop... drops) {
        this.drops.addAll(Arrays.asList(drops));
    }

    public void addDrops(final Drop... drops) {
        this.drops.addAll(Arrays.asList(drops));
    }

    public void clearDrops() {
        drops.clear();
    }

    public List<Drop> getDrops() {
        return drops;
    }

}
