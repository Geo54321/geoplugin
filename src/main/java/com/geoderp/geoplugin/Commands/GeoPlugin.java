package com.geoderp.geoplugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Utility.MagnetRequirements;

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
            else if (args[0].equals("magnet") && plugin.getConfig().getBoolean("modules.mechanics")) {
                if (sender.hasPermission("GeoPlugin.commands.geoplugin.magnet") && sender instanceof Player) {
                    Player player = (Player) sender;
                    spawnMagnet(player);
                }
                else if (sender instanceof ConsoleCommandSender) {
                    sender.sendMessage("§cYou must be in game to use this command.");
                }
                else {
                    sender.sendMessage("§cYou must be in game to use this command.");
                }
            }
            return true;
        }
        else {
            sender.sendMessage("§cSorry but you do not have permission to spawn a magnet item");
        }
        return false;
    }

    public void reloadConfig(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage("§2Config file reloaded!");
    }

    public void spawnMagnet(Player player) {
        ItemStack magnet = new ItemStack(MagnetRequirements.validMaterials[0]);
        magnet.setAmount(1);
        magnet.getItemMeta().setDisplayName(MagnetRequirements.name);
        magnet.getItemMeta().setLore(MagnetRequirements.loreList);
        magnet.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        player.getInventory().addItem(magnet);
    }
}
