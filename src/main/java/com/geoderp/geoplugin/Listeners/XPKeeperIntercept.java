package com.geoderp.geoplugin.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class XPKeeperIntercept implements Listener {
    @EventHandler
    public void signPlaced(SignChangeEvent event) {
        String[] lines = event.getLines();
        Player player = event.getPlayer();

        for(String line : lines) {
            line = line.toLowerCase();
            if(line.contains("xpkeeper")) {
                player.sendMessage("§cXPKeeper no longer exists. Please use /geokeeper instead. For more info use /help geokeeper or visit our website at https://mcnsa.com/custom-features");
            }
        }
    }
}
