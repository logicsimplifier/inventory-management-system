package com.inventoryapp.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {

    private static final String DB_URL = "jdbc:sqlite:inventory.db";

     public static Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(DB_URL);
            System.out.println("SQLite connected successfully.");
            createTablesIfNotExist(conn);
            return conn;
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
            return null;
        }
    }

    private static void createTablesIfNotExist(Connection conn) {
        try {
            Statement stmt = conn.createStatement();
            
            // Create users table
            String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL,
                    role TEXT NOT NULL DEFAULT 'STAFF',
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
                """;
            stmt.execute(createUsersTable);
            
            // Create products table
            String createProductsTable = """
                CREATE TABLE IF NOT EXISTS products (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    description TEXT,
                    price REAL NOT NULL,
                    quantity INTEGER NOT NULL DEFAULT 0,
                    category TEXT,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
                """;
            stmt.execute(createProductsTable); 
            stmt.close();
            
        } catch (SQLException e) {
            System.out.println("Failed to create tables: " + e.getMessage());
        }
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                System.out.println("SQLite connection closed.");
            }
        } catch (SQLException e) {
            System.out.println("Failed to close: " + e.getMessage());
        }
    }
}
