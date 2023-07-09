package com.geoderp.geoplugin.Listeners;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Iterator;

public class Harvest implements Listener {
    private Material[] plants = {Material.WHEAT, Material.POTATOES, Material.CARROTS, Material.BEETROOTS, Material.NETHER_WART};
    private Material[] seeds = {Material.WHEAT_SEEDS, Material.POTATO, Material.CARROT, Material.BEETROOT_SEEDS, Material.NETHER_WART};

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

    private boolean isValidSeed(Material material) {
        for(Material m : seeds)
            if(m.equals(material))
                return true;
        return false;
    }

    private void replacePlant(Block plant) {
        Iterator<ItemStack> drops = plant.getDrops().iterator();
        while (drops.hasNext()) {
            ItemStack drop = drops.next();
            if (isValidSeed(drop.getType())) {
                if (drop.getAmount() > 0) {
                    drop.setAmount(drop.getAmount() - 1);
                }
            }
            plant.getWorld().dropItemNaturally(plant.getLocation(), drop);
            plant.getWorld().playSound(plant.getLocation(), Sound.BLOCK_CROP_BREAK, (float) 0.45, (float) (0.96 + Math.random() * 0.24));
        }
        plant.setType(plant.getType());
    }
}