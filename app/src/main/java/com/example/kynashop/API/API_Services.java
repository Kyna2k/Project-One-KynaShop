package com.example.kynashop.API;

import com.example.kynashop.model.KhachHang;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.LoginModel;
import com.example.kynashop.model.NhaSanXuat;
import com.example.kynashop.model.SanPhamTop10;
import com.example.kynashop.model.SanPhams;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface API_Services {
    public static final String BASE_Service = "https://kynalab.com/api/";

    @POST("login")
    Observable<KhachHang> login(@Body LoginModel loginModel);


    @GET("getAllKhuyenMai")
    Observable<ArrayList<KhuyenMai>> getAllKhuyenMai();

    @GET("getAllNhaSanXuat")
    Observable<ArrayList<NhaSanXuat>> getAllNhaSanXuat();

    @GET("getAllSanPham")
    Observable<ArrayList<SanPhams>> getAllSanPham();

    @GET("getTopSanPham")
    Observable<ArrayList<SanPhamTop10>>getTopSanPham();
}
