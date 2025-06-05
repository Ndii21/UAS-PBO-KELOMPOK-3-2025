package com.Library.Anggota;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

public class DashboardFrame extends JFrame {

    private final Color PRIMARY_GREEN = new Color(67, 160, 71);
    private final Color DARK_GREEN = new Color(46, 125, 50);
    private final Color BG_LIGHT = new Color(248, 252, 248);
    private final Color SIDEBAR_GREEN = new Color(56, 142, 60);
    private final Color HOVER_GREEN = new Color(76, 175, 80);
    private final Color CARD_BG = new Color(255, 255, 255);
    private final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private final Color TEXT_SECONDARY = new Color(117, 117, 117);
    private final Color BORDER_COLOR = new Color(230, 230, 230);
    private final Color SUCCESS_GREEN = new Color(56, 142, 60);
    private final Color ERROR_RED = new Color(244, 67, 54);

    private JPanel mainPanel;
    private JPanel contentPanel;
    private CardLayout cardLayout;

    // Member data variables
    private String idAnggotaDisplay;
    private String namaAnggota;
    private String alamatAnggota;
    private String noTeleponAnggota;
    private String tanggalDaftarAnggota;
    private String emailAnggota;
    private String statusAktifAnggota;

    private int loggedInMemberId;
    
    // Sidebar buttons 
    private JButton homeButton;
    private JButton bukuButton;
    private JButton riwayatButton;
    private JButton profilButton;

    public DashboardFrame(int memberId) {
        this.loggedInMemberId = memberId;
        loadMemberData(this.loggedInMemberId);

        initializeFrame();
        setupComponents();
    }

    private void initializeFrame() {
        setTitle("Dashboard Anggota - Perpustakaan MariMaca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 700);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1000, 600));
        getContentPane().setBackground(BG_LIGHT);
    }

    private void setupComponents() {
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BG_LIGHT);
        add(mainPanel);

        JPanel sidebarPanel = createModernSidebarPanel();
        mainPanel.add(sidebarPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(BG_LIGHT);
        contentPanel.setBorder(new EmptyBorder(25, 25, 25, 25));

        JPanel homeContentPanel = createModernHomePanel();
        contentPanel.add(homeContentPanel, "Home");
        BukuViewPanel bukuPanel = new BukuViewPanel();
        contentPanel.add(bukuPanel, "Buku");
        
        RiwayatPanel riwayatPanel = new RiwayatPanel(loggedInMemberId); 
        contentPanel.add(riwayatPanel, "Riwayat");

        PengumumanPanel pengumumanPanel = new PengumumanPanel();
        contentPanel.add(pengumumanPanel, "Pengumuman");

        mainPanel.add(contentPanel, BorderLayout.CENTER);
        cardLayout.show(contentPanel, "Home");
    }

    private void loadMemberData(int memberId) {
        String sql = "SELECT id_anggota, nama, alamat, no_telepon, tanggal_daftar, email, status_aktif FROM anggota WHERE id_anggota = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, memberId);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                this.idAnggotaDisplay = String.valueOf(rs.getInt("id_anggota"));
                this.namaAnggota = rs.getString("nama");
                this.alamatAnggota = rs.getString("alamat");
                this.noTeleponAnggota = rs.getString("no_telepon");
                
                java.sql.Date dbSqlDate = rs.getDate("tanggal_daftar");
                if (dbSqlDate != null) {
                    SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
                    this.tanggalDaftarAnggota = sdf.format(dbSqlDate);
                } else {
                    this.tanggalDaftarAnggota = "Tanggal tidak tersedia";
                }
                this.emailAnggota = rs.getString("email");
                
                int statusInt = rs.getInt("status_aktif");
                this.statusAktifAnggota = (statusInt == 1) ? "Aktif" : "Tidak Aktif";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Gagal mengambil data anggota: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private JPanel createModernSidebarPanel() {
        JPanel sidebarPanel = new JPanel();
        sidebarPanel.setBackground(SIDEBAR_GREEN);
        sidebarPanel.setLayout(new BoxLayout(sidebarPanel, BoxLayout.Y_AXIS));
        sidebarPanel.setPreferredSize(new Dimension(260, getHeight()));
        sidebarPanel.setBorder(new EmptyBorder(20, 0, 20, 0));

        // Logo/Header Section
        JPanel headerPanel = createSidebarHeader();
        sidebarPanel.add(headerPanel);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Navigation Buttons
        homeButton = createModernSidebarButton("🏠", "Dashboard", true);
        homeButton.addActionListener(e -> {
            setActiveButton(homeButton);
            cardLayout.show(contentPanel, "Home");
        });
        sidebarPanel.add(homeButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        bukuButton = createModernSidebarButton("📚", "Buku Tersedia", false);
        bukuButton.addActionListener(e -> {
            setActiveButton(bukuButton);
            cardLayout.show(contentPanel, "Buku");
        });
        sidebarPanel.add(bukuButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        riwayatButton = createModernSidebarButton("📖", "Riwayat Pinjaman", false);
        riwayatButton.addActionListener(e -> {
            setActiveButton(riwayatButton);
            cardLayout.show(contentPanel, "Riwayat");
        });
        sidebarPanel.add(riwayatButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        profilButton = createModernSidebarButton("📢", "Pengumuman", false);
        profilButton.addActionListener(e -> {
        setActiveButton(profilButton);
        cardLayout.show(contentPanel, "Pengumuman");
        });
        sidebarPanel.add(profilButton);

        // Spacer
        sidebarPanel.add(Box.createVerticalGlue());

        // Logout Button
        JButton logoutButton = createLogoutButton();
        sidebarPanel.add(logoutButton);
        sidebarPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        return sidebarPanel;
    }

    private void setActiveButton(JButton activeButton) {
        // Reset all buttons to inactive state
        JButton[] buttons = {homeButton, bukuButton, riwayatButton, profilButton};
        for (JButton button : buttons) {
            button.setBackground(SIDEBAR_GREEN);
        }
        // Set the clicked button as active
        activeButton.setBackground(HOVER_GREEN);
    }

    private JPanel createSidebarHeader() {
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(SIDEBAR_GREEN);
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setBorder(new EmptyBorder(0, 20, 0, 20));

        JLabel logoLabel = new JLabel("📚");
        logoLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JLabel titleLabel = new JLabel("MariMaca");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        JLabel subtitleLabel = new JLabel("Library System");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        subtitleLabel.setForeground(new Color(200, 230, 201));
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(subtitleLabel);

        return headerPanel;
    }

    private JButton createModernSidebarButton(String icon, String text, boolean isActive) {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(isActive ? HOVER_GREEN : SIDEBAR_GREEN);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setForeground(Color.WHITE);
        button.add(iconLabel, BorderLayout.WEST);

        JLabel textLabel = new JLabel(text);
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(Color.WHITE);
        textLabel.setBorder(new EmptyBorder(0, 15, 0, 0));
        button.add(textLabel, BorderLayout.CENTER);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.getBackground() != HOVER_GREEN) {
                    button.setBackground(HOVER_GREEN);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Only reset background if this is not the active button
                if (button != homeButton || homeButton.getBackground() != HOVER_GREEN) {
                    if (button != bukuButton || bukuButton.getBackground() != HOVER_GREEN) {
                        if (button != riwayatButton || riwayatButton.getBackground() != HOVER_GREEN) {
                            if (button != profilButton || profilButton.getBackground() != HOVER_GREEN) {
                                button.setBackground(SIDEBAR_GREEN);
                            }
                        }
                    }
                }
            }
        });

        return button;
    }

    private JButton createLogoutButton() {
        JButton button = new JButton();
        button.setLayout(new BorderLayout());
        button.setBackground(new Color(198, 40, 40));
        button.setBorder(new EmptyBorder(12, 20, 12, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel iconLabel = new JLabel("🚪");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 18));
        iconLabel.setForeground(Color.WHITE);
        button.add(iconLabel, BorderLayout.WEST);

        JLabel textLabel = new JLabel("Keluar");
        textLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        textLabel.setForeground(Color.WHITE);
        textLabel.setBorder(new EmptyBorder(0, 15, 0, 0));
        button.add(textLabel, BorderLayout.CENTER);

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(211, 47, 47));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(198, 40, 40));
            }
        });

        button.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this,
                    "Apakah Anda yakin ingin keluar?",
                    "Konfirmasi Keluar",
                    JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                this.dispose();
                // TODO: Show login frame
            }
        });

        return button;
    }

    private JPanel createModernHomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(BG_LIGHT);

        // Header Section
        JPanel headerPanel = createHeaderSection();
        panel.add(headerPanel, BorderLayout.NORTH);

        // Main Content with Cards
        JPanel mainContentPanel = new JPanel(new GridBagLayout());
        mainContentPanel.setBackground(BG_LIGHT);
        mainContentPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.BOTH;

        // Profile Card
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 0.6; gbc.weighty = 1.0;
        JPanel profileCard = createProfileCard();
        mainContentPanel.add(profileCard, gbc);

        // Status Card
        gbc.gridx = 1; gbc.gridy = 0;
        gbc.weightx = 0.4; gbc.weighty = 1.0;
        JPanel statusCard = createStatusCard();
        mainContentPanel.add(statusCard, gbc);

        panel.add(mainContentPanel, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createHeaderSection() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_LIGHT);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel welcomeLabel = new JLabel("Selamat Datang, " + namaAnggota + "!");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel.setForeground(DARK_GREEN);
        headerPanel.add(welcomeLabel, BorderLayout.WEST);

        JLabel dateLabel = new JLabel(getCurrentDate());
        dateLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        dateLabel.setForeground(TEXT_SECONDARY);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        return headerPanel;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy");
        return sdf.format(new java.util.Date());
    }

    private JPanel createProfileCard() {
        JPanel card = createCardPanel();
        card.setLayout(new BorderLayout());

        // Card Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(CARD_BG);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel titleLabel = new JLabel("Informasi Profil");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(PRIMARY_GREEN);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        JLabel iconLabel = new JLabel("👤");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
        headerPanel.add(iconLabel, BorderLayout.EAST);

        card.add(headerPanel, BorderLayout.NORTH);

        // Profile Details
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 0, 8, 20);
        gbc.anchor = GridBagConstraints.WEST;

        String[] labels = {"ID Anggota", "Nama Lengkap", "Email", "No. Telepon", "Alamat", "Tanggal Daftar"};
        String[] values = {idAnggotaDisplay, namaAnggota, emailAnggota, noTeleponAnggota, alamatAnggota, tanggalDaftarAnggota};

        for (int i = 0; i < labels.length; i++) {
            gbc.gridx = 0; gbc.gridy = i;
            gbc.weightx = 0.3;
            JLabel labelComp = createDetailLabel(labels[i] + ":");
            detailsPanel.add(labelComp, gbc);

            gbc.gridx = 1;
            gbc.weightx = 0.7;
            JLabel valueComp = createDetailValue(values[i]);
            detailsPanel.add(valueComp, gbc);
        }

        card.add(detailsPanel, BorderLayout.CENTER);
        return card;
    }

    private JPanel createStatusCard() {
    JPanel card = createCardPanel();
    card.setLayout(new BorderLayout());

    // Card Header
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(CARD_BG);
    headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

    JLabel titleLabel = new JLabel("Status Keanggotaan");
    titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
    titleLabel.setForeground(PRIMARY_GREEN);
    headerPanel.add(titleLabel, BorderLayout.WEST);

    card.add(headerPanel, BorderLayout.NORTH);

    // Status Content
    JPanel statusPanel = new JPanel();
    statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.Y_AXIS));
    statusPanel.setBackground(CARD_BG);

    // Status Badge
    JPanel badgePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    badgePanel.setBackground(CARD_BG);
    
    JLabel statusBadge = createStatusBadge();
    badgePanel.add(statusBadge);
    statusPanel.add(badgePanel);

    statusPanel.add(Box.createRigidArea(new Dimension(0, 20)));

    // Quick Stats with real data
    JPanel statsPanel = new JPanel(new GridLayout(2, 1, 0, 10)); // Changed from 3 to 2 rows
    statsPanel.setBackground(CARD_BG);

    // Get real data from database
    int borrowedBooksCount = getBorrowedBooksCount();
    String allDueDates = getAllDueDates();

    statsPanel.add(createStatItem("📚", "Buku Dipinjam", String.valueOf(borrowedBooksCount)));
    statsPanel.add(createStatItem("⏰", "Jatuh Tempo", allDueDates));
    // Removed "Total Bacaan" as requested

    statusPanel.add(statsPanel);

    card.add(statusPanel, BorderLayout.CENTER);
    return card;
}

    private JPanel createCardPanel() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(25, 25, 25, 25)
        ));
        // Add subtle shadow effect
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 3, 3, new Color(0, 0, 0, 20)),
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(BORDER_COLOR, 1),
                        new EmptyBorder(25, 25, 25, 25)
                )
        ));
        return card;
    }

    private JLabel createDetailLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(TEXT_PRIMARY);
        return label;
    }

    private JLabel createDetailValue(String text) {
        JLabel label = new JLabel(text != null ? text : "Tidak tersedia");
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(TEXT_SECONDARY);
        return label;
    }

    private JLabel createStatusBadge() {
        JLabel badge = new JLabel("● " + statusAktifAnggota);
        badge.setFont(new Font("Segoe UI", Font.BOLD, 16));
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(8, 16, 8, 16));

        if ("Aktif".equalsIgnoreCase(statusAktifAnggota)) {
            badge.setBackground(new Color(232, 245, 233));
            badge.setForeground(SUCCESS_GREEN);
        } else if ("Tidak Aktif".equalsIgnoreCase(statusAktifAnggota)) {
            badge.setBackground(new Color(255, 235, 238));
            badge.setForeground(ERROR_RED);
        } else {
            badge.setBackground(new Color(245, 245, 245));
            badge.setForeground(TEXT_SECONDARY);
        }

        return badge;
    }

    private JPanel createStatItem(String icon, String label, String value) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 250, 252));
        panel.setBorder(new EmptyBorder(12, 15, 12, 15));

        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        panel.add(iconLabel, BorderLayout.WEST);

        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBackground(new Color(248, 250, 252));

        JLabel labelComp = new JLabel(label);
        labelComp.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        labelComp.setForeground(TEXT_SECONDARY);
        labelComp.setBorder(new EmptyBorder(0, 10, 0, 0));
        textPanel.add(labelComp, BorderLayout.NORTH);

        JLabel valueComp = new JLabel(value);
        valueComp.setFont(new Font("Segoe UI", Font.BOLD, 14));
        valueComp.setForeground(TEXT_PRIMARY);
        valueComp.setBorder(new EmptyBorder(0, 10, 0, 0));
        textPanel.add(valueComp, BorderLayout.SOUTH);

        panel.add(textPanel, BorderLayout.CENTER);
        return panel;
    }

    private int getBorrowedBooksCount() {
        String sql = "SELECT COUNT(*) as count FROM peminjaman WHERE id_anggota = ? AND status_peminjaman = 'Dipinjam'";
            try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
                pstmt.setInt(1, loggedInMemberId);
                ResultSet rs = pstmt.executeQuery();
                    if (rs.next()) {
                        return rs.getInt("count");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                    "Gagal mengambil data peminjaman: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        return 0;
    }

    private String getAllDueDates() {
        String sql = "SELECT p.tanggal_jatuh_tempo FROM peminjaman p " +
                 "JOIN buku b ON p.id_buku = b.id_buku " +
                 "WHERE p.id_anggota = ? AND p.status_peminjaman = 'Dipinjam' " +
                 "ORDER BY p.tanggal_jatuh_tempo ASC";
    
        StringBuilder result = new StringBuilder();
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
    
        try (Connection conn = DBConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        
            pstmt.setInt(1, loggedInMemberId);
            ResultSet rs = pstmt.executeQuery();
        
            int count = 0;
            while (rs.next()) {
                java.sql.Date dueDate = rs.getDate("tanggal_jatuh_tempo");
            
                if (dueDate != null) {
                    if (count > 0) {
                        result.append(", ");
                    }
                    result.append(sdf.format(dueDate));
                    count++;
                }
            }
        
            if (count == 0) {
                return "Tidak ada";
            }
            return result.toString();
        
        }   catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this,
                    "Gagal mengambil data jatuh tempo: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            }
        return "Tidak ada";
    }
}