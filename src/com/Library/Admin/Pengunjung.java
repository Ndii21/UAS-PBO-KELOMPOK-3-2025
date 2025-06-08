package com.Library.Admin;

import java.sql.Date;

public class Pengunjung {
    private int idPengunjung;
    private String nama;
    private String noTelepon;
    private Date tanggalKunjungan;
    
    // Default constructor
    public Pengunjung() {
    }
    
    // Constructor dengan parameter
    public Pengunjung(int idPengunjung, String nama, String noTelepon, Date tanggalKunjungan) {
        this.idPengunjung = idPengunjung;
        this.nama = nama;
        this.noTelepon = noTelepon;
        this.tanggalKunjungan = tanggalKunjungan;
    }
    
    // Getter dan Setter
    public int getIdPengunjung() {
        return idPengunjung;
    }
    
    public void setIdPengunjung(int idPengunjung) {
        this.idPengunjung = idPengunjung;
    }
    
    public String getNama() {
        return nama;
    }
    
    public void setNama(String nama) {
        this.nama = nama;
    }
    
    public String getNoTelepon() {
        return noTelepon;
    }
    
    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }
    
    public Date getTanggalKunjungan() {
        return tanggalKunjungan;
    }
    
    public void setTanggalKunjungan(Date tanggalKunjungan) {
        this.tanggalKunjungan = tanggalKunjungan;
    }
    
    @Override
    public String toString() {
        return "Pengunjung{" +
                "idPengunjung=" + idPengunjung +
                ", nama='" + nama + '\'' +
                ", noTelepon='" + noTelepon + '\'' +
                ", tanggalKunjungan=" + tanggalKunjungan +
                '}';
    }
}