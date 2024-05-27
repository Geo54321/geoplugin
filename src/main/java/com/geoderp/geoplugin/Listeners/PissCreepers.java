package com.geoderp.geoplugin.Listeners;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class PissCreepers implements Listener {
    @EventHandler
    public void onPlayerDeath(EntityExplodeEvent event) {
        // Creeper check
        if (event.getEntityType().equals(EntityType.CREEPER)) {
            Location location = event.getLocation();

            for (int g = 0; g < 5; g++) {
                Firework main = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
                FireworkMeta meta = main.getFireworkMeta();
                meta.clearEffects();
                meta.setPower(0);
                meta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).trail(true).with(Type.BURST).build());
                main.setFireworkMeta(meta);
            }
        }
    }
}
