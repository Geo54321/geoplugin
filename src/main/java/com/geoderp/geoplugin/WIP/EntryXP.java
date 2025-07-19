package com.geoderp.geoplugin.WIP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EntryXP implements EntryInterface {
    private int id;
    private String player;
    private int amount;
    private String table = "xp";

    public EntryXP(int id, String player, int amount) {
        this.id = id;
        this.player = player;
        this.amount = amount;
    }

    public int getID() {
        return this.id;
    }

    public String getPlayer() {
        return this.player;
    }

    public int getAmount() {
        return this.amount;
    }

    public String getTable() {
        return this.table;
    }

    public PreparedStatement getSQLInsert(Connection db) {
        String sql = "INSERT INTO " + this.table + "(player, amount) VALUES(?,?)";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, this.player);
            stmt.setInt(2, this.amount);
            return stmt;
        }
        catch (Exception e) {
            return null;
        }
    }

    public PreparedStatement getSQLUpdate(Connection db) {
        String sql = "UPDATE " + this.table + " SET amount = ? WHERE player = ?";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setInt(1, this.amount);
            stmt.setString(2, this.player);
            return stmt;
        }
        catch (Exception e) {
            return null;
        }
    }

    public PreparedStatement getSQLSelectByCriteria(Connection db, String criteria, String value) {
        String sql = "SELECT id, player, amount FROM " + this.table + " WHERE " + criteria + " = ?";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, value);
            return stmt;
        }
        catch (Exception e) {
            return null;
        }
    }

    public PreparedStatement getSQLSelectRecent(Connection db, int count) {
        // RETURNS the top amount of XP
        String sql = "SELECT id, player amount FROM " + this.table + " ORDER BY amount DESC LIMIT " + String.valueOf(count);
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            return stmt;
        }
        catch (Exception e) {
            return null;
        }
    }

    public PreparedStatement getSQLSelectRecentByCriteria(Connection db, String criteria, String value, int count) {
        // THERE IS NO NEED FOR RECENT ALWAYS RETURN NULL
        return null;
    }

    public ArrayList<EntryJoined> handleResults(ResultSet results) {
        
        return new ArrayList<EntryJoined>();
    }
}
