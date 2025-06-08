package com.Library.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.time.LocalDate;

public class MemberDialog extends JDialog {
    private JTextField namaField;
    private JTextArea alamatArea;
    private JTextField noTeleponField;
    private JTextField emailField;
    private JComboBox<String> statusComboBox;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean confirmed = false;
    private Member member;

    private final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private final Color DARK_GREEN = new Color(0, 100, 0);
    private final Color BG_GREEN = new Color(240, 255, 240);

    public MemberDialog(Frame parent, String title, Member member) {
        super(parent, title, true);
        this.member = member;
        initComponents();
        setupLayout();
        setupEvents();

        if (member != null) {
            populateFields();
        }
    }

    private void initComponents() {
        setSize(600, 550);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        namaField = new JTextField(20);
        alamatArea = new JTextArea(3, 20);
        alamatArea.setLineWrap(true);
        alamatArea.setWrapStyleWord(true);
        noTeleponField = new JTextField(20);
        emailField = new JTextField(20);

        String[] statusOptions = {"1", "0"}; // 1 = Aktif, 0 = Tidak Aktif
        statusComboBox = new JComboBox<>(statusOptions);

        saveButton = new JButton("Simpan");
        cancelButton = new JButton("Batal");

        styleTextField(namaField);
        styleTextArea(alamatArea);
        styleTextField(noTeleponField);
        styleTextField(emailField);
        styleComboBox(statusComboBox);
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

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(PRIMARY_GREEN, 1));
        
        // Buat renderer kustom untuk menampilkan teks yang lebih jelas
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if ("1".equals(value)) {
                    setText("Aktif");
                } else if ("0".equals(value)) {
                    setText("Tidak Aktif");
                }
                return this;
            }
        });
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

        JLabel titleLabel = new JLabel(getTitle());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel);

        add(headerPanel, BorderLayout.NORTH);

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(BG_GREEN);
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;

        addFormField(formPanel, gbc, 0, "Nama Lengkap:", namaField);
        
        // Alamat dengan JScrollPane
        JScrollPane alamatScroll = new JScrollPane(alamatArea);
        alamatScroll.setPreferredSize(new Dimension(300, 80));
        addFormField(formPanel, gbc, 1, "Alamat:", alamatScroll);
        
        addFormField(formPanel, gbc, 2, "No. Telepon:", noTeleponField);
        addFormField(formPanel, gbc, 3, "Email:", emailField);
        // Status hanya jika member tidak null (edit)
        if (member != null) {
            addFormField(formPanel, gbc, 4, "Status:", statusComboBox);
        }

        // Tambahan info tanggal daftar jika edit
        if (member != null) {
            JLabel tanggalInfo = new JLabel("Tanggal Daftar: " + member.getTanggalDaftar());
            tanggalInfo.setFont(new Font("Arial", Font.ITALIC, 12));
            tanggalInfo.setForeground(DARK_GREEN);
            addFormField(formPanel, gbc, 5, "", tanggalInfo);
        }

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        buttonPanel.setBackground(BG_GREEN);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    private void addFormField(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent field) {
        if (!labelText.isEmpty()) {
            JLabel label = new JLabel(labelText);
            label.setFont(new Font("Arial", Font.BOLD, 14));
            label.setForeground(DARK_GREEN);

            gbc.gridx = 0;
            gbc.gridy = row;
            gbc.gridwidth = 1;
            panel.add(label, gbc);
        }

        gbc.gridx = labelText.isEmpty() ? 0 : 1;
        gbc.gridy = row;
        gbc.gridwidth = labelText.isEmpty() ? 2 : 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(field, gbc);
        gbc.fill = GridBagConstraints.NONE;
    }

    private void setupEvents() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveMember();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    private void populateFields() {
        if (member != null) {
            namaField.setText(member.getNama());
            alamatArea.setText(member.getAlamat());
            noTeleponField.setText(member.getNoTelepon());
            emailField.setText(member.getEmail());
            statusComboBox.setSelectedItem(member.getStatusAktif());
        }
    }

    private void saveMember() {
        if (namaField.getText().trim().isEmpty()) {
            showError("Nama lengkap tidak boleh kosong!");
            namaField.requestFocus();
            return;
        }

        if (alamatArea.getText().trim().isEmpty()) {
            showError("Alamat tidak boleh kosong!");
            alamatArea.requestFocus();
            return;
        }

        if (noTeleponField.getText().trim().isEmpty()) {
            showError("No. telepon tidak boleh kosong!");
            noTeleponField.requestFocus();
            return;
        }

        if (emailField.getText().trim().isEmpty()) {
            showError("Email tidak boleh kosong!");
            emailField.requestFocus();
            return;
        }

        // Validasi format email sederhana
        String email = emailField.getText().trim();
        if (!email.contains("@") || !email.contains(".")) {
            showError("Format email tidak valid!");
            emailField.requestFocus();
            return;
        }

        if (member == null) {
            member = new Member();
            // Set tanggal daftar untuk member baru
            member.setTanggalDaftar(Date.valueOf(LocalDate.now()));
        }

        member.setNama(namaField.getText().trim());
        member.setAlamat(alamatArea.getText().trim());
        member.setNoTelepon(noTeleponField.getText().trim());
        member.setEmail(emailField.getText().trim());
        member.setStatusAktif((String) statusComboBox.getSelectedItem());

        confirmed = true;
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            "Warning! " + message,
            "Input Error",
            JOptionPane.WARNING_MESSAGE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Member getMember() {
        return member;
    }
}