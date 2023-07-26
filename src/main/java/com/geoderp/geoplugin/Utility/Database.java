package com.geoderp.geoplugin.Utility;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class Database {
    private Connection db;
    private String dbPath;
    private JavaPlugin Plugin;

    public Database(JavaPlugin plugin, String databaseName) {
        this.Plugin = plugin;
        dbPath = Plugin.getDataFolder() + File.separator + databaseName;
        connect();
        createNoteTable();
        createXPTable();
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

    public void createXPTable() {
        try {
            Statement stmt = db.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS xp(id integer PRIMARY KEY, player text, amount integer);";
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error creating xp table in " + dbPath + " database: " + e);
        }
    }

    public void addUser(String player, int amount) {
        String sql = "INSERT INTO xp(player, amount) VALUES(?,?)";

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, player);
            stmt.setInt(2, amount);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error inserting into " + dbPath + " database: " + e);
        }
    }

    public int getXP(String player) {
        String sql = "SELECT amount FROM xp WHERE player = ?";
        int foundXP = -1;

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, player);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                foundXP = rs.getInt("amount");
            }
            return foundXP;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting amount from " + dbPath + " database: " + e);
        }

        return foundXP;
    }

    public void updateXP(int amount, String player) {
        String sql = "UPDATE xp SET amount = ? " + "WHERE player = ?";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setInt(1, amount);
            stmt.setString(2, player);
            stmt.executeUpdate();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error updating amount in " + dbPath + " database: " + e);
        }
    }

    public ArrayList<String[]> getAllXP() {
        String sql = "SELECT player, amount FROM xp ORDER BY amount DESC LIMIT 10";
        ArrayList<String[]> all = new ArrayList<String[]>();

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String[] player = new String[2];
                player[0] = rs.getString("player");
                player[1] = String.valueOf(rs.getInt("amount"));

                all.add(player);
            }
            return all;
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error getting all player xp in " + dbPath + " database: " + e);
        }
        return all;
    }

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
