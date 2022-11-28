package com.example.kynashop.model;

public class Hinh {
    private int mahinh;
    private int maSanPham;
    private String url;

    public Hinh(int mahinh, int maSanPham, String url) {
        this.mahinh = mahinh;
        this.maSanPham = maSanPham;
        this.url = url;
    }

    public int getMahinh() {
        return mahinh;
    }

    public void setMahinh(int mahinh) {
        this.mahinh = mahinh;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        maSanPham = maSanPham;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
