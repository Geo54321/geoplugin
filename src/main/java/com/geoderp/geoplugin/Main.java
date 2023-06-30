package com.geoderp.geoplugin;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import com.geoderp.geoplugin.Commands.GNote;
import com.geoderp.geoplugin.Listeners.LoginNote;
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
        if(getConfig().getBoolean("modules.notes")) {
            this.getCommand("gnote").setExecutor(new GNote(dbObj));
            getServer().getPluginManager().registerEvents(new LoginNote(dbObj, this), this);
        }
        
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

        config.addDefault("modules.notes",true);
        config.addDefault("options.login-notes",true);

        config.options().copyDefaults(true);
        saveConfig();
        reloadConfig();
    }
}
