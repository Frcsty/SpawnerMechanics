package com.github.frcsty.spawnermechanics.object;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Objects;

public final class SpawnerLocation {

    private final World world;
    private final int x;
    private final int y;
    private final int z;

    public SpawnerLocation(final World world, final int x, final int y, final int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Location getLocation() {
        return new Location(world, x, y, z);
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SpawnerLocation)) {
            return false;
        }

        final SpawnerLocation input = (SpawnerLocation) obj;
        return input.getWorld().equals(this.world) && input.getX() == this.x && input.getY() == this.y && input.getZ() == this.z;
    }
}
