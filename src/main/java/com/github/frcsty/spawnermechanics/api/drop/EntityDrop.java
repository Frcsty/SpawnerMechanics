package com.github.frcsty.spawnermechanics.api.drop;

import com.github.frcsty.spawnermechanics.object.Drop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class EntityDrop {

    private static final List<Drop> DROPS = new ArrayList<>();

    public EntityDrop(final Drop... drops) {
        DROPS.addAll(Arrays.asList(drops));
    }

    public void addDrops(final Drop... drops) {
        DROPS.addAll(Arrays.asList(drops));
    }

    public void clearDrops() {
        DROPS.clear();
    }

    public List<Drop> getDrops() {
        return DROPS;
    }

}
