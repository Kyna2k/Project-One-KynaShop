package com.example.kynashop.model;

import java.io.Serializable;

public class Comment implements Serializable {
    private String noiDung;
    private int rate;
    private String ngay;
    private int maSanPham;
    private int maKhachHang;
    private KhachHang khach;

    public Comment() {
    }

    public Comment(String noiDung, int rate, String ngay, int maSanPham, int maKhachHang, KhachHang khach) {
        this.noiDung = noiDung;
        this.rate = rate;
        this.ngay = ngay;
        this.maSanPham = maSanPham;
        this.maKhachHang = maKhachHang;
        this.khach = khach;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getNgay() {
        return ngay;
    }

    public void setNgay(String ngay) {
        this.ngay = ngay;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public int getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(int maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public KhachHang getKhach() {
        return khach;
    }

    public void setKhach(KhachHang khach) {
        this.khach = khach;
    }
}
