package com.geoderp.geoplugin.Commands;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.geoderp.geoplugin.Enchants.GeoEnchants;

public class Please implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack hand = player.getInventory().getItemInMainHand();
                hand.addUnsafeEnchantment(GeoEnchants.HEWING, 1);
                ItemMeta meta = hand.getItemMeta();
                ArrayList<String> lore = new ArrayList<String>();
                lore.add("§5Hewing");
                if (meta.hasLore()) {
                    for (String l : meta.getLore()) {
                        lore.add(l);
                    }
                }
                meta.setLore(lore);
                hand.setItemMeta(meta);
                player.getInventory().setItemInMainHand(hand);
            }
        }
        else {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                ItemStack item = new ItemStack(Material.NETHERITE_AXE);
                item.addUnsafeEnchantment(GeoEnchants.HEWING, 2);
                ItemMeta meta = item.getItemMeta();
                ArrayList<String> lore = new ArrayList<String>();
                lore.add("§5Hewing");
                meta.setLore(lore);
                item.setItemMeta(meta);
                player.getInventory().addItem(item);
            }
        }
        return true;
    }
}
