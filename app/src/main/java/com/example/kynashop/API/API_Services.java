package com.example.kynashop.API;

import com.example.kynashop.model.KhachHang;
import com.example.kynashop.model.LoginModel;

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

}
