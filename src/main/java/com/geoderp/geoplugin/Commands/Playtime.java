package com.geoderp.geoplugin.Commands;

import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.geoderp.geoplugin.Utility.NotesDatabase;

public class Playtime implements CommandExecutor {
    public NotesDatabase dbObj;
    
    public Playtime(NotesDatabase dbObj) {
        this.dbObj = dbObj;
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0) {
            if (args.length == 1) {
                if (args[0].equals("top")) {
                    // display top playtimes
                    for(Player player : Bukkit.getOnlinePlayers()) {
                        updateTime(player.getUniqueId().toString());
                    }
                    displayTop(sender);
                }
                else {
                    // view other playtime
                    Player player = Bukkit.getPlayer(args[0]);
                    if (player != null) {
                        String uuid = player.getUniqueId().toString();
                        if(player.isOnline()) {
                            updateTime(uuid);
                        }
                        displayTime(sender, uuid);
                    }
                    else {
                        try {
                            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
                            String uuid = p.getUniqueId().toString();
                            displayTime(sender, uuid);
                        }
                        catch (Exception e) {
                            sender.sendMessage("§cInvalid player, make sure you typed the username correctly.");
                        }
                    }
                }
            }
            else {
                sender.sendMessage("§cInvalid number of arguments. Please try again.");
            }
        }
        else {
            if(sender instanceof Player) {
                // view self playtime
                Player player = (Player) sender;
                String uuid = player.getUniqueId().toString();
                updateTime(uuid);
                displayTime(sender, uuid);
            }
            else {
                sender.sendMessage("§cYou are a console. You can't play Minecraft. :)");
            }
        }
        return true;
    }

    public void updateTime(String uuid) {
        long now = System.currentTimeMillis();
        long then = dbObj.getPlaytime(uuid, "lastseen");

        dbObj.updatePlaytime(uuid, now-then, now);
    }

    public void displayTime(CommandSender sender, String uuid) {
        long current = dbObj.getPlaytime(uuid, "current");
        long total = dbObj.getPlaytime(uuid, "previous") + current;
        long[] times = dbObj.convertTime(total);
        String name = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();

        sender.sendMessage("§5 --- " + name + "'s Playtime ---");
        sender.sendMessage("§5Total: §d" + times[0] + "d " + times[1] + "h " + times[2] + "m");
        times = dbObj.convertTime(current);
        sender.sendMessage("§5Current Map: §d" + times[0] + "d " + times[1] + "h " + times[2] + "m");
    }

    public void displayTop(CommandSender sender) {
        ArrayList<String[]> topTop = dbObj.getTopTotal();
        ArrayList<String[]> currentTop = dbObj.getTopCurrent();
        
        sender.sendMessage("§5 --- Top Overall Playtime ---");
        int count = 1;
        for (String[] entry : topTop) {
            String name = Bukkit.getOfflinePlayer(UUID.fromString(entry[0])).getName();
            long[] times = dbObj.convertTime(Long.parseLong(entry[1]));
            sender.sendMessage("§d" + count + ". " + name + ": " + times[0] + "d " + times[1] + "h " + times[2] + "m");
            count++;
        }
        sender.sendMessage("");
        sender.sendMessage("§5 --- Top Current Playtime ---");
        count = 1;
        for (String[] entry : currentTop) {
            String name = Bukkit.getOfflinePlayer(UUID.fromString(entry[0])).getName();
            long[] times = dbObj.convertTime(Long.parseLong(entry[1]));
            sender.sendMessage("§d" + count + ". " + name + ": " + times[0] + "d " + times[1] + "h " + times[2] + "m");
            count++;
        }
    }

    
}
