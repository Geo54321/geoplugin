package com.geoderp.geoplugin.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class EntryNote implements EntryInterface {
    private int id;
    private String creator;
    private String date;
    private String target;
    private String content;
    private String table = "notes";

    public EntryNote(int id, String creator, String date, String target, String content) {
        this.id = id;
        this.creator = creator;
        this.date = date;
        this.target = target;
        this.content = content;
    }

    public int getID() {
        return this.id;
    }

    public String getCreator() {
        return this.creator;
    }

    public String getDate() {
        return this.date;
    }

    public String getTarget() {
        return this.target;
    }

    public String getContent() {
        return this.content;
    }

    public String getTable() {
        return this.table;
    }

    public PreparedStatement getSQLInsert(Connection db) {
        String sql = "INSERT INTO " + this.table + "(creator, date, target, content) VALUES(?,?,?,?)";
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            stmt.setString(1, this.creator);
            stmt.setString(2, this.date);
            stmt.setString(3, this.target);
            stmt.setString(4, this.content);
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
        String sql = "SELECT id, creator, date, target, content FROM " + this.table + " WHERE " + criteria + " = ?";
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
        String sql = "SELECT id, creator, date, target, content FROM " + this.table + " ORDER BY id DESC LIMIT " + String.valueOf(count);
        try {
            PreparedStatement stmt = db.prepareStatement(sql);
            return stmt;
        }
        catch (Exception e) {
            return null;
        }
    }

    public PreparedStatement getSQLSelectRecentByCriteria(Connection db, String criteria, String value, int count) {
        String sql = "SELECT id, creator, date, target, content FROM " + this.table + " WHERE " + criteria + " = ? ORDER BY id DESC LIMIT " + String.valueOf(count);
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
