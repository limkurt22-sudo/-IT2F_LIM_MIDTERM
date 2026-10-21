package com.lim.fleet.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {

    private static final String URL = "jdbc:sqlite:scenario.db";

    private DatabaseConnection() {
    }

    /** Opens a connection with foreign keys enforced. */
    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(URL);
        try (Statement st = conn.createStatement()) {
            st.execute("PRAGMA foreign_keys = ON;");
        }
        return conn;
    }

    /** Creates the vehicles table if it does not exist yet. */
    public static void initialize() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS vehicles (
                    ID         INTEGER PRIMARY KEY AUTOINCREMENT,
                    MODEL_NAME TEXT NOT NULL,
                    PLATE_NO   TEXT NOT NULL UNIQUE,
                    DAILY_RATE REAL NOT NULL CHECK (DAILY_RATE > 0),
                    STATUS     TEXT NOT NULL CHECK (STATUS IN ('AVAILABLE', 'RENTED'))
                )
                """;
        try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
            st.execute(sql);
        }
    }
}
