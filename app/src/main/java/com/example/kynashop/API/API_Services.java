package com.example.kynashop.API;

import com.example.kynashop.model.ChiTietHoaDon;
import com.example.kynashop.model.Comment;
import com.example.kynashop.model.HoaDon;
import com.example.kynashop.model.KhachHang;
import com.example.kynashop.model.KhachHangAddSanPhamVaoGioHang;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.LoginModel;
import com.example.kynashop.model.MuaSanPham;
import com.example.kynashop.model.NhaSanXuat;
import com.example.kynashop.model.SanPhamTop10;
import com.example.kynashop.model.SanPhams;
import com.example.kynashop.model.XoaHoaDonGioHang;

import java.util.ArrayList;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    @GET("getchitietSanPham")
    Observable<SanPhams> getchitietSanPham(@Query("maSanPham") int maSanPham);

    @GET("getKhuyenMai")
    Observable<KhuyenMai> getKhuyenMai(@Query("MaKhuyenMai") int MaKhuyenMai);

    @GET("getNhaSanXuat")
    Observable<NhaSanXuat> getNhaSanXuat(@Query("MaNhaSanXuat") int MaNhaSanXuat);

    @GET("getGioHang")
    Observable<HoaDon> getGioHang(@Query("maKhachHang") int maKhachHang, @Query("trangthai") int trangthai);
    @POST("updateSoLuongTrongGioHang")
    Observable<Integer> updateSoLuongTrongGioHang(@Body ChiTietHoaDon chiTietHoaDon);

    @POST("XoaSanPhamKhoiGioHang")
    Observable<Integer> XoaSanPhamKhoiGioHang (@Body XoaHoaDonGioHang xoaHoaDonGioHang);

    @POST("addSanPhamVaoGioHang")
    Observable<Integer> addSanPhamVaoGioHang(@Body KhachHangAddSanPhamVaoGioHang khachHangAddSanPhamVaoGioHang);

    @GET("getKhachHang")
    Observable<KhachHang> getKhachHang(@Query("maKhachHang") int maKhachHang);

    @POST("updateKhachHang")
    Observable<Integer> updateKhachHang(@Body KhachHang khachHang);

    @GET("GetAllKhachHang")
    Observable<ArrayList<KhachHang>> GetAllKhachHang();

    @GET("getAllHoaDon")
    Observable<ArrayList<HoaDon>> getAllHoaDon(@Query("maKhachHang") int maKhachHang,  @Query("trangthai") int trangthai);

    @POST("updateTrangThaiMuaHang")
    Observable<Integer> updateTrangThaiMuaHang(@Body HoaDon hoaDon);

    @POST("MuaSanPham")
    Observable<Integer> MuaSanPham(@Body MuaSanPham muaSanPham);

    @GET("getSanPhamTheoNhaSanXuat")
    Observable<ArrayList<SanPhams>> getSanPhamTheoNhaSanXuat(@Query("MaNhaSanXuat") int MaNhaSanXuat);

    @GET("getSanPhamKhuyenMai")
    Observable<ArrayList<SanPhams>> getSanPhamKhuyenMai(@Query("MaKhuyenMai") int MaKhuyenMai);

    @Multipart
    @POST("UpdateAvartar")
    Observable<Integer> UpdateAvartar(@Part("id")RequestBody id, @Part MultipartBody.Part file);

    @POST("addComment")
    Observable<Integer> addComment(@Body ArrayList<Comment> ds);

    @POST("HuyDonHang")
    Observable<Integer> HuyDonHang(@Query("MaDonHang") int MaDonHang, @Query("maNhanVien") int maNhanVien);
}
