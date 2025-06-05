package com.Library.Admin;

import java.sql.Date;

public class Member {
    private int idAnggota;
    private String nama;
    private String alamat;
    private String noTelepon;
    private Date tanggalDaftar;
    private String email;
    private String statusAktif;

    public Member() {}

    public Member(int idAnggota, String nama, String alamat, String noTelepon, 
                Date tanggalDaftar, String email, String statusAktif) {
        this.idAnggota = idAnggota;
        this.nama = nama;
        this.alamat = alamat;
        this.noTelepon = noTelepon;
        this.tanggalDaftar = tanggalDaftar;
        this.email = email;
        this.statusAktif = statusAktif;
    }

    public int getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(int idAnggota) {
        this.idAnggota = idAnggota;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getNoTelepon() {
        return noTelepon;
    }

    public void setNoTelepon(String noTelepon) {
        this.noTelepon = noTelepon;
    }

    public Date getTanggalDaftar() {
        return tanggalDaftar;
    }

    public void setTanggalDaftar(Date tanggalDaftar) {
        this.tanggalDaftar = tanggalDaftar;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatusAktif() {
        return statusAktif;
    }

    public void setStatusAktif(String statusAktif) {
        this.statusAktif = statusAktif;
    }

    @Override
    public String toString() {
        return "Member [ID=" + idAnggota + ", Nama=" + nama + ", Alamat=" + alamat +
               ", No_Telepon=" + noTelepon + ", Tanggal_Daftar=" + tanggalDaftar + 
               ", Email=" + email + ", Status=" + statusAktif + "]";
    }
}