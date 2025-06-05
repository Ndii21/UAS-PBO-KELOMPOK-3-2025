package com.Library.Admin;

import javax.swing.*;
import java.awt.*;

public class AdminLoginForm extends JFrame {

    private final Color PRIMARY_GREEN = new Color(67, 160, 71); // Similar to Anggota Login
    private final Color DARK_GREEN = new Color(46, 125, 50);
    private final Color BG_LIGHT = new Color(240, 255, 240); // Light background
    private final Color TEXT_COLOR = new Color(50, 50, 50);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel errorLabel;
    private AdminAuthDAO adminAuthDAO;

    public AdminLoginForm() {
        adminAuthDAO = new AdminAuthDAO();

        setTitle("Admin Login - Perpustakaan Mari Maca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1920, 1080); // Slightly smaller or adjust as needed
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_LIGHT);
        setLayout(new GridBagLayout());

        initComponents();
        addListeners();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15); // Adjusted insets for a cleaner look
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("ADMIN LOGIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_GREEN);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 15, 15, 15);
        add(titleLabel, gbc);

        gbc.insets = new Insets(10, 15, 10, 15); // Reset insets

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Explicitly set gridwidth
        add(usernameLabel, gbc);

        // Username Field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(usernameField, gbc);

        // Password Label
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(passwordLabel, gbc);

        // Password Field
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
         passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        // Error Label
        errorLabel = new JLabel(" ", SwingConstants.CENTER); // Start with a space to maintain layout
        errorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        errorLabel.setForeground(new Color(220, 20, 60)); // Crimson red for errors
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 15, 5, 15); // Reduced bottom inset
        add(errorLabel, gbc);

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(PRIMARY_GREEN);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(120, 40)); // Consistent button size
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginButton.setBorder(BorderFactory.createRaisedBevelBorder());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER)); // Panel to center the button
        buttonPanel.setBackground(BG_LIGHT);
        buttonPanel.add(loginButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE; // Don't stretch button panel
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 15, 20, 15); // Add some bottom padding
        add(buttonPanel, gbc);
    }

    private void addListeners() {
        loginButton.addActionListener(e -> performAdminLogin());
        passwordField.addActionListener(e -> performAdminLogin()); // Allow login on Enter in password field
    }

    private void performAdminLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Username dan password tidak boleh kosong.");
            return;
        }
        errorLabel.setText(" "); // Clear previous errors

        if (adminAuthDAO.validateAdmin(username, password)) {
            JOptionPane.showMessageDialog(this,
                    "Login Admin Berhasil!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            dispose(); // Close the login form
            new DashboardFrame().setVisible(true); // Open the admin dashboard
        } else {
            errorLabel.setText("Username atau password admin salah.");
        }
    }
}