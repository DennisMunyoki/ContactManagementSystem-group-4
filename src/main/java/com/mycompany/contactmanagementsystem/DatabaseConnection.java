package com.mycompany.contactmanagementsystem;

import java.sql.*;

public class DatabaseConnection {
    // CHANGE THESE VALUES TO MATCH YOUR MYSQL SETUP
    private static final String URL = "jdbc:mysql://localhost:3306/contactdb?useSSL=false&serverTimezone=UTC";
    private static final String USERNAME = "root"; // Change to your MySQL username
    private static final String PASSWORD = ""; // Change to your MySQL password

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found", e);
        }
    }

    public static void createTable() {
        String usersTable = "CREATE TABLE IF NOT EXISTS users (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "first_name VARCHAR(50) NOT NULL," +
                "last_name VARCHAR(50) NOT NULL," +
                "username VARCHAR(50) UNIQUE NOT NULL," +
                "password VARCHAR(255) NOT NULL," +
                "profile_picture LONGBLOB," +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")";

        String contactsTable = "CREATE TABLE IF NOT EXISTS contacts (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(100) NOT NULL," +
                "phone VARCHAR(20) NOT NULL," +
                "email VARCHAR(100) NOT NULL," +
                "user_id INT," +
                "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE" +
                ")";

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute(usersTable);
            stmt.execute(contactsTable);
            System.out.println("Tables created/verified successfully");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}