package com.Library.Admin;

public class Book {
    private int idBuku;
    private String judulBuku;
    private String author;
    private String publisher;
    private String type;
    private String Location;
    private String status;

    public Book() {}

    public Book(int idBuku, String judulBuku, String author, String publisher, String type, String Location, String status) {
        this.idBuku = idBuku;
        this.judulBuku = judulBuku;
        this.author = author;
        this.publisher = publisher;
        this.type = type;
        this.Location = Location;
        this.status = status;
    }

    public int getIdBuku() {
        return idBuku;
    }

    public void setIdBuku(int idBuku) {
        this.idBuku = idBuku;
    }

    public String getJudulBuku() {
        return judulBuku;
    }

    public void setJudulBuku(String judulBuku) {
        this.judulBuku = judulBuku;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String Location) {
        this.Location = Location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Book [ID=" + idBuku + ", Judul=" + judulBuku + ", Author=" + author +
               ", Publisher=" + publisher + ", Type=" + type + ", Location=" + Location + ", Status=" + status + "]";
    }
}