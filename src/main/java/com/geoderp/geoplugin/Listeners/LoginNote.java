package com.geoderp.geoplugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Utility.SQLiteNotesDatabase;

import java.util.ArrayList;
import java.util.UUID;

public class LoginNote implements Listener {
    public SQLiteNotesDatabase dbObj;
    public JavaPlugin plugin;

    public LoginNote(SQLiteNotesDatabase dbObj, JavaPlugin plugin) {
        this.dbObj = dbObj;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player loggedPlayer = event.getPlayer();

        if (!loggedPlayer.isBanned()) {
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
        if(player != null) {
            player.sendMessage(message);
        }
        else {
            Bukkit.getServer().getConsoleSender().sendMessage(message);
        }
    }
}
