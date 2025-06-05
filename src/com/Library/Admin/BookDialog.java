package com.Library.Admin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BookDialog extends JDialog {
    private JTextField judulField;
    private JTextField authorField;
    private JTextField publisherField;
    private JTextField typeField;
    private JTextField locationField;
    private JComboBox<String> statusComboBox;
    private JButton saveButton;
    private JButton cancelButton;
    private boolean confirmed = false;
    private Book book;

    private final Color PRIMARY_GREEN = new Color(34, 139, 34);
    private final Color DARK_GREEN = new Color(0, 100, 0);
    private final Color BG_GREEN = new Color(240, 255, 240);

    public BookDialog(Frame parent, String title, Book book) {
        super(parent, title, true);
        this.book = book;
        initComponents();
        setupLayout();
        setupEvents();

        if (book != null) {
            populateFields();
        }
    }

    private void initComponents() {
        setSize(600, 500);
        setLocationRelativeTo(getParent());
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        judulField = new JTextField(20);
        authorField = new JTextField(20);
        publisherField = new JTextField(20);
        typeField = new JTextField(20);
        locationField = new JTextField(20);

        String[] statusOptions = {"Tersedia", "Dipinjam", "Rusak", "Hilang"};
        statusComboBox = new JComboBox<>(statusOptions);

        saveButton = new JButton("Simpan");
        cancelButton = new JButton("Batal");

        styleTextField(judulField);
        styleTextField(authorField);
        styleTextField(publisherField);
        styleTextField(typeField);
        styleTextField(locationField);
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

    private void styleComboBox(JComboBox<String> comboBox) {
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

        addFormField(formPanel, gbc, 0, "Judul Buku:", judulField);
        addFormField(formPanel, gbc, 1, "Author:", authorField);
        addFormField(formPanel, gbc, 2, "Publisher:", publisherField);
        addFormField(formPanel, gbc, 3, "Type:", typeField);
        addFormField(formPanel, gbc, 4, "Location:", locationField);
        addFormField(formPanel, gbc, 5, "Status:", statusComboBox);

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
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveBook();
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
        if (book != null) {
            judulField.setText(book.getJudulBuku());
            authorField.setText(book.getAuthor());
            publisherField.setText(book.getPublisher());
            typeField.setText(book.getType());
            locationField.setText(book.getLocation());
            statusComboBox.setSelectedItem(book.getStatus());
        }
    }

    private void saveBook() {
        if (judulField.getText().trim().isEmpty()) {
            showError("Judul buku tidak boleh kosong!");
            judulField.requestFocus();
            return;
        }

        if (authorField.getText().trim().isEmpty()) {
            showError("Author tidak boleh kosong!");
            authorField.requestFocus();
            return;
        }

        if (publisherField.getText().trim().isEmpty()) {
            showError("Publisher tidak boleh kosong!");
            publisherField.requestFocus();
            return;
        }

        if (typeField.getText().trim().isEmpty()) {
            showError("Type tidak boleh kosong!");
            typeField.requestFocus();
            return;
        }

        if (locationField.getText().trim().isEmpty()) {
            showError("Location tidak boleh kosong!");
            locationField.requestFocus();
            return;
        }

        if (book == null) {
            book = new Book();
        }

        book.setJudulBuku(judulField.getText().trim());
        book.setAuthor(authorField.getText().trim());
        book.setPublisher(publisherField.getText().trim());
        book.setType(typeField.getText().trim());
        book.setLocation(locationField.getText().trim());
        book.setStatus((String) statusComboBox.getSelectedItem());

        confirmed = true;
        dispose();
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this,
            "⚠️ " + message,
            "Input Error",
            JOptionPane.WARNING_MESSAGE);
    }

    public boolean isConfirmed() {
        return confirmed;
    }

    public Book getBook() {
        return book;
    }
}
