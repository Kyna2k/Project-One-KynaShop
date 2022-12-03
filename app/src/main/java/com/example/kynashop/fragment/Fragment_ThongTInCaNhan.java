package com.example.kynashop.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.kynashop.API.API_Services;
import com.example.kynashop.R;
import com.example.kynashop.model.KhachHang;
import com.example.kynashop.model.LoginModel;
import com.example.kynashop.view.CapNhatThongTinActivity;
import com.example.kynashop.view.LoginActivity;
import com.example.kynashop.view.MainActivity;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

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
}
