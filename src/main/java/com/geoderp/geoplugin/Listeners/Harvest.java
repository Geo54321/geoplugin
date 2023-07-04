package com.geoderp.geoplugin.Listeners;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class Harvest implements Listener {
    private Material[] plants = {Material.WHEAT, Material.POTATOES, Material.CARROTS, Material.BEETROOTS, Material.NETHER_WART};
    private Material[] seeds = {Material.WHEAT_SEEDS, Material.POTATO, Material.CARROT, Material.BEETROOT_SEEDS, Material.NETHER_WART};
    private Material[] validDrops = {Material.WHEAT_SEEDS, Material.WHEAT, Material.POTATO, Material.POISONOUS_POTATO, Material.CARROT, Material.BEETROOT, Material.BEETROOT_SEEDS, Material.NETHER_WART};
    private JavaPlugin plugin;

    public Harvest(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        if(event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            if (isValidPlant(event.getClickedBlock().getType()) && ((Ageable) event.getClickedBlock().getBlockData()).getAge() == ((Ageable) event.getClickedBlock().getBlockData()).getMaximumAge()) {
                replacePlant(event.getClickedBlock());
            }
        }
    }

    public boolean isValidPlant(Material material) {
        for(Material m : plants)
            if(m.equals(material))
                return true;
        return false;
    }

    public boolean isProblemChild(Material material) {
        if(material.equals(Material.POTATO) || material.equals(Material.CARROT))
            return true;
        return false;
    }

    private boolean isValidSeed(Material material) {
        for(Material m : seeds)
            if(m.equals(material))
                return true;
        return false;
    }

    private boolean isValidDrop(Material material) {
        for(Material m : validDrops)
            if(m.equals(material))
                return true;
        return false;
    }

    private void replacePlant(Block block) {
        ItemStack[] drops = block.getDrops().toArray(new ItemStack[block.getDrops().size()]);
        block.setType(block.getType());
        if(isProblemChild(drops[0].getType())) {
            if(drops.length > 1) {
                if(drops.length > 2) {
                    try {
                        block.getWorld().dropItemNaturally(block.getLocation(), drops[2]);
                    }
                    catch(IllegalArgumentException e) {
                        plugin.getLogger().log(Level.FINE,"Potatoes are problem children.",e);
                    }
                }
                block.getWorld().dropItemNaturally(block.getLocation(), drops[1]);
            }
        }
        else {
            for (ItemStack item : drops) {
                if (isValidDrop(item.getType())) {
                    if (isValidSeed(item.getType())) {
                        item.setAmount(item.getAmount() - 1);
                        if (item.getAmount() > 0)
                            block.getWorld().dropItemNaturally(block.getLocation(), item);
                    }
                    else {
                        block.getWorld().dropItemNaturally(block.getLocation(), item);
                    }
                }
            }
        }
    }
}