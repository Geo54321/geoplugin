package com.geoderp.geoplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Commands.BlameGeo;
import com.geoderp.geoplugin.Commands.Explode;
import com.geoderp.geoplugin.Commands.GNote;
import com.geoderp.geoplugin.Commands.GeoKeeper;
import com.geoderp.geoplugin.Commands.GeoPlugin;
import com.geoderp.geoplugin.Commands.Heart;
import com.geoderp.geoplugin.Commands.Please;
import com.geoderp.geoplugin.Commands.Poggers;
import com.geoderp.geoplugin.Commands.Promotion;
import com.geoderp.geoplugin.Commands.RNG;
import com.geoderp.geoplugin.Enchants.GeoEnchants;
import com.geoderp.geoplugin.Enchants.HewingOld;
import com.geoderp.geoplugin.Listeners.DeathXP;
import com.geoderp.geoplugin.Listeners.ExtraGrow;
import com.geoderp.geoplugin.Listeners.Harvest;
import com.geoderp.geoplugin.Listeners.LoginNote;
import com.geoderp.geoplugin.Listeners.Magnet;
import com.geoderp.geoplugin.Listeners.Teleport;
import com.geoderp.geoplugin.Utility.Database;

import java.util.ArrayList;
import java.io.File;

public class Main extends JavaPlugin {
    public Database dbObj;
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
        
        // Notes Modules
        dbObj = new Database(this, "GeoDB.db");
        if (getConfig().getBoolean("modules.notes")) {
            this.getCommand("gnote").setExecutor(new GNote(dbObj));
            getServer().getPluginManager().registerEvents(new LoginNote(dbObj, this), this);
        }
        if (getConfig().getBoolean("modules.xp-storage")) {
            this.getCommand("geokeeper").setExecutor(new GeoKeeper(dbObj));
            getServer().getPluginManager().registerEvents(new DeathXP(dbObj, this), this);
        }
        if (getConfig().getBoolean("modules.mechanics")) {
            getServer().getPluginManager().registerEvents(new Magnet(this), this);
            getServer().getPluginManager().registerEvents(new Harvest(), this);
            getServer().getPluginManager().registerEvents(new ExtraGrow(this), this);
        }
        if (getConfig().getBoolean("modules.chat-commands")) {
            this.getCommand("heart").setExecutor(new Heart());
            this.getCommand("rng").setExecutor(new RNG());
            this.getCommand("blamegeo").setExecutor(new BlameGeo());
            this.getCommand("promotion").setExecutor(new Promotion());
            this.getCommand("poggers").setExecutor(new Poggers());
        }
        if (getConfig().getBoolean("modules.jank")) {
            getServer().getPluginManager().registerEvents(new Teleport(), this);
            this.getCommand("explode").setExecutor(new Explode());
        }
        if (getConfig().getBoolean("modules.enchantments")) {
            GeoEnchants.registerAll();
            getServer().getPluginManager().registerEvents(new HewingOld(this), this);
            this.getCommand("please").setExecutor(new Please());
        }
        this.getCommand("geoplugin").setExecutor(new GeoPlugin(this));
    }
    @Override
    public void onDisable() {
        getLogger().info("Been a pleasure. Truly.");
        dbObj.closeDatabase();
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
        config.addDefault("options.login-notes", true);
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
}
