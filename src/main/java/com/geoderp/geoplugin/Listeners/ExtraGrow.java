package com.geoderp.geoplugin.Listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class ExtraGrow implements Listener {
    JavaPlugin plugin;
    Material[] validMaterials = {Material.LILY_PAD, Material.OXEYE_DAISY, Material.CORNFLOWER, Material.BLUE_ORCHID, Material.AZURE_BLUET, Material.WHITE_TULIP, Material.ALLIUM, Material.LILAC, Material.ORANGE_TULIP, Material.PINK_TULIP, Material.POPPY, Material.RED_TULIP, Material.LILY_OF_THE_VALLEY, Material.DANDELION};

    public ExtraGrow(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEvent event) {
        if (event.getPlayer().hasPermission("GeoPlugin.mechanics.grow")) {
            if (event.getMaterial().equals(Material.BONE_MEAL)) {
                if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
                    Material target = event.getClickedBlock().getType();
                    if (isValidGrow(target)) {
                        event.getItem().setAmount(event.getItem().getAmount() - 1);
                        if (Math.random() <= plugin.getConfig().getDouble("options.growth-chance-percent")) {
                            event.getClickedBlock().getWorld().dropItemNaturally(event.getClickedBlock().getLocation(), new ItemStack(target, 1));
                            event.getClickedBlock().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, (float) 0.15, (float) 0.75);
                        }
                    }
                }
            }
        }
    }

    public boolean isValidGrow(Material target) {
        boolean isValid = false;
        for (Material mat : validMaterials) {
            if (mat.equals(target)) {
                return true;
            }
        }
        return isValid;
    }
}
