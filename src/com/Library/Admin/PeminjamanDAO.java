package com.Library.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanDAO {
    private Connection connection;

    public PeminjamanDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean addPeminjaman(Peminjaman peminjaman) {
        String sqlPeminjaman = "INSERT INTO peminjaman (id_buku, id_anggota, tanggal_pinjam, tanggal_jatuh_tempo, status_peminjaman) VALUES (?, ?, ?, ?, ?)";
        boolean success = false;
        try {
            // Nonaktifkan auto-commit untuk transaksi
            connection.setAutoCommit(false);

            PreparedStatement pstmtPeminjaman = connection.prepareStatement(sqlPeminjaman, Statement.RETURN_GENERATED_KEYS);
            pstmtPeminjaman.setInt(1, peminjaman.getIdBuku());
            pstmtPeminjaman.setInt(2, peminjaman.getIdAnggota());
            pstmtPeminjaman.setDate(3, peminjaman.getTanggalPinjam());
            pstmtPeminjaman.setDate(4, peminjaman.getTanggalJatuhTempo());
            pstmtPeminjaman.setString(5, peminjaman.getStatusPeminjaman());

            int resultPeminjaman = pstmtPeminjaman.executeUpdate();
            if (resultPeminjaman > 0) {
                String sqlUpdateBuku = "UPDATE buku SET status = 'Dipinjam' WHERE id_buku = ?";
                PreparedStatement pstmtUpdateBuku = connection.prepareStatement(sqlUpdateBuku);
                pstmtUpdateBuku.setInt(1, peminjaman.getIdBuku());
                int resultUpdateBuku = pstmtUpdateBuku.executeUpdate();

                if (resultUpdateBuku > 0) {
                    connection.commit(); // Commit transaksi jika semua berhasil
                    success = true;
                } else {
                    connection.rollback(); // Rollback jika update status buku gagal
                    System.err.println("Gagal mengupdate status buku. Transaksi dirollback.");
                }
                pstmtUpdateBuku.close();
            } else {
                connection.rollback(); // Rollback jika insert peminjaman gagal
                 System.err.println("Gagal menambahkan peminjaman. Transaksi dirollback.");
            }
            pstmtPeminjaman.close();
        } catch (SQLException e) {
            try {
                connection.rollback(); // Rollback jika terjadi exception
                System.err.println("SQLException terjadi. Transaksi dirollback.");
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                connection.setAutoCommit(true); // Kembalikan auto-commit ke true
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return success;
    }

    public List<Peminjaman> getAllPeminjaman() {
        List<Peminjaman> daftarPeminjaman = new ArrayList<>();
        String sql = "SELECT p.id_peminjaman, p.id_buku, b.judul_buku, p.id_anggota, a.nama AS nama_anggota, " +
                     "p.tanggal_pinjam, p.tanggal_jatuh_tempo, p.tanggal_pengembalian, p.status_peminjaman " +
                     "FROM peminjaman p " +
                     "JOIN buku b ON p.id_buku = b.id_buku " +
                     "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                     "ORDER BY p.tanggal_pinjam DESC, p.id_peminjaman DESC";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Peminjaman peminjaman = new Peminjaman();
                peminjaman.setIdPeminjaman(rs.getInt("id_peminjaman"));
                peminjaman.setIdBuku(rs.getInt("id_buku"));
                peminjaman.setJudulBuku(rs.getString("judul_buku")); 
                peminjaman.setIdAnggota(rs.getInt("id_anggota"));
                peminjaman.setNamaAnggota(rs.getString("nama_anggota")); 
                peminjaman.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
                peminjaman.setTanggalJatuhTempo(rs.getDate("tanggal_jatuh_tempo"));
                peminjaman.setTanggalPengembalian(rs.getDate("tanggal_pengembalian"));
                peminjaman.setStatusPeminjaman(rs.getString("status_peminjaman"));
                daftarPeminjaman.add(peminjaman);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarPeminjaman;
    }

    public boolean updatePeminjaman(Peminjaman peminjaman) {
        String sqlUpdatePeminjaman = "UPDATE peminjaman SET id_buku=?, id_anggota=?, tanggal_pinjam=?, " +
                                     "tanggal_jatuh_tempo=?, tanggal_pengembalian=?, status_peminjaman=? " +
                                     "WHERE id_peminjaman=?";
        boolean success = false;
        try {
            connection.setAutoCommit(false);

            PreparedStatement pstmt = connection.prepareStatement(sqlUpdatePeminjaman);
            pstmt.setInt(1, peminjaman.getIdBuku());
            pstmt.setInt(2, peminjaman.getIdAnggota());
            pstmt.setDate(3, peminjaman.getTanggalPinjam());
            pstmt.setDate(4, peminjaman.getTanggalJatuhTempo());
            pstmt.setDate(5, peminjaman.getTanggalPengembalian());
            pstmt.setString(6, peminjaman.getStatusPeminjaman());
            pstmt.setInt(7, peminjaman.getIdPeminjaman());

            int resultUpdate = pstmt.executeUpdate();
            if (resultUpdate > 0) {
                if ("Dikembalikan".equalsIgnoreCase(peminjaman.getStatusPeminjaman())) {
                    String sqlUpdateBuku = "UPDATE buku SET status = 'Tersedia' WHERE id_buku = ?";
                    PreparedStatement pstmtUpdateBuku = connection.prepareStatement(sqlUpdateBuku);
                    pstmtUpdateBuku.setInt(1, peminjaman.getIdBuku());
                    int resultUpdateBuku = pstmtUpdateBuku.executeUpdate();
                    if (resultUpdateBuku > 0) {
                        connection.commit();
                        success = true;
                    } else {
                        connection.rollback();
                        System.err.println("Gagal mengupdate status buku saat pengembalian. Transaksi dirollback.");
                    }
                    pstmtUpdateBuku.close();
                } else if ("Dipinjam".equalsIgnoreCase(peminjaman.getStatusPeminjaman())) {
                    // Jika status diubah (misal dari Terlambat) kembali ke Dipinjam atau memang Dipinjam saat update lain
                     String sqlUpdateBukuStatusToDipinjam = "UPDATE buku SET status = 'Dipinjam' WHERE id_buku = ?";
                     PreparedStatement pstmtUpdateBukuStatus = connection.prepareStatement(sqlUpdateBukuStatusToDipinjam);
                     pstmtUpdateBukuStatus.setInt(1, peminjaman.getIdBuku());
                     int resultUpdateBukuStatus = pstmtUpdateBukuStatus.executeUpdate();
                     if (resultUpdateBukuStatus > 0) {
                        connection.commit();
                        success = true;
                     } else {
                        connection.rollback();
                        System.err.println("Gagal mengupdate status buku menjadi 'Dipinjam'. Transaksi dirollback.");
                     }
                     pstmtUpdateBukuStatus.close();
                }
                 else {
                    connection.commit();
                    success = true;
                }
            } else {
                 connection.rollback();
                 System.err.println("Gagal mengupdate peminjaman. Transaksi dirollback.");
            }
            pstmt.close();
        } catch (SQLException e) {
            try { connection.rollback(); } catch (SQLException ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { connection.setAutoCommit(true); } catch (SQLException ex) { ex.printStackTrace(); }
        }
        return success;
    }

    public boolean deletePeminjaman(int idPeminjaman) {
        String sql = "DELETE FROM peminjaman WHERE id_peminjaman = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idPeminjaman);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("SQLException saat mencoba menghapus peminjaman dengan ID: " + idPeminjaman);
            e.printStackTrace();
            return false; // Mengembalikan false jika terjadi kesalahan SQL
        }
    }

    public List<Peminjaman> searchPeminjaman(String keyword) {
        List<Peminjaman> daftarPeminjaman = new ArrayList<>();
        String sql = "SELECT p.id_peminjaman, p.id_buku, b.judul_buku, p.id_anggota, a.nama AS nama_anggota, " +
                     "p.tanggal_pinjam, p.tanggal_jatuh_tempo, p.tanggal_pengembalian, p.status_peminjaman " +
                     "FROM peminjaman p " +
                     "JOIN buku b ON p.id_buku = b.id_buku " +
                     "JOIN anggota a ON p.id_anggota = a.id_anggota " +
                     "WHERE b.judul_buku LIKE ? OR a.nama LIKE ? OR p.status_peminjaman LIKE ? " +
                     "ORDER BY p.tanggal_pinjam DESC, p.id_peminjaman DESC";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Peminjaman peminjaman = new Peminjaman();
                peminjaman.setIdPeminjaman(rs.getInt("id_peminjaman"));
                peminjaman.setIdBuku(rs.getInt("id_buku"));
                peminjaman.setJudulBuku(rs.getString("judul_buku"));
                peminjaman.setIdAnggota(rs.getInt("id_anggota"));
                peminjaman.setNamaAnggota(rs.getString("nama_anggota"));
                peminjaman.setTanggalPinjam(rs.getDate("tanggal_pinjam"));
                peminjaman.setTanggalJatuhTempo(rs.getDate("tanggal_jatuh_tempo"));
                peminjaman.setTanggalPengembalian(rs.getDate("tanggal_pengembalian"));
                peminjaman.setStatusPeminjaman(rs.getString("status_peminjaman"));
                daftarPeminjaman.add(peminjaman);
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return daftarPeminjaman;
    }

    public int getTotalPeminjamanAktif() {
        String sql = "SELECT COUNT(*) as total FROM peminjaman WHERE status_peminjaman = 'Dipinjam'";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                return rs.getInt("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // Metode untuk mengambil buku yang tersedia
    public List<PeminjamanDialog.BukuItem> getAvailableBooks() {
        List<PeminjamanDialog.BukuItem> books = new ArrayList<>();
        String sql = "SELECT id_buku, judul_buku FROM buku WHERE status = 'Tersedia' ORDER BY id_buku";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                // Format tampilan: "ID - Judul Buku"
                String display = rs.getInt("id_buku") + " - " + rs.getString("judul_buku");
                books.add(new PeminjamanDialog.BukuItem(rs.getInt("id_buku"), display));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return books;
    }

    // Metode untuk mengambil anggota yang aktif
    public List<PeminjamanDialog.AnggotaItem> getActiveMembers() {
        List<PeminjamanDialog.AnggotaItem> members = new ArrayList<>();
        String sql = "SELECT id_anggota, nama FROM anggota WHERE status_aktif = '1' ORDER BY id_anggota";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                 // Format tampilan: "ID - Nama Anggota"
                String display = rs.getInt("id_anggota") + " - " + rs.getString("nama");
                members.add(new PeminjamanDialog.AnggotaItem(rs.getInt("id_anggota"), display));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }
}