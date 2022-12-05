package com.example.kynashop.view;

import static com.example.kynashop.API.API_Services.BASE_Service;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kynashop.API.API_Services;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_List_DonHang;
import com.example.kynashop.adapter.Recycle_List_GioHang;
import com.example.kynashop.model.ChiTietHoaDon;
import com.example.kynashop.model.Convent_Money;
import com.example.kynashop.model.HoaDon;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.MuaSanPham;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
//import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.AppMoMoLib;
import vn.momo.momo_partner.MoMoParameterNameMap;

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

    private String amount = "10000";
    private String fee = "0";
    int environment = 0;//developer default
    private String merchantName = "Demo Payment";
    private String merchantCode = "MOMOOJOI20210710";
    private String merchantNameLabel = "Kyna Shop";
    private String description = "Thanh toán dịch vụ ABC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_don_hang);
        AppMoMoLib.getInstance().setEnvironment(AppMoMoLib.ENVIRONMENT.DEVELOPMENT); // AppMoMoLib.ENVIRONMENT.PRODUCTION
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
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tong = findViewById(R.id.tong);
        btn_mua = findViewById(R.id.btn_mua);
        MaKhachHang = getSharedPreferences("KhachHach", Context.MODE_PRIVATE).getInt("makhachhang",-1);
        hoaDon = (HoaDon) getIntent().getExtras().getSerializable("value");
        type = getIntent().getExtras().getInt("type");
        list_chitiethoadon = hoaDon.getChiTietHoaDons();
        setGia(list_chitiethoadon);
        setData(list_chitiethoadon);
        btn_mua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    //Get token through MoMo app
    private void requestPayment(HoaDon hoaDon) {
        AppMoMoLib.getInstance().setAction(AppMoMoLib.ACTION.PAYMENT);
        AppMoMoLib.getInstance().setActionType(AppMoMoLib.ACTION_TYPE.GET_TOKEN);

        Map<String, Object> eventValue = new HashMap<>();
        //client Required
        eventValue.put("merchantname", merchantName); //Tên đối tác. được đăng ký tại https://business.momo.vn. VD: Google, Apple, Tiki , CGV Cinemas
        eventValue.put("merchantcode", merchantCode); //Mã đối tác, được cung cấp bởi MoMo tại https://business.momo.vn
        eventValue.put("amount", amount); //Kiểu integer
        eventValue.put("orderId", hoaDon.getMaHoaDon()); //uniqueue id cho Bill order, giá trị duy nhất cho mỗi đơn hàng
        eventValue.put("orderLabel", hoaDon.getMaHoaDon()); //gán nhãn

        //client Optional - bill info
        eventValue.put("merchantnamelabel", "Dịch vụ");//gán nhãn
        eventValue.put("fee", 0); //Kiểu integer
        eventValue.put("description", description); //mô tả đơn hàng - short description

        //client extra data
        eventValue.put("requestId",  merchantCode+"merchant_billId_"+System.currentTimeMillis());
        eventValue.put("partnerCode", merchantCode);
        //Example extra data
        JSONObject objExtraData = new JSONObject();
        try {
            objExtraData.put("MaKhachHang",hoaDon.getMaKhachHang()+"");
            objExtraData.put("MaNhanVien",hoaDon.getMaNhanVien()+ "");
            objExtraData.put("ngayxuathoadon",hoaDon.getNgayXuatHoaDon());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        eventValue.put("extraData", objExtraData.toString());

        eventValue.put("extra", "");
        AppMoMoLib.getInstance().requestMoMoCallBack(this, eventValue);


    }
    //Get token callback from MoMo app an submit to server side
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AppMoMoLib.getInstance().REQUEST_CODE_MOMO && resultCode == -1) {
            if(data != null) {
                if(data.getIntExtra("status", -1) == 0) {
                    //TOKEN IS AVAILABLE
                    Log.e("start", "onActivityResult: " );
                    String token = data.getStringExtra("data"); //Token response
                    String phoneNumber = data.getStringExtra("phonenumber");
                    String env = data.getStringExtra("env");
                    if(env == null){
                        env = "app";
                    }

                    if(token != null && !token.equals("")) {
                        // TODO: send phoneNumber & token to your server side to process payment with MoMo server
                        // IF Momo topup success, continue to process your order
                    } else {
                        Log.e("chabiet", "onActivityResult: " );
                    }
                } else if(data.getIntExtra("status", -1) == 1) {
                    //TOKEN FAIL
                    String message = data.getStringExtra("message") != null?data.getStringExtra("message"):"Thất bại";
                    Log.e("loi", "onActivityResult: " );
                } else if(data.getIntExtra("status", -1) == 2) {
                    //TOKEN FAIL
                    Log.e("loi", "onActivityResult: " );
                } else {
                    //TOKEN FAIL
                    Log.e("loi", "onActivityResult: " );
                }
            } else {
                Log.e("loi", "onActivityResult: " );

            }
        } else {
            Log.e("loi", "onActivityResult: " );

        }
    }
}