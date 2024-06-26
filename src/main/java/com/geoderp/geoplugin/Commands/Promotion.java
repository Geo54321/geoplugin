package com.geoderp.geoplugin.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.util.StringUtil;

public class Promotion implements CommandExecutor, TabCompleter {
    private List<String> ranks = Arrays.asList("mu","kappa","zeta","delta","phi","gamma","psi","omega");

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
        else {
            sender.sendMessage("§cSorry but you do not have permission to use the /promotion command.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> players = new ArrayList<String>();
        for(Player player : Bukkit.getOnlinePlayers()) {
            players.add(player.getName().toString());
        }
        
        List<String> completions = new ArrayList<String>();

        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], players, completions);
            return completions;
        }
        else if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], ranks, completions);
            return completions;
        }
        else {
            return null;
        }
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
        fireworks(args[1], target);
    }

    public String getRankColorCode(String rank) {
        switch (rank) {
            case "mu":
                return "§c";
            case "kappa":
                return "§6";
            case "zeta":
                return "§e";
            case "delta":
                return "§2";
            case "phi":
                return "§5";
            case "gamma":
                return "§9";
            case "psi":
                return "§b";
            case "omega":
                return "§a";
            default:
                return "§4";
        }
    }

    public void fireworks(String rank, Player player) {
        Location target = player.getLocation();
        Firework main = (Firework) target.getWorld().spawnEntity(target, EntityType.FIREWORK_ROCKET);
        FireworkMeta meta = main.getFireworkMeta();
        meta.clearEffects();
        meta.setPower(1);

        switch (rank) {
            case "mu":
                meta.addEffect(FireworkEffect.builder().withColor(Color.RED).flicker(true).trail(true).build());
                break;
            case "kappa":
                meta.addEffect(FireworkEffect.builder().withColor(Color.ORANGE).flicker(true).trail(true).build());
                break;
            case "zeta":
                meta.addEffect(FireworkEffect.builder().withColor(Color.YELLOW).flicker(true).trail(true).build());
                break;
            case "delta":
                meta.addEffect(FireworkEffect.builder().withColor(Color.GREEN).flicker(true).trail(true).build());
                break;
            case "phi":
                meta.addEffect(FireworkEffect.builder().withColor(Color.PURPLE).flicker(true).trail(true).build());
                break;
            case "gamma":
                meta.addEffect(FireworkEffect.builder().withColor(Color.BLUE).flicker(true).trail(true).build());
                break;
            case "psi":
                meta.addEffect(FireworkEffect.builder().withColor(Color.AQUA).flicker(true).trail(true).build());
                break;
            case "omega":
                meta.addEffect(FireworkEffect.builder().withColor(Color.LIME).flicker(true).trail(true).build());
                break;
            default:
                meta.addEffect(FireworkEffect.builder().withColor(Color.WHITE).flicker(true).trail(true).build());
                break;
        }
        main.setFireworkMeta(meta);

        target.setX(target.getX()+2);
        Firework left = (Firework) target.getWorld().spawnEntity(target, EntityType.FIREWORK_ROCKET);
        target.setX(target.getX()-4);
        Firework right = (Firework) target.getWorld().spawnEntity(target, EntityType.FIREWORK_ROCKET);
        target.setX(target.getX()+2);
        target.setZ(target.getZ()+2);
        Firework forward = (Firework) target.getWorld().spawnEntity(target, EntityType.FIREWORK_ROCKET);
        target.setZ(target.getZ()-4);
        Firework backward = (Firework) target.getWorld().spawnEntity(target, EntityType.FIREWORK_ROCKET);

        left.setFireworkMeta(meta);
        right.setFireworkMeta(meta);
        forward.setFireworkMeta(meta);
        backward.setFireworkMeta(meta);
    }
}
