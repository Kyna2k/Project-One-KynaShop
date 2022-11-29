package com.example.kynashop.model;

public class ChiTietHoaDon {
    private int stt;
    private int maHoaDon;
    private int maSanPham;
    private int soLuong;
    private Long triGia;
    private SanPhams sanPham;

    public ChiTietHoaDon(int stt, int maHoaDon, int maSanPham, int soLuong, Long triGia, SanPhams sanPham) {
        this.stt = stt;
        this.maHoaDon = maHoaDon;
        this.maSanPham = maSanPham;
        this.soLuong = soLuong;
        this.triGia = triGia;
        this.sanPham = sanPham;
    }

    public int getStt() {
        return stt;
    }

    public void setStt(int stt) {
        this.stt = stt;
    }

    public int getMaHoaDon() {
        return maHoaDon;
    }

    public void setMaHoaDon(int maHoaDon) {
        this.maHoaDon = maHoaDon;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public Long getTriGia() {
        return triGia;
    }

    public void setTriGia(Long triGia) {
        this.triGia = triGia;
    }

    public SanPhams getSanPham() {
        return sanPham;
    }

    public void setSanPham(SanPhams sanPham) {
        this.sanPham = sanPham;
    }
}
