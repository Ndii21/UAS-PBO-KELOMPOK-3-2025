package com.Library.Welcome;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class Welcome extends JFrame {
    private JTextField namaField;
    private JTextField nomorField;

    public Welcome() {
        setTitle("Kunjungan Perpustakaan");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon bgImage = new ImageIcon(getClass().getResource("background.png"));
        ImageIcon logoIcon = new ImageIcon(getClass().getResource("logo.jpg"));

        JPanel backgroundPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bgImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };

        backgroundPanel.setLayout(new BoxLayout(backgroundPanel, BoxLayout.Y_AXIS));
        backgroundPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

        JLabel title = new JLabel("SELAMAT DATANG", SwingConstants.CENTER);
        title.setForeground(Color.WHITE);
        title.setFont(new Font("SansSerif", Font.BOLD, 24));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("DI PERPUSTAKAAN", SwingConstants.CENTER);
        subtitle.setForeground(Color.WHITE);
        subtitle.setFont(new Font("SansSerif", Font.BOLD, 20));
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instruksi = new JLabel("Jika Anda ingin mendaftarkan diri sebagai anggota Perpustakaan silahkan tanyakan ke petugas.");
        instruksi.setForeground(Color.WHITE);
        instruksi.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instruksi.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel instruksi2 = new JLabel("Silakan masukkan nama lengkap dan nomor telepon Anda.");
        instruksi2.setForeground(Color.WHITE);
        instruksi2.setFont(new Font("SansSerif", Font.PLAIN, 14));
        instruksi2.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel website = new JLabel("DATA KUNJUNGAN HARIAN", SwingConstants.CENTER);
        website.setForeground(Color.WHITE);
        website.setFont(new Font("SansSerif", Font.BOLD, 16));
        website.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel logo = new JLabel(new ImageIcon(logoIcon.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH)));
        logo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel inputPanel = new JPanel(new GridBagLayout());
        inputPanel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 20, 10, 20);
        gbc.gridy = 0;

        JPanel namaPanel = new JPanel();
        namaPanel.setLayout(new BoxLayout(namaPanel, BoxLayout.Y_AXIS));
        namaPanel.setOpaque(false);
        namaPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel namaLabel = new JLabel("Nama Lengkap", SwingConstants.CENTER);
        namaLabel.setForeground(Color.WHITE);
        namaLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        namaLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        namaField = createRoundedTextField("");
        namaField.setMaximumSize(new Dimension(200, 40));
        namaField.setAlignmentX(Component.CENTER_ALIGNMENT);

        namaPanel.add(namaLabel);
        namaPanel.add(Box.createVerticalStrut(5));
        namaPanel.add(namaField);

        gbc.gridx = 0;
        inputPanel.add(namaPanel, gbc);

        JPanel nomorPanel = new JPanel();
        nomorPanel.setLayout(new BoxLayout(nomorPanel, BoxLayout.Y_AXIS));
        nomorPanel.setOpaque(false);
        nomorPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel nomorLabel = new JLabel("Nomor Telepon", SwingConstants.CENTER);
        nomorLabel.setForeground(Color.WHITE);
        nomorLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        nomorLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        nomorField = createRoundedTextField("");
        nomorField.setMaximumSize(new Dimension(200, 40));
        nomorField.setAlignmentX(Component.CENTER_ALIGNMENT);

        nomorPanel.add(nomorLabel);
        nomorPanel.add(Box.createVerticalStrut(5));
        nomorPanel.add(nomorField);

        gbc.gridx = 1;
        inputPanel.add(nomorPanel, gbc);
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.setMaximumSize(new Dimension(600, 100));

        JButton submitButton = new JButton("TAMBAH") {
            protected void paintComponent(Graphics g) {
                if (getModel().isPressed()) {
                    g.setColor(new Color(30, 30, 30));
                } else {
                    g.setColor(new Color(50, 50, 50));
                }
                g.fillRoundRect(0, 0, getWidth(), getHeight(), 40, 40);
                super.paintComponent(g);
            }

            protected void paintBorder(Graphics g) {
                g.setColor(Color.DARK_GRAY);
                g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);
            }
        };
        submitButton.setForeground(Color.WHITE);
        submitButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        submitButton.setFocusPainted(false);
        submitButton.setContentAreaFilled(false);
        submitButton.setOpaque(false);
        submitButton.setPreferredSize(new Dimension(150, 40));
        submitButton.setMaximumSize(new Dimension(150, 40));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        submitButton.addActionListener(e -> {
            String nama = namaField.getText();
            String nomor = nomorField.getText();

            if (nama.isEmpty() || nomor.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Field tidak boleh kosong");
            } else {
                try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_marimaca_2", "root", "")) {
                    String query = "INSERT INTO pengunjung (nama, no_telepon) VALUES (?, ?)";
                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, nama);
                    stmt.setString(2, nomor);
                    stmt.executeUpdate();

                    stmt.close();
                    dispose();
                    new LibraryDashboard().setVisible(true);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(null, "Gagal menyimpan ke database: " + ex.getMessage());
                }
            }
        });

        backgroundPanel.add(title);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        backgroundPanel.add(subtitle);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        backgroundPanel.add(instruksi);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        backgroundPanel.add(instruksi2);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 5)));
        backgroundPanel.add(website);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(logo);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(inputPanel);
        backgroundPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        backgroundPanel.add(submitButton);

        setContentPane(backgroundPanel);
    }

    private JTextField createRoundedTextField(String hint) {
        JTextField field = new JTextField() {
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(255, 255, 255, 230));
                g.fillRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);
                super.paintComponent(g);
            }

            protected void paintBorder(Graphics g) {
                g.setColor(Color.LIGHT_GRAY);
                g.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, 40, 40);
            }
        };
        field.setFont(new Font("SansSerif", Font.PLAIN, 14));
        field.setHorizontalAlignment(SwingConstants.CENTER);
        field.setPreferredSize(new Dimension(200, 40));
        field.setMaximumSize(new Dimension(200, 40));
        field.setText("");
        field.setToolTipText(hint);
        field.setOpaque(false);
        return field;
    }
}