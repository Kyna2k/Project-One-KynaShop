package com.example.kynashop.Interfaces;

import android.widget.EditText;

import com.example.kynashop.model.ChiTietHoaDon;
import com.example.kynashop.model.KhuyenMai;

public interface Click_item_GioHang {
    public void xoa(int MaSanPham);
    public void btn_tang(ChiTietHoaDon chiTietHoaDon);
    public void btn_giam(ChiTietHoaDon chiTietHoaDon);
    public void edit_soluong(EditText editText);
}
