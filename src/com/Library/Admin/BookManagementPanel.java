package com.Library.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class BookManagementPanel extends JPanel {
    private BookDAO bookDAO;
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton, searchButton;
    private JLabel statusLabel;
    
    // Warna hijau theme
    private final Color PRIMARY_GREEN = new Color(34, 139, 34);      // Forest Green
    private final Color LIGHT_GREEN = new Color(144, 238, 144);      // Light Green
    private final Color DARK_GREEN = new Color(0, 100, 0);           // Dark Green
    private final Color ACCENT_GREEN = new Color(50, 205, 50);       // Lime Green
    private final Color BG_GREEN = new Color(240, 255, 240);         // Honeydew
    
    public BookManagementPanel() {
        bookDAO = new BookDAO();
        initComponents();
        setupLayout();
        setupEvents();
        loadBooks();
    }
    
    private void initComponents() {
        setBackground(Color.WHITE);
        
        // Table
        String[] columns = {"ID", "Judul Buku", "Author", "Publisher", "Type", "Location", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa diedit langsung
            }
        };
        bookTable = new JTable(tableModel);
        bookTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        bookTable.setRowHeight(30);
        bookTable.setFont(new Font("Arial", Font.PLAIN, 12));
        bookTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        bookTable.getTableHeader().setBackground(PRIMARY_GREEN);
        bookTable.getTableHeader().setForeground(Color.WHITE);
        bookTable.setGridColor(LIGHT_GREEN);
        bookTable.setSelectionBackground(LIGHT_GREEN);
        bookTable.setSelectionForeground(DARK_GREEN);
        
        // Mengatur lebar kolom
        bookTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        bookTable.getColumnModel().getColumn(1).setPreferredWidth(200);  // Judul
        bookTable.getColumnModel().getColumn(2).setPreferredWidth(150);  // Author
        bookTable.getColumnModel().getColumn(3).setPreferredWidth(150);  // Publisher
        bookTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Type
        bookTable.getColumnModel().getColumn(5).setPreferredWidth(100);  // Location
        bookTable.getColumnModel().getColumn(6).setPreferredWidth(100);  // Status
        
        // Custom renderer untuk kolom Status
        bookTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                if (!isSelected) {
                    if ("Tersedia".equals(value)) {
                        c.setBackground(new Color(220, 255, 220));
                        c.setForeground(DARK_GREEN);
                    } else if ("Dipinjam".equals(value)) {
                        c.setBackground(new Color(230, 240, 255)); 
                        c.setForeground(new Color(0, 70, 140));
                    } else if("Rusak".equals(value)) {
                        c.setBackground(new Color(255, 200, 200)); 
                        c.setForeground(new Color(32, 0, 0)); 
                    }
                        else {
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
        statusLabel = new JLabel("Total: 0 buku");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(DARK_GREEN);
        
        // Buttons
        addButton = new JButton("Tambah Buku");
        editButton = new JButton("Edit Buku");
        deleteButton = new JButton("Hapus Buku");
        refreshButton = new JButton("Refresh");
        searchButton = new JButton("Cari");
        
        // Style buttons
        styleButton(addButton, ACCENT_GREEN);
        styleButton(editButton, new Color(255, 165, 0));
        styleButton(deleteButton, new Color(139, 0, 0));
        styleButton(refreshButton, PRIMARY_GREEN);
        styleButton(searchButton, DARK_GREEN);
    }
    
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(130, 35));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createRaisedBevelBorder());
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(DARK_GREEN);
        headerPanel.setPreferredSize(new Dimension(0, 60));
        
        JLabel titleLabel = new JLabel("DAFTAR BUKU PERPUSTAKAAN");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        add(headerPanel, BorderLayout.NORTH);
        
        // Top Panel (Search + Status)
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(BG_GREEN);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(BG_GREEN);
        
        JLabel searchLabel = new JLabel("Cari Buku:");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 14));
        searchLabel.setForeground(DARK_GREEN);
        
        searchPanel.add(searchLabel);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchField);
        searchPanel.add(Box.createHorizontalStrut(10));
        searchPanel.add(searchButton);
        
        // Status Panel
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        statusPanel.setBackground(BG_GREEN);
        statusPanel.add(statusLabel);
        
        topPanel.add(searchPanel, BorderLayout.WEST);
        topPanel.add(statusPanel, BorderLayout.EAST);
        
        add(topPanel, BorderLayout.NORTH);
        
        // Table Panel
        JScrollPane scrollPane = new JScrollPane(bookTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN, 2), 
            "Daftar Buku", 0, 0, 
            new Font("Arial", Font.BOLD, 14), PRIMARY_GREEN));
        scrollPane.getViewport().setBackground(Color.WHITE);
        
        add(scrollPane, BorderLayout.CENTER);
        
        // Button Panel
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
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddBookDialog();
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedBook();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedBook();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadBooks();
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchBooks();
            }
        });
        
        // Enter key untuk search
        searchField.addActionListener(e -> searchBooks());
    }
    
    private void loadBooks() {
    SwingUtilities.invokeLater(() -> {
        try {
            tableModel.setRowCount(0); // Clear table
            List<Book> books = bookDAO.getAllBooks();

            int tersedia = 0, dipinjam = 0, rusak = 0, hilang = 0;

            for (Book book : books) {
                Object[] row = {
                    book.getIdBuku(),
                    book.getJudulBuku(),
                    book.getAuthor(),
                    book.getPublisher(),
                    book.getType(),
                    book.getLocation(),
                    book.getStatus()
                };
                tableModel.addRow(row);

                // Hitung jumlah berdasarkan status
                switch (book.getStatus().toLowerCase()) {
                    case "tersedia":
                        tersedia++; break;
                    case "dipinjam":
                        dipinjam++; break;
                    case "rusak":
                        rusak++; break;
                    case "hilang":
                        hilang++; break;
                }
            }

            // Update status info lengkap
            int total = books.size();
            statusLabel.setText(String.format(
                 "<html>Total: %d buku<br/>Tersedia: %d, Dipinjam: %d, <br/>Rusak: %d, Hilang: %d</html>",
                total, tersedia, dipinjam, rusak, hilang
            ));

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Error loading books: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    });
}

    
    private void searchBooks() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadBooks();
            return;
        }
        
        SwingUtilities.invokeLater(() -> {
            try {
                tableModel.setRowCount(0);
                List<Book> books = bookDAO.searchBooks(keyword);
                
                for (Book book : books) {
                    Object[] row = {
                        book.getIdBuku(),
                        book.getJudulBuku(),
                        book.getAuthor(),
                        book.getPublisher(),
                        book.getType(),
                        book.getLocation(),
                        book.getStatus()
                    };
                    tableModel.addRow(row);
                }
                
                statusLabel.setText("Hasil Pencarian: " + books.size() + " buku ditemukan");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, 
                    "Error searching books: " + e.getMessage(), 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
    
    private void showAddBookDialog() {
        // Cari parent frame untuk dialog
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        Frame parentFrame = (parentWindow instanceof Frame) ? (Frame) parentWindow : null;
        
        BookDialog dialog = new BookDialog(parentFrame, "Tambah Buku Baru", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Book newBook = dialog.getBook();
            if (bookDAO.addBook(newBook)) {
                JOptionPane.showMessageDialog(this, 
                    "Buku berhasil ditambahkan!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal menambahkan buku!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Warning: Pilih buku yang ingin diedit!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Get selected book data
        int idBuku = (Integer) tableModel.getValueAt(selectedRow, 0);
        String judulBuku = (String) tableModel.getValueAt(selectedRow, 1);
        String author = (String) tableModel.getValueAt(selectedRow, 2);
        String publisher = (String) tableModel.getValueAt(selectedRow, 3);
        String Type = (String) tableModel.getValueAt(selectedRow, 4);
        String Location = (String) tableModel.getValueAt(selectedRow, 5);
        String status = (String) tableModel.getValueAt(selectedRow, 6);
        
        Book selectedBook = new Book(idBuku, judulBuku, author, publisher, Type, Location, status);
        
        // Cari parent frame untuk dialog
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        Frame parentFrame = (parentWindow instanceof Frame) ? (Frame) parentWindow : null;
        
        BookDialog dialog = new BookDialog(parentFrame, "Edit Buku", selectedBook);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Book updatedBook = dialog.getBook();
            updatedBook.setIdBuku(idBuku); 
            
            if (bookDAO.updateBook(updatedBook)) {
                JOptionPane.showMessageDialog(this, 
                    "Buku berhasil diupdate!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal mengupdate buku!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void deleteSelectedBook() {
        int selectedRow = bookTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, 
                "Warning: Pilih buku yang ingin dihapus!", 
                "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int idBuku = (Integer) tableModel.getValueAt(selectedRow, 0);
        String judulBuku = (String) tableModel.getValueAt(selectedRow, 1);
        
        int option = JOptionPane.showConfirmDialog(this,
            "Yakin ingin menghapus buku:\n'" + judulBuku + "'?",
            "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (option == JOptionPane.YES_OPTION) {
            if (bookDAO.deleteBook(idBuku)) {
                JOptionPane.showMessageDialog(this, 
                    "Buku berhasil dihapus!", 
                    "Sukses", JOptionPane.INFORMATION_MESSAGE);
                loadBooks();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Gagal menghapus buku!", 
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}