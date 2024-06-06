package com.geoderp.geoplugin.Listeners;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Scythe implements Listener {
    JavaPlugin plugin;

    public Scythe(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if(player.hasPermission("GeoPlugin.mechanics.scythe")) {
            if (event.getClickedBlock().getType().equals(Material.GRASS) || event.getClickedBlock().getType().equals(Material.TALL_GRASS)) {
                if (player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS) || player.getInventory().getItemInOffHand().getType().equals(Material.SHEARS)) {
                    killGrass(event.getClickedBlock().getLocation());
                }
            }
        }
    }

    public void killGrass(Location origin) {
        int range = plugin.getConfig().getInt("options.scythe-range");
        for(int x = (range * -1); x <= range; x++) {
            for(int y = (range * -1); y <= range; y++) {
                for(int z = (range * -1); z <= range; z++) {
                    Location target = origin.add(x,y,z);
                    if(target.getBlock().getType().equals(Material.TALL_GRASS)) {
                        target.getBlock().setType(Material.AIR);
                        target.add(0,1,0).getBlock().setType(Material.AIR);
                    } else if(target.getBlock().getType().equals(Material.GRASS)) {
                        target.getBlock().setType(Material.AIR);
                    }
                }
            }
        }
    }
}
