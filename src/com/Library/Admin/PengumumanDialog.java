package com.Library.Admin;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class PengumumanDialog extends JDialog {
    private JTextField judulField;
    private JTextArea isiArea;
    private JTextField tanggalField;
    private JButton saveButton;
    private JButton cancelButton;

    private boolean confirmed = false;
    private Pengumuman pengumuman;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private final Color DARK_GREEN = new Color(0, 100, 0);
    private final Color BG_GREEN = new Color(240, 255, 240);

    public PengumumanDialog(Frame parent, String dialogTitle, Pengumuman pengumumanToEdit) {
        super(parent, dialogTitle, true);
        this.pengumuman = pengumumanToEdit;

        initComponents();
        setupLayout();
        setupEvents();

        if (pengumumanToEdit != null) {
            populateFields(pengumumanToEdit);
        } else {
            tanggalField.setText(dateFormat.format(new java.util.Date())); // Tanggal hari ini untuk baru
        }
    }

    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        judulField = new JTextField(30);
        isiArea = new JTextArea(10, 30);
        isiArea.setLineWrap(true);
        isiArea.setWrapStyleWord(true);
        tanggalField = new JTextField(10);

        saveButton = new JButton("Simpan");
        cancelButton = new JButton("Batal");

        styleTextField(judulField);
        styleTextArea(isiArea);
        styleTextField(tanggalField);
        styleButton(saveButton, PRIMARY_GREEN);
        styleButton(cancelButton, new Color(220, 20, 60));
    }
    
    private void styleTextField(JTextField field) {
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_GREEN, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleTextArea(JTextArea area) {
        area.setFont(new Font("Arial", Font.PLAIN, 14));
        area.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(PRIMARY_GREEN, 1),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)
        ));
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
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(BG_GREEN);

        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setPreferredSize(new Dimension(0, 50));
        JLabel titleLabelDialog = new JLabel(getTitle());
        titleLabelDialog.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabelDialog.setForeground(Color.WHITE);
        headerPanel.add(titleLabelDialog);
        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG_GREEN);
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        JLabel judulLabel = new JLabel("Judul:");
        styleFormLabel(judulLabel);
        formPanel.add(judulLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(judulField, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE;
        JLabel isiLabel = new JLabel("Isi Pengumuman:");
        styleFormLabel(isiLabel);
        formPanel.add(isiLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.gridwidth = 2; gbc.fill = GridBagConstraints.BOTH; gbc.weightx = 1.0; gbc.weighty = 1.0;
        formPanel.add(new JScrollPane(isiArea), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.NONE; gbc.weighty = 0;
        JLabel tanggalLabel = new JLabel("Tanggal (YYYY-MM-DD):");
        styleFormLabel(tanggalLabel);
        formPanel.add(tanggalLabel, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 1; gbc.fill = GridBagConstraints.HORIZONTAL; gbc.weightx = 0;
        formPanel.add(tanggalField, gbc);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(BG_GREEN);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0,0,10,0));
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void styleFormLabel(JLabel label) {
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(DARK_GREEN);
    }

    private void setupEvents() {
        saveButton.addActionListener(e -> savePengumuman());
        cancelButton.addActionListener(e -> {
            confirmed = false;
            dispose();
        });
    }

    private void populateFields(Pengumuman p) {
        judulField.setText(p.getJudul());
        isiArea.setText(p.getIsi());
        tanggalField.setText(p.getTanggalDibuat() != null ? dateFormat.format(p.getTanggalDibuat()) : "");
    }

    private Date parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            showError("Tanggal tidak boleh kosong.");
            return null;
        }
        try {
            java.util.Date utilDate = dateFormat.parse(dateStr.trim());
            return new Date(utilDate.getTime());
        } catch (ParseException e) {
            showError("Format tanggal salah. Gunakan YYYY-MM-DD (Contoh: 2024-01-15).");
            return null;
        }
    }

    private void savePengumuman() {
        String judul = judulField.getText().trim();
        String isi = isiArea.getText().trim();
        String tanggalStr = tanggalField.getText().trim();

        if (judul.isEmpty()) {
            showError("Judul pengumuman tidak boleh kosong!");
            judulField.requestFocus();
            return;
        }
        if (isi.isEmpty()) {
            showError("Isi pengumuman tidak boleh kosong!");
            isiArea.requestFocus();
            return;
        }

        Date tanggalDibuat = parseDate(tanggalStr);
        if (tanggalDibuat == null) {
            tanggalField.requestFocus();
            return;
        }

        if (this.pengumuman == null) {
            this.pengumuman = new Pengumuman();
        }

        this.pengumuman.setJudul(judul);
        this.pengumuman.setIsi(isi);
        this.pengumuman.setTanggalDibuat(tanggalDibuat);

        confirmed = true;
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Input Error", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Pengumuman getPengumuman() {
        return pengumuman;
    }
}

