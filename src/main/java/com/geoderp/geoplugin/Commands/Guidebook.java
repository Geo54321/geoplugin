package com.geoderp.geoplugin.Commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Utility.Book;
import com.geoderp.geoplugin.Utility.MagnetRequirements;

public class Guidebook implements CommandExecutor {
    private JavaPlugin plugin;

    public Guidebook(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            Book guidebook = fillGuidebook();
            
            if(player.getInventory().firstEmpty() != -1) {
                // Inventory available - create book
                player.getInventory().addItem(guidebook.createBook());
            }
            else {
                // Inventory full error
                player.sendMessage("§cYour inventory is full. Please make space and try again.");
            }
        }
        else {
            sender.sendMessage("§cYou are the console. You don't need a guidebook.");
        }
        return true;
    }

    public Book fillGuidebook() {
        Book book = new Book();

        // config.addDefault("modules.notes", true);
        // config.addDefault("modules.playtime", true);
        // config.addDefault("modules.xp-storage", true);
        // config.addDefault("modules.mechanics", true);
        // config.addDefault("modules.chat-commands", true);
        // config.addDefault("modules.jank", true);
        // config.addDefault("modules.enchantments", true);

        // config.addDefault("options.xp-store-on-death", true);
        // config.addDefault("options.xp-death-percent-high", 1);
        // config.addDefault("options.xp-death-percent-medium", 0.50);
        // config.addDefault("options.xp-death-percent-low", 0.25);
        // config.addDefault("options.strong-magnet-range", 4);
        // config.addDefault("options.weak-magnet-range", 2);
        // MagnetRequirements.validStrongMaterials;
        // MagnetRequirements.validWeakMaterials;

        return book;
    }
}
