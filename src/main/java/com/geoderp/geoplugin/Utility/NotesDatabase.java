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
        createPlaytimeTable();
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
        else if (table.equals("playtime")) {
            sql = "SELECT id FROM playtime WHERE "+ criteria +" = ?";
        }
        else if (table.equals("joined")) {
            sql = "SELECT id FROM joined WHERE "+ criteria +" = ?";
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

    // PLAYTIME TABLE INTERACTIONS

    public void createPlaytimeTable() {
        try {
            Statement stmt = db.createStatement();
            String sql = "CREATE TABLE IF NOT EXISTS playtime(id integer PRIMARY KEY, player string, current integer, previous integer, lastseen integer);";
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error creating playtime table in " + dbPath + " database: " + e);
        }
    }

    public void addPlayer(String player){
        player = player.trim();
        player = player.strip();

        String sql = "INSERT INTO playtime(player, current, previous) VALUES(?,?,?)";

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, player);
            stmt.setLong(2, 0);
            stmt.setLong(3, 0);
            stmt.executeUpdate();
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error inserting into " + dbPath + " database: " + e);
        }
    }

    public void updatePlaytime(String player, long time, long seen) {
        player = player.trim();
        player = player.strip();

        String sql = "UPDATE playtime SET current = ?, lastseen = ?" + "WHERE player = ?";

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setLong(1, time);
            stmt.setLong(2, seen);
            stmt.setString(3, player);
            stmt.executeUpdate();
        }
        catch(Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error updating amount in " + dbPath + " database: " + e);
        }
    }

    public long getPlaytime(String player, String type) {
        player = player.trim();
        player = player.strip();

        String sql = "SELECT " + type + " FROM playtime WHERE player = ?";

        long result = -1;

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, player);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                result = rs.getLong(type);
            }
        }
        catch(Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting from " + dbPath + " database: " + e);
        }

        return result;
    }

    public ArrayList<String[]> getTopCurrent() {
        String sql = "SELECT player, current FROM playtime ORDER BY current DESC LIMIT 5";

        ArrayList<String[]> results = new ArrayList<String[]>();

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String[] item = new String[2];
                item[0] = rs.getString("player");
                item[1] = String.valueOf(rs.getLong("current"));
                results.add(item);
            }
        }
        catch(Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting from " + dbPath + " database: " + e);
        }

        return results;
    }

    public ArrayList<String[]> getTopTotal() {
        String sql = "SELECT player, current, total FROM playtime ORDER BY current+total DESC LIMIT 5";

        ArrayList<String[]> results = new ArrayList<String[]>();

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                String[] item = new String[2];
                item[0] = rs.getString("player");
                item[1] = String.valueOf(rs.getLong("current") + rs.getLong("total"));
                results.add(item);
            }
        }
        catch(Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting from " + dbPath + " database: " + e);
        }

        return results;
    }

    public void updatePlaytimeByID(long[] newVals) {
        String sql = "UPDATE playtime SET current = ?, previous = ? " + "WHERE id = ?";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setLong(1, newVals[1]);
            stmt.setLong(2, newVals[2]);
            stmt.setLong(3, newVals[0]);
            stmt.executeUpdate();
        }
        catch(Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error updating amount in " + dbPath + " database: " + e);
        }
    }

    public void changeVersion() {
        ArrayList<long[]> results = new ArrayList<long[]>();
        
        String sql = "SELECT player, current, previous from playtime";

        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                long[] item = new long[3];
                item[0] = rs.getLong("id");
                item[1] = rs.getLong("current");
                item[2] = rs.getLong("previous");
                results.add(item);
            }
        }
        catch(Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error selecting from " + dbPath + " database: " + e);
        }

        for(long[] item : results) {
            item[2] += item[1];
            item[1] = 0;
            updatePlaytimeByID(item);
        }
    }
}