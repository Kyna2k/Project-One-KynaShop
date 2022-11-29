package com.example.kynashop.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kynashop.API.API_Services;
import com.example.kynashop.Interfaces.Click_item_GioHang;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_List_GioHang;
import com.example.kynashop.model.ChiTietHoaDon;
import com.example.kynashop.model.Convent_Money;
import com.example.kynashop.model.HoaDon;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.XoaHoaDonGioHang;
import com.example.kynashop.view.MainActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Fragment_GioHang extends Fragment implements Click_item_GioHang {
    private RecyclerView list_giohang;
    private TextView tongsoluong, tong_gia_goc, tong_gia_ban, tong;
    private Button btn_mua;
    private Recycle_List_GioHang adapter_giohang;
    private API_Services requestInterface;
    private KhuyenMai khuyenMai2;
    private int MaHoaDon;
    public Fragment_GioHang()
    {

    }
    public static Fragment_GioHang getInstance()
    {
        Fragment_GioHang gioHang = new Fragment_GioHang();
        Bundle bundle = new Bundle();
        gioHang.setArguments(bundle);
        return gioHang;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() != null)
        {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_giohang,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestInterface = ((MainActivity)getActivity()).build_API();
        list_giohang = view.findViewById(R.id.list_giohang);
        tongsoluong = view.findViewById(R.id.tongsoluong);
        tong_gia_goc = view.findViewById(R.id.tong_gia_goc);
        tong_gia_ban = view.findViewById(R.id.tong_gia_ban);
        tong = view.findViewById(R.id.tong);
        getValue();

    }

    @Override
    public void onResume() {
        super.onResume();
        getValue();
    }

    private void getValue()
    {
        int makhachhang = getContext().getSharedPreferences("KhachHach", Context.MODE_PRIVATE).getInt("makhachhang",-1);
        if(makhachhang != -1)
        {
            getGioHang(makhachhang,0);
        }
    }
    private void getData(ArrayList<ChiTietHoaDon> ds)
    {
        adapter_giohang = new Recycle_List_GioHang(getContext(),ds,this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        list_giohang.setLayoutManager(linearLayoutManager);
        list_giohang.setAdapter(adapter_giohang);
    }
    private void getGioHang(int maKhachHang,int trangthai)
    {
        new CompositeDisposable().add(requestInterface.getGioHang(maKhachHang,trangthai)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getGioHangOk, this::getGioHangNoOk)
        );
    }

    private void getGioHangNoOk(Throwable throwable) {
    }

    private void getGioHangOk(HoaDon hoaDon) {
        if(hoaDon !=null)
        {
            MaHoaDon = hoaDon.getMaHoaDon();

            getData(hoaDon.getChiTietHoaDons());
            setMoney(hoaDon.getChiTietHoaDons());
        }
    }

    @SuppressLint("NewApi")
    public void setMoney(ArrayList<ChiTietHoaDon> ds)
    {
        int soluong_sanpham = 0;
        long tonggia_goc = 0;
        long tonggia_ban = 0;
        long tongcong = 0;
        for(ChiTietHoaDon chiTietHoaDon : ds)
        {
            soluong_sanpham +=chiTietHoaDon.getSoLuong();
            tonggia_goc += chiTietHoaDon.getTriGia()*chiTietHoaDon.getSoLuong();
            if(chiTietHoaDon.getSanPham().getMaKhuyenMai() != 0)
            {
                KhuyeMai(chiTietHoaDon.getSanPham().getMaKhuyenMai());
                Double x = Double.valueOf(khuyenMai2.getPhanTramKhuyenMai() + "") ;
                Double gia = chiTietHoaDon.getTriGia()*(Double)((100-x)/100);
                tonggia_ban += gia*chiTietHoaDon.getSoLuong();
            }else {
                tonggia_ban = tonggia_goc;
            }
        }
        tongcong = tonggia_ban;
        tongsoluong.setText("Sản phẩm " + "(" + soluong_sanpham +  ")");
        tong_gia_goc.setText(Convent_Money.money(Double.valueOf(tonggia_goc)));
        tong_gia_ban.setText(Convent_Money.money(Double.valueOf(tonggia_ban)));
        tong.setText(Convent_Money.money(Double.valueOf(tongcong)));
    }
    public void KhuyeMai(int Makhuyenmai)
    {
        new CompositeDisposable().add(requestInterface.getKhuyenMai(Makhuyenmai)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getKhuyenMaiOk, this::getKhuyenMainoOk)
        );
    }
    @SuppressLint("NewApi")
    private void getKhuyenMainoOk(Throwable throwable) {
        Log.e("khuyenmai", "getKhuyenMainoOk: " + throwable.getMessage() );

    }

    @SuppressLint("NewApi")
    private void getKhuyenMaiOk(KhuyenMai khuyenMai) {
        if(khuyenMai != null)
        {
            khuyenMai2 = khuyenMai;
        }
    }
    public void XoaSanPham(int MaSanPham)
    {

        XoaHoaDonGioHang xoaHoaDonGioHang = new XoaHoaDonGioHang(MaHoaDon,MaSanPham);
        new CompositeDisposable().add(requestInterface.XoaSanPhamKhoiGioHang(xoaHoaDonGioHang)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::xoa_ok, this::xoa_no)
        );
    }

    private void xoa_no(Throwable throwable) {
        Log.e("check_xoa", "update_nook: " + throwable.getMessage() );
    }

    private void xoa_ok(Integer integer) {
        Toast.makeText(getContext(), "Da Xoa San Pham", Toast.LENGTH_SHORT).show();
        getValue();
    }

    @Override
    public void xoa(int MaSanPham) {
        XoaSanPham(MaSanPham);
    }

    @Override
    public void btn_tang(ChiTietHoaDon chiTietHoaDon) {
        update(chiTietHoaDon,true);
    }

    @Override
    public void btn_giam(ChiTietHoaDon chiTietHoaDon) {
        update(chiTietHoaDon,false);

    }

    @Override
    public void edit_soluong(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
    public void update(ChiTietHoaDon chiTietHoaDon, boolean check)
    {
        int soluong = chiTietHoaDon.getSoLuong();
        if(check)
        {
            chiTietHoaDon.setSoLuong(soluong+1);
        }else {
            chiTietHoaDon.setSoLuong(soluong-1);

        }
        new CompositeDisposable().add(requestInterface.updateSoLuongTrongGioHang(chiTietHoaDon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::update_ok, this::update_nook)
        );
    }

    private void update_nook(Throwable throwable) {
        Log.e("check_update", "update_nook: " + throwable.getMessage() );
    }

    private void update_ok(Integer integer) {
        Toast.makeText(getContext(), "Thanh cong", Toast.LENGTH_SHORT).show();
        getValue();
    }
}
