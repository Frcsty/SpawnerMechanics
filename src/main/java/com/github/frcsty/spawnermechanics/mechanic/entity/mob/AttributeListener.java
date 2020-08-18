package com.github.frcsty.spawnermechanics.mechanic.entity.mob;

import com.github.frcsty.spawnermechanics.Identifier;
import com.github.frcsty.spawnermechanics.SpawnerMechanics;
import com.github.frcsty.spawnermechanics.object.Attribute;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.util.Vector;

import java.util.List;

public final class AttributeListener implements Listener {

    @EventHandler
    public void onEntityHit(final EntityDamageByEntityEvent event) {
        final Entity entity = event.getEntity();
        final Attribute attribute = getEntityAttribute(entity);

        if (attribute == null) {
            return;
        }

        if (attribute.getAttribute("knockback") != null) {
            final boolean value = (boolean) attribute.getAttribute("knockback");

            if (!value) {
                entity.setVelocity(new Vector(0, 0, 0));
            }
        }
    }

    private Attribute getEntityAttribute(final Entity entity) {
        final List<MetadataValue> values = entity.getMetadata(Identifier.MOB_TYPE);

        if (values.size() == 0) {
            return null;
        }

        final String type = values.get(0).asString();
        return SpawnerMechanics.getWrapper().getEntityAttribute(type);
    }

}
