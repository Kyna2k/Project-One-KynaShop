package com.example.kynashop.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.kynashop.API.API_Services;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_List_SanPhams;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.SanPhamTop10;
import com.example.kynashop.view.MainActivity;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Fragment_Top10 extends Fragment {
    private RecyclerView recy_topsanpham;
    private Recycle_List_SanPhams recycle_list_sanPhams;
    private API_Services requestInterface;
    private ArrayList<KhuyenMai> ds_khuyenmaiAPi;
    public Fragment_Top10()
    {

    }
    public static Fragment_Top10 getInstance()
    {
        Fragment_Top10 fragment_top10 = new Fragment_Top10();
        Bundle bundle = new Bundle();
        fragment_top10.setArguments(bundle);
        return fragment_top10;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments() !=null)
        {

        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_top10,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recy_topsanpham = view.findViewById(R.id.recy_topsanpham);
        requestInterface = ((MainActivity)getActivity()).build_API();
        getKhuyenMai();
        getSanPham();
    }
    public void getSanPham()
    {
        new CompositeDisposable().add(requestInterface.getTopSanPham()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getTopSanPhamOk, this::getTopSanPhamNoOk)
        );
    }

    private void getTopSanPhamNoOk(Throwable throwable) {
    }

    private void getTopSanPhamOk(ArrayList<SanPhamTop10> sanPhamTop10s) {
        if(sanPhamTop10s != null)
        {
            getDataSanPham(sanPhamTop10s);
        }
    }

    public void getDataSanPham(ArrayList<SanPhamTop10> ds)
    {
        recycle_list_sanPhams = new Recycle_List_SanPhams(getContext(),ds,ds_khuyenmaiAPi);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recy_topsanpham.setLayoutManager(linearLayoutManager);
        recy_topsanpham.setAdapter(recycle_list_sanPhams);
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

}
