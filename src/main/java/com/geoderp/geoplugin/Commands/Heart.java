package com.geoderp.geoplugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class Heart implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("GeoPlugin.commands.heart")) {
            if(sender instanceof Player) {
                Player player = (Player) sender;
                if(args.length > 0)
                    Bukkit.broadcastMessage("§c" + player.getDisplayName() + "§d wants to let §c" + args[0] + "§d know they love them. §4<3§r");
                else
                    Bukkit.broadcastMessage("§c" + player.getDisplayName() + "§d wants to let everyone know they love them. §4<3§r");
            }
            else if(sender instanceof ConsoleCommandSender) {
                if(args.length > 0)
                    Bukkit.broadcastMessage("§4The Server §dwants to let §c" + args[0] + "§d know they love them. §4<3§r");
                else
                    Bukkit.broadcastMessage("§4The Server §dwants to let everyone know they love them. §4<3§r");
            }
        }
        else {
            sender.sendMessage("§cSorry but you do not have permission to use the /heart command.");
        }
        return true;
    }
}
