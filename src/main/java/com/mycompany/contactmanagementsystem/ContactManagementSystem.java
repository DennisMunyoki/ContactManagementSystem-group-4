package com.mycompany.contactmanagementsystem;

import javax.swing.*;

public class ContactManagementSystem {
    public static void main(String[] args) {
        // Set system look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Initialize database tables
        DatabaseConnection.createTable();

        // Start the modern login application
        SwingUtilities.invokeLater(() -> {
            new ModernLoginRegister().setVisible(true);
        });
    }
}