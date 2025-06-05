package com.Library.Anggota;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import com.Library.Admin.Book;

public class BukuViewPanel extends JPanel {
    
    private final Color PRIMARY_GREEN = new Color(67, 160, 71);
    private final Color DARK_GREEN = new Color(46, 125, 50);
    private final Color LIGHT_GREEN = new Color(129, 199, 132);
    private final Color BG_LIGHT = new Color(248, 252, 248);
    private final Color CARD_BG = new Color(255, 255, 255);
    private final Color TEXT_PRIMARY = new Color(33, 33, 33);
    private final Color TEXT_SECONDARY = new Color(117, 117, 117);
    private final Color BORDER_COLOR = new Color(230, 230, 230);
    private final Color SUCCESS_GREEN = new Color(56, 142, 60);
    private final Color WARNING_ORANGE = new Color(255, 152, 0);

    private JTable bukuTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    private JTextField searchField;
    private JComboBox<String> filterComboBox;
    private JLabel totalBooksLabel;
    private JLabel availableBooksLabel;
    
    private BookDAO bookDAO;

    public BukuViewPanel() {
        this.bookDAO = new BookDAO();
        initializeComponents();
        loadBooksData();
        updateStatistics();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(BG_LIGHT);
        setBorder(new EmptyBorder(25, 25, 25, 25));

        // Header Panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Filter and Search Panel
        JPanel filterPanel = createFilterPanel();
        add(filterPanel, BorderLayout.CENTER);

        // Table Panel
        JPanel tablePanel = createTablePanel();
        add(tablePanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(BG_LIGHT);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Title
        JLabel titleLabel = new JLabel("Koleksi Buku Perpustakaan");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(DARK_GREEN);
        headerPanel.add(titleLabel, BorderLayout.WEST);

        // Statistics Panel
        JPanel statsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        statsPanel.setBackground(BG_LIGHT);

        totalBooksLabel = createStatLabel("Total Buku: 0", PRIMARY_GREEN);
        availableBooksLabel = createStatLabel("Tersedia: 0", SUCCESS_GREEN);

        statsPanel.add(totalBooksLabel);
        statsPanel.add(availableBooksLabel);
        headerPanel.add(statsPanel, BorderLayout.EAST);

        return headerPanel;
    }

    private JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(color);
        label.setOpaque(true);
        label.setBackground(CARD_BG);
        label.setBorder(new EmptyBorder(8, 12, 8, 12));
        return label;
    }

    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new BorderLayout());
        filterPanel.setBackground(CARD_BG);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Filter Controls Panel
        JPanel controlsPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        controlsPanel.setBackground(CARD_BG);

        // Search Field
        JLabel searchLabel = new JLabel("Cari Buku:");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(TEXT_PRIMARY);
        controlsPanel.add(searchLabel);

        searchField = new JTextField(20);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_COLOR, 1),
                new EmptyBorder(8, 12, 8, 12)
        ));
        controlsPanel.add(searchField);

        // Search Button
        JButton searchButton = createActionButton("Cari", "");
        searchButton.addActionListener(e -> performSearch());
        controlsPanel.add(searchButton);

        // Filter by Type
        JLabel filterLabel = new JLabel("Filter Tipe:");
        filterLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        filterLabel.setForeground(TEXT_PRIMARY);
        controlsPanel.add(filterLabel);

        filterComboBox = new JComboBox<>();
        filterComboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        filterComboBox.setPreferredSize(new Dimension(150, 35));
        loadFilterOptions();
        controlsPanel.add(filterComboBox);

        // Filter Button
        JButton filterButton = createActionButton("Filter", "");
        filterButton.addActionListener(e -> performFilter());
        controlsPanel.add(filterButton);

        // Reset Button
        JButton resetButton = createActionButton("Reset", "");
        resetButton.addActionListener(e -> resetFilters());
        controlsPanel.add(resetButton);

        filterPanel.add(controlsPanel, BorderLayout.CENTER);
        return filterPanel;
    }

    private JButton createActionButton(String text, String icon) {
        JButton button = new JButton(icon + " " + text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(PRIMARY_GREEN);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(new EmptyBorder(8, 15, 8, 15));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(DARK_GREEN);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_GREEN);
            }
        });

        return button;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(BG_LIGHT);
        tablePanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Table Model
        String[] columnNames = {"ID", "Judul Buku", "Author", "Publisher", "Type", "Location", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        // Table
        bukuTable = new JTable(tableModel);
        bukuTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        bukuTable.setRowHeight(35);
        bukuTable.setGridColor(BORDER_COLOR);
        bukuTable.setSelectionBackground(LIGHT_GREEN);
        bukuTable.setSelectionForeground(TEXT_PRIMARY);

        // Table Header
        bukuTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        bukuTable.getTableHeader().setBackground(PRIMARY_GREEN);
        bukuTable.getTableHeader().setForeground(Color.WHITE);
        bukuTable.getTableHeader().setBorder(BorderFactory.createEmptyBorder());

        // Column Widths
        bukuTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // ID
        bukuTable.getColumnModel().getColumn(1).setPreferredWidth(250); // Judul
        bukuTable.getColumnModel().getColumn(2).setPreferredWidth(180); // Pengarang
        bukuTable.getColumnModel().getColumn(3).setPreferredWidth(150); // Penerbit
        bukuTable.getColumnModel().getColumn(4).setPreferredWidth(100); // Tipe
        bukuTable.getColumnModel().getColumn(5).setPreferredWidth(120); // Lokasi
        bukuTable.getColumnModel().getColumn(6).setPreferredWidth(100); // Status

        // Custom renderer for Status column
        bukuTable.getColumnModel().getColumn(6).setCellRenderer(new StatusCellRenderer());

        // Table Sorter
        sorter = new TableRowSorter<>(tableModel);
        bukuTable.setRowSorter(sorter);

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(bukuTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1));
        scrollPane.getViewport().setBackground(CARD_BG);

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private void loadFilterOptions() {
        filterComboBox.removeAllItems();
        filterComboBox.addItem("Semua Tipe");
        
        List<String> types = bookDAO.getAllTypes();
        for (String type : types) {
            filterComboBox.addItem(type);
        }
    }

    private void loadBooksData() {
        List<Book> allBooks = bookDAO.getAllBooks();
        loadBooksDataFromList(allBooks);
    }

    private void loadBooksDataFromList(List<Book> books) {
        tableModel.setRowCount(0);
        
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
    }

    private void performSearch() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadBooksData();
        } else {
            List<Book> searchResults = bookDAO.searchBooks(keyword);
            loadBooksDataFromList(searchResults);
        }
        updateStatistics();
    }

    private void performFilter() {
        String selectedType = (String) filterComboBox.getSelectedItem();
        if ("Semua Tipe".equals(selectedType)) {
            loadBooksData();
        } else {
            List<Book> filteredBooks = bookDAO.getBooksByType(selectedType);
            loadBooksDataFromList(filteredBooks);
        }
        updateStatistics();
    }

    private void resetFilters() {
        searchField.setText("");
        filterComboBox.setSelectedIndex(0);
        loadBooksData();
        updateStatistics();
    }

    private void updateStatistics() {
        int totalBooks = bookDAO.getTotalBooks();
        int availableBooks = bookDAO.getAvailableBooksCount();
        
        totalBooksLabel.setText("Total Buku: " + totalBooks);
        availableBooksLabel.setText("Tersedia: " + availableBooks);
    }

    private class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, 
                boolean isSelected, boolean hasFocus, int row, int column) {
            
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
    
            if (!isSelected) {
                String status = (String) value;
                if ("Tersedia".equalsIgnoreCase(status)) {
                    setForeground(SUCCESS_GREEN);
                    setFont(getFont().deriveFont(Font.BOLD));
                } else if ("Dipinjam".equalsIgnoreCase(status)) {
                    setForeground(WARNING_ORANGE);
                    setFont(getFont().deriveFont(Font.BOLD));
                } else {
                    setForeground(TEXT_SECONDARY);
                    setFont(getFont().deriveFont(Font.PLAIN));
                }
            }
            
            return this;
        }
    }
}