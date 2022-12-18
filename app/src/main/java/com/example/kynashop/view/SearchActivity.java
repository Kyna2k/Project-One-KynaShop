package com.example.kynashop.view;

import static com.example.kynashop.API.API_Services.BASE_Service;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.kynashop.API.API_Services;
import com.example.kynashop.Interfaces.click_nhaphathanh;
import com.example.kynashop.LoadingSreen.LoadingScreen;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_Grid_SanPham;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.SanPhams;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity  {
    private EditText timkiem;
    private API_Services requestInterface;
    private ImageView mic,btn_back;
    private RecyclerView list_sanpham;
    private Recycle_Grid_SanPham apdater_sanpham;
    private ArrayList<KhuyenMai> ds_khuyenmaiAPi;
    private IntentFilter intentFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(API_Services.class);
        intentFilter = new IntentFilter();
        intentFilter.addAction("CHITIETSANPHAM");
        timkiem = findViewById(R.id.timkiem);
        mic = findViewById(R.id.mic);
        btn_back = findViewById(R.id.btn_back);
        list_sanpham = findViewById(R.id.list_sanpham);
        LoadingScreen.LoadingShow(this,"Đang tải dữ liệu");
        getKhuyenMai();
        GetSanPham();
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        timkiem.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_SEARCH)
                {
                    LoadingScreen.LoadingShow(SearchActivity.this,"Đang tải dữ liệu");
                    search(timkiem.getText().toString());
                    return true;
                }
                return false;
            }
        });
        timkiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(timkiem.getText().toString().equals(""))
                {
                    GetSanPham();
                }
            }
        });
        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"speak to text");
                try {
                    activityResultLauncher.launch(intent);
                }catch (Exception e)
                {
                    e.printStackTrace();
                    Toast.makeText(SearchActivity.this, "Loi roi", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        if(result.getData() != null){
                            ArrayList<String> data = result.getData().getStringArrayListExtra(
                                    RecognizerIntent.EXTRA_RESULTS);
                            search(Objects.requireNonNull(data).get(0));
                        }else{
                            Toast.makeText(SearchActivity.this, "Oops! Dzui lòng thử lại.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    private void getDataSanPham(ArrayList<SanPhams> ds)
    {
        apdater_sanpham = new Recycle_Grid_SanPham(this,ds,ds_khuyenmaiAPi);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        list_sanpham.setLayoutManager(gridLayoutManager);
        list_sanpham.setAdapter(apdater_sanpham);
        LoadingScreen.LoadingDismi();
    }
    private void GetSanPham()
    {
        new CompositeDisposable().add(requestInterface.getAllSanPham()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getSanPhamOk, this::getSanPhamNoOk)
        );
    }

    private void getSanPhamOk(ArrayList<SanPhams> sanPhams) {
        if(sanPhams != null)
        {
            getDataSanPham(sanPhams);

        }
    }

    private void getSanPhamNoOk(Throwable throwable) {
        Toast.makeText(this, "Lỗi sản phẩm", Toast.LENGTH_SHORT).show();
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

    }

    private void getKhuyenMaiOk(ArrayList<KhuyenMai> khuyenMais) {
        if(khuyenMais != null)
        {
            ds_khuyenmaiAPi = khuyenMais;

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
                    Intent intentSend = new Intent(SearchActivity.this,ChiTietSanPhamActivity.class);
                    intentSend.putExtra("MASANPHAM",MASANPHAM);
                    startActivity(intentSend);
                    break;
                default:
                    break;
            }
        }
    };
    private void search(String value)
    {
        new CompositeDisposable().add(requestInterface.Search(value)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getSanPhamOk_s, this::getSanPhamNoOk_s)
        );
    }

    private void getSanPhamOk_s(ArrayList<SanPhams> sanPhams) {
        if(sanPhams != null)
        {
            getDataSanPham(sanPhams);

        }
    }

    private void getSanPhamNoOk_s(Throwable throwable) {
        Toast.makeText(this, "Lỗi sản phẩm", Toast.LENGTH_SHORT).show();
    }
}