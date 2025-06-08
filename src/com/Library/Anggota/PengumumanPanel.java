package com.Library.Anggota;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class PengumumanPanel extends JPanel {

    private final Color CARD_BG = new Color(255, 255, 255);
    private final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private final Color TEXT_SECONDARY = new Color(117, 117, 117);
    private final Color BORDER_COLOR = new Color(230, 230, 230);
    private final Color BG_LIGHT = new Color(248, 252, 248);
    private final Color PRIMARY_GREEN = new Color(67, 160, 71); 

    private JPanel announcementsContainer;

    private static class Announcement {
        String title;
        String content;
        Date publishDate;

        Announcement(String title, String content, Date publishDate, String author) {
            this.title = title;
            this.content = content;
            this.publishDate = publishDate;
        }
    }

    public PengumumanPanel() {
        initializeUI();
        loadAnnouncements();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel panelTitleLabel = new JLabel("Pengumuman Perpustakaan");
        panelTitleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        panelTitleLabel.setForeground(PRIMARY_GREEN);
        panelTitleLabel.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(panelTitleLabel, BorderLayout.NORTH);

        announcementsContainer = new JPanel();
        announcementsContainer.setLayout(new BoxLayout(announcementsContainer, BoxLayout.Y_AXIS));
        announcementsContainer.setBackground(BG_LIGHT);

        JScrollPane scrollPane = new JScrollPane(announcementsContainer);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.setBackground(BG_LIGHT);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadAnnouncements() {
        List<Announcement> announcements = new ArrayList<>();
        String sql = "SELECT id_pengumuman, judul, isi, tanggal_dibuat FROM pengumuman ORDER BY tanggal_dibuat DESC";

        try (Connection conn = DBConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String title = rs.getString("judul");
                String content = rs.getString("isi");
                java.sql.Timestamp publishTimestamp = rs.getTimestamp("tanggal_dibuat");
                Date publishDate = (publishTimestamp != null) ? new Date(publishTimestamp.getTime()) : null;
                announcements.add(new Announcement(title, content, publishDate, "Admin"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Show error message on the panel itself or a JOptionPane
            announcementsContainer.add(new JLabel("Gagal memuat pengumuman: " + e.getMessage()));
        }

        displayAnnouncements(announcements);
    }

    private void displayAnnouncements(List<Announcement> announcements) {
        announcementsContainer.removeAll(); // Clear previous announcements if any

        if (announcements.isEmpty()) {
            JLabel noAnnouncementsLabel = new JLabel("Belum ada pengumuman saat ini.");
            noAnnouncementsLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            noAnnouncementsLabel.setForeground(TEXT_SECONDARY);
            noAnnouncementsLabel.setHorizontalAlignment(SwingConstants.CENTER);
            // Center the label in the available space
            JPanel centerPanel = new JPanel(new GridBagLayout());
            centerPanel.setBackground(BG_LIGHT);
            centerPanel.add(noAnnouncementsLabel);
            announcementsContainer.add(centerPanel);

        } else {
            for (Announcement announcement : announcements) {
                announcementsContainer.add(createAnnouncementCard(announcement));
                announcementsContainer.add(Box.createRigidArea(new Dimension(0, 15))); // Spacer
            }
        }
        announcementsContainer.revalidate();
        announcementsContainer.repaint();
    }

    private JPanel createAnnouncementCard(Announcement announcement) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(15, 20, 15, 20)
        ));
        card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));
        card.setMinimumSize(new Dimension(300, 100));


        JLabel titleLabel = new JLabel(announcement.title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(TEXT_PRIMARY);
        card.add(titleLabel, BorderLayout.NORTH);

        JTextArea contentArea = new JTextArea(announcement.content);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        contentArea.setForeground(TEXT_SECONDARY);
        contentArea.setWrapStyleWord(true);
        contentArea.setLineWrap(true);
        contentArea.setOpaque(false); 
        contentArea.setEditable(false);
        contentArea.setFocusable(false);
        card.add(contentArea, BorderLayout.CENTER);


        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerPanel.setBackground(CARD_BG);

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM yyyy");
        String dateStr = (announcement.publishDate != null) ? sdf.format(announcement.publishDate) : "Tanggal tidak diketahui";
        
        JLabel dateLabel = new JLabel("Dipublikasikan : " + dateStr);
        dateLabel.setFont(new Font("Segoe UI", Font.ITALIC, 12));
        dateLabel.setForeground(TEXT_SECONDARY);
        footerPanel.add(dateLabel);

        card.add(footerPanel, BorderLayout.SOUTH);

        return card;
    }

    
}