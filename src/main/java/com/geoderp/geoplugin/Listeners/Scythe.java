package com.geoderp.geoplugin.Listeners;

import java.util.Iterator;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
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
            if (event.getClickedBlock() != null) {
                if (event.getClickedBlock().getType().equals(Material.GRASS) || event.getClickedBlock().getType().equals(Material.TALL_GRASS) || event.getClickedBlock().getType().equals(Material.FERN) || event.getClickedBlock().getType().equals(Material.LARGE_FERN)) {
                    if (player.getInventory().getItemInMainHand().getType().equals(Material.SHEARS) || player.getInventory().getItemInOffHand().getType().equals(Material.SHEARS)) {
                        killGrass(event.getClickedBlock().getLocation());
                    }
                }
            }
        }
    }

    public void killGrass(Location origin) {
        int range = plugin.getConfig().getInt("options.scythe-range");
        for(int x = (range * -1); x <= range; x++) {
            for(int y = (range * -1); y <= range; y++) {
                for(int z = (range * -1); z <= range; z++) {
                    //Location target = new Location(origin.getWorld(), origin.getX()+x, origin.getY()+y, origin.getZ()+z);
                    Block target = origin.getWorld().getBlockAt(origin.getBlockX()+x, origin.getBlockY()+y, origin.getBlockZ()+z);
                    if(target.getType().equals(Material.TALL_GRASS) || target.getType().equals(Material.LARGE_FERN)) {
                        removeTarget(target);
                        origin.getWorld().getBlockAt(origin.getBlockX(), origin.getBlockY()+1, origin.getBlockZ()).setType(Material.AIR);
                    } else if(target.getType().equals(Material.GRASS) || target.getType().equals(Material.FERN)) {
                        removeTarget(target);
                    }
                }
            }
        }
    }

    public void removeTarget(Block target) {
        Iterator<ItemStack> drops = target.getDrops().iterator();
        while (drops.hasNext()) {
            target.getWorld().dropItemNaturally(target.getLocation(), drops.next());
        }
        target.setType(Material.AIR);
        target.getWorld().playSound(target.getLocation(), Sound.BLOCK_GRASS_BREAK, (float) 0.45, (float) (0.96 + Math.random() * 0.24));
    }
}
