package com.geoderp.geoplugin.Listeners;

import org.bukkit.entity.AbstractHorse;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Teleport implements Listener {
    
    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("GeoPlugin.jank.mountTeleport")) {
            Entity mount = player.getVehicle();
            if (mount != null) {
                if (mount instanceof AbstractHorse) {

                    mount.teleport(event.getTo());
                }
            }
        }
    }
}
