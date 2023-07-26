package com.geoderp.geoplugin.Enchants;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public class Hewing implements Listener {
    JavaPlugin plugin;
    
    public Hewing(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void getBlockBreak(BlockBreakEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item != null) {
            if (item.hasItemMeta()) {
                if (item.getItemMeta().hasEnchant(GeoEnchants.HEWING)) {
                    event.getPlayer().sendMessage("§9This is a valid Hewing tool.");
                }
            }
        }
    }

    @EventHandler
    public void anvilEvent(PrepareAnvilEvent event) {
        ItemStack inOne = event.getInventory().getItem(0);
        ItemStack inTwo = event.getInventory().getItem(1);
        ItemStack result = event.getInventory().getItem(2);
        if (inOne != null && inTwo != null) {
            ItemMeta metaOne = inOne.getItemMeta();
            ItemMeta metaTwo = inTwo.getItemMeta();
            Bukkit.getLogger().info("items exist");
            ItemMeta resultMeta;
            if (result == null) {
                resultMeta = metaOne;
                result = new ItemStack(inOne.getType());
            }
            else {
                resultMeta = result.getItemMeta();
            }
            
            boolean hewing = false;

            if (metaOne != null) {
                if (hasEnchantment(inOne, GeoEnchants.HEWING)) {
                    hewing = true;
                }
            }

            if (metaTwo != null) {
                if (hasEnchantment(inTwo, GeoEnchants.HEWING)) {
                    hewing = true;
                }
            }

            if (hewing) {
                boolean test = false;
                Bukkit.getLogger().info("why tho?");
                if (resultMeta != null) {
                    Bukkit.getLogger().info("aintnoway");
                    if (!hasEnchantment(result, GeoEnchants.HEWING)) {
                        Bukkit.getLogger().info("enchant tho");
                        test = true;
                    }
                    if (resultMeta.getLore() == null) {
                        Bukkit.getLogger().info("no lore tho");
                        ArrayList<String> lore = new ArrayList<String>();
                            lore.add("§2Hewing");
                            resultMeta.setLore(lore);
                    }
                    else {
                        if (!resultMeta.getLore().contains("§2Hewing")) {
                            Bukkit.getLogger().info("the actual mother fucking what");
                            ArrayList<String> lore = new ArrayList<String>();
                            lore.add("§2Hewing");
                            lore.add("§6Forge");
                            lore.add("§cDrain");
                            lore.add("§7Death Woven");
                            for (String l : resultMeta.getLore()) {
                                lore.add(l);
                            }
                            resultMeta.setLore(lore);
                        }
                    }
                }
                result.setItemMeta(resultMeta);
                if (test)
                    result.addUnsafeEnchantment(GeoEnchants.HEWING, 2);
                event.getInventory().setItem(2, result);
                event.setResult(result);
            }
        }
    }

    public boolean hasEnchantment(ItemStack item, Enchantment enchant){
        if(item.getItemMeta() != null && item.getItemMeta().getEnchants() != null && item.getItemMeta().getEnchants().size() > 0){
            for (Iterator<java.util.Map.Entry<Enchantment, Integer>> it = item.getItemMeta().getEnchants().entrySet().iterator(); it.hasNext();) {
                java.util.Map.Entry<Enchantment, Integer> e = it.next();
                if(e.getKey().equals(enchant)){
                    return true;
                }
            }
        }
        return false;
    }
}
