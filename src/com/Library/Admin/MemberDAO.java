package com.Library.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {
    private Connection connection;

    public MemberDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean addMember(Member member) {
        String sql = "INSERT INTO anggota (nama, alamat, no_telepon, tanggal_daftar, email, status_aktif) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getNama());
            pstmt.setString(2, member.getAlamat());
            pstmt.setString(3, member.getNoTelepon());
            pstmt.setDate(4, member.getTanggalDaftar());
            pstmt.setString(5, member.getEmail());
            pstmt.setString(6, "1");

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM anggota ORDER BY id_anggota";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Member member = new Member();
                member.setIdAnggota(rs.getInt("id_anggota"));
                member.setNama(rs.getString("nama"));
                member.setAlamat(rs.getString("alamat"));
                member.setNoTelepon(rs.getString("no_telepon"));
                member.setTanggalDaftar(rs.getDate("tanggal_daftar"));
                member.setEmail(rs.getString("email"));
                member.setStatusAktif(rs.getString("status_aktif"));
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return members;
    }

    public boolean updateMember(Member member) {
        String sql = "UPDATE anggota SET nama=?, alamat=?, no_telepon=?, tanggal_daftar=?, email=?, status_aktif=? WHERE id_anggota=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, member.getNama());
            pstmt.setString(2, member.getAlamat());
            pstmt.setString(3, member.getNoTelepon());
            pstmt.setDate(4, member.getTanggalDaftar());
            pstmt.setString(5, member.getEmail());
            pstmt.setString(6, member.getStatusAktif());
            pstmt.setInt(7, member.getIdAnggota());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteMember(int idAnggota) {
        String sql = "DELETE FROM anggota WHERE id_anggota=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idAnggota);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Member> searchMembers(String keyword) {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT * FROM anggota WHERE nama LIKE ? OR no_telepon LIKE ? OR status_aktif LIKE ? OR Alamat LIKE ? ORDER BY id_anggota";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Member member = new Member();
                member.setIdAnggota(rs.getInt("id_anggota"));
                member.setNama(rs.getString("nama"));
                member.setAlamat(rs.getString("alamat"));
                member.setNoTelepon(rs.getString("no_telepon"));
                member.setTanggalDaftar(rs.getDate("tanggal_daftar"));
                member.setEmail(rs.getString("email"));
                member.setStatusAktif(rs.getString("status_aktif"));
                members.add(member);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return members;
    }

    public int getActiveMembers() {
        String sql = "SELECT COUNT(*) as total FROM anggota WHERE status_aktif = '1'";
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

    public int getInactiveMembers() {
        String sql = "SELECT COUNT(*) as total FROM anggota WHERE status_aktif = '0'";
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