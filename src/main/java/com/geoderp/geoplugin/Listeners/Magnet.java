package com.geoderp.geoplugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Utility.MagnetRequirements;

public class Magnet implements Listener {
    JavaPlugin plugin;
    private int weakRange;
    private int strongRange;
    private Material[] validStrongMaterials = MagnetRequirements.validStrongMaterials;
    private Material[] validWeakMaterials = MagnetRequirements.validWeakMaterials;

    public Magnet(JavaPlugin plugin) {
        this.plugin = plugin;
        this.weakRange = plugin.getConfig().getInt("options.weak-magnet-range");
        this.strongRange = plugin.getConfig().getInt("options.strong-magnet-range");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfig().getBoolean("options.sneak-disable-magnet") && player.isSneaking()) {
            // Don't do magnet
        }
        else {
            if (player.getInventory().firstEmpty() != -1) {
                if (Math.abs(event.getFrom().distance(event.getTo())) > 0) {
                    doMagnet(player);
                }   
            }
        }
    }

    public void doMagnet(Player player) {
        if (player.hasPermission("GeoPlugin.mechanics.magnet.strong") && isValidMagnet(player).equals("strong")){
            for(Entity entity : player.getNearbyEntities(strongRange, strongRange/2, strongRange)) {
                if(entity instanceof Item || entity instanceof ExperienceOrb) {
                    entity.teleport(player);
                }
            }
        }
        else if (player.hasPermission("GeoPlugin.mechanics.magnet.weak") && isValidMagnet(player).equals("weak")) {
            for(Entity entity : player.getNearbyEntities(weakRange, weakRange, weakRange)) {
                if(entity instanceof Item || entity instanceof ExperienceOrb) {
                    entity.teleport(player);
                }
            }
        }
    }

    public String isValidMagnet(Player player) {
        try {
            ItemStack offhandItem = player.getInventory().getItemInOffHand();
            if (offhandItem != null && offhandItem.getItemMeta() != null) {
                ItemMeta offhandMeta = offhandItem.getItemMeta();
                Material offhandMaterial = offhandItem.getType();
                
                if (offhandMeta.hasLore()) {
                    if (offhandMeta.getLore().equals(MagnetRequirements.lore)) {
                        for (Material mat : validStrongMaterials) {
                            if (mat.equals(offhandMaterial)) {
                                return "strong";
                            }
                        }
                        for (Material mat : validWeakMaterials) {
                            if (mat.equals(offhandMaterial)) {
                                return "weak";
                            }
                        }
                    }
                }
            }
            return "null";
        }
        catch (Exception e) {
            Bukkit.getLogger().info(player.getName() + "'s magnet is angy" + e);
        }
        return "null";
    }
}
