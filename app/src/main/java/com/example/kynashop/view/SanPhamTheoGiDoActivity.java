package com.example.kynashop.view;

import static com.example.kynashop.API.API_Services.BASE_Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.kynashop.API.API_Services;
import com.example.kynashop.LoadingSreen.LoadingScreen;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_Grid_SanPham;
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

public class SanPhamTheoGiDoActivity extends AppCompatActivity {
    private ImageView btn_back;
    private TextView title;
    private ImageSlider slide_sanpham;
    private Recycle_Grid_SanPham adapter;
    private RecyclerView list_item;
    private API_Services requestInterface;
    private ArrayList<KhuyenMai> ds_khuyenmaiAPi;
    private IntentFilter intentFilter;
    private ArrayList<SlideModel> ds_khuyenmai = new ArrayList<>();

    private int MA,loai;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(API_Services.class);
        setContentView(R.layout.activity_san_pham_theo_gi_do);
        intentFilter = new IntentFilter();
        intentFilter.addAction("CHITIETSANPHAM");
        btn_back = findViewById(R.id.btn_back);
        title = findViewById(R.id.title);
        slide_sanpham = findViewById(R.id.slide_sanpham);
        list_item = findViewById(R.id.list_item);
        getKhuyenMai();
        MA = getIntent().getExtras().getInt("MA",-1);
        loai = getIntent().getExtras().getInt("loai",-1);
        switch (loai)
        {
            case 1 :
                LoadingScreen.LoadingShow(this,"Đang tải dữ liệu");
                GetSanPhamKhuyenMai(MA);
                title.setText("Khuyến mãi");
                break;
            case 2:
                LoadingScreen.LoadingShow(this,"Đang tải dữ liệu");
                title.setText("Nhà sản xuất");
                NhaSanXuat nhaSanXuat = (NhaSanXuat) getIntent().getExtras().getSerializable("nhasanxuat");
                getSanPhamNSX(nhaSanXuat.getMaNhaSanXuat());
                ds_khuyenmai.add(new SlideModel(nhaSanXuat.getHinh(), ScaleTypes.CENTER_INSIDE));
                slide_sanpham.setImageList(ds_khuyenmai);
                break;

        }
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }
    public void setData(ArrayList<SanPhams> ds)
    {
        adapter = new Recycle_Grid_SanPham(this,ds,ds_khuyenmaiAPi);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        list_item.setLayoutManager(gridLayoutManager);
        list_item.setAdapter(adapter);
        LoadingScreen.LoadingDismi();
    }
    private void getSanPhamNSX(int MANHASANXUAT)
    {
        new CompositeDisposable().add(requestInterface.getSanPhamTheoNhaSanXuat(MANHASANXUAT)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getSanPhamOk, this::getSanPhamNoOk)
        );
    }
    private void GetSanPhamKhuyenMai(int MaKhuyenMai)
    {
        new CompositeDisposable().add(requestInterface.getSanPhamKhuyenMai(MaKhuyenMai)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::GetSanPhamKhuyenMaiok, this::GetSanPhamKhuyenMainook)
        );
    }

    private void GetSanPhamKhuyenMainook(Throwable throwable) {
        Log.e("loiget", "getKhuyenMaiNoOk: " + throwable.getMessage() );

    }

    private void GetSanPhamKhuyenMaiok(ArrayList<SanPhams> sanPhams) {
        if(sanPhams != null)
        {
            setData(sanPhams);
        }
    }

    private void getSanPhamNoOk(Throwable throwable) {
        Log.e("loiget", "getKhuyenMaiNoOk: " + throwable.getMessage() );

    }

    private void getSanPhamOk(ArrayList<SanPhams> sanPhams) {
        if(sanPhams != null)
        {
            setData(sanPhams);
        }
    }
    public void getKhuyenMai()
    {
        new CompositeDisposable().add(requestInterface.getAllKhuyenMai()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getKhuyenMaiOk, this::getKhuyenMaiNoOk)
        );
    }

    private void getKhuyenMaiNoOk(Throwable throwable) {
        Log.e("loiget", "getKhuyenMaiNoOk: " + throwable.getMessage() );
    }

    private void getKhuyenMaiOk(ArrayList<KhuyenMai> khuyenMais) {
        if(khuyenMais != null)
        {
            ds_khuyenmaiAPi = khuyenMais;
            for(KhuyenMai kh : khuyenMais)
            {
                if(kh.getMaKhuyenMai() == MA)
                {
                    ds_khuyenmai.add(new SlideModel(kh.getHinh(), ScaleTypes.FIT));
                    slide_sanpham.setImageList(ds_khuyenmai);
                    break;
                }
            }
        }

    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(getclick,intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(getclick);
    }
    public BroadcastReceiver getclick = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            switch (intent.getAction())
            {
                case "CHITIETSANPHAM":
                    int MASANPHAM = intent.getExtras().getInt("MASANPHAM");
                    Intent intentSend = new Intent(SanPhamTheoGiDoActivity.this,ChiTietSanPhamActivity.class);
                    intentSend.putExtra("MASANPHAM",MASANPHAM);
                    startActivity(intentSend);
                    break;
                default:
                    break;
            }
        }
    };
}