package com.github.frcsty.spawnermechanics.object;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.wrapper.equipment.EquipmentSet;
import com.github.frcsty.spawnermechanics.mechanic.event.CustomMobSpawnEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;

public final class CustomEntity {

    private final SpawnerMechanics plugin = JavaPlugin.getPlugin(SpawnerMechanics.class);
    private final EntityType type;
    private final int batch;
    private final Location location;
    private final String mobType;
    private Entity entity;

    public CustomEntity(final EntityType type, final int batch, final Location location, final String mobType) {
        this.type = type;
        this.batch = batch;
        this.location = location;
        this.mobType = mobType.toUpperCase();
    }

    public void spawn(final boolean call) {
        final LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, type);
        this.entity = entity;

        entity.setMetadata(Identifier.MOB_TYPE, new FixedMetadataValue(plugin, mobType));
        entity.setMetadata(Identifier.MOB_AMOUNT, new FixedMetadataValue(plugin, batch));

        entity.setCustomName(batch + "x " + SpawnerMechanics.getWrapper().getMobDisplay(mobType));
        SpawnerMechanics.getWrapper().getEntityAttributes().applyEntityAttributes(entity, mobType);

        final EquipmentSet equipment = SpawnerMechanics.getWrapper().getEquipment(mobType);
        if (equipment != null) {
            final EntityEquipment entityEquipment = entity.getEquipment();
            equipment.getEquipment().forEach((key, value) -> {
                final ItemStack item = getItem(value);

                switch (key) {
                    case "HELMET":
                        entityEquipment.setHelmet(item);
                        break;
                    case "CHESTPLATE":
                        entityEquipment.setChestplate(item);
                        break;
                    case "LEGGINGS":
                        entityEquipment.setLeggings(item);
                        break;
                    case "BOOTS":
                        entityEquipment.setBoots(item);
                        break;
                    case "HAND":
                        entityEquipment.setItemInHand(item);
                }

                clearDropChances(entityEquipment);
            });
        }
        if (call) {
            Bukkit.getPluginManager().callEvent(new CustomMobSpawnEvent(this));
        }
    }

    private void clearDropChances(final EntityEquipment equipment) {
        equipment.setHelmetDropChance(0);
        equipment.setChestplateDropChance(0);
        equipment.setLeggingsDropChance(0);
        equipment.setBootsDropChance(0);
        equipment.setItemInHandDropChance(0);
    }

    private ItemStack getItem(final Equipment equipment) {
        final ItemStack item = new ItemStack(equipment.getMaterial(), 1, equipment.getData());

        for (final Enchantment enchant : equipment.getEnchantments().keySet()) {
            final int level = equipment.getEnchantments().get(enchant);
            if (enchant == null || level < 0) {
                continue;
            }
            item.addEnchantment(enchant, level);
        }

        return item;
    }

    public String getMobType() {
        return mobType;
    }

    public EntityType getType() {
        return type;
    }

    public Location getLocation() {
        return location;
    }

    public int getBatch() {
        return batch;
    }

    public Entity getEntity() {
        return entity;
    }
}
