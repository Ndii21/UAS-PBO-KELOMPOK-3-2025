package com.Library.Admin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class PengumumanDAO {
    private Connection connection;

    public PengumumanDAO() {
        this.connection = DBConnection.getConnection(); // Pastikan DBConnection.java ada dan berfungsi
        if (this.connection == null) {
            System.err.println("KRITIS: Koneksi database gagal didapatkan di PengumumanDAO.");
            // Pertimbangkan untuk melempar RuntimeException agar aplikasi tidak lanjut dengan state tidak valid
            // throw new RuntimeException("Tidak dapat terkoneksi ke database.");
        }
    }

    public boolean addPengumuman(Pengumuman pengumuman) {
        if (connection == null) return false; // Tidak bisa operasi tanpa koneksi
        String sql = "INSERT INTO pengumuman (judul, isi, tanggal_dibuat) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, pengumuman.getJudul());
            pstmt.setString(2, pengumuman.getIsi());
            // Jika tanggal dibuat null, set ke tanggal sekarang
            if (pengumuman.getTanggalDibuat() == null) {
                pengumuman.setTanggalDibuat(new Date(System.currentTimeMillis()));
            }
            pstmt.setDate(3, pengumuman.getTanggalDibuat());
            
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pengumuman.setIdPengumuman(generatedKeys.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("SQL Error saat menambah pengumuman: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<Pengumuman> getAllPengumuman() {
        List<Pengumuman> daftarPengumuman = new ArrayList<>();
        if (connection == null) return daftarPengumuman;
        String sql = "SELECT id_pengumuman, judul, isi, tanggal_dibuat FROM pengumuman ORDER BY tanggal_dibuat DESC, id_pengumuman DESC";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pengumuman p = new Pengumuman();
                p.setIdPengumuman(rs.getInt("id_pengumuman"));
                p.setJudul(rs.getString("judul"));
                p.setIsi(rs.getString("isi"));
                p.setTanggalDibuat(rs.getDate("tanggal_dibuat"));
                daftarPengumuman.add(p);
            }
        } catch (SQLException e) {
            System.err.println("SQL Error saat mengambil semua pengumuman: " + e.getMessage());
            e.printStackTrace();
        }
        return daftarPengumuman;
    }

    public Pengumuman getPengumumanById(int id) { // Metode ini mungkin berguna
        if (connection == null) return null;
        String sql = "SELECT id_pengumuman, judul, isi, tanggal_dibuat FROM pengumuman WHERE id_pengumuman = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Pengumuman p = new Pengumuman();
                p.setIdPengumuman(rs.getInt("id_pengumuman"));
                p.setJudul(rs.getString("judul"));
                p.setIsi(rs.getString("isi"));
                p.setTanggalDibuat(rs.getDate("tanggal_dibuat"));
                return p;
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("SQL Error saat mengambil pengumuman by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }


    public boolean updatePengumuman(Pengumuman pengumuman) {
        if (connection == null) return false;
        String sql = "UPDATE pengumuman SET judul = ?, isi = ?, tanggal_dibuat = ? WHERE id_pengumuman = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, pengumuman.getJudul());
            pstmt.setString(2, pengumuman.getIsi());
            if (pengumuman.getTanggalDibuat() == null) {
                pengumuman.setTanggalDibuat(new Date(System.currentTimeMillis()));
            }
            pstmt.setDate(3, pengumuman.getTanggalDibuat());
            pstmt.setInt(4, pengumuman.getIdPengumuman());
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error saat update pengumuman: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deletePengumuman(int idPengumuman) {
        if (connection == null) return false;
        String sql = "DELETE FROM pengumuman WHERE id_pengumuman = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idPengumuman);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("SQL Error saat menghapus pengumuman: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public List<Pengumuman> searchPengumuman(String keyword) {
        List<Pengumuman> daftarPengumuman = new ArrayList<>();
        if (connection == null) return daftarPengumuman;
        String sql = "SELECT id_pengumuman, judul, isi, tanggal_dibuat FROM pengumuman " +
                     "WHERE judul LIKE ? OR isi LIKE ? " + // Cari di judul atau isi
                     "ORDER BY tanggal_dibuat DESC, id_pengumuman DESC";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Pengumuman p = new Pengumuman();
                p.setIdPengumuman(rs.getInt("id_pengumuman"));
                p.setJudul(rs.getString("judul"));
                p.setIsi(rs.getString("isi"));
                p.setTanggalDibuat(rs.getDate("tanggal_dibuat"));
                daftarPengumuman.add(p);
            }
            rs.close();
        } catch (SQLException e) {
            System.err.println("SQL Error saat mencari pengumuman: " + e.getMessage());
            e.printStackTrace();
        }
        return daftarPengumuman;
    }

     public int getTotalPengumuman() {
        if (connection == null) return 0;
        String sql = "SELECT COUNT(*) as total FROM pengumuman";
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
}

