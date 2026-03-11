package com.mycompany.contactmanagementsystem;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ModernContactManagementGUI extends JFrame {
    private String currentUser;
    private JTable contactsTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private Color primaryColor = new Color(79, 70, 229);
    private Color accentColor = new Color(16, 185, 129);
    private Color dangerColor = new Color(239, 68, 68);
    private Color backgroundColor = new Color(249, 250, 251);
    private Color searchButtonColor = new Color(59, 130, 246); 

    public ModernContactManagementGUI(String username) {
        this.currentUser = username;
        setTitle("ContactMaster - Contact Management");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        initUI();
        loadContacts();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        mainPanel.add(createTopBar(), BorderLayout.NORTH);
        mainPanel.add(createTablePanel(), BorderLayout.CENTER);

        add(mainPanel);
    }

    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(Color.WHITE);
        topBar.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230, 230, 230)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));

        // Left: Welcome message
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        welcomePanel.setOpaque(false);

        JLabel welcomeIcon = new JLabel("👋");
        welcomeIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));

        JLabel welcomeText = new JLabel("Welcome, " + currentUser);
        welcomeText.setFont(new Font("Segoe UI", Font.BOLD, 20));
        welcomeText.setForeground(new Color(17, 24, 39));

        welcomePanel.add(welcomeIcon);
        welcomePanel.add(welcomeText);

        // Right: Action buttons
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setOpaque(false);

        // Search field
        searchField = new JTextField(15);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        searchField.setToolTipText("Enter name to search");

        // Search button (now more visible)
        JButton searchButton = new JButton("🔍 Search");
        searchButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        searchButton.setBackground(searchButtonColor);
        searchButton.setForeground(Color.WHITE);
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        searchButton.setPreferredSize(new Dimension(90, 35));
        searchButton.setToolTipText("Search contacts by name");
        searchButton.addActionListener(e -> searchContacts());

        // Refresh button (new)
        JButton refreshButton = new JButton("↻ View Contacts");
        refreshButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        refreshButton.setBackground(new Color(107, 114, 128)); // Gray
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setBorderPainted(false);
        refreshButton.setFocusPainted(false);
        refreshButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        refreshButton.setPreferredSize(new Dimension(90, 35));
        refreshButton.setToolTipText("Show all contacts");
        refreshButton.addActionListener(e -> refreshContacts());

        // Add contact button
        JButton addButton = createStyledButton("+ Add Contact", accentColor);
        addButton.setToolTipText("Add a new contact");
        addButton.addActionListener(e -> showAddContactDialog());

        // Logout button
        JButton logoutButton = createStyledButton("Logout", dangerColor);
        logoutButton.setToolTipText("Logout from your account");
        logoutButton.addActionListener(e -> logout());

        actionPanel.add(searchField);
        actionPanel.add(searchButton);
        actionPanel.add(refreshButton);
        actionPanel.add(addButton);
        actionPanel.add(logoutButton);

        topBar.add(welcomePanel, BorderLayout.WEST);
        topBar.add(actionPanel, BorderLayout.EAST);

        return topBar;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        String[] columns = {"Name", "Phone", "Email", "Actions"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 3;
            }
        };

        contactsTable = new JTable(tableModel);
        contactsTable.setRowHeight(50);
        contactsTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contactsTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        contactsTable.getTableHeader().setBackground(Color.WHITE);
        contactsTable.setShowGrid(false);
        contactsTable.setIntercellSpacing(new Dimension(0, 0));

        contactsTable.getColumnModel().getColumn(0).setPreferredWidth(200);
        contactsTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        contactsTable.getColumnModel().getColumn(2).setPreferredWidth(250);
        contactsTable.getColumnModel().getColumn(3).setPreferredWidth(200);

        contactsTable.getColumnModel().getColumn(3).setCellRenderer(new ActionsRenderer());
        contactsTable.getColumnModel().getColumn(3).setCellEditor(new ActionsEditor());

        JScrollPane scrollPane = new JScrollPane(contactsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Color.WHITE);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    // New method to refresh (show all contacts)
    private void refreshContacts() {
        searchField.setText(""); // Clear search field
        loadContacts(); // Reload all contacts
    }

    // Search contacts
    private void searchContacts() {
        String searchTerm = searchField.getText().trim();
        if (searchTerm.isEmpty()) {
            refreshContacts(); // If empty, just show all
            return;
        }

        tableModel.setRowCount(0);

        String sql = "SELECT * FROM contacts WHERE user_id = (SELECT id FROM users WHERE username = ?) AND LOWER(name) LIKE ? ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentUser);
            pstmt.setString(2, "%" + searchTerm.toLowerCase() + "%");
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int id = rs.getInt("id");

                tableModel.addRow(new Object[]{name, phone, email, id});
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "No contacts found matching \"" + searchTerm + "\"");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error searching contacts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Show add contact dialog
    private void showAddContactDialog() {
        JDialog dialog = new JDialog(this, "Add New Contact", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextField nameField = new JTextField(20);
        JTextField phoneField = new JTextField(20);
        JTextField emailField = new JTextField(20);

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Name:"), gbc);
        gbc.gridx = 1;
        formPanel.add(nameField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Phone:"), gbc);
        gbc.gridx = 1;
        formPanel.add(phoneField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        formPanel.add(emailField, gbc);

        dialog.add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            if (addContactToDatabase(nameField.getText(), phoneField.getText(), emailField.getText())) {
                loadContacts();
                dialog.dispose();
                JOptionPane.showMessageDialog(this, "Contact added successfully!");
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }

    // Add contact to database
    private boolean addContactToDatabase(String name, String phone, String email) {
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        String sql = "INSERT INTO contacts (name, phone, email, user_id) VALUES (?, ?, ?, (SELECT id FROM users WHERE username = ?))";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setString(4, currentUser);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error adding contact: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    // Load all contacts for current user
    private void loadContacts() {
        tableModel.setRowCount(0);

        String sql = "SELECT * FROM contacts WHERE user_id = (SELECT id FROM users WHERE username = ?) ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, currentUser);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String name = rs.getString("name");
                String phone = rs.getString("phone");
                String email = rs.getString("email");
                int id = rs.getInt("id");

                tableModel.addRow(new Object[]{name, phone, email, id});
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading contacts: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Edit contact
    private void editContact(int contactId) {
        String sql = "SELECT * FROM contacts WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, contactId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String currentName = rs.getString("name");
                String currentPhone = rs.getString("phone");
                String currentEmail = rs.getString("email");

                JTextField nameField = new JTextField(currentName);
                JTextField phoneField = new JTextField(currentPhone);
                JTextField emailField = new JTextField(currentEmail);

                Object[] fields = {
                    "Name:", nameField,
                    "Phone:", phoneField,
                    "Email:", emailField
                };

                int result = JOptionPane.showConfirmDialog(this, fields, "Edit Contact", JOptionPane.OK_CANCEL_OPTION);

                if (result == JOptionPane.OK_OPTION) {
                    updateContact(contactId, nameField.getText(), phoneField.getText(), emailField.getText());
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error editing contact: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Update contact
    private void updateContact(int contactId, String name, String phone, String email) {
        if (name.isEmpty() || phone.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All fields are required!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sql = "UPDATE contacts SET name = ?, phone = ?, email = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            pstmt.setString(3, email);
            pstmt.setInt(4, contactId);

            pstmt.executeUpdate();
            loadContacts();
            JOptionPane.showMessageDialog(this, "Contact updated successfully!");

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error updating contact: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Delete contact
    private void deleteContact(int contactId) {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this contact?", "Delete Contact", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            String sql = "DELETE FROM contacts WHERE id = ?";

            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setInt(1, contactId);
                pstmt.executeUpdate();

                loadContacts();
                JOptionPane.showMessageDialog(this, "Contact deleted successfully!");

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error deleting contact: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Logout
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            dispose();
            SwingUtilities.invokeLater(() -> new ModernLoginRegister().setVisible(true));
        }
    }

    // Helper to create styled buttons (for Add and Logout)
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 35));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    // Inner class for rendering action buttons in table
    class ActionsRenderer extends JPanel implements TableCellRenderer {
        public ActionsRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 10));
            setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            removeAll();

            JButton editBtn = new JButton("Edit");
            editBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            editBtn.setBackground(new Color(59, 130, 246));
            editBtn.setForeground(Color.WHITE);
            editBtn.setBorderPainted(false);

            JButton deleteBtn = new JButton("Delete");
            deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            deleteBtn.setBackground(dangerColor);
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setBorderPainted(false);

            add(editBtn);
            add(deleteBtn);

            return this;
        }
    }

    // Inner class for editing action buttons in table
    class ActionsEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private int currentRow;

        public ActionsEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 10));
            panel.setBackground(Color.WHITE);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            panel.removeAll();
            currentRow = row;

            JButton editBtn = new JButton("Edit");
            editBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            editBtn.setBackground(new Color(59, 130, 246));
            editBtn.setForeground(Color.WHITE);
            editBtn.setBorderPainted(false);

            JButton deleteBtn = new JButton("Delete");
            deleteBtn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
            deleteBtn.setBackground(dangerColor);
            deleteBtn.setForeground(Color.WHITE);
            deleteBtn.setBorderPainted(false);

            editBtn.addActionListener(e -> {
                int contactId = (int) tableModel.getValueAt(currentRow, 3);
                editContact(contactId);
                fireEditingStopped();
            });

            deleteBtn.addActionListener(e -> {
                int contactId = (int) tableModel.getValueAt(currentRow, 3);
                deleteContact(contactId);
                fireEditingStopped();
            });

            panel.add(editBtn);
            panel.add(deleteBtn);

            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return null;
        }
    }
}