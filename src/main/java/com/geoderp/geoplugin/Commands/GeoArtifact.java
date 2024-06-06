package com.geoderp.geoplugin.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
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
            sender.sendMessage("§2Types of GeoArtifacts Currently Available: §a" + validArtifacts.toString());
        }
        else if (args.length == 1) {
            if (sender instanceof Player) {
                if (isValidArtifact(args[0])) {
                    if (hasPerms(sender, args[0])) {
                        createArtifact(sender, args, false);
                    }
                    else {
                        sender.sendMessage("§cSorry you do not have permissions to create this type of artifact.");
                    }
                }
            }
            else {
                sender.sendMessage("§cConsole cannot recieve items. Please add a target or join the game.");
            }
        }
        else if (args.length == 2) {
            if (isValidArtifact(args[0]) && isValidTarget(args[1])) {
                if (sender.hasPermission("GeoPlugin.geoartifact.staff")) {
                    if (hasPerms(sender, args[0])) {
                        createArtifact(sender, args, true);
                    }
                    else {
                        sender.sendMessage("§cSorry you do not have permissions to create this type of artifact.");
                    }
                }
                else {
                    sender.sendMessage("§cYou don't have permissions to spawn GeoArtifacts on other players. Using your inventory instead.");
                    createArtifact(sender, args, false);
                }
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

    public boolean hasPerms(CommandSender sender, String type) {
        switch (type) {
            case "magnet":
                if (sender.hasPermission("GeoPlugin.geoartifact.magnet.strong") || sender.hasPermission("GeoPlugin.geoartifact.magnet.weak")) {
                    return true;
                }
                break;
            case "zoomies":
                if (sender.hasPermission("GeoPlugin.geoartifact.zoomies.weak") || sender.hasPermission("GeoPlugin.geoartifact.zoomies.strong") || sender.hasPermission("GeoPlugin.geoartifact.zoomies.giga")) {
                    return true;
                }
                break;
        }
        return false;
    }

    public boolean hasSpace(Player player) {
        if (player.getInventory().firstEmpty() == -1) {
            return false;
        }
        return true;
    }

    public boolean isValidMaterial(ItemStack input, String type) {
        Material item = input.getType();
        if (type.equals("magnet")) {
            for (Material mat : ArtifactRequirements.validStrongMagnetMaterials) {
                if (mat.equals(item)) {
                    return true;
                }
            }
            for (Material mat : ArtifactRequirements.validWeakMagnetMaterials) {
                if (mat.equals(item)) {
                    return true;
                }
            }
        }
        else if (type.equals("zoomies")) {
            for (Material mat : ArtifactRequirements.validZoomiesMaterials) {
                if (mat.equals(item)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void createArtifact(CommandSender sender, String[] args, Boolean other) {
        String artifactType = args[0];
        Player target;
        if (other) {
            target = Bukkit.getPlayer(args[1]);
        }
        else {
            target = (Player) sender;
        }

        switch (artifactType) {
            case "magnet":
                if (other) {
                    if (hasSpace(target)) {
                        spawnMagnet(target, "strong");
                    }
                }
                else {
                    ItemStack offhand = target.getInventory().getItemInOffHand();
                    if (isValidMaterial(offhand, artifactType)) {
                        makeMagnet(offhand);
                    }
                }
                break;
            case "zoomies":
                if (other) {
                    if (hasSpace(target)) {
                        spawnZoomies(target, "giga");
                    }
                }
                else {
                    ItemStack offhand = target.getInventory().getItemInOffHand();
                    if (isValidMaterial(offhand, artifactType)) {
                        makeZoomies(offhand);
                    }
                }
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

    public void spawnZoomies(Player player, String strength) {
        ItemStack zoomies;
        if (strength.equals("giga")) {
            zoomies = new ItemStack(ArtifactRequirements.validStrongMagnetMaterials[2]);
        }
        else if (strength.equals("strong")) {
            zoomies = new ItemStack(ArtifactRequirements.validStrongMagnetMaterials[1]);
        }
        else {
            zoomies = new ItemStack(ArtifactRequirements.validWeakMagnetMaterials[0]);
        }
        
        zoomies.setAmount(1);

        zoomies = makeZoomies(zoomies);
        
        player.getInventory().addItem(zoomies);
    }

    public ItemStack makeZoomies(ItemStack zoomies) {
        ItemMeta zoomiesMeta = zoomies.getItemMeta();

        zoomiesMeta.setDisplayName(ArtifactRequirements.zoomiesName);
        zoomiesMeta.setLore(ArtifactRequirements.zoomiesLore);

        zoomies.addUnsafeEnchantment(Enchantment.KNOCKBACK, 1);
        zoomies.setItemMeta(zoomiesMeta);

        return zoomies;
    }
}
