package com.geoderp.geoplugin.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EntryPlaytime implements EntryInterface {
    private int id;
    private String player;
    private int current;
    private int previous;
    private long lastseen;
    private String table = "playtime";

    public EntryPlaytime(int id, String player, int current, int previous, long lastseen) {
        this.id = id;
        this.player = player;
        this.current = current;
        this.previous = previous;
        this.lastseen = lastseen;
    }

    public int getID() {
        return this.id;
    }

    public String getPlayer() {
        return this.player;
    }

    public int getCurrent() {
        return this.current;
    }

    public int getPrevious() {
        return this.previous;
    }  

    public long getLastSeen() {
        return this.lastseen;
    }

    public String getTable() {
        return this.table;
    }

    public PreparedStatement getSQLInsert(Connection db) {
        String sql = "INSERT INTO " + this.table + "(player, current, previous) VALUES(?,?,?)";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, this.player);
            stmt.setLong(2, 0);
            stmt.setLong(3, 0);
            return stmt;
        }
        catch (Exception e) {
            return null;
        }
    }
    
    public PreparedStatement getSQLUpdate(Connection db) {
        String sql = "UPDATE " + this.table + " SET current = ?, lastseen = ? WHERE player = ?";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setLong(1, this.current);
            stmt.setLong(2, this.lastseen);
            stmt.setString(3, this.player);
            return stmt;
        }
        catch (Exception e) {
            return null;
        }
    }

    public PreparedStatement getSQLSelectByCriteria(Connection db, String criteria, String value) {
        String sql = "SELECT id, player, current, previous, lastseen FROM " + this.table + " WHERE " + criteria + " = ?";
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
        // RETURNS TOP CURRENT PLAYTIME
        String sql = "SELECT id, player, current, previous, lastseen FROM " + this.table + " ORDER BY desc DESC LIMIT 5";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            return stmt;
        }
        catch (Exception e) {
            return null;
        }
    }

    public PreparedStatement getSQLSelectRecentByCriteria(Connection db, String criteria, String value, int count) {
        // RETURNS TOP OVERALL PLAYTIME
        String sql = "SELECT id, creator, date, target, content FROM " + this.table + " ORDER BY current+previous DESC LIMIT 5";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, value);
            return stmt;
        }
        catch (Exception e) {
            return null;
        }
    }

    public ArrayList<EntryJoined> handleResults(ResultSet results) {
        
        return new ArrayList<EntryJoined>();
    }
}
