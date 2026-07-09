package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DBConnection - Handles MySQL connection.
 * Automatically creates database "employee_management" and table "employees" if missing.
 */
public class DBConnection {

    // ---- Connection settings (as provided) ----
    private static final String BASE_URL = "jdbc:mysql://127.0.0.1:3306/";
    private static final String DB_URL   = "jdbc:mysql://127.0.0.1:3306/employee_management";
    private static final String USER     = "root";
    private static final String PASSWORD = "Ankit@789";

    /**
     * Ensures DB + table exist, then returns a live connection to employee_management.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Load MySQL driver (optional for modern JDBC, but safe)
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("Error: MySQL JDBC Driver not found.");
            throw new SQLException("MySQL JDBC Driver not found. Add mysql-connector-j to the classpath.", e);
        }

        // Step 1: create database if not exists using base URL
        try (Connection baseConn = DriverManager.getConnection(BASE_URL, USER, PASSWORD);
             Statement stmt = baseConn.createStatement()) {
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS employee_management");
        } catch (SQLException e) {
            System.err.println("Error connecting to MySQL server: " + e.getMessage());
            System.err.println("Make sure MySQL is running and credentials (root/Ankit@789) are correct.");
            throw e;
        }

        // Step 2: connect to the database
        Connection conn = DriverManager.getConnection(DB_URL, USER, PASSWORD);

        // Step 3: create employees table if not exists
        String createTable =
                "CREATE TABLE IF NOT EXISTS employees (" +
                "    employee_id INT PRIMARY KEY AUTO_INCREMENT," +
                "    name VARCHAR(100)," +
                "    department VARCHAR(100)," +
                "    designation VARCHAR(100)," +
                "    salary DOUBLE," +
                "    phone VARCHAR(20)," +
                "    email VARCHAR(100)," +
                "    address TEXT" +
                ")";
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createTable);
        }

        return conn;
    }
}
