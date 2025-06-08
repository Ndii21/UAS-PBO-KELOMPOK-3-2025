package com.Library.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PengunjungDAO {
    
    // Method untuk mengambil semua data pengunjung
    public List<Pengunjung> getAllPengunjung() throws SQLException {
        List<Pengunjung> pengunjungList = new ArrayList<>();
        String query = "SELECT id_pengunjung, nama, no_telepon, tanggal_kunjungan FROM pengunjung ORDER BY tanggal_kunjungan DESC";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {
            
            while (resultSet.next()) {
                Pengunjung pengunjung = new Pengunjung(
                    resultSet.getInt("id_pengunjung"),
                    resultSet.getString("nama"),
                    resultSet.getString("no_telepon"),
                    resultSet.getDate("tanggal_kunjungan")
                );
                pengunjungList.add(pengunjung);
            }
        }
        
        return pengunjungList;
    }
    
    // Method untuk mencari pengunjung berdasarkan tanggal
    public List<Pengunjung> getPengunjungByDate(String dateText) throws SQLException {
        List<Pengunjung> pengunjungList = new ArrayList<>();
        String query = "SELECT id_pengunjung, nama, no_telepon, tanggal_kunjungan FROM pengunjung WHERE DATE(tanggal_kunjungan) = ? ORDER BY tanggal_kunjungan DESC";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            
            // Convert string to SQL Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(dateText);
            statement.setDate(1, sqlDate);
            
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Pengunjung pengunjung = new Pengunjung(
                        resultSet.getInt("id_pengunjung"),
                        resultSet.getString("nama"),
                        resultSet.getString("no_telepon"),
                        resultSet.getDate("tanggal_kunjungan")
                    );
                    pengunjungList.add(pengunjung);
                }
            }
        }
        
        return pengunjungList;
    }
}

