package com.geoderp.geoplugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

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
        }
        return false;
    }

    public void reloadConfig(CommandSender sender) {
        plugin.reloadConfig();
        sender.sendMessage("§2Config file reloaded!");
    }
}
