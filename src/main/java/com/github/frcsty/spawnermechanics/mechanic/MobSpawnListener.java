package com.github.frcsty.spawnermechanics.mechanic;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.Setting;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.mechanic.event.CustomMobSpawnEvent;
import com.github.frcsty.spawnermechanics.object.CustomEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.*;
import java.util.stream.Collectors;

public final class MobSpawnListener implements Listener {

    private final SpawnerMechanics plugin;

    public MobSpawnListener(final SpawnerMechanics plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobSpawn(final CustomMobSpawnEvent event) {
        final CustomEntity spawned = event.getCustomEntity();

        final Map<Entity, Integer> eligibleEntities = new HashMap<>();

        spawned.getEntity().getNearbyEntities(
                Setting.DISTANCE_X,
                Setting.DISTANCE_Y,
                Setting.DISTANCE_Z
        ).forEach(entity -> {
            if (entity.equals(spawned.getEntity())) {
                return;
            }

            final List<MetadataValue> types = entity.getMetadata(Identifier.MOB_TYPE);
            if (types.size() == 0) {
                return;
            }

            final String type = types.get(0).asString();
            if (!type.equalsIgnoreCase(spawned.getMobType())) {
                return;
            }

            final List<MetadataValue> amounts = entity.getMetadata(Identifier.MOB_AMOUNT);
            if (amounts.size() == 0) {
                return;
            }

            final int amount = amounts.get(0).asInt();
            eligibleEntities.put(entity, amount == 0 ? 1 : amount);
        });

        final List<Entity> sortedEligibleEntities = getSortedEntities(eligibleEntities);
        if (sortedEligibleEntities.size() == 0) {
            return;
        }

        final Entity entity = sortedEligibleEntities.get(0);

        final List<MetadataValue> amounts = entity.getMetadata(Identifier.MOB_AMOUNT);
        final int amount = amounts.get(0).asInt();

        int remainder = 0;
        int batch = amount == 0 ? spawned.getBatch() : amount + spawned.getBatch();
        if (batch >= Setting.MAX_MOB_STACK) {
            remainder = (amount + spawned.getBatch()) - Setting.MAX_MOB_STACK;
            batch -= remainder;
        }

        if (remainder > 0) {
            final CustomEntity newMob = new CustomEntity(spawned.getType(), remainder, spawned.getLocation(), spawned.getMobType());

            newMob.spawn(false);
        }

        entity.setCustomName(batch + "x " + SpawnerMechanics.WRAPPER.getMobDisplay(entity.getMetadata(Identifier.MOB_TYPE).get(0).asString()));
        entity.setMetadata(Identifier.MOB_AMOUNT, new FixedMetadataValue(plugin, batch));
        spawned.getEntity().remove();
    }

    private List<Entity> getSortedEntities(final Map<Entity, Integer> input) {
        final Map<Entity, Integer> sorted = input.entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));

        return new ArrayList<>(sorted.keySet());
    }
}
