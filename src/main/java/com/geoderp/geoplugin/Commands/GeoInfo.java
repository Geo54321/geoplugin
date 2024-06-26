package com.geoderp.geoplugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class GeoInfo implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage("§2For more info on the custom features you can view them on the MCNSA website at: https://mcnsa.com/custom-features");
        sender.sendMessage("§2For even more info follow the Github links on the page on the MCNSA website.");
        return false;
    }
    
}
