package com.geoderp.geoplugin.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import com.geoderp.geoplugin.Utility.Database;
import com.geoderp.geoplugin.Utility.XP;

public class GeoKeeper implements CommandExecutor, TabCompleter {
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
                            sender.sendMessage("§2" + player.getName() + " currently has " + getStoredXP(player) + " XP points stored.");
                        }
                        else {
                            sender.sendMessage("§cInvalid target player. Please try again.");
                        }
                    }
                    else if (args.length == 2) {
                        // store or retrive self
                        if (args[0].equals("retrieve")) {
                            retrieveXP(player, args);
                            sender.sendMessage("§aYou currently have " + getStoredXP(player) + " XP points stored.");
                        }
                        else if (args[0].equals("store")) {
                            storeXP(player, args);
                            sender.sendMessage("§aYou currently have " + getStoredXP(player) + " XP points stored.");
                        }
                        else {
                            sender.sendMessage("§cInvalid subcommand, use store or retrieve.");
                        }
                    }
                    else if (args.length == 3) {
                        // store or retrieve other
                        if (sender.hasPermission("GeoPlugin.commands.geokeeper.others")) {
                            player = Bukkit.getPlayer(args[2]);
                            if (player != null) {
                                if (args[0].equals("retrieve")) {
                                    retrieveXP(player, args);
                                    sender.sendMessage("§a" + player.getName() + " currently have " + getStoredXP(player) + " XP points stored.");
                                }
                                else if (args[0].equals("store")) {
                                    storeXP(player, args);
                                    sender.sendMessage("§a" + player.getName() + "You currently have " + getStoredXP(player) + " XP points stored.");
                                }
                                else {
                                    sender.sendMessage("§cInvalid subcommand, use store or retrieve.");
                                }
                            }
                            else {
                                sender.sendMessage("§cInvalid target player. Please try again.");
                            }
                        }
                        else {
                            sender.sendMessage("§cYou do not have permission to edit other player's stored XP.");
                        }
                    }
                    else {
                        sender.sendMessage("§cInvalid number of arguments. Please try again.");
                    }
                }
                else {
                    sender.sendMessage("§aYou currently have " + getStoredXP(player) + " XP points stored.");
                }
            }
            else {
                if (args.length == 1) {
                    // view other
                    Player player = Bukkit.getPlayer(args[0]);
                    if (player != null) {
                        sender.sendMessage("§2" + player.getName() + " currently has " + getStoredXP(player) + " XP points stored.");
                    }
                    else {
                        sender.sendMessage("§cInvalid target player. Please try again.");
                    }
                }
                else if (args.length == 3) {
                    // store or retrieve other
                    Player player = Bukkit.getPlayer(args[2]);
                    if (player != null) {
                        if (args[0].equals("retrieve")) {
                            retrieveXP(player, args);
                            sender.sendMessage("§a" + player.getName() + " currently have " + getStoredXP(player) + " XP points stored.");
                        }
                        else if (args[0].equals("store")) {
                            storeXP(player, args);
                            sender.sendMessage("§a" + player.getName() + "You currently have " + getStoredXP(player) + " XP points stored.");
                        }
                        else {
                            sender.sendMessage("§cInvalid subcommand, use store or retrieve.");
                        }
                    }
                    else {
                        sender.sendMessage("§cInvalid target player. Please try again.");
                    }
                }
                else {
                    sender.sendMessage("§cInvalid number of arguments. Please try again.");
                }
            }
            return true;
        }
        else {
            sender.sendMessage("§cSorry but you do not have permission to store XP.");
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> subCommands = Arrays.asList("store", "retrieve");
        List<String> emptyList = Arrays.asList("");
        if(args.length > 1)
            return Collections.emptyList();
        else if(args.length > 0)
            return StringUtil.copyPartialMatches(args[0], subCommands, emptyList);
        else
            return null;
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

    public int getStoredXP(Player player) {
        if (!validPlayer(player)) {
            addUser(player, 0);
        }

        String uuid = player.getUniqueId().toString();
        return dbObj.getXP(uuid);
    }

    public void storeXP(Player player, String[] args) {
        if (!validPlayer(player)) {
            addUser(player, 0);
        }

        if (args[1].equals("all")) {
            storeAllLevel(player);
        }
        else {
            storeSomeLevels(player, args[1]);
        }
        
    }

    public void storeAllLevel(Player player) {
        int stored = getStoredXP(player);
        int current = XP.getXPOfLevel(player.getLevel());
        current += player.getExp() * (XP.getXPOfLevel(player.getLevel()+1) - XP.getXPOfLevel(player.getLevel()));
        
        dbObj.updateXP(stored+current, player.getUniqueId().toString());
        XP.setPlayerXP(player, 0);

        player.sendMessage("§2Successfully stored all stored levels!");
    }

    public void storeSomeLevels(Player player, String levelArg) {
        try {
            int levelToStore = Integer.parseInt(levelArg);
            int xpToStore = XP.getXPOfLevel(player.getLevel()) - XP.getXPOfLevel(player.getLevel()-levelToStore);
            int stored = getStoredXP(player);
            int current = XP.getXPOfLevel(player.getLevel());
            current += player.getExp() * (XP.getXPOfLevel(player.getLevel()+1) - XP.getXPOfLevel(player.getLevel()));

            if (levelToStore > player.getLevel()) {
                dbObj.updateXP(stored+current, player.getUniqueId().toString());
                XP.setPlayerXP(player, 0);

                player.sendMessage("§6You do not have " + levelToStore + " levels to store. Storing all XP.");
            }
            else {
                dbObj.updateXP(stored+xpToStore, player.getUniqueId().toString());
                XP.setPlayerXP(player, current-xpToStore);

                player.sendMessage("§2Successfully stored "+ levelToStore +" levels!");
            }

            
        }
        catch (Exception e) {
            player.sendMessage("§cIncorrect parameter, either use 'all' or an integer number of levels.");
        }
    }

    public void retrieveXP(Player player, String[] args) {
        if (!validPlayer(player)) {
            addUser(player, 0);
        }
        
        if (args[1].equals("all")) {
            retrieveAllLevels(player);
        }
        else {
            retrieveSomeLevels(player, args[1]);
        }
    }

    public void retrieveAllLevels(Player player) {
        int stored = getStoredXP(player);
        int current = XP.getXPOfLevel(player.getLevel());
        current += player.getExp() * (XP.getXPOfLevel(player.getLevel()+1) - XP.getXPOfLevel(player.getLevel()));

        XP.setPlayerXP(player, current+stored);
        dbObj.updateXP(0, player.getUniqueId().toString());
        
        player.sendMessage("§2Successfully retrieved all stored levels!");
    }

    public void retrieveSomeLevels(Player player, String levelArg) {
        try {
            int levelToPull = Integer.parseInt(levelArg);
            int xpToPull = XP.getXPOfLevel(player.getLevel()+levelToPull) - XP.getXPOfLevel(player.getLevel());
            int stored = getStoredXP(player);
            int current = XP.getXPOfLevel(player.getLevel());
            current += player.getExp() * ((XP.getXPOfLevel(player.getLevel()+1) - XP.getXPOfLevel(player.getLevel())));
            
            if (xpToPull > stored) {
                XP.setPlayerXP(player, current+stored);
                dbObj.updateXP(0, player.getUniqueId().toString());

                player.sendMessage("§6You did not have enough XP stored to retrieve "+ levelToPull +" levels. Retrieved all stored XP.");
            }
            else {
                XP.setPlayerXP(player, current+xpToPull);
                dbObj.updateXP(stored-xpToPull, player.getUniqueId().toString());

                player.sendMessage("§2Successfully retrieved "+ levelToPull +" levels!");
            }
        }
        catch (Exception e) {
            player.sendMessage("§cIncorrect parameter, either use 'all' or an integer number of levels.");
        }
    }
}
