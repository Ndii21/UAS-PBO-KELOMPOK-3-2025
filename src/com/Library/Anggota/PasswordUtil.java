package com.Library.Anggota;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtil {

    // Metode sederhana untuk "hashing" (gunakan BCrypt di produksi)
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256"); // Contoh, bukan yang terkuat untuk password
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Gagal melakukan hashing password", e);
        }
    }

    // Metode untuk verifikasi (sesuaikan jika menggunakan BCrypt.checkpw())
    public static boolean verifyPassword(String inputPassword, String hashedPasswordFromDB) {
        // Jika menggunakan BCrypt: return BCrypt.checkpw(inputPassword, hashedPasswordFromDB);
        return hashPassword(inputPassword).equals(hashedPasswordFromDB); // Untuk contoh sederhana ini
    }
}
