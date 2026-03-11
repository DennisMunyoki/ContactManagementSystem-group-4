package com.mycompany.contactmanagementsystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ModernLoginRegister extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private Color primaryColor = new Color(79, 70, 229);
    private Color secondaryColor = new Color(67, 56, 202);
    private Color textColor = new Color(17, 24, 39);

    public ModernLoginRegister() {
        setTitle("Contact Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());

        // Left Panel - Branding
        JPanel brandPanel = createBrandPanel();
        add(brandPanel, BorderLayout.WEST);

        // Right Panel - Forms
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setPreferredSize(new Dimension(450, 600));

        mainPanel.add(createLoginPanel(), "login");
        mainPanel.add(createRegisterPanel(), "register");

        add(mainPanel, BorderLayout.EAST);
        cardLayout.show(mainPanel, "login");
    }

    private JPanel createBrandPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                GradientPaint gp = new GradientPaint(0, 0, primaryColor, getWidth(), getHeight(), secondaryColor);
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        panel.setPreferredSize(new Dimension(450, 600));
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel iconLabel = new JLabel("📇");
        iconLabel.setFont(new Font("Segoe UI", Font.PLAIN, 80));
        iconLabel.setForeground(Color.WHITE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        panel.add(iconLabel, gbc);

        JLabel appName = new JLabel("ContactMaster");
        appName.setFont(new Font("Segoe UI", Font.BOLD, 36));
        appName.setForeground(Color.WHITE);

        gbc.gridy = 1;
        panel.add(appName, gbc);

        JLabel tagline = new JLabel("Manage your contacts with style");
        tagline.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tagline.setForeground(new Color(255, 255, 255, 200));

        gbc.gridy = 2;
        gbc.insets = new Insets(10, 0, 0, 0);
        panel.add(tagline, gbc);

        String[] features = {"✓ Secure Cloud Storage", "✓ Easy Contact Management", "✓ Modern Interface"};
        gbc.gridy = 3;
        gbc.insets = new Insets(40, 0, 0, 0);

        JPanel featuresPanel = new JPanel(new GridLayout(3, 1, 0, 10));
        featuresPanel.setOpaque(false);

        for (String feature : features) {
            JLabel featureLabel = new JLabel(feature);
            featureLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
            featureLabel.setForeground(new Color(255, 255, 255, 220));
            featuresPanel.add(featureLabel);
        }

        panel.add(featuresPanel, gbc);
        return panel;
    }

    private JPanel createLoginPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 40, 10, 40);

        JLabel welcomeLabel = new JLabel("Welcome Back!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(textColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(welcomeLabel, gbc);

        JLabel subtitle = new JLabel("Please login to your account");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 40, 30, 40);
        panel.add(subtitle, gbc);

        // Username Field
        gbc.insets = new Insets(10, 40, 10, 40);
        gbc.gridy = 2;
        panel.add(createStyledLabel("Username"), gbc);

        gbc.gridy = 3;
        JTextField usernameField = createStyledTextField();
        panel.add(usernameField, gbc);

        // Password Field
        gbc.gridy = 4;
        panel.add(createStyledLabel("Password"), gbc);

        gbc.gridy = 5;
        JPasswordField passwordField = createStyledPasswordField();
        panel.add(passwordField, gbc);

        // Login Button
        gbc.gridy = 6;
        gbc.insets = new Insets(20, 40, 10, 40);
        JButton loginButton = createStyledButton("Login", primaryColor);
        panel.add(loginButton, gbc);

        // Register Link
        gbc.gridy = 7;
        gbc.insets = new Insets(10, 40, 10, 40);
        JPanel registerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        registerPanel.setOpaque(false);

        JLabel noAccount = new JLabel("Don't have an account?");
        noAccount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        noAccount.setForeground(Color.GRAY);

        JLabel registerLink = new JLabel("Sign Up");
        registerLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        registerLink.setForeground(primaryColor);
        registerLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        registerPanel.add(noAccount);
        registerPanel.add(registerLink);
        panel.add(registerPanel, gbc);

        // Login Action
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (authenticateUser(username, password)) {
                JOptionPane.showMessageDialog(this, "Login Successful!");
                openContactManagement(username);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        registerLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "register");
            }
        });

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 40, 8, 40);

        JLabel createLabel = new JLabel("Create Account");
        createLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        createLabel.setForeground(textColor);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(createLabel, gbc);

        JLabel subtitle = new JLabel("Fill in your details to get started");
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitle.setForeground(Color.GRAY);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 40, 15, 40);
        panel.add(subtitle, gbc);

        // First Name
        gbc.insets = new Insets(5, 40, 5, 40);
        gbc.gridy = 2;
        panel.add(createStyledLabel("First Name"), gbc);

        gbc.gridy = 3;
        JTextField firstNameField = createStyledTextField();
        panel.add(firstNameField, gbc);

        // Last Name
        gbc.gridy = 4;
        panel.add(createStyledLabel("Last Name"), gbc);

        gbc.gridy = 5;
        JTextField lastNameField = createStyledTextField();
        panel.add(lastNameField, gbc);

        // Username
        gbc.gridy = 6;
        panel.add(createStyledLabel("Username"), gbc);

        gbc.gridy = 7;
        JTextField usernameField = createStyledTextField();
        panel.add(usernameField, gbc);

        // Password
        gbc.gridy = 8;
        panel.add(createStyledLabel("Password"), gbc);

        gbc.gridy = 9;
        JPasswordField passwordField = createStyledPasswordField();
        panel.add(passwordField, gbc);

        // Retype Password
        gbc.gridy = 10;
        panel.add(createStyledLabel("Retype Password"), gbc);

        gbc.gridy = 11;
        JPasswordField retypeField = createStyledPasswordField();
        panel.add(retypeField, gbc);

        // Create Account Button
        gbc.gridy = 12;
        gbc.insets = new Insets(15, 40, 10, 40);
        JButton createButton = createStyledButton("Create Account", primaryColor);
        panel.add(createButton, gbc);

        // Login Link
        gbc.gridy = 13;
        gbc.insets = new Insets(5, 40, 10, 40);
        JPanel loginPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        loginPanel.setOpaque(false);

        JLabel haveAccount = new JLabel("Already have an account?");
        haveAccount.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        haveAccount.setForeground(Color.GRAY);

        JLabel loginLink = new JLabel("Login");
        loginLink.setFont(new Font("Segoe UI", Font.BOLD, 12));
        loginLink.setForeground(primaryColor);
        loginLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        loginPanel.add(haveAccount);
        loginPanel.add(loginLink);
        panel.add(loginPanel, gbc);

        // Register Action
        createButton.addActionListener(e -> {
            String firstName = firstNameField.getText();
            String lastName = lastNameField.getText();
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String retype = new String(retypeField.getPassword());

            if (firstName.isEmpty() || lastName.isEmpty() || username.isEmpty() ||
                password.isEmpty() || retype.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(retype)) {
                JOptionPane.showMessageDialog(this, "Passwords do not match!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (registerUser(firstName, lastName, username, password)) {
                JOptionPane.showMessageDialog(this, "Registration Successful! Please login.");
                cardLayout.show(mainPanel, "login");
            }
        });

        loginLink.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                cardLayout.show(mainPanel, "login");
            }
        });

        return panel;
    }

    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        label.setForeground(textColor);
        return label;
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setPreferredSize(new Dimension(200, 35));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (getModel().isPressed()) {
                    g2.setColor(bgColor.darker());
                } else if (getModel().isRollover()) {
                    g2.setColor(bgColor.brighter());
                } else {
                    g2.setColor(bgColor);
                }

                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(200, 40));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        return button;
    }

    private boolean authenticateUser(String username, String password) {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            return rs.next();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean registerUser(String firstName, String lastName, String username, String password) {
        String sql = "INSERT INTO users (first_name, last_name, username, password) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, firstName);
            pstmt.setString(2, lastName);
            pstmt.setString(3, username);
            pstmt.setString(4, password);

            pstmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) {
                JOptionPane.showMessageDialog(this, "Username already exists!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                e.printStackTrace();
            }
            return false;
        }
    }

    private void openContactManagement(String username) {
        SwingUtilities.invokeLater(() -> {
            new ModernContactManagementGUI(username).setVisible(true);
        });
    }
}