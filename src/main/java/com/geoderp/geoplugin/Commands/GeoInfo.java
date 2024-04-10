package com.geoderp.geoplugin.Commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

import com.geoderp.geoplugin.Utility.MagnetRequirements;

public class GeoInfo implements CommandExecutor, TabCompleter {
    private JavaPlugin plugin;
    private ArrayList<String> topics = new ArrayList<String>();
    private ArrayList<String> staffTopics = new ArrayList<String>();
    private double[] storePercents = new double[3];
    private int[] magnetRanges = new int[2];
    private Material[] strongMaterials;
    private Material[] weakMaterials;
    private double bonemealChance;
    private boolean loginNotes;

    public GeoInfo(JavaPlugin plugin) {
        this.plugin = plugin;
        generateTopicList();
    }

    public void generateTopicList() {
        // GeoKeeper
        if(plugin.getConfig().getBoolean("modules.xp-storage")) {
            topics.add("geokeeper");
            staffTopics.add("geokeeper-staff");
            if(plugin.getConfig().getBoolean("options.xp-store-on-death")) {
                topics.add("geokeeper-death");
                this.storePercents[0] = plugin.getConfig().getDouble("options.xp-death-percent-high");
                this.storePercents[1] = plugin.getConfig().getDouble("options.xp-death-percent-medium");
                this.storePercents[2] = plugin.getConfig().getDouble("options.xp-death-percent-low");
            }
            
        }

        // Mechanics
        if(plugin.getConfig().getBoolean("modules.mechanics")) {
            // Features
            topics.add("magnet");
            staffTopics.add("magnet-staff");
            topics.add("right-click-harvest");
            topics.add("duplicate-flora");

            // Feature Specifics
            this.magnetRanges[0] = plugin.getConfig().getInt("options.strong-magnet-range");
            this.magnetRanges[1] = plugin.getConfig().getInt("options.weak-magnet-range");
            this.strongMaterials = MagnetRequirements.validStrongMaterials;
            this.weakMaterials = MagnetRequirements.validWeakMaterials;
            this.bonemealChance = plugin.getConfig().getDouble("options.growth-chance-percent");
        }

        if(plugin.getConfig().getBoolean("modules.chat-commands")) {
            topics.add("chat-commands");
            staffTopics.add("chat-commands-staff");
        }

        if(plugin.getConfig().getBoolean("modules.notes")) {
            staffTopics.add("notes");
            staffTopics.add("login-notes");
            this.loginNotes = plugin.getConfig().getBoolean("options.login-notes");
        }

        if(plugin.getConfig().getBoolean("modules.playtime")) {
            topics.add("playtime");
            if(plugin.getConfig().getBoolean("options.login-playtime")) {
                staffTopics.add("login-playtime");
            }
        }

        if(plugin.getConfig().getBoolean("modules.enchantmants")) {
            staffTopics.add("enchants");
        }

        if(plugin.getConfig().getBoolean("modules.jank")) {
            staffTopics.add("jank");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        ArrayList<String> topicList = topics;
        List<String> completions = new ArrayList<String>();
        if (sender.hasPermission("GeoPlugin.commands.geoinfo.staff")) {
            topicList.addAll(staffTopics);
        }
        
        if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], topicList, completions);
            return completions;
        }
        else {
            return null;
        }
    }

    public String parseInput(String input) {
        // Accept input for specific commands and route to the given topic

        return "";
    }

    public void info(String topic) {
        // Outputs the information on the given topic


        // Player topics:
        //      geokeeper
        //      geokeeper-death
        //      magnet
        //      right-click-harvest
        //      duplicate-flora
        //      chat-commands
        //      playtime
        // Staff topics:
        //      geokeeper-staff
        //      magnet-staff
        //      chat-commands-staff
        //      notes
        //      login-notes
        //      login-playtime
        //      enchants
        //      jank
    }
}
