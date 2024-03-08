package org.courseRegistration.db;

import java.sql.*;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());

    // Database connection details
    private static final String URL = "jdbc:mysql://localhost:3306/CourseRegistration?useSSL=false";
    private static final String USER = "root";
    private static final String PASSWORD = "XTcyh991226!";

    public DatabaseManager() {
        try {
            // MySQL JDBC Driver class name
            Class.forName("com.mysql.cj.jdbc.Driver");
            LOGGER.log(Level.INFO, "MySQL JDBC Driver registered!");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "MySQL JDBC Driver registration failed!", e);
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Getting connection failed!", e);
            return null;
        }
    }

    public boolean createRecord(String table, String[] columns, Object... values) {
        if (columns.length != values.length) {
            LOGGER.log(Level.SEVERE, "Columns and values count mismatch");
            return false;
        }
        String columnNames = String.join(", ", columns);
        String placeholders = String.join(", ", Collections.nCopies(values.length, "?"));
        String sql = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columnNames, placeholders);
        try (Connection conn = getConnection()) {
            return executeSQL(conn, sql, values);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Closing connection failed", e);
            return false;
        }
    }

    public ResultSet readRecords(String table, String conditions, Object... parameters) {
        String sql = String.format("SELECT * FROM %s WHERE %s", table, conditions);
        Connection conn = getConnection();
        return executeSelectSQL(conn, sql, parameters);
        // The caller must ensure to close the ResultSet and the Connection
    }

    // more complicated SQL query to read the record
    public ResultSet readCustomRecords(String sql, Object... parameters) {
        Connection conn = getConnection();
        if (conn == null) return null;
        return executeSelectSQL(conn, sql, parameters);
    }

    public boolean updateRecord(String table, String updates, String conditions, Object... parameters) {
        String sql = String.format("UPDATE %s SET %s WHERE %s", table, updates, conditions);
        try (Connection conn = getConnection()) {
            return executeSQL(conn, sql, parameters);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Closing connection failed", e);
            return false;
        }
    }

    public boolean deleteRecord(String table, String conditions, Object... parameters) {
        String sql = String.format("DELETE FROM %s WHERE %s", table, conditions);
        try (Connection conn = getConnection()) {
            return executeSQL(conn, sql, parameters);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Closing connection failed", e);
            return false;
        }
    }

    private boolean executeSQL(Connection conn, String sql, Object... parameters) {
        if (conn == null) return false;
        try (PreparedStatement statement = conn.prepareStatement(sql)) {
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL operation failed", e);
            return false;
        }
    }

    private ResultSet executeSelectSQL(Connection conn, String sql, Object... parameters) {
        if (conn == null) return null;
        try {
            PreparedStatement statement = conn.prepareStatement(sql);
            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }
            return statement.executeQuery();
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "SQL select operation failed", e);
            try {
                conn.close();
            } catch (SQLException ex) {
                LOGGER.log(Level.SEVERE, "Closing connection failed", ex);
            }
            return null;
        }
    }
}
