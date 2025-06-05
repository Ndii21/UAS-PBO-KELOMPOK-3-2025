package com.Library.Anggota;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import com.Library.Admin.PasswordUtil;

public class RegisterForm extends JFrame {

    private final Color PRIMARY_GREEN = new Color(67, 160, 71);
    private final Color DARK_GREEN = new Color(46, 125, 50);
    private final Color BG_LIGHT = new Color(248, 252, 248);
    private final Color TEXT_COLOR = new Color(50, 50, 50);

    private JTextField idAnggotaField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JButton registerButton;
    private JButton backToLoginButton;
    private JLabel errorLabel;

    public RegisterForm() {
        setTitle("Registrasi Akun Anggota - Perpustakaan MariMaca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 450); 
        setLocationRelativeTo(null);
        getContentPane().setBackground(BG_LIGHT);
        setLayout(new GridBagLayout());

        initComponents();
        addListeners();
    }

    private void initComponents() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8); 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("REGISTRASI AKUN", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        titleLabel.setForeground(DARK_GREEN);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        add(titleLabel, gbc);

        // ID Anggota
        JLabel idAnggotaLabel = new JLabel("ID Anggota:");
        idAnggotaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        idAnggotaLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1;
        add(idAnggotaLabel, gbc);
        idAnggotaField = new JTextField(20);
        idAnggotaField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 1;
        add(idAnggotaField, gbc);

        // Username
        JLabel usernameLabel = new JLabel("Username Baru:");
        usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        usernameLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 2;
        add(usernameLabel, gbc);
        usernameField = new JTextField(20);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 2;
        add(usernameField, gbc);

        // Password
        JLabel passwordLabel = new JLabel("Password Baru:");
        passwordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 3;
        add(passwordLabel, gbc);
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 3;
        add(passwordField, gbc);

        // Konfirmasi Password
        JLabel confirmPasswordLabel = new JLabel("Konfirmasi Password:");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        confirmPasswordLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 4;
        add(confirmPasswordLabel, gbc);
        confirmPasswordField = new JPasswordField(20);
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        gbc.gridx = 1; gbc.gridy = 4;
        add(confirmPasswordField, gbc);

        // Error Label
        errorLabel = new JLabel(" ", SwingConstants.CENTER);
        errorLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        errorLabel.setForeground(Color.RED);
        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
        add(errorLabel, gbc);

        // Register Button
        registerButton = new JButton("Register");
        registerButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        registerButton.setBackground(PRIMARY_GREEN);
        registerButton.setForeground(Color.WHITE);
        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(registerButton, gbc);

        // Back to Login Button
        backToLoginButton = new JButton("Kembali ke Login");
        backToLoginButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        backToLoginButton.setForeground(DARK_GREEN);
        backToLoginButton.setBorderPainted(false);
        backToLoginButton.setContentAreaFilled(false);
        backToLoginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 0; gbc.gridy = 7; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.CENTER;
        add(backToLoginButton, gbc);
    }

    private void addListeners() {
        registerButton.addActionListener(e -> performRegistration());
        backToLoginButton.addActionListener(e -> {
            this.dispose();
            new LoginForm().setVisible(true);
        });
    }

    private void performRegistration() {
        String idAnggotaStr = idAnggotaField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        // Validasi Input Kosong
        if (idAnggotaStr.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Semua field harus diisi!");
            return;
        }

        // Validasi Password Match
        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Password dan konfirmasi password tidak cocok!");
            return;
        }

        int idAnggota;
        try {
            idAnggota = Integer.parseInt(idAnggotaStr);
        } catch (NumberFormatException e) {
            errorLabel.setText("ID Anggota harus berupa angka!");
            return;
        }
        errorLabel.setText(" "); // Clear error

        try (Connection conn = DBConnection.getConnection()) {
            // 1. Cek apakah id_anggota valid (ada di tabel anggota)
            if (!isIdAnggotaValid(conn, idAnggota)) {
                errorLabel.setText("ID Anggota tidak ditemukan atau tidak valid.");
                return;
            }

            // 2. Cek apakah id_anggota sudah punya akun di tabel user
            if (isIdAnggotaRegistered(conn, idAnggota)) {
                errorLabel.setText("ID Anggota ini sudah memiliki akun terdaftar.");
                return;
            }

            // 3. Cek apakah username sudah digunakan
            if (isUsernameTaken(conn, username)) {
                errorLabel.setText("Username '" + username + "' sudah digunakan. Pilih yang lain.");
                return;
            }

            // Jika semua validasi lolos, lakukan registrasi
            String hashedPassword = PasswordUtil.hashPassword(password); // HASH PASSWORD!

            String sqlInsert = "INSERT INTO user (id_anggota, username, password) VALUES (?, ?, ?)";
            try (PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {
                pstmtInsert.setInt(1, idAnggota);
                pstmtInsert.setString(2, username);
                pstmtInsert.setString(3, hashedPassword);
                int rowsAffected = pstmtInsert.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Registrasi akun berhasil! Silakan login dengan username dan password baru Anda.",
                            "Registrasi Sukses", JOptionPane.INFORMATION_MESSAGE);
                    this.dispose(); // Tutup form registrasi
                    new LoginForm().setVisible(true); // Buka form login
                } else {
                    errorLabel.setText("Registrasi gagal. Silakan coba lagi.");
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            errorLabel.setText("Terjadi kesalahan database saat registrasi.");
        }
    }

    private boolean isIdAnggotaValid(Connection conn, int idAnggota) throws SQLException {
        String sql = "SELECT COUNT(*) FROM anggota WHERE id_anggota = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAnggota);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private boolean isIdAnggotaRegistered(Connection conn, int idAnggota) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user WHERE id_anggota = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, idAnggota);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }

    private boolean isUsernameTaken(Connection conn, String username) throws SQLException {
        String sql = "SELECT COUNT(*) FROM user WHERE username = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
}