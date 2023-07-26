package com.geoderp.geoplugin.Commands;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Promotion implements CommandExecutor {
    private List<String> ranks = Arrays.asList("Mu","Kappa","Zeta","Delta","Phi","Gamma","Psi","Omega");

    public Promotion() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender.hasPermission("GeoPlugin.commands.promotion")) {
            if (args.length > 1) {
                if (isValidArgs(args)) {
                    doPromote(args);
                    sender.sendMessage("§6Don't forget to make a note of this promotion, if you haven't already. <3");
                }
            }
            else {
                sender.sendMessage("§cInvalid number of arguments. Please try again.");
            }
        }
        return true;
    }

    public boolean isValidArgs(String[] args) {
        boolean validName = false;
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getName().equals(args[0])) {
                validName = true;
            }
        }

        boolean validRank = false;
        for (String rank : ranks) {
            if (rank.equals(args[1])) {
                validRank = true;
            }
        }

        return validName && validRank;
    }

    public void doPromote(String[] args) {
        Player target = Bukkit.getPlayer(args[0]);
        String color = getRankColorCode(args[1]);
        String rank = args[1];

        target.sendTitle(color + "Congratulations!", "§7You have been promoted to " + color + rank + "§7.", 4, 40, 4);
        Bukkit.broadcastMessage("§3Congrats " + color + target.getName() + "§3 on getting promoted to " + color + rank + "§3!");
        fireworks(target);
    }

    public String getRankColorCode(String rank) {
        switch (rank) {
            case "Mu":
                return "§c";
            case "Kappa":
                return "§6";
            case "Zeta":
                return "§e";
            case "Delta":
                return "§2";
            case "Phi":
                return "§5";
            case "Gamma":
                return "§9";
            case "Psi":
                return "§b";
            case "Omega":
                return "§a";
            default:
                return "§4";
        }
    }

    public void fireworks(Player target) {
        
    }
}
