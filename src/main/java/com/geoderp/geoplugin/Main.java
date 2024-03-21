package com.geoderp.geoplugin;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.geoderp.geoplugin.Commands.AdvanceVersion;
import com.geoderp.geoplugin.Commands.BlameGeo;
import com.geoderp.geoplugin.Commands.Explode;
import com.geoderp.geoplugin.Commands.GNote;
import com.geoderp.geoplugin.Commands.GeoKeeper;
import com.geoderp.geoplugin.Commands.GeoPlugin;
import com.geoderp.geoplugin.Commands.Heart;
import com.geoderp.geoplugin.Commands.Playtime;
import com.geoderp.geoplugin.Commands.Poggers;
import com.geoderp.geoplugin.Commands.Promotion;
import com.geoderp.geoplugin.Commands.RNG;
import com.geoderp.geoplugin.Listeners.DeathXP;
import com.geoderp.geoplugin.Listeners.ExtraGrow;
import com.geoderp.geoplugin.Listeners.Harvest;
import com.geoderp.geoplugin.Listeners.LoginNote;
import com.geoderp.geoplugin.Listeners.Magnet;
import com.geoderp.geoplugin.Listeners.Teleport;
import com.geoderp.geoplugin.Utility.NotesDatabase;
import com.geoderp.geoplugin.Utility.XPDatabase;

import java.util.ArrayList;
import java.io.File;

public class Main extends JavaPlugin {
    public NotesDatabase notesDB;
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
        notesDB = new NotesDatabase(this, "GeoDB.db");
        xpDB = new XPDatabase(this, "GeoXPDB.db");

        // Notes Module
        if (getConfig().getBoolean("modules.notes")) {
            this.getCommand("gnote").setExecutor(new GNote(notesDB));
            getServer().getPluginManager().registerEvents(new LoginNote(notesDB, this), this);
        }

        // Playtime Module
        if (getConfig().getBoolean("modules.playtime")) {
            this.getCommand("playtime").setExecutor(new Playtime(notesDB));
            this.getCommand("advanceserverversion").setExecutor(new AdvanceVersion(notesDB,xpDB));


            // 5-minute scheduler to update playtime
            new BukkitRunnable() {
                @Override
                public void run() {
                    updateAllPlaytimes();
                }
            }.runTaskTimer(this,0,6000);
        }

        // XP Storage Module
        if (getConfig().getBoolean("modules.xp-storage")) {
            this.getCommand("geokeeper").setExecutor(new GeoKeeper(xpDB));
            getServer().getPluginManager().registerEvents(new DeathXP(xpDB, this), this);
        }

        // Mechanics Module
        if (getConfig().getBoolean("modules.mechanics")) {
            getServer().getPluginManager().registerEvents(new Magnet(this), this);
            getServer().getPluginManager().registerEvents(new Harvest(), this);
            getServer().getPluginManager().registerEvents(new ExtraGrow(this), this);
        }

        // Chat Module
        if (getConfig().getBoolean("modules.chat-commands")) {
            this.getCommand("heart").setExecutor(new Heart());
            this.getCommand("rng").setExecutor(new RNG());
            this.getCommand("blamegeo").setExecutor(new BlameGeo());
            this.getCommand("promotion").setExecutor(new Promotion());
            this.getCommand("poggers").setExecutor(new Poggers());
        }

        // Jank Module
        if (getConfig().getBoolean("modules.jank")) {
            getServer().getPluginManager().registerEvents(new Teleport(), this);
            this.getCommand("explode").setExecutor(new Explode());
        }

        // Enchantment Module
        // if (getConfig().getBoolean("modules.enchantments")) {
        //     GeoEnchants.registerAll();
        //     getServer().getPluginManager().registerEvents(new HewingOld(this), this);
        //     this.getCommand("please").setExecutor(new Please());
        // }

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
        config.addDefault("modules.xp-storage", true);
        config.addDefault("modules.mechanics", true);
        config.addDefault("modules.chat-commands", true);
        config.addDefault("modules.jank", true);
        config.addDefault("modules.enchantments", true);
        config.addDefault("modules.playtime", true);
        config.addDefault("options.login-notes", true);
        config.addDefault("options.login-playtime", true);
        config.addDefault("options.xp-store-on-death", true);
        config.addDefault("options.xp-death-percent-high", 1);
        config.addDefault("options.xp-death-percent-medium", 0.50);
        config.addDefault("options.xp-death-percent-low", 0.25);
        config.addDefault("options.strong-magnet-range", 4);
        config.addDefault("options.weak-magnet-range", 2);
        config.addDefault("options.sneak-disable-magnet", true);
        config.addDefault("options.growth-chance-percent",0.3);
        config.addDefault("options.hewing-max-block-break", 50);

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
