package com.geoderp.geoplugin.Utility;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

interface EntryInterface {
    public PreparedStatement getSQLInsert(Connection db);
    public PreparedStatement getSQLUpdate(Connection db);
    public PreparedStatement getSQLSelectByCriteria(Connection db, String criteria, String value);
    public PreparedStatement getSQLSelectRecent(Connection db, int count);
    public PreparedStatement getSQLSelectRecentByCriteria(Connection db, String criteria, String value, int count);
    public String getTable();
    public ArrayList<?> handleResults(ResultSet results);
}
