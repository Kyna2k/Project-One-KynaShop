package com.example.kynashop.view;

import static com.example.kynashop.API.API_Services.BASE_Service;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kynashop.API.API_Services;
import com.example.kynashop.Helper.AppInfo;
import com.example.kynashop.Helper.CreateOrder;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_List_DonHang;
import com.example.kynashop.model.ChiTietHoaDon;
import com.example.kynashop.model.Convent_Money;
import com.example.kynashop.model.HoaDon;
import com.example.kynashop.model.KhachHang;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.MuaSanPham;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.json.JSONObject;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import vn.zalopay.sdk.Environment;
import vn.zalopay.sdk.ZaloPayError;
import vn.zalopay.sdk.ZaloPaySDK;
import vn.zalopay.sdk.listeners.PayOrderListener;


public class DonHangActivity extends AppCompatActivity {
    private RecyclerView list_giohang;
    private TextView tongsoluong, tong_gia_goc, tong_gia_ban, tong;
    private Button btn_mua;
    private Recycle_List_DonHang adapter;
    private API_Services requestInterface;
    private KhuyenMai khuyenMai2;
    private ArrayList<KhuyenMai> ds_khuyenmaiAPi;
    private int MaHoaDon;
    private ArrayList<ChiTietHoaDon> list_chitiethoadon;
    private ImageView btn_back;
    private HoaDon hoaDon;
    private int type,MaKhachHang;

    private CardView ThongTin;
    private TextView ten_khachhang,sdt,diachi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        ZaloPaySDK.init(AppInfo.APP_ID, Environment.SANDBOX);

        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(API_Services.class);
        list_giohang = findViewById(R.id.list_giohang);
        tongsoluong = findViewById(R.id.tongsoluong);
        tong_gia_goc = findViewById(R.id.tong_gia_goc);
        tong_gia_ban = findViewById(R.id.tong_gia_ban);
        btn_back = findViewById(R.id.btn_back);
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
        MaKhachHang = getSharedPreferences("KhachHach", Context.MODE_PRIVATE).getInt("makhachhang",-1);
        KhachHang(MaKhachHang);
        hoaDon = (HoaDon) getIntent().getExtras().getSerializable("value");
        type = getIntent().getExtras().getInt("type");
        list_chitiethoadon = hoaDon.getChiTietHoaDons();
        setGia(list_chitiethoadon);
        setData(list_chitiethoadon);
        btn_mua.setEnabled(false);
        btn_mua.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {

                CreateOrder orderApi = new CreateOrder();
                try {
                    JSONObject data = orderApi.createOrder("1000");
                    String code = data.getString("returncode");

                    if (code.equals("1")) {

                        String token = data.getString("zptranstoken");

                        ZaloPaySDK.getInstance().payOrder(DonHangActivity.this, token, "demozpdk://app", new PayOrderListener() {
                            @Override
                            public void onPaymentSucceeded(final String transactionId, final String transToken, final String appTransID) {
                                Toast.makeText(DonHangActivity.this, "Thanh toán thành công", Toast.LENGTH_SHORT).show();
                                switch (type)
                                {
                                    case 1:
                                        MuaSanPham muaSanPham = new MuaSanPham(MaKhachHang,hoaDon.getChiTietHoaDons().get(0).getMaSanPham(),1,1,hoaDon.getChiTietHoaDons().get(0).getTriGia(),Convent_Money.date());
                                        muahangnhanh(muaSanPham);
                                        break;
                                    case 2:
                                        hoaDon.setTrangThai(1);
                                        hoaDon.setNgayXuatHoaDon(Convent_Money.date());
                                        muatheogio(hoaDon);
                                        break;
                                }
                            }

                            @Override
                            public void onPaymentCanceled(String zpTransToken, String appTransID) {
                                Toast.makeText(DonHangActivity.this, "Thanh toán bị hủy", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onPaymentError(ZaloPayError zaloPayError, String zpTransToken, String appTransID) {
                                Toast.makeText(DonHangActivity.this, "Thanh toán thất bại", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        });
    }
    public void muatheogio(HoaDon hiHoaDon)
    {
        new CompositeDisposable().add(requestInterface.updateTrangThaiMuaHang(hiHoaDon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::muatheogioOk, this::muatheogioNoOk)
        );
    }

    private void muatheogioOk(Integer integer) {
        if(integer > 0)
        {
            Toast.makeText(this, "Mua Hàng thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void muatheogioNoOk(Throwable throwable) {
        Log.e("loi_muahang", "muatheogioNoOk: " + throwable.getMessage() );
    }
    public void muahangnhanh(MuaSanPham muaSanPham)
    {
        new CompositeDisposable().add(requestInterface.MuaSanPham(muaSanPham)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::muahangnhanh, this::muahangnhanhNo)
        );
    }

    private void muahangnhanh(Integer integer) {
        if(integer > 0)
        {
            Toast.makeText(this, "Mua Hàng thành công", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void muahangnhanhNo(Throwable throwable) {
        Log.e("loi_muahang", "muatheogioNoOk: " + throwable.getMessage() );

    }
    //copy ham ngoai thang nay thoi
    @SuppressLint("NewApi")
    private void setGia(ArrayList<ChiTietHoaDon> ds)
    {
        int soluong_sanpham = 0;
        long tonggia_goc = 0;
        long tonggia_ban = 0;
        long tongcong = 0;
        for(ChiTietHoaDon chiTietHoaDon : ds)
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
    private void setData(ArrayList<ChiTietHoaDon> ds)
    {
        adapter = new Recycle_List_DonHang(this,ds);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        list_giohang.setLayoutManager(linearLayoutManager);
        list_giohang.setAdapter(adapter);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        ZaloPaySDK.getInstance().onResult(intent);
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
        Toast.makeText(this, "Lỗi Lấy thông tin", Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(KhachHang khachHang) {
        if(khachHang.getSoDienThoai() != null && khachHang.getDiaChi() != null && khachHang.getTenKhachHang() != null)
        {
            ten_khachhang.setText("Tên khách hàng: " + khachHang.getTenKhachHang());
            sdt.setText("Số điện thoại: " + khachHang.getSoDienThoai());
            diachi.setText("Địa chỉ: "+ khachHang.getDiaChi());
            btn_mua.setEnabled(true);
        }else {
            Intent intent = new Intent(DonHangActivity.this, CapNhatThongTinActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("KH",khachHang);
            bundle.putInt("type",005);
            intent.putExtras(bundle);
            capnhatdulieu.launch(intent);
        }
    }
    ActivityResultLauncher<Intent> capnhatdulieu = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {

                    switch (result.getResultCode())
                    {
                        case 1809:
                            KhachHang(MaKhachHang);
                            break;

                        default:
                            break;
                    }
                }
            });
}