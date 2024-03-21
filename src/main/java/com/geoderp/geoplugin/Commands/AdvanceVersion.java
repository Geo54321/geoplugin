package com.geoderp.geoplugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;

import com.geoderp.geoplugin.Utility.NotesDatabase;
import com.geoderp.geoplugin.Utility.XPDatabase;

public class AdvanceVersion implements CommandExecutor {
    public NotesDatabase dbObj;
    public XPDatabase xpDB;
    
    public AdvanceVersion(NotesDatabase dbObj, XPDatabase xpDB) {
        this.dbObj = dbObj;
        this.xpDB = xpDB;
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof ConsoleCommandSender) {
            dbObj.changeVersion();
            xpDB.changeVersion();
        }
        else {
            sender.sendMessage("§cYou are not console. You cannot run this command.");
        }
        return true;
    }
}
