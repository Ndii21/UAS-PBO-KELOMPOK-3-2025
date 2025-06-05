package com.Library.Anggota;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import com.Library.Admin.Book;

public class BookDAO {
    private Connection connection;

    public BookDAO() {
        this.connection = DBConnection.getConnection();
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM buku ORDER BY id_buku";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book();
                book.setIdBuku(rs.getInt("id_buku"));
                book.setJudulBuku(rs.getString("judul_buku"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setType(rs.getString("type"));
                book.setLocation(rs.getString("location"));
                book.setStatus(rs.getString("status"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public List<Book> getAvailableBooks() {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM buku WHERE status = 'Tersedia' ORDER BY id_buku";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book();
                book.setIdBuku(rs.getInt("id_buku"));
                book.setJudulBuku(rs.getString("judul_buku"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setType(rs.getString("type"));
                book.setLocation(rs.getString("location"));
                book.setStatus(rs.getString("status"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM buku WHERE judul_buku LIKE ? OR author LIKE ? OR type LIKE ? OR location LIKE ? ORDER BY id_buku";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Book book = new Book();
                book.setIdBuku(rs.getInt("id_buku"));
                book.setJudulBuku(rs.getString("judul_buku"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setType(rs.getString("type"));
                book.setLocation(rs.getString("location"));
                book.setStatus(rs.getString("status"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public List<Book> getBooksByType(String type) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM buku WHERE type = ? ORDER BY id_buku";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, type);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Book book = new Book();
                book.setIdBuku(rs.getInt("id_buku"));
                book.setJudulBuku(rs.getString("judul_buku"));
                book.setAuthor(rs.getString("author"));
                book.setPublisher(rs.getString("publisher"));
                book.setType(rs.getString("type"));
                book.setLocation(rs.getString("location"));
                book.setStatus(rs.getString("status"));
                books.add(book);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return books;
    }

    public int getTotalBooks() {
        String sql = "SELECT COUNT(*) as total FROM buku";
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

    public int getAvailableBooksCount() {
        String sql = "SELECT COUNT(*) as total FROM buku WHERE status = 'Tersedia'";
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

    public List<String> getAllTypes() {
        List<String> types = new ArrayList<>();
        String sql = "SELECT DISTINCT type FROM buku WHERE type IS NOT NULL ORDER BY type";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                types.add(rs.getString("type"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return types;
    }
}