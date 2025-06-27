package com.geoderp.geoplugin.Listeners;

import java.time.LocalDate;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Utility.SQLiteNotesDatabase;

public class PlaytimeTracker implements Listener {
    public SQLiteNotesDatabase dbObj;
    public JavaPlugin plugin;

    public PlaytimeTracker(SQLiteNotesDatabase dbObj, JavaPlugin plugin) {
        this.dbObj = dbObj;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();

        checkExistingPlayer(event.getPlayer(), uuid);

        long now = System.currentTimeMillis();
        dbObj.updatePlaytime(uuid, 0, now);
    }

    @EventHandler
    public void onPlayerLogout(PlayerQuitEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        updateTime(uuid);
    }

    @EventHandler
    public void onPlayerLogout(PlayerKickEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        updateTime(uuid);
    }

    public void checkExistingPlayer(Player player, String uuid) {
        // First Joined check
        if (dbObj.selectID("joined","player",uuid) == -1) {
            LocalDate date = LocalDate.now();
            String dateString = date.toString();
            dbObj.addJoined(uuid, dateString);
            Bukkit.broadcastMessage("§c" + player.getName() + " joined the server for the first time!");
        }
        
        // Playtime check
        if (dbObj.selectID("playtime","player",uuid) == -1) {
            dbObj.addPlayer(uuid);
        }
    }

    public void updateTime(String uuid) {
        long now = System.currentTimeMillis();
        long then = dbObj.getPlaytime(uuid, "lastseen");

        dbObj.updatePlaytime(uuid, now-then, now);
    }
}
