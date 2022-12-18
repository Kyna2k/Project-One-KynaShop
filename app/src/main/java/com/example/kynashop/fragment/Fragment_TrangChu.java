package com.example.kynashop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.kynashop.API.API_Services;
import com.example.kynashop.Interfaces.click_nhaphathanh;
import com.example.kynashop.LoadingSreen.LoadingScreen;
import com.example.kynashop.R;
import com.example.kynashop.adapter.Recycle_Grid_SanPham;
import com.example.kynashop.adapter.Recycle_NhaSanXuat;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.NhaSanXuat;
import com.example.kynashop.model.SanPhams;
import com.example.kynashop.view.MainActivity;
import com.example.kynashop.view.SanPhamTheoGiDoActivity;
import com.example.kynashop.view.SearchActivity;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class Fragment_TrangChu extends Fragment implements click_nhaphathanh {
    private RecyclerView list_nsx,list_sanpham;
    private Recycle_NhaSanXuat adapter_nsx;
    private API_Services requestInterface;
    private ImageSlider Slide_khuyenMai;
    private ArrayList<KhuyenMai> ds_khuyenmaiAPi;
    private Recycle_Grid_SanPham apdater_sanpham;
    private EditText timkiem;
    private TextInputLayout textInputLayout;
    private ArrayList<SlideModel> ds_khuyenmai = new ArrayList<>();
    public Fragment_TrangChu(){

    }
    public static Fragment_TrangChu getInstance()
    {
        Fragment_TrangChu fragment_trangChu = new Fragment_TrangChu();
        Bundle bundle = new Bundle();
        fragment_trangChu.setArguments(bundle);
        return fragment_trangChu;
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
        return inflater.inflate(R.layout.fragment_trangchu,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoadingScreen.LoadingShow(getContext(),"Đang tải");
        ViewPager2 viewPager2 = ((MainActivity)getActivity()).viewPager2();
        list_nsx = view.findViewById(R.id.list_nsx);
        Slide_khuyenMai = view.findViewById(R.id.Slide_khuyenMai);
        list_sanpham = view.findViewById(R.id.list_sanpham);
        timkiem = view.findViewById(R.id.timkiem);
        textInputLayout = view.findViewById(R.id.textInputLayout);
        requestInterface = ((MainActivity)getActivity()).build_API();
        getNSX();
        getKhuyenMai();
        ((MainActivity)getActivity()).controlHorizontalScrollingInViewPager2(list_nsx,viewPager2);
        GetSanPham();
        timkiem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),SearchActivity.class));
            }
        });
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
        Toast.makeText(getContext(), "Lỗi sản phẩm", Toast.LENGTH_SHORT).show();
    }

    private void getDataSanPham(ArrayList<SanPhams> ds)
    {
        apdater_sanpham = new Recycle_Grid_SanPham(getContext(),ds,ds_khuyenmaiAPi);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        list_sanpham.setLayoutManager(gridLayoutManager);
        list_sanpham.setAdapter(apdater_sanpham);
        LoadingScreen.LoadingDismi();
    }
    private void getDataNSX(ArrayList<NhaSanXuat> ds)
    {
        adapter_nsx = new Recycle_NhaSanXuat(getContext(),ds,this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),1);
        gridLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        list_nsx.setLayoutManager(gridLayoutManager);
        list_nsx.setAdapter(adapter_nsx);

    }
    private void getNSX()
    {
        new CompositeDisposable().add(requestInterface.getAllNhaSanXuat()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getNsxOk, this::getNsxNoOk)
        );
    }

    private void getNsxNoOk(Throwable throwable) {
        Toast.makeText(getContext(), "Lỗi", Toast.LENGTH_SHORT).show();
    }

    private void getNsxOk(ArrayList<NhaSanXuat> nhaSanXuats) {
        if(nhaSanXuats != null)
        {
            getDataNSX(nhaSanXuats);
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

    }

    private void getKhuyenMaiOk(ArrayList<KhuyenMai> khuyenMais) {
        if(khuyenMais != null)
        {
            ds_khuyenmaiAPi = khuyenMais;
            for(KhuyenMai Kh : khuyenMais)
            {
                ds_khuyenmai.add(new SlideModel(Kh.getHinh(), ScaleTypes.FIT));
            }
            Slide_khuyenMai.setImageList(ds_khuyenmai);
            Slide_khuyenMai.setItemClickListener(i -> {
                Intent intent = new Intent(getContext(), SanPhamTheoGiDoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("MA",ds_khuyenmaiAPi.get(i).getMaKhuyenMai());
                bundle.putInt("loai",1);
                intent.putExtras(bundle);
                startActivity(intent);
                Log.i("click", "getKhuyenMaiOk: ");
            });
        }

    }

    @Override
    public void click(NhaSanXuat nhaSanXuat) {
        Intent intent = new Intent(getContext(),SanPhamTheoGiDoActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("nhasanxuat",nhaSanXuat);
        bundle.putInt("loai",2);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
