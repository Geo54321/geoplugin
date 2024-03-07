package com.geoderp.geoplugin.Utility;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class NotesDatabase {
    private Connection db;
    private String dbPath;
    private JavaPlugin Plugin;

    public NotesDatabase(JavaPlugin plugin, String databaseName) {
        this.Plugin = plugin;
        dbPath = Plugin.getDataFolder() + File.separator + databaseName;
        connect();
        createNoteTable();
        createJoinDateTable();
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

    public void remove(String table, int id) {
        String sql = "";
        if(table.equals("notes")) {
            sql = "DELETE FROM notes WHERE id = ?";
        }

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error removing from " + dbPath + " database: " + e);
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

    public int selectID(String table, String criteria, String value) {
        String sql = "";
        int foundID = -1;
        criteria = criteria.trim();
        criteria = criteria.strip();
        value = value.trim();
        value = value.strip();

        if (table.equals("notes")) {
            sql = "SELECT id FROM notes WHERE "+ criteria +" = ?";
        } 
        else if (table.equals("xp")) {
            sql = "SELECT id FROM xp WHERE "+ criteria +" = ?";
        }

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                foundID = rs.getInt("id");
            }
            return foundID;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting ID from " + table + " table in " + dbPath + " database: " + e);
        }

        return foundID;
    }

    // NOTES TABLE INTERACTIONS

    public void createNoteTable() {
        try {
            Statement stmt = db.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS notes(id integer PRIMARY KEY, creator text, date text, target text, content text);";
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error creating notes table in " + dbPath + " database: " + e);
        }
    }

    public void insertNote(String creator, String date, String target, String content) {
        String sql = "INSERT INTO notes(creator, date, target, content) VALUES(?,?,?,?)";

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, creator);
            stmt.setString(2, date);
            stmt.setString(3, target);
            stmt.setString(4, content);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error inserting into " + dbPath + " database: " + e);
        }
    }

    public ArrayList<String[]> selectAllNotes(String criteria, String value) {
        ArrayList<String[]> results = new ArrayList<String[]>();
        criteria = criteria.trim();
        criteria = criteria.strip();
        value = value.trim();
        value = value.strip();

        String sql = "SELECT id, creator, date, target, content FROM notes WHERE "+ criteria +" = ?";

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String[] item = new String[5];
                item[0] = String.valueOf(rs.getInt("id"));
                item[1] = rs.getString("creator");
                item[2] = rs.getString("date");
                item[3] = rs.getString("target");
                item[4] = rs.getString("content");
                results.add(item);
            }
            return results;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting from " + dbPath + " database: " + e);
        }
        return results;
    }

    public ArrayList<String[]> selectRecentNotes() {
        ArrayList<String[]> results = new ArrayList<String[]>();
        String sql = "SELECT id, creator, date, target, content FROM notes ORDER BY id DESC LIMIT 5";

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String[] item = new String[5];
                item[0] = String.valueOf(rs.getInt("id"));
                item[1] = rs.getString("creator");
                item[2] = rs.getString("date");
                item[3] = rs.getString("target");
                item[4] = rs.getString("content");
                results.add(item);
            }
            return results;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting recent from " + dbPath + " database: " + e);
        }

        return results;
    }

    public String[] selectNewestNote(String criteria, String value) {
        String[] result = {};
        criteria = criteria.trim();
        criteria = criteria.strip();
        value = value.trim();
        value = value.strip();

        String sql = "SELECT id, creator, date, target, content FROM notes WHERE "+ criteria +" = ? ORDER BY id DESC LIMIT 1";

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, value);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                result = new String[5];
                result[0] = String.valueOf(rs.getInt("id"));
                result[1] = rs.getString("creator");
                result[2] = rs.getString("date");
                result[3] = rs.getString("target");
                result[4] = rs.getString("content");
            }
            return result;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting from " + dbPath + " database: " + e);
        }
        return result;
    }

    // JOINED DATE TABLE INTERACTIONS

    public void createJoinDateTable() {
        try {
            Statement stmt = db.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS joined(id integer PRIMARY KEY, player text, date text);";
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error creating join date table in " + dbPath + " database: " + e);
        }
    }

    public void addJoined(String player, String date) {
        String sql = "INSERT INTO joined(player, date) VALUES(?,?)";

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, player);
            stmt.setString(2, date);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error inserting into " + dbPath + " database: " + e);
        }
    }

    public String getJoined(String player) {
        String sql = "SELECT date FROM joined WHERE player = ?";
        String joined = null;

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, player);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                joined = rs.getString("date");
            }
            return joined;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting date from " + dbPath + " database: " + e);
        }

        return joined;
    }
}
