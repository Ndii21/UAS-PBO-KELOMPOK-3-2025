package com.Library.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
// import java.awt.event.ActionEvent;
// import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class PengumumanManagementPanel extends JPanel {
    private PengumumanDAO pengumumanDAO;
    private JTable pengumumanTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton, searchButton;
    private JLabel statusLabel;
    private SimpleDateFormat tableDateFormat = new SimpleDateFormat("dd MMM yyyy");

    private final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private final Color LIGHT_GREEN = new Color(144, 238, 144);
    private final Color DARK_GREEN = new Color(0, 100, 0);
    private final Color ACCENT_GREEN = new Color(50, 205, 50);
    private final Color BG_GREEN = new Color(240, 255, 240);

    public PengumumanManagementPanel() {
        pengumumanDAO = new PengumumanDAO();
        initComponents();
        setupLayout();
        setupEvents();
        loadPengumumanData();
    }

    private void initComponents() {
        setBackground(Color.WHITE);

        String[] columns = {"ID", "Judul", "Isi (Ringkas)", "Tanggal"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        pengumumanTable = new JTable(tableModel);
        pengumumanTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        pengumumanTable.setRowHeight(28);
        pengumumanTable.setFont(new Font("Arial", Font.PLAIN, 12));
        pengumumanTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        pengumumanTable.getTableHeader().setBackground(PRIMARY_GREEN);
        pengumumanTable.getTableHeader().setForeground(Color.WHITE);
        pengumumanTable.setGridColor(LIGHT_GREEN);
        pengumumanTable.setSelectionBackground(LIGHT_GREEN);
        pengumumanTable.setSelectionForeground(DARK_GREEN);

        pengumumanTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        pengumumanTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        pengumumanTable.getColumnModel().getColumn(2).setPreferredWidth(350);
        pengumumanTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        
        pengumumanTable.getColumnModel().getColumn(3).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Date) {
                    value = tableDateFormat.format((Date) value);
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
         pengumumanTable.getColumnModel().getColumn(2).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (value instanceof String) {
                    String text = (String) value;
                    setText(text.length() > 80 ? text.substring(0, 77) + "..." : text);
                }
                return this;
            }
        });

        searchField = new JTextField(20);
        searchField.setFont(new Font("Arial", Font.PLAIN, 14));
        statusLabel = new JLabel("Total: 0 pengumuman");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(DARK_GREEN);

        addButton = new JButton("Tambah Pengumuman");
        editButton = new JButton("Edit Pengumuman");
        deleteButton = new JButton("Hapus Pengumuman");
        refreshButton = new JButton("Refresh");
        searchButton = new JButton("Cari");

        styleButton(addButton, ACCENT_GREEN, 180);
        styleButton(editButton, new Color(255, 165, 0), 180);
        styleButton(deleteButton, new Color(139, 0, 0), 180);
        styleButton(refreshButton, PRIMARY_GREEN, 120);
        styleButton(searchButton, DARK_GREEN, 100);
    }

    private void styleButton(JButton button, Color color, int width) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(width, 35));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    private void setupLayout() {
        setLayout(new BorderLayout(0,0));

        JPanel headerPanelTitle = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanelTitle.setBackground(DARK_GREEN);
        headerPanelTitle.setPreferredSize(new Dimension(0, 50));
        JLabel titleLabel = new JLabel("MANAGEMENT PENGUMUMAN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        headerPanelTitle.add(titleLabel);
        
        JPanel topPanel = new JPanel(new BorderLayout(10,10));
        topPanel.setBackground(BG_GREEN);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setOpaque(false);
        JLabel searchLabel = new JLabel("Cari (Judul/Isi):");
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

        JScrollPane scrollPane = new JScrollPane(pengumumanTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN, 2),
            "Daftar Pengumuman", 0, 0,
            new Font("Arial", Font.BOLD, 14), PRIMARY_GREEN));
        scrollPane.getViewport().setBackground(Color.WHITE);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 15));
        buttonPanel.setBackground(BG_GREEN);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(refreshButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void setupEvents() {
        addButton.addActionListener(e -> showPengumumanDialog(null, "Tambah Pengumuman Baru"));
        editButton.addActionListener(e -> editSelectedPengumuman());
        deleteButton.addActionListener(e -> deleteSelectedPengumuman());
        refreshButton.addActionListener(e -> loadPengumumanData());
        searchButton.addActionListener(e -> searchPengumumanData());
        searchField.addActionListener(e -> searchPengumumanData());
    }

    private void loadPengumumanData() {
        SwingUtilities.invokeLater(() -> {
            try {
                tableModel.setRowCount(0);
                List<Pengumuman> daftarPengumuman = pengumumanDAO.getAllPengumuman();
                for (Pengumuman p : daftarPengumuman) {
                    Object[] row = {
                        p.getIdPengumuman(),
                        p.getJudul(),
                        p.getIsi(),
                        p.getTanggalDibuat()
                    };
                    tableModel.addRow(row);
                }
                statusLabel.setText("Total: " + daftarPengumuman.size() + " pengumuman");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void searchPengumumanData() {
        String keyword = searchField.getText().trim();
        SwingUtilities.invokeLater(() -> {
            try {
                tableModel.setRowCount(0);
                List<Pengumuman> results = keyword.isEmpty() ? pengumumanDAO.getAllPengumuman() : pengumumanDAO.searchPengumuman(keyword);
                for (Pengumuman p : results) {
                    Object[] row = { p.getIdPengumuman(), p.getJudul(), p.getIsi(), p.getTanggalDibuat() };
                    tableModel.addRow(row);
                }
                statusLabel.setText((keyword.isEmpty() ? "Total: " : "Hasil Pencarian: ") + results.size() + " pengumuman");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Error mencari data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void showPengumumanDialog(Pengumuman pengumumanToEdit, String dialogTitle) {
        Frame parentFrame = (Frame) SwingUtilities.getWindowAncestor(this);
        PengumumanDialog dialog = new PengumumanDialog(parentFrame, dialogTitle, pengumumanToEdit);
        dialog.setVisible(true);

        if (dialog.isConfirmed()) {
            Pengumuman pengumumanData = dialog.getPengumuman();
            boolean success;
            if (pengumumanToEdit == null) {
                success = pengumumanDAO.addPengumuman(pengumumanData);
                if (success) JOptionPane.showMessageDialog(this, "Pengumuman berhasil ditambahkan!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                else JOptionPane.showMessageDialog(this, "Gagal menambahkan pengumuman!", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                success = pengumumanDAO.updatePengumuman(pengumumanData);
                 if (success) JOptionPane.showMessageDialog(this, "Pengumuman berhasil diupdate!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                else JOptionPane.showMessageDialog(this, "Gagal mengupdate pengumuman!", "Error", JOptionPane.ERROR_MESSAGE);
            }
            if (success) loadPengumumanData();
        }
    }
    
    private void editSelectedPengumuman() {
        int selectedRow = pengumumanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pengumuman yang ingin diedit!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPengumuman = (Integer) tableModel.getValueAt(selectedRow, 0);
        Pengumuman pengumumanToEdit = pengumumanDAO.getPengumumanById(idPengumuman); // Ambil data lengkap dari DAO

        if (pengumumanToEdit == null) {
             JOptionPane.showMessageDialog(this, "Data pengumuman tidak ditemukan untuk diedit.", "Error", JOptionPane.ERROR_MESSAGE);
             return;
        }
        showPengumumanDialog(pengumumanToEdit, "Edit Pengumuman");
    }

    private void deleteSelectedPengumuman() {
        int selectedRow = pengumumanTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih pengumuman yang ingin dihapus!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int idPengumuman = (Integer) tableModel.getValueAt(selectedRow, 0);
        String judul = (String) tableModel.getValueAt(selectedRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus pengumuman:\n\"" + judul + "\"?", "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            if (pengumumanDAO.deletePengumuman(idPengumuman)) {
                JOptionPane.showMessageDialog(this, "Pengumuman berhasil dihapus!", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadPengumumanData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus pengumuman!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

