package com.Library.Admin;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.List;

public class MemberManagementPanel extends JPanel {
    private MemberDAO memberDAO;
    private JTable memberTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JButton addButton, editButton, deleteButton, refreshButton, searchButton, printCardButton;
    private JLabel statusLabel;
    

    private final Color PRIMARY_GREEN = new Color(34, 139, 34);      // Forest Green
    private final Color LIGHT_GREEN = new Color(144, 238, 144);      // Light Green
    private final Color DARK_GREEN = new Color(0, 100, 0);           // Dark Green
    private final Color ACCENT_GREEN = new Color(50, 205, 50);       // Lime Green
    private final Color BG_GREEN = new Color(240, 255, 240);         // Honeydew
    
    public MemberManagementPanel() {
        memberDAO = new MemberDAO();
        initComponents();
        setupLayout();
        setupEvents();
        loadMembers();
    }
    
    private void initComponents() {
        setBackground(Color.WHITE);
        
        // Table
        String[] columns = {"ID", "Nama", "Alamat", "No_Telepon", "Tanggal_Daftar", "Email", "Status_Aktif"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Tabel tidak bisa diedit langsung
            }
        };
        memberTable = new JTable(tableModel);
        memberTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        memberTable.setRowHeight(30);
        memberTable.setFont(new Font("Arial", Font.PLAIN, 12));
        memberTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        memberTable.getTableHeader().setBackground(PRIMARY_GREEN);
        memberTable.getTableHeader().setForeground(Color.WHITE);
        memberTable.setGridColor(LIGHT_GREEN);
        memberTable.setSelectionBackground(LIGHT_GREEN);
        memberTable.setSelectionForeground(DARK_GREEN);
        
        // Mengatur lebar kolom
        memberTable.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        memberTable.getColumnModel().getColumn(1).setPreferredWidth(150);  // Nama
        memberTable.getColumnModel().getColumn(2).setPreferredWidth(200);  // Alamat
        memberTable.getColumnModel().getColumn(3).setPreferredWidth(120);  // No_Telepon
        memberTable.getColumnModel().getColumn(4).setPreferredWidth(100);  // Tanggal_Daftar
        memberTable.getColumnModel().getColumn(5).setPreferredWidth(180);  // Email
        memberTable.getColumnModel().getColumn(6).setPreferredWidth(80);   // Status_Aktif
        
        // Custom renderer untuk kolom Status Aktif
        memberTable.getColumnModel().getColumn(6).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (!isSelected) {
                    if ("1".equalsIgnoreCase(value.toString())) {
                        c.setBackground(new Color(220, 255, 220));
                        c.setForeground(DARK_GREEN);
                        setText("Aktif");
                    } else if ("0".equalsIgnoreCase(value.toString())) {
                        c.setBackground(new Color(255, 240, 240));
                        c.setForeground(new Color(139, 0, 0));
                        setText("Tidak Aktif");
                    } else {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                } else {
                    if ("1".equalsIgnoreCase(value.toString())) {
                        setText("Aktif");
                    } else if ("0".equalsIgnoreCase(value.toString())) {
                        setText("Tidak Aktif");
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
        statusLabel = new JLabel("Total: 0 anggota");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 12));
        statusLabel.setForeground(DARK_GREEN);
        
        // Buttons
        addButton = new JButton("Tambah Anggota");
        editButton = new JButton("Edit Anggota");
        deleteButton = new JButton("Hapus Anggota");
        refreshButton = new JButton("Refresh");
        searchButton = new JButton("Cari");
        printCardButton = new JButton("Cetak Kartu");
        
        // Style buttons
        styleButton(addButton, ACCENT_GREEN);
        styleButton(editButton, new Color(255, 165, 0));
        styleButton(deleteButton, new Color(139, 0, 0));
        styleButton(refreshButton, PRIMARY_GREEN);
        styleButton(searchButton, DARK_GREEN);
        styleButton(printCardButton, new Color(138, 43, 226)); // Purple untuk print
    }
    
    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(140, 35));
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
        
        JLabel titleLabel = new JLabel("DAFTAR ANGGOTA PERPUSTAKAAN");
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
        
        JLabel searchLabel = new JLabel("Cari Anggota:");
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
        JScrollPane scrollPane = new JScrollPane(memberTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(PRIMARY_GREEN, 2), 
            "Daftar Anggota", 0, 0, 
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
        buttonPanel.add(printCardButton);
        buttonPanel.add(refreshButton);
        
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void setupEvents() {
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddMemberDialog();
            }
        });
        
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedMember();
            }
        });
        
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteSelectedMember();
            }
        });
        
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loadMembers();
            }
        });
        
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchMembers();
            }
        });
        
        printCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                printMemberCard();
            }
        });
        
        // Event untuk Enter pada search field
        searchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchMembers();
            }
        });
    }
    
    private void showAddMemberDialog() {
        MemberDialog dialog = new MemberDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                               "Tambah Anggota Baru", null);
        dialog.setVisible(true);
        
        if (dialog.isConfirmed()) {
            Member newMember = dialog.getMember();
            if (memberDAO.addMember(newMember)) {
                JOptionPane.showMessageDialog(this,
                    "Anggota berhasil ditambahkan!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                loadMembers();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal menambahkan anggota!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void editSelectedMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih anggota yang akan diedit!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int memberId = (Integer) tableModel.getValueAt(selectedRow, 0);
        Member selectedMember = findMemberById(memberId);
        
        if (selectedMember != null) {
            MemberDialog dialog = new MemberDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                                   "Edit Anggota", selectedMember);
            dialog.setVisible(true);
            
            if (dialog.isConfirmed()) {
                Member updatedMember = dialog.getMember();
                if (memberDAO.updateMember(updatedMember)) {
                    JOptionPane.showMessageDialog(this,
                        "Anggota berhasil diperbarui!",
                        "Sukses",
                        JOptionPane.INFORMATION_MESSAGE);
                    loadMembers();
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Gagal memperbarui anggota!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
    
    private void deleteSelectedMember() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Pilih anggota yang akan dihapus!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int memberId = (Integer) tableModel.getValueAt(selectedRow, 0);
        String memberName = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Apakah Anda yakin ingin menghapus anggota:\n" + memberName + " (ID: " + memberId + ")?",
            "Konfirmasi Hapus",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (memberDAO.deleteMember(memberId)) {
                JOptionPane.showMessageDialog(this,
                    "Anggota berhasil dihapus!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
                loadMembers();
            } else {
                JOptionPane.showMessageDialog(this,
                    "Gagal menghapus anggota!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void searchMembers() {
        String keyword = searchField.getText().trim();
        if (keyword.isEmpty()) {
            loadMembers();
            return;
        }
        
        List<Member> searchResults = memberDAO.searchMembers(keyword);
        populateTable(searchResults);
        updateStatusLabel(searchResults.size());
    }
    
    private void printMemberCard() {
        int selectedRow = memberTable.getSelectedRow();
        if (selectedRow == -1) {
            // Jika tidak ada yang dipilih, tampilkan peringatan
            JOptionPane.showMessageDialog(this,
                "Pilih anggota untuk mencetak kartu!",
                "Peringatan",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Ambil ID anggota dari baris yang dipilih
        int memberId = (Integer) tableModel.getValueAt(selectedRow, 0);
        // Cari data lengkap anggota di database berdasarkan ID
        Member selectedMember = findMemberById(memberId);
        
        // Jika anggota ditemukan, cetak kartu
        if (selectedMember != null) {
            printCard(selectedMember);
        }
    }
    
    private void printCard(Member member) {
        // Membuat objek PrinterJob untuk mencetak kartu
        PrinterJob job = PrinterJob.getPrinterJob();
        // Mengatur Printable untuk mencetak kartu anggota
        job.setPrintable(new MemberCardPrintable(member));
        
        if (job.printDialog()) {
            try {
                job.print();
                JOptionPane.showMessageDialog(this,
                    "Kartu anggota berhasil dicetak!",
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE);
            } catch (PrinterException e) {
                JOptionPane.showMessageDialog(this,
                    "Gagal mencetak kartu: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void loadMembers() {
        List<Member> members = memberDAO.getAllMembers();
        populateTable(members);
        updateStatusLabel(members.size());
    }
    
    private void populateTable(List<Member> members) {
        tableModel.setRowCount(0);
        for (Member member : members) {
            Object[] row = {
                member.getIdAnggota(),
                member.getNama(),
                member.getAlamat(),
                member.getNoTelepon(),
                member.getTanggalDaftar(),
                member.getEmail(),
                member.getStatusAktif()
            };
            tableModel.addRow(row);
        }
    }
    
    private void updateStatusLabel(int count) {
        int activeMembers = memberDAO.getActiveMembers();
        int inactiveMembers = memberDAO.getInactiveMembers();
        statusLabel.setText(String.format("Total: %d anggota | Aktif: %d | Tidak Aktif: %d", 
                                          count, activeMembers, inactiveMembers));
    }
    
    private Member findMemberById(int id) {
        List<Member> members = memberDAO.getAllMembers();
        for (Member member : members) {
            if (member.getIdAnggota() == id) {
                return member;
            }
        }
        return null;
    }

    // Inner class untuk mencetak kartu anggota
    private class MemberCardPrintable implements Printable {
        private Member member;
    
        public MemberCardPrintable(Member member) {
            this.member = member;
        }
        
        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) 
            throws PrinterException {
            if (pageIndex > 0) {
                return NO_SUCH_PAGE;
            }
        
            Graphics2D g2d = (Graphics2D) graphics;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
        
            // Enable antialiasing untuk hasil yang lebih halus
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
            int cardWidth = 400;
            int cardHeight = 280; 
            int margin = 50;
        
            // Draw card border dengan rounded rectangle
            g2d.setColor(new Color(102, 165, 130));
            g2d.setStroke(new BasicStroke(3));
            g2d.drawRoundRect(margin, margin, cardWidth, cardHeight, 15, 15);
        
            // Header background dengan gradient
            GradientPaint headerGradient = new GradientPaint(
                margin, margin, new Color(102, 165, 130),
                margin, margin + 60, new Color(139, 195, 158)
            );

            g2d.setPaint(headerGradient);
            g2d.fillRoundRect(margin + 2, margin + 2, cardWidth - 4, 58, 12, 12);
        
            // Informasi perpustakaan (header)
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.BOLD, 18));
            g2d.drawString("PERPUSTAKAAN MARI MACA", margin + 20, margin + 25);
        
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            g2d.drawString("Jl. Galuh Mas No. 123, Karawang Barat 10430", margin + 20, margin + 42);
            g2d.drawString("Telp: (021) 1234-5678 | Email: marimaca@perpusdigital.id", margin + 20, margin + 55);
        
            // Title kartu anggota
            g2d.setColor(new Color(102, 165, 130)); // Soft Green
            g2d.setFont(new Font("Arial", Font.BOLD, 16));
            g2d.drawString("KARTU ANGGOTA", margin + 20, margin + 85);
        
            // Garis pemisah
            g2d.setStroke(new BasicStroke(1));
            g2d.drawLine(margin + 20, margin + 95, margin + cardWidth - 20, margin + 95);
        
            // Data anggota
            g2d.setColor(Color.BLACK);
            int startY = margin + 115;
            int lineHeight = 16; 
        
            // ID Anggota dengan highlight
            g2d.setFont(new Font("Arial", Font.BOLD, 12));
            g2d.drawString("ID ANGGOTA", margin + 20, startY);
            g2d.setFont(new Font("Arial", Font.BOLD, 14));
            g2d.setColor(new Color(220, 20, 60)); // Crimson untuk ID
            g2d.drawString(String.valueOf(member.getIdAnggota()), margin + 120, startY);
        
            // Reset warna
            g2d.setColor(Color.BLACK);
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
        
            startY += lineHeight + 3;
            g2d.drawString("Nama", margin + 20, startY);
            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            g2d.drawString(": " + member.getNama(), margin + 120, startY);
        
            startY += lineHeight;
            g2d.setFont(new Font("Arial", Font.PLAIN, 11));
            g2d.drawString("Alamat", margin + 20, startY);
            g2d.drawString(": " + member.getAlamat(), margin + 120, startY);
        
            startY += lineHeight;
            g2d.drawString("No. Telepon", margin + 20, startY);
            g2d.drawString(": " + member.getNoTelepon(), margin + 120, startY);
        
            startY += lineHeight;
            g2d.drawString("Email", margin + 20, startY);
            g2d.drawString(": " + member.getEmail(), margin + 120, startY);
        
            startY += lineHeight;
            g2d.drawString("Tgl. Daftar", margin + 20, startY);
            g2d.drawString(": " + member.getTanggalDaftar(), margin + 120, startY);
        
            startY += lineHeight + 3; 
            String status = "1".equals(member.getStatusAktif()) ? "AKTIF" : "TIDAK AKTIF";
            g2d.drawString("Status", margin + 20, startY);
        
            // Status dengan warna berbeda
            if ("AKTIF".equals(status)) {
                g2d.setColor(new Color(76, 153, 102)); // Darker Soft Green untuk AKTIF
            } else {
                g2d.setColor(new Color(220, 20, 60)); // Crimson tetap untuk TIDAK AKTIF
            }

            g2d.setFont(new Font("Arial", Font.BOLD, 11));
            g2d.drawString(": " + status, margin + 120, startY);
        
            // Footer dengan garis dan informasi tambahan
            g2d.setColor(new Color(102, 165, 130)); 
            g2d.setStroke(new BasicStroke(2));
            int footerLineY = margin + cardHeight - 55; // Posisi garis footer 
            g2d.drawLine(margin + 20, footerLineY, margin + cardWidth - 20, footerLineY);
        
            // Teks footer
            g2d.setFont(new Font("Arial", Font.ITALIC, 9));
            g2d.setColor(new Color(100, 100, 100));
            g2d.drawString("Kartu ini adalah bukti keanggotaan resmi dan berlaku selama status aktif", 
                    margin + 20, footerLineY + 18);
            g2d.drawString("Harap dibawa setiap kali mengunjungi perpustakaan", 
                    margin + 20, footerLineY + 30);
        
            // Nomor seri kartu
            g2d.setFont(new Font("Arial", Font.PLAIN, 8));
            g2d.setColor(Color.LIGHT_GRAY);
            String serialNumber = "PD-" + member.getIdAnggota() + "-" + 
                        member.getTanggalDaftar().toString().replace("-", "").substring(2);
            g2d.drawString("No. Seri: " + serialNumber, margin + cardWidth - 120, margin + cardHeight - 8);
        return PAGE_EXISTS;
        }
    }
}