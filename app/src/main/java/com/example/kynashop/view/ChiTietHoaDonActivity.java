package com.example.kynashop.view;

import static com.example.kynashop.API.API_Services.BASE_Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kynashop.API.API_Services;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_List_DonHang;
import com.example.kynashop.model.Convent_Money;
import com.example.kynashop.model.HoaDon;
import com.example.kynashop.model.KhachHang;
import com.example.kynashop.model.KhuyenMai;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChiTietHoaDonActivity extends AppCompatActivity {

    private RecyclerView list_giohang;
    private TextView tongsoluong, tong_gia_goc, tong_gia_ban, tong;
    private Button btn_mua;
    private Recycle_List_DonHang adapter;
    private API_Services requestInterface;
    private KhuyenMai khuyenMai2;
    private ArrayList<KhuyenMai> ds_khuyenmaiAPi;
    private int MaHoaDon;
    private ArrayList<com.example.kynashop.model.ChiTietHoaDon> list_chitiethoadon;
    private ImageView btn_back;
    private HoaDon hoaDon;
    private int type,MaKhachHang;
    private CardView ThongTin;
    private TextView ten_khachhang,sdt,diachi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(API_Services.class);
        MaKhachHang = getSharedPreferences("KhachHach", Context.MODE_PRIVATE).getInt("makhachhang",-1);
        setContentView(R.layout.activity_don_hang);
        list_giohang = findViewById(R.id.list_giohang);
        tongsoluong = findViewById(R.id.tongsoluong);
        tong_gia_goc = findViewById(R.id.tong_gia_goc);
        tong_gia_ban = findViewById(R.id.tong_gia_ban);
        btn_back = findViewById(R.id.btn_back);
        ThongTin = findViewById(R.id.ThongTin);
        ten_khachhang = findViewById(R.id.ten_khachhang);
        sdt = findViewById(R.id.sdt);
        diachi = findViewById(R.id.diachi);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tong = findViewById(R.id.tong);
        btn_mua = findViewById(R.id.btn_mua);
        KhachHang(MaKhachHang);
        hoaDon = (HoaDon) getIntent().getExtras().getSerializable("HOADON");
        list_chitiethoadon = hoaDon.getChiTietHoaDons();
        setGia(list_chitiethoadon);
        setData(list_chitiethoadon);
        btn_mua.setEnabled(false);
        btn_mua.setBackground(getDrawable(R.drawable.background_btn3));
        btn_mua.setTextColor(Color.parseColor("#11998e"));
        switch (hoaDon.getTrangThai())
        {
            case 255:
                btn_mua.setText("Đã Hủy");
                btn_mua.setTextColor(Color.parseColor("#44494D"));

                break;
            case 1:
                btn_mua.setText("Chờ xác nhận");
                btn_mua.setTextColor(Color.parseColor("#FF4545"));
                break;
            case 2:
                btn_mua.setText("Đã xác nhận");
                break;
            case 3:
                btn_mua.setText("Đánh giá");
                btn_mua.setEnabled(true);
                break;
            case 4:
                btn_mua.setText("Đã Đánh giá");
                break;
        }
    }
    //copy ham ngoai thang nay thoi
    @SuppressLint("NewApi")
    private void setGia(ArrayList<com.example.kynashop.model.ChiTietHoaDon> ds)
    {
        int soluong_sanpham = 0;
        long tonggia_goc = 0;
        long tonggia_ban = 0;
        long tongcong = 0;
        for(com.example.kynashop.model.ChiTietHoaDon chiTietHoaDon : ds)
        {
            soluong_sanpham +=chiTietHoaDon.getSoLuong();
            tonggia_goc += chiTietHoaDon.getSanPham().getGiaGoc()*chiTietHoaDon.getSoLuong();
            tonggia_ban += chiTietHoaDon.getTriGia()*chiTietHoaDon.getSoLuong();
        }
        tongcong = tonggia_ban;
        tongsoluong.setText("Sản phẩm " + "(" + soluong_sanpham +  ")");
        tong_gia_goc.setText(Convent_Money.money(Double.valueOf(tonggia_goc)));
        tong_gia_ban.setText(Convent_Money.money(Double.valueOf(tonggia_ban)));
        tong.setText(Convent_Money.money(Double.valueOf(tongcong)));
    }
    private void setData(ArrayList<com.example.kynashop.model.ChiTietHoaDon> ds)
    {
        adapter = new Recycle_List_DonHang(this,ds);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list_giohang.setLayoutManager(linearLayoutManager);
        list_giohang.setAdapter(adapter);
    }
    private void KhachHang(int getKhachHang)
    {
        new CompositeDisposable().add(requestInterface.getKhachHang(getKhachHang)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        );
    }

    private void handleError(Throwable throwable) {
        Toast.makeText(this, "Lỗi đăng nhập", Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(KhachHang khachHang) {
        ten_khachhang.setText("Tên khách hàng: " + khachHang.getTenKhachHang());
        sdt.setText("Số điện thoại: " + khachHang.getSoDienThoai());
        diachi.setText("Địa chỉ: "+ khachHang.getDiaChi());
    }

}