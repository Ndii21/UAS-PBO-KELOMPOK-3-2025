package com.Library.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class PengunjungManagementPanel extends JPanel {
    
    // Color palette
    private final Color PRIMARY_GREEN = new Color(34, 139, 34);      // Forest Green
    private final Color LIGHT_GREEN = new Color(144, 238, 144);     // Light Green
    private final Color DARK_GREEN = new Color(0, 100, 0);          // Dark Green
    private final Color BG_GREEN = new Color(240, 255, 240);        // Honeydew 
    
    private JTable pengunjungTable;
    private DefaultTableModel tableModel;
    private JTextField dateField;
    private JLabel dateFormatLabel;
    private JButton searchButton;
    private JButton refreshButton;
    private JLabel totalLabel;
    
    private PengunjungDAO pengunjungDAO;
    
    public PengunjungManagementPanel() {
        pengunjungDAO = new PengunjungDAO();
        initComponents();
        setupLayout();
        loadPengunjungData();
        updateTotalCount();
    }
    
    private void initComponents() {
        setBackground(BG_GREEN);
        
        // Setup table model dengan kolom yang sesuai
        String[] columnNames = {"ID Pengunjung", "Nama", "No Telepon", "Tanggal Kunjungan"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa diedit
            }
        };
        
        // Setup table
        pengunjungTable = new JTable(tableModel);
        pengunjungTable.setBackground(Color.WHITE);
        pengunjungTable.setSelectionBackground(LIGHT_GREEN);
        pengunjungTable.setSelectionForeground(DARK_GREEN);
        pengunjungTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        pengunjungTable.setRowHeight(25);
        pengunjungTable.setGridColor(PRIMARY_GREEN);
        
        // Setup table header
        JTableHeader header = pengunjungTable.getTableHeader();
        header.setBackground(PRIMARY_GREEN);
        header.setForeground(Color.WHITE);
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setReorderingAllowed(false);
        
        // Setup date field untuk pencarian
        dateField = new JTextField(15);
        dateField.setPreferredSize(new Dimension(150, 35));
        dateField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        dateField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN, 1),
            BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
        
        // Label untuk format tanggal
        dateFormatLabel = new JLabel("(yyyy-mm-dd)");
        dateFormatLabel.setFont(new Font("Segoe UI", Font.ITALIC, 10));
        dateFormatLabel.setForeground(DARK_GREEN);
        
        // Setup buttons
        searchButton = createStyledButton("Cari", PRIMARY_GREEN);
        refreshButton = createStyledButton("Refresh", DARK_GREEN);
        
        // Setup label untuk total pengunjung
        totalLabel = new JLabel("Total Pengunjung: 0");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        totalLabel.setForeground(DARK_GREEN);
        
        // Setup events
        setupEvents();
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(100, 35));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);
        return button;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Panel header dengan judul
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(BG_GREEN);
        
        JLabel titleLabel = new JLabel("Manajemen Data Pengunjung");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_GREEN);
        headerPanel.add(titleLabel);
        
        // Panel untuk pencarian
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        searchPanel.setBackground(BG_GREEN);
        searchPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN, 2), 
            "Pencarian Berdasarkan Tanggal",
            0, 0, new Font("Segoe UI", Font.BOLD, 12), DARK_GREEN
        ));
        
        searchPanel.add(new JLabel("Tanggal:"));
        searchPanel.add(dateField);
        searchPanel.add(dateFormatLabel);
        searchPanel.add(searchButton);
        searchPanel.add(refreshButton);
        
        // Panel untuk informasi total
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.setBackground(BG_GREEN);
        infoPanel.add(totalLabel);
        
        // Panel bagian atas (header + search + info)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_GREEN);
        topPanel.add(headerPanel, BorderLayout.NORTH);
        topPanel.add(searchPanel, BorderLayout.CENTER);
        topPanel.add(infoPanel, BorderLayout.SOUTH);
        
        // Table dengan scroll pane
        JScrollPane scrollPane = new JScrollPane(pengunjungTable);
        scrollPane.setBackground(Color.WHITE);
        scrollPane.getViewport().setBackground(Color.WHITE);
        scrollPane.setBorder(BorderFactory.createLineBorder(PRIMARY_GREEN, 2));
        
        // Layout utama
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void setupEvents() {
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchByDate();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dateField.setText("");
                loadPengunjungData();
                updateTotalCount();
            }
        });
    }
    
    private void loadPengunjungData() {
        // Clear existing data
        tableModel.setRowCount(0);
        
        try {
            List<Pengunjung> pengunjungList = pengunjungDAO.getAllPengunjung();
            
            for (Pengunjung pengunjung : pengunjungList) {
                Object[] row = {
                    pengunjung.getIdPengunjung(),
                    pengunjung.getNama(),
                    pengunjung.getNoTelepon(),
                    pengunjung.getTanggalKunjungan()
                };
                tableModel.addRow(row);
            }
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saat memuat data pengunjung: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void searchByDate() {
        String dateText = dateField.getText().trim();
        
        if (dateText.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Silakan masukkan tanggal terlebih dahulu!\nFormat: yyyy-mm-dd (contoh: 2024-01-15)", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Validasi format tanggal
        if (!dateText.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, 
                "Format tanggal salah!\nGunakan format: yyyy-mm-dd (contoh: 2024-01-15)", 
                "Format Error", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Clear existing data
        tableModel.setRowCount(0);
        
        try {
            List<Pengunjung> pengunjungList = pengunjungDAO.getPengunjungByDate(dateText);
            
            for (Pengunjung pengunjung : pengunjungList) {
                Object[] row = {
                    pengunjung.getIdPengunjung(),
                    pengunjung.getNama(),
                    pengunjung.getNoTelepon(),
                    pengunjung.getTanggalKunjungan()
                };
                tableModel.addRow(row);
            }
            
            updateTotalCount();
            
            // Show message if no data found
            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, 
                    "Tidak ada data pengunjung pada tanggal " + dateText, 
                    "Informasi", 
                    JOptionPane.INFORMATION_MESSAGE);
            }
            
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, 
                "Format tanggal tidak valid!\nGunakan format: yyyy-mm-dd (contoh: 2024-01-15)", 
                "Format Error", 
                JOptionPane.ERROR_MESSAGE);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, 
                "Error saat mencari data pengunjung: " + e.getMessage(), 
                "Database Error", 
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
    
    private void updateTotalCount() {
        int totalRows = tableModel.getRowCount();
        totalLabel.setText("Total Pengunjung: " + totalRows);
    }
}