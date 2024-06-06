package com.geoderp.geoplugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Utility.ArtifactRequirements;

public class GeoPlugin implements CommandExecutor {
    JavaPlugin plugin;

    public GeoPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("GeoPlugin.commands.geoplugin")) {
            if (args[0].equals("reload")) {
                if (sender.hasPermission("GeoPlugin.commands.geoplugin.reload")) {
                    reloadConfig(sender);
                }
                else {
                    sender.sendMessage("§cSorry but you do not have permission to reload the GeoPlugin config.");
                }
            }
        //     else if (args[0].equals("magnet") && plugin.getConfig().getBoolean("modules.mechanics")) {
        //         if (sender.hasPermission("GeoPlugin.commands.geoplugin.magnet") && sender instanceof Player) {
        //             Player player = (Player) sender;
        //             if (args.length == 1 && player.getInventory().getItemInMainHand().hasItemMeta()) {
        //                 ItemStack item = player.getInventory().getItemInMainHand();
        //                 if (item != null) {
        //                     item = makeMagnet(item);
        //                 }
        //             }
        //             else if (args.length > 1) {
        //                 if (args[1].equals("strong") || args[1].equals("weak")) {
        //                     spawnMagnet(player, args[1]);
        //                 }
        //                 else {
        //                     sender.sendMessage("§cYou must specify the magnet strength.");
        //                 }
        //             }
        //         }
        //         else if (sender instanceof ConsoleCommandSender) {
        //             sender.sendMessage("§cYou must be in game to use this command.");
        //         }
        //         else {
        //             sender.sendMessage("§cYou do not have permission to create a magnet.");
        //         }
        //     }
        //     return true;
        // }
        // else {
        //     sender.sendMessage("§cSorry but you do not have permission to spawn a magnet item");
        }
        return false;
    }

    public void reloadConfig(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage("§2Config file reloaded!");
    }

    // public void spawnMagnet(Player player, String strength) {
    //     ItemStack magnet;
    //     if (strength.equals("strong")) {
    //         magnet = new ItemStack(ArtifactRequirements.validStrongMagnetMaterials[0]);
    //     }
    //     else {
    //         magnet = new ItemStack(ArtifactRequirements.validWeakMagnetMaterials[0]);
    //     }
        
    //     magnet.setAmount(1);

    //     magnet = makeMagnet(magnet);
        
    //     player.getInventory().addItem(magnet);
    // }

    // public ItemStack makeMagnet(ItemStack magnet) {
    //     ItemMeta magnetMeta = magnet.getItemMeta();

    //     magnetMeta.setDisplayName(ArtifactRequirements.magnetName);
    //     magnetMeta.setLore(ArtifactRequirements.magnetLore);

    //     magnet.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
    //     magnet.setItemMeta(magnetMeta);

    //     return magnet;
    // }
}
