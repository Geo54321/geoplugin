package com.geoderp.geoplugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.geoderp.geoplugin.Commands.AdvanceVersion;
import com.geoderp.geoplugin.Commands.GNote;
import com.geoderp.geoplugin.Commands.GeoInfo;
import com.geoderp.geoplugin.Commands.GeoKeeper;
import com.geoderp.geoplugin.Commands.GeoPlugin;
import com.geoderp.geoplugin.Commands.Playtime;
import com.geoderp.geoplugin.Commands.Promotion;
import com.geoderp.geoplugin.Listeners.DeathXP;
import com.geoderp.geoplugin.Listeners.LoginNote;
import com.geoderp.geoplugin.Listeners.PlaytimeTracker;
import com.geoderp.geoplugin.Listeners.XPKeeperIntercept;
import com.geoderp.geoplugin.Utility.SQLiteNotesDatabase;
import com.geoderp.geoplugin.Utility.XPDatabase;

import java.util.ArrayList;
import java.io.File;

public class Main extends JavaPlugin {
    public SQLiteNotesDatabase notesDB;
    public XPDatabase xpDB;
    public FileConfiguration config = getConfig();
    
    @Override
    public void onEnable() {
        // Plugin Setup
        getLogger().info("Geo is here to break things.");
        File pluginDir = new File(getDataFolder() + "/");
        if(!pluginDir.exists()) {
            pluginDir.mkdir();
        }
        loadDefaultConfigFile();
        saveDefaultConfig();
        
        // Database setups
        notesDB = new SQLiteNotesDatabase(this, "GeoDB.db");
        xpDB = new XPDatabase(this, "GeoXPDB.db");

        // GeoInfo Command
        this.getCommand("geoinfo").setExecutor(new GeoInfo());

        // Notes Module
        if (getConfig().getBoolean("modules.notes")) {
            this.getCommand("gnote").setExecutor(new GNote(notesDB));
            getServer().getPluginManager().registerEvents(new LoginNote(notesDB, this), this);
        }

        // Playtime Module
        if (getConfig().getBoolean("modules.playtime")) {
            this.getCommand("playtime").setExecutor(new Playtime(notesDB));
            getServer().getPluginManager().registerEvents(new PlaytimeTracker(notesDB, this), this);
            this.getCommand("advanceserverversion").setExecutor(new AdvanceVersion(notesDB,xpDB));

            // 5-minute scheduler to update playtime
            new BukkitRunnable() {
                @Override
                public void run() {
                    updateAllPlaytimes();
                }
            }.runTaskTimer(this,0,60);
        }

        // XP Storage Module
        if (getConfig().getBoolean("modules.xp-storage")) {
            this.getCommand("geokeeper").setExecutor(new GeoKeeper(xpDB));
            getServer().getPluginManager().registerEvents(new DeathXP(xpDB, this), this);
            getServer().getPluginManager().registerEvents(new XPKeeperIntercept(), this);
        }

        // Chat Module
        if (getConfig().getBoolean("modules.chat-commands")) {
            this.getCommand("promotion").setExecutor(new Promotion());
        }

        this.getCommand("geoplugin").setExecutor(new GeoPlugin(this));
    }
    
    @Override
    public void onDisable() {
        getLogger().info("Updating Playtime.");
        updateAllPlaytimes();

        getLogger().info("Been a pleasure. Truly.");
        notesDB.closeDatabase();
        xpDB.closeDatabase();
    }

    private void loadDefaultConfigFile() {
        ArrayList<String> header = new ArrayList<String>();
        header.add("GeoPlugin Config File");
        config.options().setHeader(header);

        config.addDefault("modules.notes", true);
        config.addDefault("modules.playtime", true);
        config.addDefault("modules.xp-storage", true);
        config.addDefault("modules.chat-commands", true);
        config.addDefault("options.login-notes", true);
        config.addDefault("options.login-playtime", true);
        config.addDefault("options.xp-store-on-death", true);
        config.addDefault("options.xp-death-percent-high", 1);
        config.addDefault("options.xp-death-percent-medium", 0.50);
        config.addDefault("options.xp-death-percent-low", 0.25);
        config.addDefault("database.type", "sqlite");
        config.addDefault("database.connection.address", "localhost");
        config.addDefault("database.connection.port", "3306");
        config.addDefault("database.connection.database", "GeoPlugin");
        config.addDefault("database.connection.username", "root");
        config.addDefault("database.connection.password", "");

        config.options().copyDefaults(true);
        saveConfig();
        reloadConfig();
    }

    private void updateAllPlaytimes() {
        for(Player player : Bukkit.getOnlinePlayers()) {
            String uuid = player.getUniqueId().toString();
            long now = System.currentTimeMillis();
            long then = notesDB.getPlaytime(uuid, "lastseen");

            notesDB.updatePlaytime(uuid, now-then, now);
        }
    }
}
