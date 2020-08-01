package com.github.frcsty.spawnermechanics.api.calculation;

import com.github.frcsty.spawnermechanics.object.Spawner;
import org.bukkit.Location;

import java.util.HashMap;
import java.util.Map;
import java.util.SplittableRandom;

public final class EntitySpawn {

    private static final int BATCH_AMOUNT = 3;
    private static final int DEVIATION_AMOUNT = 5;
    private static final SplittableRandom RANDOM = new SplittableRandom();
    private static final Map<Location, Integer> MOB_SPAWNS = new HashMap<>();
    private final Spawner spawner;

    public EntitySpawn(final Spawner spawner) {
        this.spawner = spawner;

        calculateSpawns();
    }

    private void calculateSpawns() {
        final Location baseLocation = spawner.getLocation();
        final int total = RANDOM.nextInt(spawner.getStack() * 3);

        for (int i = 0; i < BATCH_AMOUNT; i++) {
            final int deviation = Integer.valueOf(getAddition() + DEVIATION_AMOUNT);
            final int batch = (total / BATCH_AMOUNT) + deviation;

            if (batch <= 0) {
                continue;
            }

            final Location location = baseLocation.clone();
            //final int x = location.getBlockX() + getRandomX();
            //final int z = location.getBlockZ() + getRandomZ();
            location.setX(location.getBlockX() + getRandomX());
            location.setZ(location.getBlockZ() + getRandomZ());

            MOB_SPAWNS.put(location, batch);
        }
    }

    private String getAddition() {
        return RANDOM.nextInt(2) == 1 ? "+" : "-";
    }

    private int getRandomX() {
        return RANDOM.nextInt(2) == 1 ? -1 : +1;
    }

    private int getRandomZ() {
        return RANDOM.nextInt(2) == 1 ? -1 : +1;
    }

    public Map<Location, Integer> getMobSpawns() {
        return MOB_SPAWNS;
    }

}
