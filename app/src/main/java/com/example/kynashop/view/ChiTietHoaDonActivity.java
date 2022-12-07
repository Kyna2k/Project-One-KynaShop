package com.example.kynashop.view;

import static com.example.kynashop.API.API_Services.BASE_Service;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kynashop.API.API_Services;
import com.example.kynashop.Interfaces.getValueDanhGia;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_DanhGia;
import com.example.kynashop.adapter.Recycle_List_DonHang;
import com.example.kynashop.model.ChiTietHoaDon;
import com.example.kynashop.model.Comment;
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

public class ChiTietHoaDonActivity extends AppCompatActivity implements getValueDanhGia {

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
    private ArrayList<Comment> ds_comment = new ArrayList<>();
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
        for(int i = 0; i < list_chitiethoadon.size();i++)
        {
            ds_comment.add(new Comment());
        }
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
                btn_mua.setTextColor(Color.parseColor("#ffffff"));
                btn_mua.setBackground(getDrawable(R.drawable.background_btn));
                break;
            case 4:
                btn_mua.setText("Đã Đánh giá");
                break;
        }
        btn_mua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDanhGia(hoaDon.getChiTietHoaDons());
            }
        });
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
    private void dialogDanhGia(ArrayList<ChiTietHoaDon> ds_ct)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_danhgia,null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        Button btn_okay = view.findViewById(R.id.btn_okay);
        RecyclerView list = view.findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list.setLayoutManager(linearLayoutManager);
        list.setAdapter(new Recycle_DanhGia(this,ds_ct,this));
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment(ds_comment);
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

    private void addComment(ArrayList<Comment> ds_up)
    {
        new CompositeDisposable().add(requestInterface.addComment(ds_up)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::upcomment_z, this::upcomment_no)
        );
    }

    private void upcomment_no(Throwable throwable) {
        Log.e("comment", "upcomment_no: " + throwable.getMessage() );
    }

    private void upcomment_z(Integer integer) {
        if(integer > 0)
        {
            Toast.makeText(this, "Thành công", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void getThongTin(int MaSanPham, int potion, String noidung_) {
        ds_comment.get(potion).setMaKhachHang(MaKhachHang);
        ds_comment.get(potion).setNoiDung(noidung_);
        ds_comment.get(potion).setMaSanPham(MaSanPham);
        ds_comment.get(potion).setNgay(Convent_Money.date());
    }
}