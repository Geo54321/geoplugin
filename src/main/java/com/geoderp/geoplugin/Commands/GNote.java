package com.geoderp.geoplugin.Commands;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import com.geoderp.geoplugin.Utility.Database;

import java.util.ArrayList;
import java.time.LocalDate;
import java.util.UUID;

public class GNote implements CommandExecutor{
    public Database dbObj;
    
    public GNote(Database dbObj) {
        this.dbObj = dbObj;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0) {
            if(args.length > 1) {
                if(args[0].equals("remove")) {
                    removeNote(sender, args[1]);
                } else {
                    addNote(sender, args);
                }
            }
            else {
                if(args[0].equals("recent")) {
                    viewRecent(sender);
                }
                else {
                    viewNotes(sender, args[0]);
                }
            }
            return true;
        }
        return false;
    }

    public String getUUID(CommandSender sender, String name) {
        String uuid = "";
        try {
            OfflinePlayer p = Bukkit.getOfflinePlayer(name);
            uuid = p.getUniqueId().toString();
        }
        catch (Exception e) {
            message(sender, "§cInvalid player, make sure you typed the username correctly.");
        }
        return uuid;
    }

    public void addNote(CommandSender sender, String[] args) {
        String note = "";
        LocalDate date = LocalDate.now();
        String dateString = date.toString();
        for(int g = 1; g < args.length; g++) {
            note += args[g].trim().strip() + " ";
        }
        String targetuuid = getUUID(sender, args[0].trim().strip());
        String senderuuid = sender.getName();
        if (sender instanceof Player) {
            Player p = (Player) sender;
            senderuuid = p.getUniqueId().toString();
        }
        if (!targetuuid.equals("")) {
            dbObj.insert("notes", senderuuid, dateString, targetuuid, note);
            int noteID = dbObj.selectID("notes", "creator", senderuuid);
            message(sender,"§2Note #" + noteID + " for "+ args[0].trim().strip() +" successfully created!");
        }
    }

    public void viewNotes(CommandSender sender, String player) {
        String uuid = getUUID(sender, player);
        ArrayList<String[]> output = dbObj.selectAll("notes", "target", uuid);

        if(output.size() > 0) {
            message(sender,"§3===== "+player+"'s notes =====");
            for(int g = 0; g < output.size(); g++) {
                String creator = output.get(g)[1];
                if (!creator.equals("CONSOLE")) {
                    creator = Bukkit.getOfflinePlayer(UUID.fromString(creator)).getName();
                }

                message(sender, "§3Note ID: §7" + output.get(g)[0]);
                message(sender, "§3Author: §7" + creator);
                message(sender, "§3Date: §7" + output.get(g)[2]);
                message(sender, "§3Note: §f" + output.get(g)[4]);
                message(sender, "");
            }
        }
        else {
            message(sender, "§6"+player+" does not have any notes yet.");
        }
    }

    public void removeNote(CommandSender sender, String IDString) {
        try {
            int id = Integer.parseInt(IDString.trim().strip());
            if(dbObj.selectID("notes", "id", IDString) < 0) {
                message(sender, "§cNote #" + id + " does not exist.");
            }
            else {
                dbObj.remove("notes", id);
                message(sender, "§6Note ID " + id + " has been removed.");
            }
        }
        catch(Exception e) {
            message(sender, "§cNot a valid integer input.");
        }
    }

    public void viewRecent(CommandSender sender) {
        ArrayList<String[]> output = dbObj.selectRecent("notes");
        message(sender,"§b===== 5 most recent notes =====");
        for(int g = output.size()-1; g >= 0; g--) {
            String creator = output.get(g)[1].trim().strip();
            if (!creator.equals("CONSOLE")) {
                creator = Bukkit.getOfflinePlayer(UUID.fromString(creator)).getName();
            }
            String target = output.get(g)[3].trim().strip();
            target = Bukkit.getOfflinePlayer(UUID.fromString(target)).getName();

            message(sender, "§bNote ID: §7" + output.get(g)[0]);
            message(sender, "§bPlayer: §7" + target);
            message(sender, "§bAuthor: §7" + creator);
            message(sender, "§bDate: §7" + output.get(g)[2]);
            message(sender, "§bNote: §f" + output.get(g)[4]);
            message(sender,"");
        }
    }

    public void message(CommandSender sender, String message) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            player.sendMessage(message);
        }
        else if(sender instanceof ConsoleCommandSender) {
            Bukkit.getServer().getConsoleSender().sendMessage(message);
        }
    }
}
