package com.Library.Admin;

import java.sql.Date;

public class Peminjaman {
    private int idPeminjaman;
    private int idBuku;
    private int idAnggota;
    private Date tanggalPinjam;
    private Date tanggalJatuhTempo;
    private Date tanggalPengembalian;
    private String statusPeminjaman;

    // Tambahan untuk menampilkan judul buku dan nama anggota
    private String judulBuku;
    private String namaAnggota;

    public Peminjaman() {
    }

    public Peminjaman(int idPeminjaman, int idBuku, int idAnggota, Date tanggalPinjam, Date tanggalJatuhTempo, Date tanggalPengembalian, String statusPeminjaman, String judulBuku, String namaAnggota) {
        this.idPeminjaman = idPeminjaman;
        this.idBuku = idBuku;
        this.idAnggota = idAnggota;
        this.tanggalPinjam = tanggalPinjam;
        this.tanggalJatuhTempo = tanggalJatuhTempo;
        this.tanggalPengembalian = tanggalPengembalian;
        this.statusPeminjaman = statusPeminjaman;
        this.judulBuku = judulBuku;
        this.namaAnggota = namaAnggota;
    }

    // Getters and Setters
    public int getIdPeminjaman() {
        return idPeminjaman;
    }

    public void setIdPeminjaman(int idPeminjaman) {
        this.idPeminjaman = idPeminjaman;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(int idBuku) {
        this.idBuku = idBuku;
    }

    public int getIdAnggota() {
        return idAnggota;
    }

    public void setIdAnggota(int idAnggota) {
        this.idAnggota = idAnggota;
    }

    public Date getTanggalPinjam() {
        return tanggalPinjam;
    }

    public void setTanggalPinjam(Date tanggalPinjam) {
        this.tanggalPinjam = tanggalPinjam;
    }

    public Date getTanggalJatuhTempo() {
        return tanggalJatuhTempo;
    }

    public void setTanggalJatuhTempo(Date tanggalJatuhTempo) {
        this.tanggalJatuhTempo = tanggalJatuhTempo;
    }

    public Date getTanggalPengembalian() {
        return tanggalPengembalian;
    }

    public void setTanggalPengembalian(Date tanggalPengembalian) {
        this.tanggalPengembalian = tanggalPengembalian;
    }

    public String getStatusPeminjaman() {
        return statusPeminjaman;
    }

    public void setStatusPeminjaman(String statusPeminjaman) {
        this.statusPeminjaman = statusPeminjaman;
    }

    public String getJudulBuku() {
        return judulBuku;
    }

    public void setJudulBuku(String judulBuku) {
        this.judulBuku = judulBuku;
    }

    public String getNamaAnggota() {
        return namaAnggota;
    }

    public void setNamaAnggota(String namaAnggota) {
        this.namaAnggota = namaAnggota;
    }

    @Override
    public String toString() {
        return "Peminjaman{" +
                "idPeminjaman=" + idPeminjaman +
                ", idBuku=" + idBuku +
                ", idAnggota=" + idAnggota +
                ", tanggalPinjam=" + tanggalPinjam +
                ", tanggalJatuhTempo=" + tanggalJatuhTempo +
                ", tanggalPengembalian=" + tanggalPengembalian +
                ", statusPeminjaman='" + statusPeminjaman + '\'' +
                ", judulBuku='" + judulBuku + '\'' +
                ", namaAnggota='" + namaAnggota + '\'' +
                '}';
    }
}