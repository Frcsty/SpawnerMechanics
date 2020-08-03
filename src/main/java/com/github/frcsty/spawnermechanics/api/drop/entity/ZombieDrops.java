package com.github.frcsty.spawnermechanics.api.drop.entity;

import com.github.frcsty.spawnermechanics.api.drop.EntityDrop;
import com.github.frcsty.spawnermechanics.object.Drop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public final class ZombieDrops implements EntityDrop {

    private static final List<Drop> DROPS = new ArrayList<>();

    public ZombieDrops(final Drop... drops) {
        DROPS.addAll(Arrays.asList(drops));
    }

    @Override
    public void addDrops(final Drop... drops) {
        DROPS.addAll(Arrays.asList(drops));
    }

    @Override
    public void clearDrops() {
        DROPS.clear();
    }

    @Override
    public List<Drop> getDrops() {
        return DROPS;
    }
}
