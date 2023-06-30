package com.geoderp.geoplugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Utility.Database;

import java.util.ArrayList;
import java.util.UUID;

public class LoginNote implements Listener {
    public Database dbObj;
    public JavaPlugin plugin;

    public LoginNote(Database dbObj, JavaPlugin plugin) {
        this.dbObj = dbObj;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        if(plugin.getConfig().getBoolean("options.login-notes")) {
            // Show recent note on login
            Player loggedPlayer = event.getPlayer();
            String[] note = dbObj.selectNewestNote("target", loggedPlayer.getUniqueId().toString());

            if (note.length > 0) {
                String creator = note[1];
                if (!creator.equals("CONSOLE")) {
                    creator = Bukkit.getOfflinePlayer(UUID.fromString(creator)).getName();
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("GeoPlugin.notes.loginNote")) {
                        messagePlayer(player, loggedPlayer.getName(), note, creator);
                    }
                }
                messageConsole(loggedPlayer.getName(), note, creator);
            }
            else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("GeoPlugin.notes.loginNote")) {
                        player.sendMessage("§e" + loggedPlayer.getName() + " has no notes yet.");
                    }
                }
                Bukkit.getServer().getConsoleSender().sendMessage("§e" + loggedPlayer.getName() + " has no notes yet.");
            }
        }
        else {
            // Show count of notes on login
            Player loggedPlayer = event.getPlayer();
            ArrayList<String[]> notes = dbObj.selectAllNotes("target", loggedPlayer.getUniqueId().toString());
            if (notes.size() > 0) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("GeoPlugin.notes.loginNote")) {
                        player.sendMessage("§3" + loggedPlayer.getName() + " has " + notes.size() + " notes.");
                    }
                }
                Bukkit.getServer().getConsoleSender().sendMessage("§3" + loggedPlayer.getName() + " has " + notes.size() + " notes.");
            }
            else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.hasPermission("GeoPlugin.notes.loginNote")) {
                        player.sendMessage("§6" + loggedPlayer.getName() + " has no notes yet.");
                    }
                }
                Bukkit.getServer().getConsoleSender().sendMessage("§6" + loggedPlayer.getName() + " has no notes yet.");
            }
        }
    }

    public void messagePlayer(Player player, String name, String[] note, String creator) {
        player.sendMessage("§6===== "+name+"'s most recent note =====");
        player.sendMessage("§6Author: §7" + creator);
        player.sendMessage("§6Date: §7" + note[2]);
        player.sendMessage("§6Note: §f" + note[4]);
    }

    public void messageConsole(String name, String[] note, String creator) {
        Bukkit.getServer().getConsoleSender().sendMessage("§dAuthor: " + creator);
        Bukkit.getServer().getConsoleSender().sendMessage("§dDate: " + note[2]);
        Bukkit.getServer().getConsoleSender().sendMessage("§dNote: " + note[4]);
    }
}
