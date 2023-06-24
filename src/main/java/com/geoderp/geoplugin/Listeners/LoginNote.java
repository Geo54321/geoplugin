package com.geoderp.geoplugin.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.geoderp.geoplugin.Utility.Database;

import java.util.UUID;

public class LoginNote implements Listener {
    public Database dbObj;

    public LoginNote(Database dbObj) {
        this.dbObj = dbObj;
    }

    @EventHandler
    public void onPlayerLogin(PlayerLoginEvent event) {
        Player loggedPlayer = event.getPlayer();
        String[] note = dbObj.selectNewest("notes", "target", loggedPlayer.getUniqueId().toString());

        if (note.length > 0) {
            String creator = note[1];
            if (!creator.equals("CONSOLE")) {
                creator = Bukkit.getOfflinePlayer(UUID.fromString(creator)).getName();
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("GeoXP.notes.loginNote")) {
                    messagePlayer(player, loggedPlayer.getName(), note, creator);
                }
            }
            messageConsole(loggedPlayer.getName(), note, creator);
        }
        else {
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.hasPermission("GeoXP.notes.loginNote")) {
                    player.sendMessage("§e"+loggedPlayer.getName()+" has no notes yet.");
                }
            }
            Bukkit.getServer().getConsoleSender().sendMessage("§e"+loggedPlayer.getName()+" has no notes yet.");
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
