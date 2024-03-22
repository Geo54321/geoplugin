package com.geoderp.geoplugin.Listeners;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class JankStep implements Listener {
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        if(player.hasPermission("GeoPlugin.jank.jankStep")) {
            Location to = event.getTo();
            Location from = event.getFrom();

            if(to.getBlockY() > from.getBlockY()) {
                // Teleport up
                if(validColumn(to)) {
                    to.setY(to.getY()+1.5);
                    player.teleport(to);
                }
            }
            // else {
            //     if(validAssist(to, from)) {
            //         // Teleport up
            //         to.setY(to.getY()+1.5);
            //         player.teleport(to);
            //     }
            // }
        }
    }

    public boolean validAssist(Location to, Location from) {
        ArrayList<Location> dest = new ArrayList<Location>();

        // next X to dest
        if(to.getX() < from.getX()) {
            dest.add(new Location(from.getWorld(), from.getX() - 1, from.getY(), from.getZ()));
        }
        else {
            dest.add(new Location(from.getWorld(), from.getX() + 1, from.getY(), from.getZ()));
        }

        // next Z to dest
        if(to.getZ() < from.getZ()) {
            dest.add(new Location(from.getWorld(), from.getX(), from.getY(), from.getZ() - 1));
        }
        else {
            dest.add(new Location(from.getWorld(), from.getX(), from.getY(), from.getZ() + 1));
        }

        // next corner
        dest.add(new Location(from.getWorld(), dest.get(0).getX(), from.getY(), dest.get(1).getZ()));

        // check each block
        for(Location loc : dest) {
            if(!validColumn(loc)) {
                return false;
            }
        }
        return true;
    }

    public boolean validColumn(Location loc) {
        if(!loc.getBlock().isEmpty()) {
            // upwards floor exists
            for(int g = 0; g < 3; g++) {
                loc.setY(loc.getY()+1);
                if(!loc.getBlock().isEmpty())
                    // no space - not valid
                    return false;
            }
            return true;
        }
        return false;
    }
}
