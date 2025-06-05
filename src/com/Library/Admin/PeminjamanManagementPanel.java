package com.Library.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class PeminjamanManagementPanel extends JPanel {
    private PeminjamanDAO peminjamanDAO;
    private JTable peminjamanTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, processReturnButton, refreshButton, searchButton, delleteButton; 
    private JLabel statusLabel;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");


    // Warna dari BookManagementPanel (atau DashboardFrame)
    private final Color PRIMARY_GREEN = new Color(34, 139, 34);      // Forest Green
    private final Color LIGHT_GREEN = new Color(144, 238, 144);     // Light Green
    private final Color DARK_GREEN = new Color(0, 100, 0);          // Dark Green
    private final Color ACCENT_GREEN = new Color(50, 205, 50);      // Lime Green
    private final Color BG_GREEN = new Color(240, 255, 240);        // Honeydew (background panel)

    public PeminjamanManagementPanel() {
        peminjamanDAO = new PeminjamanDAO();
        initComponents();
        setupLayout();
        setupEvents();
        loadPeminjamanData();
    }

    private void initComponents() {
        setBackground(Color.WHITE); // Background utama panel

        // Table
        String[] columns = {"ID Pinjam", "Judul Buku", "Nama Anggota", "Tgl Pinjam", "Tgl Jatuh Tempo", "Tgl Kembali", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        peminjamanTable = new JTable(tableModel);
        peminjamanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        peminjamanTable.setRowHeight(30);
        peminjamanTable.setFont(new Font("Arial", Font.PLAIN, 12));
        peminjamanTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        peminjamanTable.getTableHeader().setBackground(PRIMARY_GREEN);
        peminjamanTable.getTableHeader().setForeground(Color.WHITE);
        peminjamanTable.setGridColor(LIGHT_GREEN);
        peminjamanTable.setSelectionBackground(LIGHT_GREEN);
        peminjamanTable.setSelectionForeground(DARK_GREEN);


        // Mengatur lebar kolom
        peminjamanTable.getColumnModel().getColumn(0).setPreferredWidth(60);  // ID Pinjam
        peminjamanTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Judul Buku
        peminjamanTable.getColumnModel().getColumn(2).setPreferredWidth(150); // Nama Anggota
        peminjamanTable.getColumnModel().getColumn(3).setPreferredWidth(100); // Tgl Pinjam
        peminjamanTable.getColumnModel().getColumn(4).setPreferredWidth(110); // Tgl Jatuh Tempo
        peminjamanTable.getColumnModel().getColumn(5).setPreferredWidth(100); // Tgl Kembali
        peminjamanTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Status

        // Custom renderer untuk kolom Status Peminjaman
        peminjamanTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    String status = (String) value;
                    if ("Dipinjam".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(255, 255, 220)); // Light Yellow
                        c.setForeground(new Color(139, 69, 19));   // SaddleBrown
                    } else if ("Dikembalikan".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(220, 255, 220)); // Light Green (mirip tersedia di buku)
                        c.setForeground(DARK_GREEN);
                    } else if ("Terlambat".equalsIgnoreCase(status)) {
                        c.setBackground(new Color(255, 200, 200)); // Light Red
                        c.setForeground(new Color(139, 0, 0));     // Dark Red
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                }
                setHorizontalAlignment(SwingConstants.CENTER);
                return c;
            }
        });
        
        // Search field
        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));

        // Status label
        statusLabel = new JLabel("Total Peminjaman Aktif: 0");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(DARK_GREEN);

        // Buttons
        addButton = new JButton("Buat Peminjaman");
        processReturnButton = new JButton("Proses Pengembalian/Edit");
        refreshButton = new JButton("Refresh");
        searchButton = new JButton("Cari");
        delleteButton = new JButton("Hapus Peminjaman");

        styleButton(addButton, ACCENT_GREEN);
        styleButton(processReturnButton, new Color(255, 165, 0));
        styleButton(refreshButton, PRIMARY_GREEN);
        styleButton(searchButton, DARK_GREEN);
        styleButton(delleteButton, new Color(139, 0, 0)); 
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(180, 35)); 
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0,0)); 

        // Header Panel (Judul Panel) 
        JPanel headerPanelTitle = new JPanel(new FlowLayout(FlowLayout.CENTER)); 
        headerPanelTitle.setBackground(DARK_GREEN); 
        headerPanelTitle.setPreferredSize(new Dimension(0, 50)); 

        JLabel titleLabel = new JLabel("MANAGEMENT PEMINJAMAN BUKU");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanelTitle.add(titleLabel);
        
        // Top Panel (Search + Status)
        JPanel topPanel = new JPanel(new BorderLayout(10,10));
        topPanel.setBackground(BG_GREEN);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false); 
        JLabel searchLabel = new JLabel("Cari (Judul/Anggota/Status):");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabel.setForeground(DARK_GREEN);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        statusPanel.setOpaque(false);
        statusPanel.add(statusLabel);

        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(statusPanel, BorderLayout.EAST);

        
        JPanel northPanelContainer = new JPanel(new BorderLayout());
        northPanelContainer.add(headerPanelTitle, BorderLayout.NORTH);
        northPanelContainer.add(topPanel, BorderLayout.CENTER);
        add(northPanelContainer, BorderLayout.NORTH);


        // Table Panel
        JScrollPane scrollPane = new JScrollPane(peminjamanTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN, 2),
            "Daftar Transaksi Peminjaman", 0, 0,
            new Font("Arial", Font.BOLD, 14), PRIMARY_GREEN));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        // Button Panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BG_GREEN); // Background untuk area tombol
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(processReturnButton);
        buttonPanel.add(delleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEvents() {
        addButton.addActionListener(e -> showPeminjamanDialog(null));
        processReturnButton.addActionListener(e -> showPeminjamanDialogForUpdate());
        refreshButton.addActionListener(e -> loadPeminjamanData());
        searchButton.addActionListener(e -> searchPeminjamanData());
        searchField.addActionListener(e -> searchPeminjamanData());
        delleteButton.addActionListener(e -> deletePeminjamanData());
    }

    private String formatDate(Date date) {
        if (date == null) {
            return "-";
        }
        return dateFormat.format(date);
    }
    
    private void loadPeminjamanData() {
        SwingUtilities.invokeLater(() -> {
            try {
                tableModel.setRowCount(0); // Clear table
                List<Peminjaman> daftarPeminjaman = peminjamanDAO.getAllPeminjaman();

                for (Peminjaman p : daftarPeminjaman) {
                    Object[] row = {
                        p.getIdPeminjaman(),
                        p.getJudulBuku(),
                        p.getNamaAnggota(),
                        formatDate(p.getTanggalPinjam()),
                        formatDate(p.getTanggalJatuhTempo()),
                        formatDate(p.getTanggalPengembalian()),
                        p.getStatusPeminjaman()
                    };
                    tableModel.addRow(row);
                }
                statusLabel.setText("Total Peminjaman Aktif: " + peminjamanDAO.getTotalPeminjamanAktif());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error loading peminjaman data: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }

    private void searchPeminjamanData() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadPeminjamanData();
            return;
        }
        SwingUtilities.invokeLater(() -> {
            try {
                tableModel.setRowCount(0);
                List<Peminjaman> results = peminjamanDAO.searchPeminjaman(keyword);
                for (Peminjaman p : results) {
                     Object[] row = {
                        p.getIdPeminjaman(),
                        p.getJudulBuku(),
                        p.getNamaAnggota(),
                        formatDate(p.getTanggalPinjam()),
                        formatDate(p.getTanggalJatuhTempo()),
                        formatDate(p.getTanggalPengembalian()),
                        p.getStatusPeminjaman()
                    };
                    tableModel.addRow(row);
                }
                statusLabel.setText("Hasil Pencarian: " + results.size() + " transaksi ditemukan");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Error searching peminjaman: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void showPeminjamanDialog(Peminjaman peminjamanToEdit) {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        String dialogTitle = (peminjamanToEdit == null) ? "Tambah Peminjaman Baru" : "Edit Data Peminjaman";
        PeminjamanDialog dialog = new PeminjamanDialog(parentFrame, dialogTitle, peminjamanToEdit);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Peminjaman peminjamanData = dialog.getPeminjaman();
            boolean success;
            if (peminjamanToEdit == null) { // Tambah baru
                success = peminjamanDAO.addPeminjaman(peminjamanData);
                if (success) {
                    JOptionPane.showMessageDialog(this, "Peminjaman berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal menambahkan peminjaman!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else { // Edit
                peminjamanData.setIdPeminjaman(peminjamanToEdit.getIdPeminjaman()); // Pastikan ID ter-set
                success = peminjamanDAO.updatePeminjaman(peminjamanData);
                 if (success) {
                    JOptionPane.showMessageDialog(this, "Data peminjaman berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Gagal mengupdate data peminjaman!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
            if (success) loadPeminjamanData();
        }
    }
    
    private void showPeminjamanDialogForUpdate() {
        int selectedRow = peminjamanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih transaksi peminjaman yang ingin diproses/diedit!",
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int idPeminjaman = (Integer) tableModel.getValueAt(selectedRow, 0);
        
        Peminjaman selectedPeminjaman = new Peminjaman();
        selectedPeminjaman.setIdPeminjaman((Integer) tableModel.getValueAt(selectedRow, 0));
        selectedPeminjaman.setJudulBuku((String) tableModel.getValueAt(selectedRow, 1));
        selectedPeminjaman.setNamaAnggota((String) tableModel.getValueAt(selectedRow, 2));

        List<Peminjaman> currentList = peminjamanDAO.getAllPeminjaman();
        Peminjaman actualPeminjamanToEdit = null;
        for(Peminjaman p : currentList) {
            if(p.getIdPeminjaman() == idPeminjaman) {
                actualPeminjamanToEdit = p;
                break;
            }
        }

        if(actualPeminjamanToEdit == null) {
             JOptionPane.showMessageDialog(this, "Data peminjaman tidak ditemukan untuk diedit.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        
        showPeminjamanDialog(actualPeminjamanToEdit);
    }

    private void deletePeminjamanData() {
        int selectedRow = peminjamanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih data peminjaman yang akan dihapus!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idPeminjaman = (Integer) tableModel.getValueAt(selectedRow, 0);
        String judulBuku = (String) tableModel.getValueAt(selectedRow, 1);
        String namaAnggota = (String) tableModel.getValueAt(selectedRow, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus data peminjaman:\n" + namaAnggota + " (judul buku: " + judulBuku + ")?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (peminjamanDAO.deletePeminjaman(idPeminjaman)) {
                JOptionPane.showMessageDialog(this,
                    "Data berhasil dihapus!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                loadPeminjamanData();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal menghapus data!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

}