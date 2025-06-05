package com.Library.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AdminRegisterForm extends JFrame {

    private final Color PRIMARY_BLUE = new Color(33, 150, 243);
    private final Color DARK_BLUE = new Color(25, 118, 210);
    private final Color BG_LIGHT = new Color(248, 250, 255);
    private final Color TEXT_COLOR = new Color(50, 50, 50);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backToLoginButton;
    private JLabel errorLabel;

    public AdminRegisterForm() {
        setTitle("Register Admin - Perpustakaan MariMaca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 400);
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_LIGHT);
        setLayout(new GridBagLayout());

        initComponents();
        addListeners();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 10, 8, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Title
        JLabel titleLabel = new JLabel("REGISTER ADMIN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_BLUE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(15, 10, 10, 10);
        add(titleLabel, gbc);

        gbc.insets = new Insets(8, 10, 8, 10);

        // Username Label
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        add(usernameLabel, gbc);

        // Username Field
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
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
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(passwordField, gbc);

        // Confirm Password Label
        JLabel confirmPasswordLabel = new JLabel("Konfirmasi Password:");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(confirmPasswordLabel, gbc);

        // Confirm Password Field
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(confirmPasswordField, gbc);

        // Error Label
        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 0, 10);
        add(errorLabel, gbc);

        gbc.insets = new Insets(8, 10, 8, 10);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(PRIMARY_BLUE);
        registerButton.setForeground(Color.WHITE);
        registerButton.setFocusPainted(false);
        registerButton.setPreferredSize(new Dimension(120, 35));
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        add(registerButton, gbc);

        // Back to Login Button
        backToLoginButton = new JButton("Sudah punya akun? Login di sini");
        backToLoginButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backToLoginButton.setForeground(DARK_BLUE);
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setContentAreaFilled(false);
        backToLoginButton.setOpaque(false);
        backToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 10, 10);
        add(backToLoginButton, gbc);
    }

    private void addListeners() {
        registerButton.addActionListener(e -> performRegister());

        backToLoginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                new AdminLoginForm().setVisible(true);
            }
        });
    }

    private void performRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Semua field harus diisi.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Password dan konfirmasi password tidak sama.");
            return;
        }

        if (password.length() < 6) {
            errorLabel.setText("Password minimal 6 karakter.");
            return;
        }

        errorLabel.setText(" ");

        String sql = "INSERT INTO admin (username, password) VALUES (?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            String hashedPassword = PasswordUtil.hashPassword(password);
            pstmt.setString(1, username);
            pstmt.setString(2, hashedPassword);

            int result = pstmt.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this,
                        "Admin berhasil didaftarkan! Silakan login.",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                dispose();
                new AdminLoginForm().setVisible(true);
            } else {
                errorLabel.setText("Gagal mendaftarkan admin.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            if (ex.getMessage().contains("Duplicate entry")) {
                errorLabel.setText("Username sudah digunakan.");
            } else {
                errorLabel.setText("Terjadi kesalahan database.");
            }
        }
    }
}