package com.Library.Admin;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private JButton bookListButton;
    private JButton memberListButton;
    private JButton peminjamanButton;
    private JButton pengumumanButton;
    private JButton logoutButton;
    private JButton homeButton;
    private JLabel logoLabel;
    private JPanel mainContentPanel;
    private JPanel homePanel;
    private BookManagementPanel bookManagementPanel;
    private MemberManagementPanel MemberManagementPanel;
    private PeminjamanManagementPanel peminjamanManagementPanel; 
    private PengumumanManagementPanel pengumumanManagementPanel;

    private final String LOGO_PATH = "image/logo.jpg";

    private final Color PRIMARY_GREEN = new Color(67, 160, 71);
    private final Color DARK_GREEN = new Color(46, 125, 50);
    private final Color SOFT_GREEN = new Color(129, 199, 132);
    private final Color BG_LIGHT = new Color(248, 252, 248);
    private final Color SIDEBAR_GREEN = new Color(56, 142, 60);
    private final Color HOVER_GREEN = new Color(76, 175, 80);

    public DashboardFrame() {
        initComponents();
        setupLayout();
        setupEvents();
        loadSavedLogo();
        showHomePanel(); // tampilkan panel home secara default
    }

    private void initComponents() {
        setTitle("Dashboard Admin - Sistem Perpustakaan Mari Maca");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);

        logoLabel = new JLabel("", SwingConstants.CENTER);

        homeButton = new JButton("HOME");
        bookListButton = new JButton("DAFTAR BUKU");
        memberListButton = new JButton("DAFTAR ANGGOTA");
        peminjamanButton = new JButton("PEMINJAMAN"); 
        pengumumanButton = new JButton("PENGUMUMAN");
        logoutButton = new JButton("KELUAR");

        logoutButton.setBackground(new Color(139, 0, 0));
        logoutButton.setForeground(Color.WHITE);
        logoutButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        logoutButton.setPreferredSize(new Dimension(280, 50));
        logoutButton.setMaximumSize(new Dimension(280, 50));
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        logoutButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        logoutButton.setOpaque(true);
        logoutButton.setContentAreaFilled(true);
        logoutButton.setHorizontalAlignment(SwingConstants.LEFT);
        

        styleActiveButton(homeButton);
        styleSidebarButton(bookListButton);
        styleSidebarButton(memberListButton);
        styleSidebarButton(peminjamanButton); 
        styleSidebarButton(pengumumanButton);

        createHomePanel();
        bookManagementPanel = new BookManagementPanel();
        MemberManagementPanel = new MemberManagementPanel();
        peminjamanManagementPanel = new PeminjamanManagementPanel();
        pengumumanManagementPanel = new PengumumanManagementPanel();

        mainContentPanel = new JPanel(new CardLayout());
        mainContentPanel.setBackground(BG_LIGHT);
    }

    private void styleActiveButton(JButton button) {
        button.setBackground(PRIMARY_GREEN);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(280, 50));
        button.setMaximumSize(new Dimension(280, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);
    }

    private void styleSidebarButton(JButton button) {
        button.setBackground(SOFT_GREEN);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(280, 50));
        button.setMaximumSize(new Dimension(280, 50));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.getBackground().equals(SOFT_GREEN)) {
                    button.setBackground(HOVER_GREEN);
                }
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button.getBackground().equals(HOVER_GREEN)) {
                    button.setBackground(SOFT_GREEN);
                }
            }
        });
    }

    private void setActiveButton(JButton activeButton) {
        styleSidebarButton(homeButton);
        styleSidebarButton(bookListButton);
        styleSidebarButton(memberListButton);
        styleSidebarButton(peminjamanButton);
        styleSidebarButton(pengumumanButton);

        styleActiveButton(activeButton);
    }

    private void createHomePanel() {
        homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(BG_LIGHT);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 30, 0);

        JPanel logoSection = new JPanel(new GridBagLayout());
        logoSection.setBackground(Color.WHITE);
        logoSection.setPreferredSize(new Dimension(250, 250));
        logoSection.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN, 3),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        logoSection.add(logoLabel);

        homePanel.add(logoSection, gbc);

        gbc.gridy = 1;
        JLabel welcomeLabel1 = new JLabel("Selamat Menikmati Layanan Online", SwingConstants.CENTER);
        welcomeLabel1.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel1.setForeground(DARK_GREEN);
        homePanel.add(welcomeLabel1, gbc);

        gbc.gridy = 2;
        JLabel welcomeLabel2 = new JLabel("Perpustakaan Mari Maca", SwingConstants.CENTER);
        welcomeLabel2.setFont(new Font("Segoe UI", Font.BOLD, 28));
        welcomeLabel2.setForeground(DARK_GREEN);
        homePanel.add(welcomeLabel2, gbc);
    }

    private void setupLayout() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(BG_LIGHT);

        add(createSidebar(), BorderLayout.WEST);

        mainContentPanel.add(homePanel, "HOME");
        mainContentPanel.add(bookManagementPanel, "BOOKS");
        mainContentPanel.add(MemberManagementPanel, "MEMBERS");
        mainContentPanel.add(peminjamanManagementPanel, "PEMINJAMAN");
        mainContentPanel.add(pengumumanManagementPanel, "PENGUMUMAN");
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel(new BorderLayout());
        sidebar.setBackground(SIDEBAR_GREEN);
        sidebar.setPreferredSize(new Dimension(320, 0));

        JPanel userSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
        userSection.setBackground(DARK_GREEN);
        userSection.setPreferredSize(new Dimension(0, 80));

        JLabel userIcon = new JLabel("");
        userIcon.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        userIcon.setForeground(Color.WHITE);

        JLabel userLabel = new JLabel("Admin");
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        userLabel.setForeground(Color.WHITE);
        userLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));

        userSection.add(userIcon);
        userSection.add(userLabel);

        JPanel menuSection = new JPanel();
        menuSection.setLayout(new BoxLayout(menuSection, BoxLayout.Y_AXIS));
        menuSection.setBackground(SIDEBAR_GREEN);
        menuSection.setBorder(BorderFactory.createEmptyBorder(30, 20, 20, 20));

        menuSection.add(homeButton);
        menuSection.add(Box.createRigidArea(new Dimension(0, 10)));
        menuSection.add(bookListButton);
        menuSection.add(Box.createRigidArea(new Dimension(0, 10)));
        menuSection.add(memberListButton);
        menuSection.add(Box.createRigidArea(new Dimension(0, 10)));
        menuSection.add(peminjamanButton);
        menuSection.add(Box.createRigidArea(new Dimension(0, 10)));
        menuSection.add(pengumumanButton);
        menuSection.add(Box.createRigidArea(new Dimension(0, 10)));

        JPanel bottomSection = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomSection.setBackground(SIDEBAR_GREEN);
        bottomSection.setBorder(BorderFactory.createEmptyBorder(20, 20, 30, 20));
        bottomSection.add(logoutButton);

        JPanel titleSection = new JPanel();
        titleSection.setLayout(new BoxLayout(titleSection, BoxLayout.Y_AXIS));
        titleSection.setBackground(SIDEBAR_GREEN);
        titleSection.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));

        JLabel titleLabel1 = new JLabel("Layanan Online Perpustakaan");
        titleLabel1.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel1.setForeground(Color.WHITE);
        titleLabel1.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel titleLabel2 = new JLabel("Perpustakaan Mari Maca Kab. Karawang");
        titleLabel2.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titleLabel2.setForeground(Color.WHITE);
        titleLabel2.setAlignmentX(Component.CENTER_ALIGNMENT);

        titleSection.add(titleLabel1);
        titleSection.add(titleLabel2);

        sidebar.add(userSection, BorderLayout.NORTH);
        sidebar.add(menuSection, BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(SIDEBAR_GREEN);
        bottomPanel.add(bottomSection, BorderLayout.NORTH);
        bottomPanel.add(titleSection, BorderLayout.SOUTH);

        sidebar.add(bottomPanel, BorderLayout.SOUTH);

        return sidebar;
    }

    private void loadSavedLogo() {
        ImageIcon icon = new ImageIcon(LOGO_PATH);

        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        int maxWidth = 250;
        int maxHeight = 250;

        if (width > maxWidth || height > maxHeight) {
            double widthRatio = (double) maxWidth / width;
            double heightRatio = (double) maxHeight / height;
            double scale = Math.min(widthRatio, heightRatio);

            int newWidth = (int) (width * scale);
            int newHeight = (int) (height * scale);

            Image scaledImage = icon.getImage().getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            logoLabel.setIcon(new ImageIcon(scaledImage));
        } else {
            logoLabel.setIcon(icon);
        }

        logoLabel.setText("");
        logoLabel.setPreferredSize(new Dimension(Math.min(width, maxWidth), Math.min(height, maxHeight)));
    }

    private void setupEvents() {
        homeButton.addActionListener(e -> showHomePanel());
        bookListButton.addActionListener(e -> showBookPanel());
        memberListButton.addActionListener(e -> showMemberPanel());
        peminjamanButton.addActionListener(e -> showPeminjamanPanel()); 
        pengumumanButton.addActionListener(e -> showPengumumanPanel());
        
        logoutButton.addActionListener(e -> {
            this.dispose();
        });;
    }

    private void showHomePanel() {
        CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();
        cardLayout.show(mainContentPanel, "HOME");
        setActiveButton(homeButton);
    }

    private void showBookPanel() {
        CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();
        cardLayout.show(mainContentPanel, "BOOKS");
        setActiveButton(bookListButton);
    }

    private void showMemberPanel() {
        CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();
        cardLayout.show(mainContentPanel, "MEMBERS");
        setActiveButton(memberListButton);
    }

    private void showPeminjamanPanel() { 
        CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();
        cardLayout.show(mainContentPanel, "PEMINJAMAN");
        setActiveButton(peminjamanButton);
    }

    private void showPengumumanPanel() { // Method baru
        CardLayout cardLayout = (CardLayout) mainContentPanel.getLayout();
        cardLayout.show(mainContentPanel, "PENGUMUMAN");
        setActiveButton(pengumumanButton);
    }
}