package com.example.aplikasitokolaptop.model;

public class Laptop {
    private String id, nama, harga, avatar, spesifikasi;

    public Laptop(){

    }

    public Laptop(String nama, String harga, String avatar, String spesifikasi){
        this.nama = nama;
        this.harga = harga;
        this.avatar = avatar;
        this.spesifikasi = spesifikasi;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getHarga() {
        return harga;
    }

    public void setHarga(String harga) {
        this.harga = harga;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getSpesifikasi() {
        return spesifikasi;
    }

    public void setSpesifikasi(String spesifikasi) {
        this.spesifikasi = spesifikasi;
    }
}
