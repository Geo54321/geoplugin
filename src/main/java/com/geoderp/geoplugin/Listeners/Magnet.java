package com.geoderp.geoplugin.Listeners;

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
    private int range;
    private Material[] validMaterials = MagnetRequirements.validMaterials;

    public Magnet(JavaPlugin plugin) {
        this.plugin = plugin;
        this.range = plugin.getConfig().getInt("options.magnet-range");
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        if (player.hasPermission("GeoPlugin.mechanics.magnet")){
            if(player.getInventory().getItemInOffHand().getType().equals(Material.NETHER_STAR) || player.getInventory().getItemInOffHand().getType().equals(Material.WITHER_ROSE)) {
                for(Entity entity : player.getNearbyEntities(range, range, range)) {
                    if(entity instanceof Item || entity instanceof ExperienceOrb) {
                        entity.teleport(player);
                    }
                }
            }
        }
    }

    public boolean isValidMagnet(Player player) {
        ItemStack offhandItem = player.getInventory().getItemInOffHand();
        ItemMeta offhandMeta = offhandItem.getItemMeta();
        Material offhandMaterial = offhandItem.getType();

        if (offhandMeta.isUnbreakable() && offhandMeta.hasLore() && offhandMeta.getLore().contains(MagnetRequirements.lore)) {
            for (Material mat : validMaterials) {
                if (mat.equals(offhandMaterial)) {
                    return true;
                }
            }
        }
        return false;
    }
}
