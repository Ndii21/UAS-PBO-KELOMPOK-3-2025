package com.Library.Admin;

import java.sql.Date;

public class Pengumuman {
    private int idPengumuman;
    private String judul;
    private String isi;
    private Date tanggalDibuat;

    public Pengumuman() {
    }

    public Pengumuman(int idPengumuman, String judul, String isi, Date tanggalDibuat) {
        this.idPengumuman = idPengumuman;
        this.judul = judul;
        this.isi = isi;
        this.tanggalDibuat = tanggalDibuat;
    }

    public int getIdPengumuman() {
        return idPengumuman;
    }

    public void setIdPengumuman(int idPengumuman) {
        this.idPengumuman = idPengumuman;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi() {
        return isi;
    }

    public void setIsi(String isi) {
        this.isi = isi;
    }

    public Date getTanggalDibuat() {
        return tanggalDibuat;
    }

    public void setTanggalDibuat(Date tanggalDibuat) {
        this.tanggalDibuat = tanggalDibuat;
    }

    @Override
    public String toString() {
        return judul + " (ID: " + idPengumuman + ")";
    }
}
