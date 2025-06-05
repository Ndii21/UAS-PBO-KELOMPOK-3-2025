package com.Library.Welcome;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LibraryDashboard extends JFrame {
    private final Color PRIMARY_GREEN = new Color(67, 160, 71);
    private final Color DARK_GREEN = new Color(46, 125, 50);
    private final Color SOFT_GREEN = new Color(129, 199, 132);
    private final Color BG_LIGHT = new Color(248, 252, 248);
    private final Color HOVER_GREEN = new Color(76, 175, 80);

    private JTextField searchField;
    private JPanel pengumumanPanel;
    private JPanel bukuPanel;
    private JTabbedPane tabbedPane;

    public LibraryDashboard() {
        setTitle("Dashboard Perpustakaan");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(DARK_GREEN);
        topPanel.setPreferredSize(new Dimension(0, 80));
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));

        JLabel logoLabel = new JLabel("MARIMACA");
        logoLabel.setFont(new Font("SansSerif", Font.BOLD, 26));
        logoLabel.setForeground(Color.WHITE);
        ImageIcon logoIcon = new ImageIcon("logo.jpg");
        if (logoIcon.getImageLoadStatus() == MediaTracker.COMPLETE) {
            logoLabel.setIcon(new ImageIcon(logoIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH)));
            logoLabel.setIconTextGap(15);
        }
        topPanel.add(logoLabel, BorderLayout.WEST);

        JPanel rightTopPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        rightTopPanel.setBackground(DARK_GREEN);

        searchField = new JTextField(30);
        searchField.setFont(new Font("SansSerif", Font.PLAIN, 14));
        JButton searchButton = new JButton("Search");
        searchButton.setBackground(PRIMARY_GREEN);
        searchButton.setForeground(Color.WHITE);
        searchButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        searchButton.setFocusPainted(false);

        searchButton.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                searchButton.setBackground(HOVER_GREEN);
            }
            public void mouseExited(MouseEvent evt) {
                searchButton.setBackground(PRIMARY_GREEN);
            }
        });

        JButton backButton = new JButton("Kembali");
        backButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        backButton.setBackground(Color.RED);
        backButton.setForeground(Color.WHITE);
        backButton.setFocusPainted(false);
        backButton.addActionListener(e -> {
            new Welcome().setVisible(true);
            this.dispose();
        });

        rightTopPanel.add(searchField);
        rightTopPanel.add(searchButton);
        rightTopPanel.add(backButton);
        topPanel.add(rightTopPanel, BorderLayout.EAST);

        JLabel titleLabel = new JLabel("Dashboard Perpustakaan");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 10));

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("SansSerif", Font.BOLD, 16));

        pengumumanPanel = new JPanel(new GridLayout(0, 2, 20, 20));
        pengumumanPanel.setBackground(BG_LIGHT);
        pengumumanPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane pengumumanScroll = new JScrollPane(pengumumanPanel);
        pengumumanScroll.setBorder(null);
        pengumumanScroll.getVerticalScrollBar().setUnitIncrement(16);
        tabbedPane.addTab("Pengumuman", pengumumanScroll);

        // --- Changes for bukuPanel ---
        bukuPanel = new JPanel(new GridLayout(0, 4, 15, 15)); // Changed to 4 columns, reduced gaps
        bukuPanel.setBackground(BG_LIGHT);
        bukuPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane bukuScroll = new JScrollPane(bukuPanel);
        bukuScroll.setBorder(null);
        bukuScroll.getVerticalScrollBar().setUnitIncrement(16);
        tabbedPane.addTab("Daftar Pustaka", bukuScroll);

        searchButton.addActionListener(e -> {
            String keyword = searchField.getText();
            if (tabbedPane.getSelectedIndex() == 0) {
                loadPengumuman(keyword);
            } else {
                loadBuku(keyword);
            }
        });

        tabbedPane.addChangeListener(e -> {
            String keyword = searchField.getText();
            if (tabbedPane.getSelectedIndex() == 0) {
                loadPengumuman(keyword);
            } else {
                loadBuku(keyword);
            }
        });

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(BG_LIGHT);
        topWrapper.add(topPanel, BorderLayout.NORTH);
        topWrapper.add(titleLabel, BorderLayout.SOUTH);

        mainPanel.add(topWrapper, BorderLayout.NORTH);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);

        getContentPane().add(mainPanel);
        loadPengumuman("");
        loadBuku("");
    }

    private void loadPengumuman(String keyword) {
        pengumumanPanel.removeAll();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_marimaca_2", "root", "")) {
            String query = "SELECT * FROM pengumuman WHERE judul LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, "%" + keyword + "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                JPanel card = createCard(
                    rs.getString("judul"),
                    rs.getString("isi"),
                    rs.getDate("tanggal_dibuat").toString()
                );
                pengumumanPanel.add(card);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat pengumuman: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        pengumumanPanel.revalidate();
        pengumumanPanel.repaint();
    }

    private void loadBuku(String keyword) {
        bukuPanel.removeAll();
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_marimaca_2", "root", "")) {
            String query = "SELECT * FROM buku WHERE judul_buku LIKE ? OR author LIKE ? OR publisher LIKE ? OR type LIKE ? OR location LIKE ? OR status LIKE ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            for (int i = 1; i <= 6; i++) stmt.setString(i, "%" + keyword + "%");

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                JPanel card = createBookCard(
                    rs.getString("judul_buku"),
                    rs.getString("author"),
                    rs.getString("publisher"),
                    rs.getString("type"),
                    rs.getString("location"),
                    rs.getString("status")
                );
                bukuPanel.add(card);
            }

            rs.close();
            stmt.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal memuat daftar buku: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        bukuPanel.revalidate();
        bukuPanel.repaint();
    }

    private JPanel createCard(String title, String content, String date) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SOFT_GREEN, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        card.setMaximumSize(new Dimension(400, 200));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JTextArea contentArea = new JTextArea(content);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setEditable(false);
        contentArea.setOpaque(false);
        contentArea.setFont(new Font("SansSerif", Font.PLAIN, 14));
        contentArea.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel dateLabel = new JLabel(date);
        dateLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        dateLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        dateLabel.setForeground(Color.GRAY);

        card.add(Box.createVerticalStrut(5));
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(contentArea);
        card.add(Box.createVerticalGlue());
        card.add(dateLabel);

        return card;
    }

    private JPanel createBookCard(String title, String author, String publisher, String type, String location, String status) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(SOFT_GREEN, 1),
            BorderFactory.createEmptyBorder(10, 10, 10, 10))); // Reduced padding
        card.setMaximumSize(new Dimension(250, 180)); // Smaller dimensions
        card.setPreferredSize(new Dimension(250, 180)); // Set preferred size as well for consistent layout

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 14)); // Smaller font
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel authorLabel = new JLabel("Penulis: " + author);
        JLabel publisherLabel = new JLabel("Penerbit: " + publisher);
        JLabel typeLabel = new JLabel("Jenis: " + type);
        JLabel locationLabel = new JLabel("Lokasi: " + location);
        JLabel statusLabel = new JLabel("Status: " + status);

        for (JLabel label : new JLabel[]{authorLabel, publisherLabel, typeLabel, locationLabel, statusLabel}) {
            label.setFont(new Font("SansSerif", Font.PLAIN, 12)); // Smaller font
            label.setAlignmentX(Component.LEFT_ALIGNMENT);
        }

        statusLabel.setFont(new Font("SansSerif", Font.BOLD, 12)); // Smaller font
        if (status.equalsIgnoreCase("Tersedia")) {
            statusLabel.setForeground(new Color(0, 100, 0));
        } else if (status.equalsIgnoreCase("Dipinjam")) {
            statusLabel.setForeground(new Color(139, 0, 0));
        } else {
            statusLabel.setForeground(Color.BLACK);
        }

        card.add(Box.createVerticalStrut(3)); // Reduced strut
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(7)); // Reduced strut
        card.add(authorLabel);
        card.add(publisherLabel);
        card.add(typeLabel);
        card.add(locationLabel);
        card.add(Box.createVerticalGlue());
        card.add(statusLabel);

        return card;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LibraryDashboard dashboard = new LibraryDashboard();
            dashboard.setVisible(true);
            dashboard.setLocationRelativeTo(null);
        });
    }
}