package com.geoderp.geoplugin.Utility;

import java.util.ArrayList;

interface DatabaseInterface {
    public void connect();
    public void closeDatabase();
    public void createTable(String table, ArrayList<String[]> columns);
    public <T extends EntryInterface> void addItem(String table, T item);
    public <T extends EntryInterface> void updateItem(String table, T item);
    public void removeItem(String table, int id);
    public int findItem(String table, String criteria, String value);
    public ArrayList<?> getAllItems(String table);
    public ArrayList<?> getSelectItems(String table, String criteria, String value);
    public ArrayList<?> getRecentItems(String table);
    public String getType(String type);
}

// Tables
// Notes
//      ID integer autoincrement PK, creator string, date string, target string, content string
// Joined
//      ID integer autoincrement PK, player string, date string
// Playtime
//      ID integer autoincrement PK, player string, current integer, previous integer, lastseen bigint
// XP
//      ID integer autoincrement PK, player string, amount integer

// NEED VARIABLE TYPE CONVERTER