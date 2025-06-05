package com.Library.Admin;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List; 
import java.util.Objects;

public class PeminjamanDialog extends JDialog {
    private JComboBox<BukuItem> bukuComboBox;
    private JComboBox<AnggotaItem> anggotaComboBox;

    private JTextField tanggalPinjamField;
    private JTextField tanggalJatuhTempoField;
    private JTextField tanggalPengembalianField;
    private JComboBox<String> statusComboBox;
    private JButton saveButton;
    private JButton cancelButton;

    private boolean confirmed = false;
    private Peminjaman peminjaman; 
    private PeminjamanDAO peminjamanDAO; 
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private final Color DARK_GREEN = new Color(0, 100, 0);
    private final Color BG_GREEN = new Color(240, 255, 240);

    // Inner class untuk item di JComboBox Buku
    public static class BukuItem {
        private int id;
        private String displayValue;

        public BukuItem(int id, String displayValue) {
            this.id = id;
            this.displayValue = displayValue;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return displayValue; // Ini yang akan ditampilkan di JComboBox
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            BukuItem bukuItem = (BukuItem) o;
            return id == bukuItem.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    // Inner class untuk item di JComboBox Anggota
    public static class AnggotaItem {
        private int id;
        private String displayValue;

        public AnggotaItem(int id, String displayValue) {
            this.id = id;
            this.displayValue = displayValue;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return displayValue; // Ini yang akan ditampilkan di JComboBox
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            AnggotaItem that = (AnggotaItem) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }


    public PeminjamanDialog(Frame parent, String title, Peminjaman peminjamanToEdit) {
        super(parent, title, true);
        this.peminjaman = peminjamanToEdit; 
        this.peminjamanDAO = new PeminjamanDAO(); 

        initComponents(); 
        setupLayout();    
        setupEvents();    

        if (peminjamanToEdit != null) { // Mode Edit
            populateFields(peminjamanToEdit);
            bukuComboBox.setEnabled(false); // Tidak bisa ganti buku saat edit
            anggotaComboBox.setEnabled(false); // Tidak bisa ganti anggota saat edit
            if ("Dikembalikan".equalsIgnoreCase(peminjamanToEdit.getStatusPeminjaman())) {
                tanggalPinjamField.setEditable(false);
                tanggalJatuhTempoField.setEditable(false);
                statusComboBox.setEnabled(false); // Jika sudah kembali, status juga tidak bisa diubah lagi sembarangan
            }
        } else { 
            loadAvailableBooksToComboBox();
            loadActiveMembersToComboBox();
            tanggalPinjamField.setText(dateFormat.format(new java.util.Date())); // Tanggal hari ini
            statusComboBox.setSelectedItem("Dipinjam");
            tanggalPengembalianField.setEnabled(false); // Nonaktifkan saat baru
            bukuComboBox.setEnabled(true);
            anggotaComboBox.setEnabled(true);
        }
    }

    private void initComponents() {
        setSize(600, 550);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        bukuComboBox = new JComboBox<>();
        anggotaComboBox = new JComboBox<>();
        tanggalPinjamField = new JTextField(15);
        tanggalJatuhTempoField = new JTextField(15);
        tanggalPengembalianField = new JTextField(15);

        String[] statusOptions = {"Dipinjam", "Dikembalikan", "Terlambat"};
        statusComboBox = new JComboBox<>(statusOptions);

        saveButton = new JButton("Simpan");
        cancelButton = new JButton("Batal");

        // Styling
        styleComboBox(bukuComboBox); // Style untuk JComboBox
        styleComboBox(anggotaComboBox); // Style untuk JComboBox
        styleTextField(tanggalPinjamField);
        styleTextField(tanggalJatuhTempoField);
        styleTextField(tanggalPengembalianField);
        styleComboBox(statusComboBox);
        styleButton(saveButton, PRIMARY_GREEN);
        styleButton(cancelButton, new Color(220, 20, 60));
    }

    private void loadAvailableBooksToComboBox() {
        bukuComboBox.removeAllItems(); // Kosongkan dulu
        bukuComboBox.addItem(new BukuItem(0, "--- Pilih Buku ---")); // Placeholder
        List<BukuItem> availableBooks = peminjamanDAO.getAvailableBooks();
        for (BukuItem book : availableBooks) {
            bukuComboBox.addItem(book);
        }
    }

    private void loadActiveMembersToComboBox() {
        anggotaComboBox.removeAllItems(); // Kosongkan dulu
        anggotaComboBox.addItem(new AnggotaItem(0, "--- Pilih Anggota ---")); // Placeholder
        List<AnggotaItem> activeMembers = peminjamanDAO.getActiveMembers();
        for (AnggotaItem member : activeMembers) {
            anggotaComboBox.addItem(member);
        }
    }

    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_GREEN, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleComboBox(JComboBox<?> comboBox) { 
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(PRIMARY_GREEN, 1));
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setPreferredSize(new Dimension(120, 40));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }

    private void setupLayout() {
        setLayout(new BorderLayout());

        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        JLabel titleLabelDialog = new JLabel(getTitle());
        titleLabelDialog.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabelDialog.setForeground(Color.WHITE);
        titleLabelDialog.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabelDialog);
        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG_GREEN);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        addFormField(formPanel, gbc, 0, "Buku:", bukuComboBox); 
        addFormField(formPanel, gbc, 1, "Anggota:", anggotaComboBox); 
        addFormField(formPanel, gbc, 2, "Tgl Pinjam (YYYY-MM-DD):", tanggalPinjamField);
        addFormField(formPanel, gbc, 3, "Tgl Jatuh Tempo (YYYY-MM-DD):", tanggalJatuhTempoField);
        addFormField(formPanel, gbc, 4, "Status Peminjaman:", statusComboBox);
        addFormField(formPanel, gbc, 5, "Tgl Pengembalian (YYYY-MM-DD):", tanggalPengembalianField);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(BG_GREEN);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(DARK_GREEN);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.gridy = row;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
        gbc.fill = GridBagConstraints.NONE;
    }

    private void setupEvents() {
        saveButton.addActionListener(e -> savePeminjamanData());
        cancelButton.addActionListener(e -> dispose());

        statusComboBox.addActionListener(e -> {
            String selectedStatus = (String) statusComboBox.getSelectedItem();
            if ("Dikembalikan".equalsIgnoreCase(selectedStatus)) {
                tanggalPengembalianField.setEnabled(true);
                if (tanggalPengembalianField.getText().trim().isEmpty()) {
                    tanggalPengembalianField.setText(dateFormat.format(new java.util.Date())); // Default ke hari ini
                }
            } else {
                tanggalPengembalianField.setEnabled(false);
                tanggalPengembalianField.setText(""); // Kosongkan jika bukan dikembalikan
            }
        });
    }

    private void populateFields(Peminjaman p) {
        // Untuk mode edit, buku dan anggota sudah ditentukan dan tidak bisa diubah
        // Tampilkan buku yang sedang dipinjam
        bukuComboBox.removeAllItems();
        String bukuDisplay = (p.getJudulBuku() != null) ? p.getIdBuku() + " - " + p.getJudulBuku() : "ID: " + p.getIdBuku();
        bukuComboBox.addItem(new BukuItem(p.getIdBuku(), bukuDisplay));
        bukuComboBox.setSelectedIndex(0);

        // Tampilkan anggota yang meminjam
        anggotaComboBox.removeAllItems();
        String anggotaDisplay = (p.getNamaAnggota() != null) ? p.getIdAnggota() + " - " + p.getNamaAnggota() : "ID: " + p.getIdAnggota();
        anggotaComboBox.addItem(new AnggotaItem(p.getIdAnggota(), anggotaDisplay));
        anggotaComboBox.setSelectedIndex(0);

        tanggalPinjamField.setText(p.getTanggalPinjam() != null ? dateFormat.format(p.getTanggalPinjam()) : "");
        tanggalJatuhTempoField.setText(p.getTanggalJatuhTempo() != null ? dateFormat.format(p.getTanggalJatuhTempo()) : "");
        statusComboBox.setSelectedItem(p.getStatusPeminjaman());
        tanggalPengembalianField.setText(p.getTanggalPengembalian() != null ? dateFormat.format(p.getTanggalPengembalian()) : "");

        if ("Dikembalikan".equalsIgnoreCase(p.getStatusPeminjaman())) {
            tanggalPengembalianField.setEnabled(true);
        } else {
            tanggalPengembalianField.setEnabled(false);
        }
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            java.util.Date utilDate = dateFormat.parse(dateStr.trim());
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            showError("Format tanggal salah untuk '" + dateStr + "'. Gunakan YYYY-MM-DD.");
            return null;
        }
    }

    private void savePeminjamanData() {
        Date tglPinjam = parseDate(tanggalPinjamField.getText());
        if (tglPinjam == null && !tanggalPinjamField.getText().trim().isEmpty()) return;
        if (tglPinjam == null) {
            showError("Tanggal Pinjam tidak boleh kosong!");
            tanggalPinjamField.requestFocus();
            return;
        }

        Date tglJatuhTempo = parseDate(tanggalJatuhTempoField.getText());
        if (tglJatuhTempo == null && !tanggalJatuhTempoField.getText().trim().isEmpty()) return;
        if (tglJatuhTempo == null) {
            showError("Tanggal Jatuh Tempo tidak boleh kosong!");
            tanggalJatuhTempoField.requestFocus();
            return;
        }

        String status = (String) statusComboBox.getSelectedItem();
        Date tglPengembalian = null;
        if ("Dikembalikan".equalsIgnoreCase(status)) {
            tglPengembalian = parseDate(tanggalPengembalianField.getText());
            if (tglPengembalian == null && !tanggalPengembalianField.getText().trim().isEmpty()) return;
            if (tglPengembalian == null) {
                showError("Tanggal Pengembalian harus diisi jika status 'Dikembalikan'!");
                tanggalPengembalianField.requestFocus();
                return;
            }
        }

        if (this.peminjaman == null) { // Mode Tambah Baru
            this.peminjaman = new Peminjaman();

            BukuItem selectedBuku = (BukuItem) bukuComboBox.getSelectedItem();
            if (selectedBuku == null || selectedBuku.getId() == 0) { // 0 adalah ID placeholder
                showError("Buku harus dipilih!");
                bukuComboBox.requestFocus();
                return;
            }
            this.peminjaman.setIdBuku(selectedBuku.getId());

            AnggotaItem selectedAnggota = (AnggotaItem) anggotaComboBox.getSelectedItem();
            if (selectedAnggota == null || selectedAnggota.getId() == 0) { // 0 adalah ID placeholder
                showError("Anggota harus dipilih!");
                anggotaComboBox.requestFocus();
                return;
            }
            this.peminjaman.setIdAnggota(selectedAnggota.getId());

        }
        // Untuk mode edit, ID Buku dan ID Anggota sudah ada di this.peminjaman dan tidak diubah

        this.peminjaman.setTanggalPinjam(tglPinjam);
        this.peminjaman.setTanggalJatuhTempo(tglJatuhTempo);
        this.peminjaman.setStatusPeminjaman(status);
        this.peminjaman.setTanggalPengembalian(tglPengembalian);

        confirmed = true;
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, "⚠️ " + message, "Input Error", JOptionPane.WARNING_MESSAGE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Peminjaman getPeminjaman() {
        return peminjaman;
    }
}