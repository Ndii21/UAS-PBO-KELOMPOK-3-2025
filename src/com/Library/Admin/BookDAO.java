package com.Library.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {
    private Connection connection;

    public BookDAO() {
        this.connection = DBConnection.getConnection();
    }

    public boolean addBook(Book book) {
        String sql = "INSERT INTO buku (judul_buku, author, publisher, type, location, status) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, book.getJudulBuku());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setString(4, book.getType());
            pstmt.setString(5, book.getLocation());
            pstmt.setString(6, book.getStatus());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
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

    public boolean updateBook(Book book) {
        String sql = "UPDATE buku SET judul_buku=?, author=?, publisher=?, type=?, location=?, status=? WHERE id_buku=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, book.getJudulBuku());
            pstmt.setString(2, book.getAuthor());
            pstmt.setString(3, book.getPublisher());
            pstmt.setString(4, book.getType());
            pstmt.setString(5, book.getLocation());
            pstmt.setString(6, book.getStatus());
            pstmt.setInt(7, book.getIdBuku());

            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteBook(int idBuku) {
        String sql = "DELETE FROM buku WHERE id_buku=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, idBuku);
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Book> searchBooks(String keyword) {
        List<Book> books = new ArrayList<>();
        String sql = "SELECT * FROM buku WHERE judul_buku LIKE ? OR author LIKE ? OR Type LIKE ? OR Location  LIKE ? OR Status LIKE ? ORDER BY id_buku";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            String searchPattern = "%" + keyword + "%";
            pstmt.setString(1, searchPattern);
            pstmt.setString(2, searchPattern);
            pstmt.setString(3, searchPattern);
            pstmt.setString(4, searchPattern);
            pstmt.setString(5, searchPattern);

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
}
