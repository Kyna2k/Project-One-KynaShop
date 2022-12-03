package com.example.kynashop.model;

import java.util.ArrayList;

public class SanPhamTop10 {
    private int maSanPham;
    private String tenSanPham;
    private Double giaGoc;
    private Double giaBan;
    private Integer soLuongTrongKho;
    private String tenNhaSanXuat;
    private String moTa;
    private Integer trangThai;
    private Integer maKhuyenMai;
    private Integer tong;
    private ArrayList<Hinh> hinhs;

    public SanPhamTop10(int maSanPham, String tenSanPham, Double giaGoc, Double giaBan, Integer soLuongTrongKho, String tenNhaSanXuat, String moTa, Integer trangThai, Integer maKhuyenMai, Integer tong, ArrayList<Hinh> hinhs) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.giaGoc = giaGoc;
        this.giaBan = giaBan;
        this.soLuongTrongKho = soLuongTrongKho;
        this.tenNhaSanXuat = tenNhaSanXuat;
        this.moTa = moTa;
        this.trangThai = trangThai;
        this.maKhuyenMai = maKhuyenMai;
        this.tong = tong;
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

    public Double getGiaGoc() {
        return giaGoc;
    }

    public void setGiaGoc(Double giaGoc) {
        this.giaGoc = giaGoc;
    }

    public Double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(Double giaBan) {
        this.giaBan = giaBan;
    }

    public Integer getSoLuongTrongKho() {
        return soLuongTrongKho;
    }

    public void setSoLuongTrongKho(Integer soLuongTrongKho) {
        this.soLuongTrongKho = soLuongTrongKho;
    }

    public String getTenNhaSanXuat() {
        return tenNhaSanXuat;
    }

    public void setTenNhaSanXuat(String tenNhaSanXuat) {
        this.tenNhaSanXuat = tenNhaSanXuat;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public Integer getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(Integer trangThai) {
        this.trangThai = trangThai;
    }

    public Integer getMaKhuyenMai() {
        return maKhuyenMai;
    }

    public void setMaKhuyenMai(Integer maKhuyenMai) {
        this.maKhuyenMai = maKhuyenMai;
    }

    public Integer getTong() {
        return tong;
    }

    public void setTong(Integer tong) {
        this.tong = tong;
    }

    public ArrayList<Hinh> getHinhs() {
        return hinhs;
    }

    public void setHinhs(ArrayList<Hinh> hinhs) {
        this.hinhs = hinhs;
    }
}
