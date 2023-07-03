package com.geoderp.geoplugin.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Utility.Database;
import com.geoderp.geoplugin.Utility.XP;

public class DeathXP implements Listener {
    public Database dbObj;
    public JavaPlugin plugin;

    public DeathXP (Database dbObj, JavaPlugin plugin) {
        this.dbObj = dbObj;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.hasPermission("GeoPlugin.geokeeper.death.high") || player.hasPermission("GeoPlugin.geokeeper.death.medium") || player.hasPermission("GeoPlugin.geokeeper.death.low")) {
            int fromDeath = XP.getXPOfLevel(player.getLevel());
            fromDeath += player.getExp() * (XP.getXPOfLevel(player.getLevel()+1) - XP.getXPOfLevel(player.getLevel()));

            if (player.hasPermission("GeoPlugin.geokeeper.death.high")) {
                fromDeath = (int) Math.ceil(fromDeath * plugin.getConfig().getDouble("options.xp-death-percent-high"));
            } 
            else if (player.hasPermission("GeoPlugin.geokeeper.death.medium")) {
                fromDeath = (int) Math.ceil(fromDeath * plugin.getConfig().getDouble("options.xp-death-percent-medium"));
            }
            else if (player.hasPermission("GeoPlugin.geokeeper.death.low")) {
                fromDeath = (int) Math.ceil(fromDeath * plugin.getConfig().getDouble("options.xp-death-percent-low"));
            }

            int stored = getStoredXP(player);
            dbObj.updateXP(stored+fromDeath, player.getUniqueId().toString());
            event.setDroppedExp(0);
        }
    }

    public boolean validPlayer(Player player) {
        String uuid = player.getUniqueId().toString();
        if (dbObj.selectID("xp","player",uuid) == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    public void addUser(Player player, int amount) {
        String uuid = player.getUniqueId().toString();
        dbObj.addUser(uuid, amount);
    }

    public int getStoredXP(Player player) {
        if (!validPlayer(player)) {
            addUser(player, 0);
        }

        String uuid = player.getUniqueId().toString();
        return dbObj.getXP(uuid);
    }
}
