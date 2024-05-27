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
            Location splashZone = event.getLocation();
            splashZone.setY(splashZone.getY()+1);

            Firework firework = (Firework) splashZone.getWorld().spawnEntity(splashZone, EntityType.FIREWORK);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.clearEffects();
            meta.setPower(1);
            meta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).trail(true).with(Type.BURST).build());
            firework.setFireworkMeta(meta);

            for (int g = 0; g < 5; g++) {
                Firework piss = (Firework) splashZone.getWorld().spawnEntity(splashZone, EntityType.FIREWORK);
                piss.setFireworkMeta(meta);
                piss.detonate();
            }
            firework.detonate();
        }
    }
}
