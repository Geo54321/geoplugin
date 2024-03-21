package com.geoderp.geoplugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Utility.NotesDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class LoginNote implements Listener {
    public NotesDatabase dbObj;
    public JavaPlugin plugin;

    public LoginNote(NotesDatabase dbObj, JavaPlugin plugin) {
        this.dbObj = dbObj;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player loggedPlayer = event.getPlayer();

        for(Player player : Bukkit.getOnlinePlayers()) {
            // Login note
            if(player.hasPermission("GeoPlugin.notes.loginNote")) {
                notesMessage(player, loggedPlayer);
            }
            // Login playtime
            if(player.hasPermission("GeoPlugin.notes.loginPlaytime")) {
                playtimeMessage(player, loggedPlayer);
            }
        }

        // Console infos
        notesMessage(null, loggedPlayer);
        playtimeMessage(null, loggedPlayer);
    }

    public void notesMessage(Player disPlayer, Player loggedPlayer) {
        ArrayList<String[]> notes = dbObj.selectAllNotes("target", loggedPlayer.getUniqueId().toString());
        int noteCount = notes.size();
        if(plugin.getConfig().getBoolean("options.login-notes")) {
            // Login Note - Recent Note Edition
            if(noteCount > 0) {
                // Recent note
                String[] recentNote = notes.get(noteCount-1);
                String creator = recentNote[1];
                if (!creator.equals("CONSOLE")) {
                    creator = Bukkit.getOfflinePlayer(UUID.fromString(creator)).getName();
                }

                sendGenericMessage(disPlayer, "§6===== " + loggedPlayer.getName() + "'s most recent note =====");
                sendGenericMessage(disPlayer, "§6Author: §7" + creator + " §6Date: §7" + recentNote[2]);
                sendGenericMessage(disPlayer, "§6Note: §f" + recentNote[4]);
            }
            else {
                // No notes
                sendGenericMessage(disPlayer, "§6" + loggedPlayer.getName() + " has no notes yet.");
            }
        }
        else {
            // Login Note - Count Edition
            if(noteCount > 0) {
                // Note count
                sendGenericMessage(disPlayer, "§3" + loggedPlayer.getName() + " has " + notes.size() + " notes.");
            }
            else {
                // No notes
                sendGenericMessage(disPlayer, "§6" + loggedPlayer.getName() + " has no notes yet.");
            }
        }
    }

    public void playtimeMessage(Player disPlayer, Player loggedPlayer) {
        if(plugin.getConfig().getBoolean("options.login-playtime")) {
            long[] times = dbObj.convertTime(dbObj.getPlaytime(loggedPlayer.getUniqueId().toString(), "current"));
            sendGenericMessage(disPlayer, "§6Playtime: §7" + times[0] + "d " + times[1] + "h " + times[2] + "m");
        }
    }

    public void sendGenericMessage(Player player, String message) {
        if(player.equals(null)) {
            Bukkit.getServer().getConsoleSender().sendMessage(message);
        }
        else {
            player.sendMessage(message);
        }
    }

    // Old Login Note Code:

    // @EventHandler
    // public void onPlayerLogin(PlayerLoginEvent event) {
    //     Player loggedPlayer = event.getPlayer();

    //     // Login Notes
    //     if(plugin.getConfig().getBoolean("options.login-notes")) {
    //         // Show recent note on login
    //         String[] note = dbObj.selectNewestNote("target", loggedPlayer.getUniqueId().toString());

    //         if (note.length > 0) {
    //             String creator = note[1];
    //             if (!creator.equals("CONSOLE")) {
    //                 creator = Bukkit.getOfflinePlayer(UUID.fromString(creator)).getName();
    //             }

    //             for (Player player : Bukkit.getOnlinePlayers()) {
    //                 if (player.hasPermission("GeoPlugin.notes.loginNote")) {
    //                     // Display notes
    //                     noteToPlayer(player, loggedPlayer.getName(), note, creator);
    //                     // Display playtime
    //                     if(plugin.getConfig().getBoolean("options.login-playtime")) {
    //                         long[] times = dbObj.convertTime(dbObj.getPlaytime(loggedPlayer.getUniqueId().toString(), "current"));
    //                         player.sendMessage("§6Playtime: §7" + times[0] + "d " + times[1] + "h " + times[2] + "m");
    //                     }
    //                 }
    //             }
    //             messageConsole(loggedPlayer.getName(), note, creator);
    //         }
    //         else {
    //             for (Player player : Bukkit.getOnlinePlayers()) {
    //                 if (player.hasPermission("GeoPlugin.notes.loginNote")) {
    //                     // Display notes
    //                     player.sendMessage("§e" + loggedPlayer.getName() + " has no notes yet.");
    //                     // Display playtime
    //                     if(plugin.getConfig().getBoolean("options.login-playtime")) {
    //                         long[] times = dbObj.convertTime(dbObj.getPlaytime(loggedPlayer.getUniqueId().toString(), "current"));
    //                         player.sendMessage("§6Playtime: §7" + times[0] + "d " + times[1] + "h " + times[2] + "m");
    //                     }
    //                 }
    //             }
    //             Bukkit.getServer().getConsoleSender().sendMessage("§e" + loggedPlayer.getName() + " has no notes yet.");
    //         }
    //     }
    //     else {
    //         // Show count of notes on login
    //         ArrayList<String[]> notes = dbObj.selectAllNotes("target", loggedPlayer.getUniqueId().toString());
    //         if (notes.size() > 0) {
    //             for (Player player : Bukkit.getOnlinePlayers()) {
    //                 if (player.hasPermission("GeoPlugin.notes.loginNote")) {
    //                     // Display notes
    //                     player.sendMessage("§3" + loggedPlayer.getName() + " has " + notes.size() + " notes.");
    //                     // Display playtime
    //                     if(plugin.getConfig().getBoolean("options.login-playtime")) {
    //                         long[] times = dbObj.convertTime(dbObj.getPlaytime(loggedPlayer.getUniqueId().toString(), "current"));
    //                         player.sendMessage("§6Playtime: §7" + times[0] + "d " + times[1] + "h " + times[2] + "m");
    //                     }
    //                 }
    //             }
    //             Bukkit.getServer().getConsoleSender().sendMessage("§3" + loggedPlayer.getName() + " has " + notes.size() + " notes.");
    //         }
    //         else {
    //             for (Player player : Bukkit.getOnlinePlayers()) {
    //                 if (player.hasPermission("GeoPlugin.notes.loginNote")) {
    //                     // Display notes
    //                     player.sendMessage("§6" + loggedPlayer.getName() + " has no notes yet.");
    //                     // Display playtime
    //                     if(plugin.getConfig().getBoolean("options.login-playtime")) {
    //                         long[] times = dbObj.convertTime(dbObj.getPlaytime(loggedPlayer.getUniqueId().toString(), "current"));
    //                         player.sendMessage("§6Playtime: §7" + times[0] + "d " + times[1] + "h " + times[2] + "m");
    //                     }
    //                 }
    //             }
    //             Bukkit.getServer().getConsoleSender().sendMessage("§6" + loggedPlayer.getName() + " has no notes yet.");
    //         }
    //     }
    // }

    // public void noteToPlayer(Player player, String name, String[] note, String creator) {
    //     player.sendMessage("§6===== "+name+"'s most recent note =====");
    //     player.sendMessage("§6Author: §7" + creator + " §6Date: §7" + note[2]);
    //     player.sendMessage("§6Note: §f" + note[4]);
    // }

    // public void messageConsole(String name, String[] note, String creator) {
    //     Bukkit.getServer().getConsoleSender().sendMessage("§5Author: " + creator + " §5Date: " + note[2]);
    //     Bukkit.getServer().getConsoleSender().sendMessage("§5Note: " + note[4]);
    // }
}
