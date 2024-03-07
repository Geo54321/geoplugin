package com.geoderp.geoplugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import com.geoderp.geoplugin.Utility.NotesDatabase;

public class Playtime implements CommandExecutor {
    public NotesDatabase dbObj;
    
    public Playtime(NotesDatabase dbObj) {
        this.dbObj = dbObj;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        return true;
    }
}
