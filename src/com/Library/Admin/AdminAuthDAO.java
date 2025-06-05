
package com.Library.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminAuthDAO {
    private Connection connection;

    public AdminAuthDAO() {
        this.connection = DBConnection.getConnection(); // Uses the existing DBConnection
         if (this.connection == null) {
            System.err.println("FATAL: Database connection failed in AdminAuthDAO.");
            // Optionally throw an exception here to halt execution if connection is critical
        }
    }

    /**
     * Validates admin credentials against the database.
     * Assumes an 'admin_users' table with 'username' and 'password' (hashed) columns.
     *
     * @param username The admin's username.
     * @param password The admin's plain text password.
     * @return true if credentials are valid, false otherwise.
     */
    public boolean validateAdmin(String username, String password) {
        if (connection == null) {
            System.err.println("AdminAuthDAO: No database connection available.");
            return false;
        }
        String sql = "SELECT password FROM admin_users WHERE username = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                // Use PasswordUtil to verify the password
                return PasswordUtil.verifyPassword(password, storedHashedPassword);
            } else {
                return false; // Username not found
            }
        } catch (SQLException e) {
            System.err.println("SQL Error during admin validation: " + e.getMessage());
            e.printStackTrace();
            return false;
        } catch (RuntimeException e) {
            System.err.println("Hashing Error during admin validation: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Optional: Method to add a new admin (for setup purposes)
    // Ensure you hash the password before storing it.
    public boolean addAdmin(String username, String plainPassword) {
        if (connection == null) {
             System.err.println("AdminAuthDAO: No database connection available for adding admin.");
            return false;
        }
        String hashedPassword = PasswordUtil.hashPassword(plainPassword);
        String sql = "INSERT INTO admin_users (username, password) VALUES (?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error adding admin: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}