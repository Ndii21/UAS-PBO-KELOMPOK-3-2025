package com.Library.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class AdminLoginForm extends JFrame {

    private final Color PRIMARY_BLUE = new Color(33, 150, 243);
    private final Color DARK_BLUE = new Color(25, 118, 210);
    private final Color BG_LIGHT = new Color(248, 250, 255);
    private final Color TEXT_COLOR = new Color(50, 50, 50);

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton registerLinkButton; 
    private JLabel errorLabel;

    public AdminLoginForm() {
        setTitle("Login Admin - Perpustakaan MariMaca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 350); 
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
        JLabel titleLabel = new JLabel("LOGIN ADMIN", SwingConstants.CENTER);
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
        
        // Error Label
        errorLabel = new JLabel(" ", SwingConstants.CENTER); 
        errorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 10, 0, 10); 
        add(errorLabel, gbc);

        gbc.insets = new Insets(8, 10, 8, 10); 

        // Login Button
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(PRIMARY_BLUE);
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.setPreferredSize(new Dimension(100, 35));
        gbc.gridx = 0;
        gbc.gridy = 4; 
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE; 
        add(loginButton, gbc);

        // Register Link/Button
        registerLinkButton = new JButton("Belum punya akun admin? Register di sini");
        registerLinkButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        registerLinkButton.setForeground(DARK_BLUE); 
        registerLinkButton.setBorderPainted(false);
        registerLinkButton.setContentAreaFilled(false);
        registerLinkButton.setOpaque(false);
        registerLinkButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0;
        gbc.gridy = 5; 
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 10, 10, 10); 
        add(registerLinkButton, gbc);
    }

    private void addListeners() {

        loginButton.addActionListener(e -> performLogin());
        passwordField.addActionListener(e -> performLogin());

        // Listener untuk tombol/link registrasi admin
        registerLinkButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Tutup form login saat ini
                new AdminRegisterForm().setVisible(true); // Buka form registrasi admin
            }
        });
    }

    private void performLogin() {
        String username = usernameField.getText().trim();
        String passwordInput = new String(passwordField.getPassword());

        if (username.isEmpty() || passwordInput.isEmpty()) {
            errorLabel.setText("Username dan password tidak boleh kosong.");
            return;
        }
        errorLabel.setText(" "); // Bersihkan error sebelumnya

        String sql = "SELECT password, id FROM admin WHERE username = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String storedHashedPassword = rs.getString("password");
                

                // Verifikasi password menggunakan PasswordUtil
                if (PasswordUtil.verifyPassword(passwordInput, storedHashedPassword)) { 
                    JOptionPane.showMessageDialog(this, 
                        "Login berhasil! Selamat datang Admin.", 
                        "Sukses", 
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    dispose(); // Tutup AdminLoginForm
                    
                    // Buka AdminDashboardFrame
                    DashboardFrame dashboard = new DashboardFrame();
                    dashboard.setVisible(true);
                } else {
                    errorLabel.setText("Username atau password salah.");
                }
            } else {
                errorLabel.setText("Username atau password salah.");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            errorLabel.setText("Terjadi kesalahan database. Silakan coba lagi.");
        } catch (RuntimeException rex) { // Untuk menangkap error dari PasswordUtil jika ada
             rex.printStackTrace();
             errorLabel.setText("Terjadi kesalahan sistem saat verifikasi.");
        }
    }
}