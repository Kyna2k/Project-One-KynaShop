package com.example.kynashop.view;

import static com.example.kynashop.API.API_Services.BASE_Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.kynashop.API.API_Services;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_List_Comment;
import com.example.kynashop.model.Comment;
import com.example.kynashop.model.Convent_Money;
import com.example.kynashop.model.Hinh;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.NhaSanXuat;
import com.example.kynashop.model.SanPhams;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChiTietSanPhamActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView btn_back;
    private ImageSlider slide_sanpham;
    private TextView ten_sp,gia_ban,gia_goc,giamgia,thongso,ten_nsx;
    private RecyclerView list_comment;
    private Button themgiohang,muangay;
    private int MaSanPham;
    private SanPhams sanPhams_get;
    private API_Services requestInterface;
    private Recycle_List_Comment apdater_comment;
    private ArrayList<SlideModel> hinhSan = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_san_pham);
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(API_Services.class);
        toolbar = findViewById(R.id.toolbar);
        btn_back = findViewById(R.id.btn_back);
        slide_sanpham = findViewById(R.id.slide_sanpham);
        ten_sp = findViewById(R.id.ten_sp);
        gia_ban = findViewById(R.id.gia_ban);
        gia_goc = findViewById(R.id.gia_goc);
        giamgia = findViewById(R.id.giamgia);
        thongso = findViewById(R.id.thongso);
        ten_nsx = findViewById(R.id.ten_nsx);
        list_comment = findViewById(R.id.list_comment);
        themgiohang = findViewById(R.id.themgiohang);
        muangay = findViewById(R.id.muangay);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        MaSanPham = getIntent().getIntExtra("MASANPHAM",-1);
        SanPham(MaSanPham);
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    public void setDataComment(ArrayList<Comment> ds)
    {
        apdater_comment = new Recycle_List_Comment(this,ds);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list_comment.setLayoutManager(linearLayoutManager);
        list_comment.setAdapter(apdater_comment);
    }
    public void SanPham(int maSanPham)
    {
        new CompositeDisposable().add(requestInterface.getchitietSanPham(maSanPham)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getSanPhamOk, this::getSanPhamNoOk)
        );
    }

    private void getSanPhamNoOk(Throwable throwable) {
        Toast.makeText(this, "Lỗi get" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void getSanPhamOk(SanPhams sanPhams) {
        if(sanPhams != null)
        {
            sanPhams_get = sanPhams;
            ten_sp.setText(sanPhams.getTenSanPham());
            if(sanPhams.getMaKhuyenMai() != 0)
            {
                KhuyeMai(sanPhams.getMaKhuyenMai());

            }else {
                gia_ban.setText(Convent_Money.money(sanPhams.getGiaGoc()));
                gia_goc.setText(Convent_Money.money(sanPhams.getGiaGoc()));
            }
            getMaNhaSan(sanPhams.getMaNhaSanXuat());
            if(thongso !=null){
                thongso.setText(sanPhams.getThongSo());

            }
            if(sanPhams.getCommentApis().size() >0)
            {
                setDataComment(sanPhams.getCommentApis());
            }else {
                setDataComment(new ArrayList<Comment>());
            }
            SlideShowSanPham(sanPhams.getHinhs());
        }
    }
    private void SlideShowSanPham(ArrayList<Hinh> ds)
    {
        if(ds.size() > 0)
        {
            for(Hinh hinh : ds)
            {
                hinhSan.add(new SlideModel(hinh.getUrl(), ScaleTypes.CENTER_INSIDE));
            }
        }else {
            hinhSan.add(new SlideModel(R.mipmap.laptop,ScaleTypes.FIT));
        }
        slide_sanpham.setImageList(hinhSan);

    }
    public void getMaNhaSan(int MaNhaSanxuat)
    {
        new CompositeDisposable().add(requestInterface.getNhaSanXuat(MaNhaSanxuat)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getNhaSanXuatOk, this::getNhaSanXuatNoOk)
        );
    }

    private void getNhaSanXuatNoOk(Throwable throwable) {
    }

    private void getNhaSanXuatOk(NhaSanXuat nhaSanXuat) {
        if(nhaSanXuat != null)
        {
            ten_nsx.setText(nhaSanXuat.getTenNhaSanXuat());
        }

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
            Long gia = sanPhams_get.getGiaGoc()*((100-khuyenMai.getPhanTramKhuyenMai())*100);
            gia_ban.setText(Convent_Money.money(gia));
            gia_goc.setText(Convent_Money.money(sanPhams_get.getGiaGoc()));
            gia_goc.setPaintFlags(gia_ban.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            giamgia.setVisibility(View.VISIBLE);
            giamgia.setText(String.valueOf(khuyenMai.getPhanTramKhuyenMai())+"%");
        }else {
            gia_ban.setText(Convent_Money.money(sanPhams_get.getGiaGoc()));
            gia_goc.setText(Convent_Money.money(sanPhams_get.getGiaGoc()));
        }
    }
}