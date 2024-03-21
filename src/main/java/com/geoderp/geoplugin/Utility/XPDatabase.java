package com.geoderp.geoplugin.Utility;

import java.io.File;
import java.sql.*;
import java.util.logging.Level;
import java.util.ArrayList;

import org.bukkit.plugin.java.JavaPlugin;

public class XPDatabase {
    private Connection db;
    private String dbPath;
    private JavaPlugin Plugin;

    public XPDatabase(JavaPlugin plugin, String databaseName) {
        this.Plugin = plugin;
        dbPath = Plugin.getDataFolder() + File.separator + databaseName;
        connect();
        createXPTable();
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

    public int selectID(String table, String criteria, String value) {
        String sql = "";
        int foundID = -1;
        criteria = criteria.trim();
        criteria = criteria.strip();
        value = value.trim();
        value = value.strip();

        sql = "SELECT id FROM xp WHERE "+ criteria +" = ?";

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

    public void changeVersion() {
        // Drop old XP table
        try {
            Statement stmt = db.createStatement();
            String sql = "DROP TABLE xp";
            stmt.executeUpdate(sql);
            stmt.close();
        }
        catch (Exception e) {
            Plugin.getLogger().log(Level.INFO, "Error dropping table in " + dbPath + " database: " + e);
        }

        // Make new empty table
        createXPTable();
    }
}
