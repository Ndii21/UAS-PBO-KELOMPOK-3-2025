package com.Library.Anggota;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); 
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Gagal melakukan hashing password", e);
        }
    }

    public static boolean verifyPassword(String inputPassword, String hashedPasswordFromDB) {
        return hashPassword(inputPassword).equals(hashedPasswordFromDB); 
    }
}