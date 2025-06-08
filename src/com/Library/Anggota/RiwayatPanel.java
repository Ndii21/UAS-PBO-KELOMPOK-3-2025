package com.Library.Anggota;

import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class RiwayatPanel extends JPanel {

    private final Color PRIMARY_GREEN = new Color(67, 160, 71);
    private final Color BG_LIGHT = new Color(248, 252, 248);
    private final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private final Color TEXT_SECONDARY = new Color(117, 117, 117);
    private final Color BORDER_COLOR = new Color(230, 230, 230);
    private final Color CARD_BG = Color.WHITE;
    private final Color HEADER_BG = new Color(240, 240, 240);

    private JTable riwayatTable;
    private DefaultTableModel tableModel;
    private final int loggedInMemberId;
    private JPanel centerContentPanel; 
    private JLabel emptyMessageLabel;
    private JScrollPane scrollPane;

    public RiwayatPanel(int memberId) {
        this.loggedInMemberId = memberId;
        initComponents();
        loadRiwayatPeminjaman();
    }

    private void initComponents() {
        setBackground(BG_LIGHT);
        setLayout(new BorderLayout(15, 15)); 
        setBorder(new EmptyBorder(25, 25, 25, 25)); 

        JLabel titleLabel = new JLabel("Riwayat Peminjaman Anda");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(PRIMARY_GREEN);
        titleLabel.setHorizontalAlignment(SwingConstants.LEFT);
        add(titleLabel, BorderLayout.NORTH);

        // Setup Table
        String[] columnNames = {"Judul Buku", "Tgl Pinjam", "Jatuh Tempo", "Tgl Kembali", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Membuat sel tabel tidak bisa diedit
            }
        };
        riwayatTable = new JTable(tableModel);
        setupTableStyle();

        scrollPane = new JScrollPane(riwayatTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        scrollPane.getViewport().setBackground(CARD_BG);

        // Label untuk pesan jika tidak ada riwayat
        emptyMessageLabel = new JLabel("Anda belum memiliki riwayat peminjaman.", SwingConstants.CENTER);
        emptyMessageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        emptyMessageLabel.setForeground(TEXT_SECONDARY);

        // Panel tengah untuk menampung tabel atau pesan kosong
        centerContentPanel = new JPanel(new CardLayout());
        centerContentPanel.setBackground(BG_LIGHT);
        centerContentPanel.add(scrollPane, "TABLE_VIEW");
        centerContentPanel.add(emptyMessageLabel, "EMPTY_VIEW");

        add(centerContentPanel, BorderLayout.CENTER);
    }

    private void setupTableStyle() {
        riwayatTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        riwayatTable.setRowHeight(30);
        riwayatTable.setGridColor(BORDER_COLOR);
        riwayatTable.setShowGrid(true);
        riwayatTable.setFillsViewportHeight(true); // Tabel mengisi tinggi scroll pane
        riwayatTable.setSelectionBackground(PRIMARY_GREEN.brighter());
        riwayatTable.setSelectionForeground(Color.WHITE);
        riwayatTable.setIntercellSpacing(new Dimension(0, 1)); // Menghilangkan spasi horizontal antar sel

        JTableHeader header = riwayatTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(HEADER_BG);
        header.setForeground(TEXT_PRIMARY);
        header.setBorder(BorderFactory.createLineBorder(BORDER_COLOR));
        header.setReorderingAllowed(false);

        // Atur lebar kolom 
        TableColumn column;
        column = riwayatTable.getColumnModel().getColumn(0); // Judul Buku
        column.setPreferredWidth(250);
        column = riwayatTable.getColumnModel().getColumn(1); // Tgl Pinjam
        column.setPreferredWidth(100);
        column = riwayatTable.getColumnModel().getColumn(2); // Jatuh Tempo
        column.setPreferredWidth(100);
        column = riwayatTable.getColumnModel().getColumn(3); // Tgl Kembali
        column.setPreferredWidth(100);
        column = riwayatTable.getColumnModel().getColumn(4); // Status
        column.setPreferredWidth(100);
    }

    public void loadRiwayatPeminjaman() {
        tableModel.setRowCount(0); // Bersihkan data tabel sebelum memuat yang baru

        String sql = "SELECT b.judul_buku, p.tanggal_pinjam, p.tanggal_jatuh_tempo, " +
                     "p.tanggal_pengembalian, p.status_peminjaman " +
                     "FROM peminjaman p " +
                     "JOIN buku b ON p.id_buku = b.id_buku " +
                     "WHERE p.id_anggota = ? " +
                     "ORDER BY p.tanggal_pinjam DESC";

        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy"); // Format tanggal
        boolean hasData = false;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, loggedInMemberId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                hasData = true;
                Vector<String> row = new Vector<>();
                row.add(rs.getString("judul_buku"));

                java.sql.Date tglPinjam = rs.getDate("tanggal_pinjam");
                row.add(tglPinjam != null ? sdf.format(tglPinjam) : "N/A");

                java.sql.Date tglJatuhTempo = rs.getDate("tanggal_jatuh_tempo");
                row.add(tglJatuhTempo != null ? sdf.format(tglJatuhTempo) : "N/A");

                java.sql.Date tglKembali = rs.getDate("tanggal_pengembalian");
                row.add(tglKembali != null ? sdf.format(tglKembali) : "Belum Kembali");

                row.add(rs.getString("status_peminjaman"));
                tableModel.addRow(row);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Gagal mengambil data riwayat peminjaman: " + e.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            hasData = false; // Jika error, anggap tidak ada data untuk UI
        }

        CardLayout cl = (CardLayout) centerContentPanel.getLayout();
        if (hasData) {
            cl.show(centerContentPanel, "TABLE_VIEW");
        } else {
            // Jika tidak ada data atau terjadi error, pastikan tabel kosong dan tampilkan pesan
            if (tableModel.getRowCount() > 0) tableModel.setRowCount(0);
            cl.show(centerContentPanel, "EMPTY_VIEW");
        }
    }

    // Metode untuk memuat ulang data
    public void refreshData() {
        loadRiwayatPeminjaman();
    }
}