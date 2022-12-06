package com.example.kynashop.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.kynashop.API.API_Services;
import com.example.kynashop.LoadingSreen.LoadingScreen;
import com.example.kynashop.R;
import com.example.kynashop.model.KhachHang;
import com.example.kynashop.model.LoginModel;
import com.example.kynashop.view.CameraActivity;
import com.example.kynashop.view.CapNhatThongTinActivity;
import com.example.kynashop.view.LoginActivity;
import com.example.kynashop.view.MainActivity;
import com.facebook.login.LoginManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class Fragment_ThongTInCaNhan extends Fragment {
    private ImageView avatar_user;
    private TextView tenuser;
    private CardView doiavatar,capnhatthongtin,dangxuat;
    private KhachHang khachHang_send;
    private API_Services requestInterface;
    public Fragment_ThongTInCaNhan()
    {

    }
    public static Fragment_ThongTInCaNhan getInstance()
    {
        Fragment_ThongTInCaNhan fragment_thongTInCaNhan = new Fragment_ThongTInCaNhan();
        Bundle bundle = new Bundle();
        fragment_thongTInCaNhan.setArguments(bundle);
        return fragment_thongTInCaNhan;
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
        return inflater.inflate(R.layout.fragment_account,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestInterface = ((MainActivity)getActivity()).build_API();
        avatar_user = view.findViewById(R.id.avatar_user);
        tenuser = view.findViewById(R.id.tenuser);
        doiavatar = view.findViewById(R.id.doiavatar);
        capnhatthongtin = view.findViewById(R.id.capnhatthongtin);
        dangxuat = view.findViewById(R.id.dangxuat);
        capnhatthongtin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CapNhatThongTinActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("KH",khachHang_send);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        doiavatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog();
            }
        });
        dangxuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                LoginManager.getInstance().logOut();
            }
        });
        setValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        setValue();
    }

    public void setValue()
    {
        int makhachhang = getContext().getSharedPreferences("KhachHach", Context.MODE_PRIVATE).getInt("makhachhang",-1);
        if(makhachhang != -1)
        {
            KhachHang(makhachhang);
        }
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
        Toast.makeText(getContext(), "Lỗi đăng nhập", Toast.LENGTH_SHORT).show();
    }

    private void handleResponse(KhachHang khachHang) {
        Glide.with(this).load(khachHang.getAvatar()).circleCrop().into(avatar_user);
        tenuser.setText(khachHang.getTenKhachHang());
        khachHang_send = khachHang;
    }
    private void chooseImage() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent();
            intent.setType("image/*");// if you want to you can use pdf/gif/video
            intent.setAction(Intent.ACTION_GET_CONTENT);
            someActivityResultLauncher.launch(intent);
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            if (result.getResultCode() == Activity.RESULT_OK) {
                // There are no request codes
                Intent data = result.getData();
                Uri imagePath = data.getData();
                File file_ne = new File(getContext().getCacheDir(),"hinh.png");
                try {
                    InputStream in = getContext().getContentResolver().openInputStream(imagePath);
                    OutputStream out = new FileOutputStream(file_ne);
                    byte[] buf = new byte[1024];
                    int len;
                    while((len=in.read(buf))>0){
                        out.write(buf,0,len);
                    }
                    out.close();
                    in.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                int makhachhang = getContext().getSharedPreferences("KhachHach", Context.MODE_PRIVATE).getInt("makhachhang",-1);
                updateHinh(String.valueOf(makhachhang),file_ne);
            }
        }
    });
    private void Dialog()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        View view = getLayoutInflater().inflate(R.layout.diaglog_chonhinh,null);
        builder.setView(view);
        CardView layhinh = view.findViewById(R.id.layhinh);
        CardView chuphinh = view.findViewById(R.id.chuphinh);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        layhinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
                alertDialog.dismiss();
            }
        });
        chuphinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHinh.launch(new Intent(getActivity(), CameraActivity.class));
                alertDialog.dismiss();
            }
        });
    }
    private ActivityResultLauncher<Intent> getHinh = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
        @Override
        public void onActivityResult(ActivityResult result) {
            switch (result.getResultCode()) {
                case 555:
                    Intent intent = result.getData();
                    Bundle bundle = intent.getExtras();
                    String x = bundle.getString("data");
                    File file = new File(bundle.getString("data"));
                    int makhachhang = getContext().getSharedPreferences("KhachHach", Context.MODE_PRIVATE).getInt("makhachhang",-1);
                    updateHinh(String.valueOf(makhachhang),file);
            }
        }
    });
    public void updateHinh(String id, File file_update)
    {
        LoadingScreen.LoadingShow(getContext(),"Đang cập nhật hình");
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file_update);
        RequestBody requestname = RequestBody.create(MediaType.parse("multipart/form-data"), id);
        MultipartBody.Part multipartBody = MultipartBody.Part.createFormData("file",file_update.getName(),requestFile);

        new CompositeDisposable().add(requestInterface.UpdateAvartar(requestname,multipartBody)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(this::updateHinh_ok, this::updateHinh_nook)
    );
    }

    private void updateHinh_nook(Throwable throwable) {
        Log.e("loi up hinh", "updateHinh_nook: " + throwable.getMessage() );
        LoadingScreen.LoadingDismi();
    }

    private void updateHinh_ok(Integer integer) {
        LoadingScreen.LoadingDismi();
        if(integer > 0)
        {
            Toast.makeText(getContext(), "Thành công", Toast.LENGTH_SHORT).show();
            onResume();
        }
    }
}
