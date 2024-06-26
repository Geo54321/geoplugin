package com.geoderp.geoplugin.Listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.geoderp.geoplugin.Utility.ArtifactRequirements;

public class Zoomies implements Listener {
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
        if (p.hasPermission("GeoPlugin.geoartifact.zoomies.weak")) {
            return 5;
        }
        else if (p.hasPermission("GeoPlugin.geoartifact.zoomies.strong")) {
            return 2;
        }
        else if (p.hasPermission("GeoPlugin.geoartifact.zoomies.giga")) {
            return 1;
        }
        return 0;
    }

    public int hasSpeedItem(Player p) {
        int speedLevel = 0;

        for (ItemStack item : p.getInventory()) {
            if (item != null) {
                if (item.hasItemMeta()) {
                    ItemMeta itemMeta = item.getItemMeta();
                    Material itemMaterial = item.getType();
                    
                    if (itemMeta.hasLore()) {
                        if (itemMeta.getLore().equals(ArtifactRequirements.zoomiesLore)) {
                            if (itemMaterial.equals(ArtifactRequirements.validZoomiesMaterials[0])) {
                                if (1 > speedLevel) {
                                    speedLevel = 1;
                                }
                            }
                            else if (itemMaterial.equals(ArtifactRequirements.validZoomiesMaterials[1])) {
                                if (2 > speedLevel) {
                                    speedLevel = 2;
                                }
                            }
                            else if (itemMaterial.equals(ArtifactRequirements.validZoomiesMaterials[2])) {
                                if (5 > speedLevel) {
                                    speedLevel = 5;
                                }
                            }
                        }
                    }
                }
            }
        }
        return speedLevel;
    }
}
