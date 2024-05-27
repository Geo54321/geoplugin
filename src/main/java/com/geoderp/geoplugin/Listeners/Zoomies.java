package com.geoderp.geoplugin.Listeners;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Zoomies implements Listener {
    ArrayList<String> lore = new ArrayList<String>();

    public Zoomies() {
        lore.add("§5GeoZoomies");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        int permissionLevel = getPermissionLevel(event.getPlayer());
        if (permissionLevel > 0) {
            if (event.getPlayer().isSprinting() || event.getPlayer().isFlying()) {
                int speedLevel = hasSpeedItem(event.getPlayer());
                if (speedLevel > 0 && permissionLevel >= speedLevel) {
                    event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 4, speedLevel));
                }
            }
        }
    }

    public int getPermissionLevel(Player p) {
        if (p.hasPermission("GeoPlugin.mechanics.speed.giga")) {
            return 5;
        }
        else if (p.hasPermission("GeoPlugin.mechanics.speed.strong")) {
            return 2;
        }
        else if (p.hasPermission("GeoPlugin.mechanics.speed.weak")) {
            return 1;
        }
        return 0;
    }

    public int hasSpeedItem(Player p) {
        int speedLevel = 0;

        for (ItemStack item : p.getInventory()) {
            // Other potential items - sugar, rabbit foot, 
            
            if (item.hasItemMeta()) {
                ItemMeta itemMeta = item.getItemMeta();
                Material itemMaterial = item.getType();
                
                if (itemMeta.hasLore()) {
                    if (itemMeta.getLore().equals(lore)) {
                        if (itemMaterial.equals(Material.FEATHER)) {
                            if (1 > speedLevel) {
                                speedLevel = 1;
                            }
                        }
                        else if (itemMaterial.equals(Material.BLAZE_POWDER)) {
                            if (2 > speedLevel) {
                                speedLevel = 2;
                            }
                        }
                        else if (itemMaterial.equals(Material.POWERED_RAIL)) {
                            if (5 > speedLevel) {
                                speedLevel = 5;
                            }
                        }
                    }
                }
            }
        }
        return speedLevel;
    }
}
