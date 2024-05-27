package com.geoderp.geoplugin.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.StringUtil;

import com.geoderp.geoplugin.Utility.ArtifactRequirements;

public class GeoArtifact implements CommandExecutor, TabCompleter {
    private ArrayList<String> validArtifacts = new ArrayList<String>();

    public GeoArtifact() {
        validArtifacts.add("magnet");
        validArtifacts.add("zoomies");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage("§2Types of GeoArtifacts Currently Available: §amagnet, zoomies");
        }
        else if (args.length == 1) {
            if (sender instanceof Player) {
                if (isValidArtifact(args[0])) {
                    createArtifact(sender, args, false);
                }
            }
            else {
                sender.sendMessage("§cConsole cannot recieve items. Please add a target or join the game.");
            }
        }
        else if (args.length == 2) {
            if (isValidArtifact(args[0]) && isValidTarget(args[1])) {
                createArtifact(sender, args, true);
            }
        }
        else {
            sender.sendMessage("§cInvalid number of arguments. Please try again.");
        }
        
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> names = new ArrayList<String>();
        for (Player p : Bukkit.getOnlinePlayers()) {
            names.add(p.getName());
        }
        List<String> completions = new ArrayList<String>();
        
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], validArtifacts, completions);
            return completions;
        }
        else if (args.length == 2) {
            StringUtil.copyPartialMatches(args[1], names, completions);
            return completions;
        }
        else {
            return null;
        }
    }

    public boolean isValidArtifact(String input) {
        for (String artifact : validArtifacts) {
            if (artifact.equals(input)) {
                return true;
            }
        }
        return false;
    }

    public boolean isValidTarget(String input) {
        for (Player p : Bukkit.getOnlinePlayers()) {
            if (p.getName().equals(input)) {
                return true;
            }
        }
        return false;
    }

    public void createArtifact(CommandSender sender, String[] args, Boolean other) {
        String artifactType = args[0];
        String name;
        if (other) {
            name = args[1];
        }
        else {
            name = sender.getName();
        }
        Player target = Bukkit.getPlayer(name);

        if (target.getInventory().firstEmpty() == -1) {
            sender.sendMessage("");
        }

        switch (artifactType) {
            case "magnet":

                break;
            case "zoomies":
                
                break;
        }
    }

    public void spawnMagnet(Player player, String strength) {
        ItemStack magnet;
        if (strength.equals("strong")) {
            magnet = new ItemStack(ArtifactRequirements.validStrongMagnetMaterials[0]);
        }
        else {
            magnet = new ItemStack(ArtifactRequirements.validWeakMagnetMaterials[0]);
        }
        
        magnet.setAmount(1);

        magnet = makeMagnet(magnet);
        
        player.getInventory().addItem(magnet);
    }

    public ItemStack makeMagnet(ItemStack magnet) {
        ItemMeta magnetMeta = magnet.getItemMeta();

        magnetMeta.setDisplayName(ArtifactRequirements.magnetName);
        magnetMeta.setLore(ArtifactRequirements.magnetLore);

        magnet.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        magnet.setItemMeta(magnetMeta);

        return magnet;
    }
}
