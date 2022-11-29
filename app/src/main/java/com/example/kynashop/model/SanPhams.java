package com.example.kynashop.model;

import java.util.ArrayList;

public class SanPhams {
    private int maSanPham;
    private String tenSanPham;
    private Long giaGoc;
    private Long giaBan;
    private int soLuongTrongKho;
    private int maNhaSanXuat;
    private String mota;
    private int trangThai;
    private int maKhuyenMai;
    private String thongSo;
    private ArrayList<Comment> commentApis;
    private ArrayList<Hinh> hinhs;
    public SanPhams() {
    }

    public SanPhams(int maSanPham, String tenSanPham, Long giaGoc, Long giaBan, int soLuongTrongKho, int maNhaSanXuat, String mota, int trangThai, int maKhuyenMai, String thongSo, ArrayList<Comment> commentApis, ArrayList<Hinh> hinhs) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaGoc = giaGoc;
        this.giaBan = giaBan;
        this.soLuongTrongKho = soLuongTrongKho;
        this.maNhaSanXuat = maNhaSanXuat;
        this.mota = mota;
        this.trangThai = trangThai;
        this.maKhuyenMai = maKhuyenMai;
        this.thongSo = thongSo;
        this.commentApis = commentApis;
        this.hinhs = hinhs;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public Long getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(Long giaGoc) {
        this.giaGoc = giaGoc;
    }

    public Long getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Long giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuongTrongKho() {
        return soLuongTrongKho;
    }

    public void setSoLuongTrongKho(int soLuongTrongKho) {
        this.soLuongTrongKho = soLuongTrongKho;
    }

    public int getMaNhaSanXuat() {
        return maNhaSanXuat;
    }

    public void setMaNhaSanXuat(int maNhaSanXuat) {
        this.maNhaSanXuat = maNhaSanXuat;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(int maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public String getThongSo() {
        return thongSo;
    }

    public void setThongSo(String thongSo) {
        this.thongSo = thongSo;
    }

    public ArrayList<Comment> getCommentApis() {
        return commentApis;
    }

    public void setCommentApis(ArrayList<Comment> commentApis) {
        this.commentApis = commentApis;
    }

    public ArrayList<Hinh> getHinhs() {
        return hinhs;
    }

    public void setHinhs(ArrayList<Hinh> hinhs) {
        this.hinhs = hinhs;
    }
}