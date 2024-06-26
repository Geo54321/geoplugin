package com.geoderp.geoplugin.Commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.StringUtil;

public class GeoInfo implements CommandExecutor, TabCompleter {
    private JavaPlugin plugin;
    private ArrayList<String> topics = new ArrayList<String>();
    private ArrayList<String> staffTopics = new ArrayList<String>();
    private double[] storePercents = new double[3];
    private int[] magnetRanges = new int[2];
    private Material[] strongMaterials;
    private Material[] weakMaterials;
    private boolean magnetSneak;
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
            //this.strongMaterials = ArtifactRequirements.validStrongMagnetMaterials;
            //this.weakMaterials = ArtifactRequirements.validWeakMagnetMaterials;
            this.magnetSneak = plugin.getConfig().getBoolean("options.sneak-disable-magnet");
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean staff = false;
        if (sender instanceof ConsoleCommandSender || sender.hasPermission("GeoPlugin.notes.moderator") || sender.hasPermission("GeoPlugin.commands.geokeeper.others")) {
            staff = true;
        }
        if (args.length == 0) {
            // No topic - show topics list
            sender.sendMessage("§2Available Topics: ");
            String topicStr = String.join(", ", topics);
            sender.sendMessage("§a" + topicStr);

            if (staff) {
                // Staff topics
                sender.sendMessage("§9Staff Topics: ");
                String staffStr = String.join(", ", staffTopics);
                sender.sendMessage("§b" + staffStr);
            }
        }
        if (args.length > 0) {
            // Has topic - display info
            String chosenTopic = parseInput(args[0]);
            if (chosenTopic.equals("tryagain")) {
                // Topic not valid - display error
                sender.sendMessage("§cThat is not an available topic. Please enter a valid topic.");
            }
            else {
                // Topic valid - display info
                sender.sendMessage(info(chosenTopic));
            }
        }
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
        if (topics.contains(input)) {
            return input;
        }
        else {
            // TODO - route commands to their relevant help page
        }
        return "tryagain";
    }

    public String info(String topic) {
        // Outputs the information on the given topic
        String message = "";

        switch (topic) {
            case "geokeeper":
                message = "GeoKeeper is a chat command based XP storage. It allows you to store and retrieve XP so it is not lost on death. \nCommand Usage: \nView Stored XP - /geokeeper \nStore XP - /geokeeper store [all/# of levels] \nRetrieve XP - /geokeeper retrieve [all/# of levels]";
                break;
            case "geokeeper-death":
                message = "GeoKeeper has a module that will automatically store part or all of your XP on death. The storage percentages are: ";
                message += "\nLow Percentage - " + storePercents[2] + "%";
                message += "\nMedium Percentage - " + storePercents[1] + "%";
                message += "\nHigh Percentage - " + storePercents[0] + "%";
                break;
            case "magnet":
                message = "This plugin adds magnets that when held in your off hand will increase your pickup range for both items and XP. ";
                if (magnetSneak) {
                    message += "The magnets will only work while you are moving and not sneaking. ";
                }
                else {
                    message += "The magnets will only work while you are moving. ";
                }
                message += "There are two different strengths of magnets, weak magnets will pickup from a radius of ";
                message += magnetRanges[1];
                message += " blocks around the player and strong magnets have a range of ";
                message += magnetRanges[0];
                message += " blocks. To create a magnet you will need to hold one listed materials in your main hand and use /geoplugin magnet \nValid Materials: \nStrong Materials - ";
                message += Arrays.toString(strongMaterials);
                message += "\nWeak Materials - ";
                message += Arrays.toString(weakMaterials);
                break;
            case "right-click-harvest":
                message = "If you have played modded MC before this will be very familiar. You can now right click on fully grown crops to harvest and automatically replant the crop.";
                break;
            case "duplicate-flora":
                message = "You are now able to duplicate lily pads and all one block tall flowers except wither roses by right clicking them with bonemeal in your main hand. 1 Bonemeal will be consumed per click and there is a ";
                message += bonemealChance;
                message += " chance for a duplicate of the targeted plant to be duplicated.";
                break;
            case "chat-commands":
                message = "There a a handful of miscellaneous chat commands bundled in with this plugin that have silly little effects. Chat Commands: ";
                message += "\nHeart - shows love for everyone or a target - /heart (name)";
                message += "\nRNG - generates a random number between 1-20 or multiple numbers up to a given range - /rng (range max) (# of numbers to generate)";
                message += "\nBlameGeo - Assigns blame properly - /blamegeo";
                message += "\nPoggers - Poggers - /poggers";
                break;
            case "playtime":
                message = "Playtime allows the viewing of the top playtime, a speific player's playtime or your own playtime. Tracking started in version 1.21. Playtime will be split between current game version and the total of all version tracked versions. \nCommand Usage: \nYour Playtime - /played \nTarget's Playtime - /played [username] \nTop Playtime - /played top";
                break;
            case "geokeeper-staff":
                message = "Staff have access to view and alter other player's geokeeper storages, it will give and take from your current xp level, so to gift xp to someone you will need to have the xp yourself. \nCommand Usage: \nView Storage - /geokeeper [username] \nAdd to Storage - /geokeeper store [all/# of levels] [username] \nRemove from Storage - /geokeeper retrieve [all/# of levels] [username]";
                break;
            case "magnet-staff":
                message = "Staff have to access to directly spawn a working magnet either weak or strong. \nCommand Usage: \n/geoplugin magnet [weak/strong]";
                break;
            case "chat-commands-staff":
                message = "Chat commands only available to staff. Chat Commands: ";
                message += "\nPoggers - Poggers - /poggers [username]";
                message += "\nPromotion - Announces the promotion of a player in chat, put fireworks on the promoted player and sends them an on screen message - /promo [username] [new rank]";
                break;
            case "notes":
                message = "Staff are able to create notes about about players and read existing notes made about players. The idea is to have info available to all staff and good and bad things players have done. Examples would be a note fo rthem making a public grinder, or a note about griefing. Please create a note whenever you promote, demote, or ban a player for other mods to reference. \nCommand Usage: \nMaking Notes - /gnote [username] [note] \nReading Notes - /gnote [username] \nReading Recent Notes - /gnote recent";
                break;
            case "login-notes":
                message = "Staff will have a notification in chat whenever any player joins about the player's notes. There are two versions of this, it will either display the entire most recent note on that player or it will display the number of notes a player has. Currently the version is: ";
                if (loginNotes) {
                    message += "Displaying the whole note.";
                }
                else {
                    message += "Displaying the count of notes.";
                }
                break;
            case "login-playtime":
                message = "Staff will have a notification in chat whenever any player joins about the player's playtime. It will show the amount of time that player has played on the current game version. To view their total playtime you will need to use the /played command.";
                break;
            case "enchants":
                message = "WIP none are implemented yet. Potentially coming enchantments include: Soulbound, Auto-Smelt, Tree Cutting, Life-Steal, and Mount Teleport on Death? It is doubltful these will see the light of day. Though maybe?";
                break;
            case "jank":
                message = "This is either WIP features or features that are janky to use. Currently the jank features are mount teleports, step assist, and explode. Mount teleports have a delay between when you and the mount are teleported so you can teleport and be 'riding' the mount while not actually riding it. Step assist is not smooth like it is in modded, you get 'teleported' up and block you would step up., this is vomit inducing to play with enabled. Explode will cause the user to self-destruct.";
                break;
            default:
                break;
        }

        return message;
    }
}
