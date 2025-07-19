package com.geoderp.geoplugin.WIP;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

public class SQLite {
    private Connection db;
    private String dbPath;
    private JavaPlugin Plugin;
    private ArrayList<String> tables;
    
    public SQLite(JavaPlugin plugin, String databaseName) {
        this.Plugin = plugin;
        dbPath = Plugin.getDataFolder() + File.separator + databaseName;
        connect();
    }

    public void connect() {
        db = null;
        try {
            Class.forName("org.sqlite.JDBC");
            db = DriverManager.getConnection("jdbc:sqlite:" + dbPath);
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Connection and Tables Error: " + e);
        }
    }

    public void closeDatabase() {
        try {
            db.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error closing " + dbPath + " database: " + e);
        }
    }

    public void createTable(String table, ArrayList<String[]> columns) {
        try {
            Statement stmt = db.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS " + table + "(id integer PRIMARY KEY, ";
            for (int g = 0; g < columns.size(); g++) {
                sql = sql + columns.get(g)[0] + " " + getType(columns.get(g)[1]);
                if (g++ < columns.size()) {
                    sql = sql + ", ";
                }
            }
            sql = sql + ");";

            stmt.executeUpdate(sql);
            stmt.close();
            tables.add(table);
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error creating " + table + " table in " + dbPath + " database: " + e);
        }
    }

    public <T extends EntryInterface> void addItem(T item) {
        // Check table exists
        if (!tables.contains(item.getTable())) {
            Plugin.getLogger().log(Level.WARNING, "Error inserting into " + dbPath + " database: " + item.getTable() + " table does not exist.");
            return;
        }

        // Insert to table
        try {
            PreparedStatement stmt = item.getSQLInsert(db);
            if (stmt == null) {
                Plugin.getLogger().log(Level.INFO, "Error inserting into " + dbPath + " database: Failed ot build statement.");
                return;
            }
            stmt.executeUpdate();
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error inserting into " + dbPath + " database: " + e);
        }
    }

    public <T extends EntryInterface> void updateItem(T item) {
        // Check table exists
        if (!tables.contains(item.getTable())) {
            Plugin.getLogger().log(Level.WARNING, "Error updating entry in " + dbPath + " database: " + item.getTable() + " table does not exist.");
            return;
        }

        // Insert to table
        try {
            PreparedStatement stmt = item.getSQLUpdate(db);
            if (stmt == null) {
                Plugin.getLogger().log(Level.INFO, "Error updating entry in " + dbPath + " database: Failed to build statement.");
                return;
            }
            stmt.executeUpdate();
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error updating entry in " + dbPath + " database: " + e);
        }
    }

    public void removeItem(String table, int id) {
        // Check table exists
        if (!tables.contains(table)) {
            Plugin.getLogger().log(Level.WARNING, "Error updating entry in " + dbPath + " database: " + table + " table does not exist.");
            return;
        }

        String sql = "DELETE FROM " + table + " WHERE id = ?";

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error removing from " + dbPath + " database: " + e);
        }
    }

    public int findItem(String table, String criteria, String value) {
        // Check table exists
        if (!tables.contains(table)) {
            Plugin.getLogger().log(Level.WARNING, "Error updating entry in " + dbPath + " database: " + table + " table does not exist.");
            return -1;
        }

        String sql = "SELECT id FROM " + table + " WHERE " + criteria + " = ?";
        int id = -1;
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                id = rs.getInt("id");
            }
            return id;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting ID from " + table + " table in " + dbPath + " database: " + e);
        }
        return -1;
    }

    public <T extends EntryInterface> ArrayList<?> getAllItems(T item) {
        // Check table exists
        if (!tables.contains(item.getTable())) {
            Plugin.getLogger().log(Level.WARNING, "Error selecting entries in " + dbPath + " database: " + item.getTable() + " table does not exist.");
            return null;
        }

        String sql = "SELECT * FROM " + item.getTable();
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            ResultSet results = stmt.executeQuery();
            ArrayList<?> output = item.handleResults(results);
            return output;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting entries in " + item.getTable() + " table in " + dbPath + " database: " + e);
        }
        return null;
    }

    public <T extends EntryInterface> ArrayList<?> getSelectItems(T item, String criteria, String value) {
        // Check table exists
        if (!tables.contains(item.getTable())) {
            Plugin.getLogger().log(Level.WARNING, "Error updating entry in " + dbPath + " database: " + item.getTable() + " table does not exist.");
            return null;
        }

        try {
            PreparedStatement stmt = item.getSQLSelectByCriteria(db, criteria, value);
            ResultSet results = stmt.executeQuery();
            ArrayList<?> output = item.handleResults(results);
            return output;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting entries in " + item.getTable() + " table in " + dbPath + " database: " + e);
        }
        return null;
    }
    
    public <T extends EntryInterface> ArrayList<?> getRecentItems(T item, int count) {
        // Check table exists
        if (!tables.contains(item.getTable())) {
            Plugin.getLogger().log(Level.WARNING, "Error updating entry in " + dbPath + " database: " + item.getTable() + " table does not exist.");
            return null;
        }

        try {
            PreparedStatement stmt = item.getSQLSelectRecent(db, count);
            ResultSet results = stmt.executeQuery();
            ArrayList<?> output = item.handleResults(results);
            return output;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting entries in " + item.getTable() + " table in " + dbPath + " database: " + e);
        }
        return null;
    }

    public String getType(String type) {
        switch (type) {
            case "int":
                return "integer";
            case "long":
                return "integer";
            case "double":
                return "real";
            case "string":
                return "text";
            case "bool":
                return "integer";
            default:
                return "null";
        }
    }
}
