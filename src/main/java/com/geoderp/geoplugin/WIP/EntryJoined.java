package com.geoderp.geoplugin.WIP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EntryJoined implements EntryInterface {
    private int id;
    private String player;
    private String date;
    private String table = "joined";

    public EntryJoined() {
    }

    public EntryJoined(int id, String player, String date) {
        this.id = id;
        this.player = player;
        this.date = date;
    }

    public int getID() {
        return this.id;
    }

    public String getPlayer() {
        return this.player;
    }

    public String getDate() {
        return this.date;
    }

    public String getTable() {
        return this.table;
    }

    public PreparedStatement getSQLInsert(Connection db) {
        String sql = "INSERT INTO " + this.table + "(player, date) VALUES(?,?)";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, this.player);
            stmt.setString(2, this.date);
            return stmt;
        }
        catch (Exception e) {
            return null;
        }
    }

    public PreparedStatement getSQLUpdate(Connection db) {
        // THERE IS NO UPDATE FOR THIS ALWAYS RETURN NULL
        return null;
    }

    public PreparedStatement getSQLSelectByCriteria(Connection db, String criteria, String value) {
        String sql = "SELECT id, player, date FROM " + this.table + " WHERE " + criteria + " = ?";
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
        // THERE IS NO NEED FOR RECENT ALWAYS RETURN NULL
        return null;
    }

    public PreparedStatement getSQLSelectRecentByCriteria(Connection db, String criteria, String value, int count) {
        // THERE IS NO NEED FOR RECENT ALWAYS RETURN NULL
        return null;
    }

    public ArrayList<EntryJoined> handleResults(ResultSet results) {
        
        return new ArrayList<EntryJoined>();
    }
}
