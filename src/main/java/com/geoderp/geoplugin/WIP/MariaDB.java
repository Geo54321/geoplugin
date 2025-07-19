package com.geoderp.geoplugin.WIP;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public class MariaDB {
    private Connection db;
    private JavaPlugin Plugin;
    private String connection,username,password;

    public MariaDB(JavaPlugin plugin, String host, String port, String database, String username, String password) {
        this.Plugin = plugin;
        this.connection = "jdbc:mariadb://" + host + ":" + port + "/" + database;
        this.username = username;
        this.password = password;
        connect();
    }

    public void connect() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
            db = DriverManager.getConnection(connection,username,password);
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Database Connection Error: " + e);
        }
    }

    public void closeDatabase() {
        try {
            db.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error closing " + connection + " database: " + e);
        }
    }

    public void createTable(String table, ArrayList<String[]> columns) {

    }

    public void addItem(String table, Object item) {

    }

    public void removeItem(String table, int id) {

    }

    public int findItem(String table, String criteria, String value) {

        return 0;
    }

    public ArrayList<?> getAllItems(String table) {

        return new ArrayList<>();
    }

    public ArrayList<?> getSelectItems(String table, String criteria, String value) {

        return new ArrayList<>();
    }

    public ArrayList<?> getRecentItems(String table) {

        return new ArrayList<>();
    }

    public String getType(String type) {
        switch (type) {
            case "int":
                return "integer";
            case "long":
                return "bigint";
            case "double":
                return "float";
            case "string":
                return "varchar(255)";
            case "bool":
                return "bool";
            default:
                return "null";
        }
    }

    public boolean getTable(String table) {

        return false;
    }
}
