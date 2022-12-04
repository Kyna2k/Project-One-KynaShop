package com.example.kynashop.fragment;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kynashop.API.API_Services;
import com.example.kynashop.LoadingSreen.LoadingScreen;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_List_HoaDon;
import com.example.kynashop.model.HoaDon;
import com.example.kynashop.view.MainActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Fragment_HoaDon extends Fragment {
    private RecyclerView list_hoadon;
    private API_Services requestInterface;
    private Recycle_List_HoaDon adapter;
    private int maKhachHang;
    public Fragment_HoaDon()
    {

    }
    public static Fragment_HoaDon getInstance()
    {
        Fragment_HoaDon fragment_hoaDon = new Fragment_HoaDon();
        Bundle bundle = new Bundle();
        fragment_hoaDon.setArguments(bundle);
        return fragment_hoaDon;
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
        return inflater.inflate(R.layout.fragment_hoadon,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestInterface = ((MainActivity)getActivity()).build_API();
        list_hoadon = view.findViewById(R.id.list_hoadon);
        getValue();
    }

    @Override
    public void onResume() {
        super.onResume();
        getValue();
    }

    public void getValue()
    {
        LoadingScreen.LoadingShow(getContext(),"Đang tải dữ liệu");
        maKhachHang = getContext().getSharedPreferences("KhachHach", Context.MODE_PRIVATE).getInt("makhachhang",-1);
        if(maKhachHang != -1)
        {
            getHoaDon(maKhachHang,0);
        }
    }
    public void setData(ArrayList<HoaDon> ds)
    {
        adapter = new Recycle_List_HoaDon(getContext(),ds);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        list_hoadon.setLayoutManager(linearLayoutManager);
        list_hoadon.setAdapter(adapter);
    }
    public void getHoaDon(int maKhachHang_ne,int trangthai)
    {
        new CompositeDisposable().add(requestInterface.getAllHoaDon(maKhachHang_ne,trangthai)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getHoaDon, this::getHoaDonNo)
        );
    }

    private void getHoaDon(ArrayList<HoaDon> hoaDons) {
        if(hoaDons != null)
        {
            setData(hoaDons);
            LoadingScreen.LoadingDismi();

        }
        else {
            LoadingScreen.LoadingDismi();
        }
    }

    private void getHoaDonNo(Throwable throwable) {
        Log.e("getValue", "getHoaDonNo: "+ throwable.getMessage() );
        LoadingScreen.LoadingDismi();

    }
}
