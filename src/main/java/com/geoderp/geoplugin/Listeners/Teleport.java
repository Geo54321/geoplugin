package com.geoderp.geoplugin.Listeners;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class Teleport implements Listener {
    
    @EventHandler
    public void onMove(PlayerTeleportEvent event) {
        Logger log = Bukkit.getLogger();
        log.info("§5Test teleport event intercept: ");
        log.info("§5Player: "+ event.getPlayer().getName() + " | From: " + event.getFrom().toString() + " | To: " + event.getTo().toString());
    }
}
