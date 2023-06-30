package com.geoderp.geoplugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.geoderp.geoplugin.Utility.Database;

public class GeoKeeper implements CommandExecutor {
    public Database dbObj;
    
    public GeoKeeper(Database dbObj) {
        this.dbObj = dbObj;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("GeoPlugin.commands.geokeeper")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                if (args.length > 0) {
                    if (args.length == 1) {
                        // view other
                        player = Bukkit.getPlayer(args[0]);
                        if (player != null) {
                            if (validPlayer(player)) {
                                sender.sendMessage("§2" + player.getName() + " currently has " + getXP(sender, player) + " XP points stored.");
                            } 
                            else {
                                addUser(player, 0);
                                sender.sendMessage("§2" + player.getName() + " currently has " + getXP(sender, player) + " XP points stored.");
                            }
                        }
                        else {
                            sender.sendMessage("§cInvalid target player. Please try again.");
                        }
                    }
                    else if (args.length == 2) {
                        // store or retrive self
                        if (validPlayer(player)) {
                            storeXP(player, args);
                        } 
                        else {
                            storeXP(player, args);
                            sender.sendMessage("§aYou currently have " + getXP(sender, player) + " XP points stored.");
                        }
                    }
                    else if (args.length == 3) {
                        // store or retrieve other
                        player = Bukkit.getPlayer(args[0]);
                        if (player != null) {

                        }
                        else {
                            sender.sendMessage("§cInvalid target player. Please try again.");
                        }
                    }
                    else {
                        sender.sendMessage("§cInvalid number of arguments. Please try again.");
                    }
                }
                else {
                    if (validPlayer(player)) {
                        sender.sendMessage("§aYou currently have " + getXP(sender, player) + " XP points stored.");
                    } 
                    else {
                        addUser(player, 0);
                        sender.sendMessage("§aYou currently have " + getXP(sender, player) + " XP points stored.");
                    }
                }
                return true;
            }
            else {
                // Console version here
            }
        }
        else {
            sender.sendMessage("§cSorry but you do not have permission to use /geokeeper.");
        }
        return false;
    }

    public boolean validPlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (dbObj.selectID("xp","player",uuid) == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public void addUser(Player player, int amount) {
        String uuid = player.getUniqueId().toString();
        dbObj.addUser(uuid, amount);
    }

    public int getXP(CommandSender sender, Player player) {
        String uuid = player.getUniqueId().toString();
        return dbObj.getXP(uuid);
    }

    public void storeXP(Player player, String[] args) {

    }

    public void retrieveXP(Player player, String[] args) {

    }

    public void displayXP(CommandSender sender, int amount) {
        int levels = -1;
    }

    public int getLevels(int amount) {
        int level = -1;

        return level;
    }
}
