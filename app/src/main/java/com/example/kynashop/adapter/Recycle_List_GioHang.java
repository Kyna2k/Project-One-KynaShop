package com.example.kynashop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kynashop.Interfaces.Click_item_GioHang;
import com.example.kynashop.R;
import com.example.kynashop.model.ChiTietHoaDon;
import com.example.kynashop.model.Convent_Money;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.SanPhams;
import com.example.kynashop.view.MainActivity;

import java.util.ArrayList;

public class Recycle_List_GioHang extends RecyclerView.Adapter<Recycle_List_GioHang.ViewHolder> {
    private Context context;
    private ArrayList<ChiTietHoaDon> ds;
    private Click_item_GioHang click_item_gioHang;
    private ArrayList<KhuyenMai> ds_khuyenMai;
    public Recycle_List_GioHang(Context context, ArrayList<ChiTietHoaDon> ds, ArrayList<KhuyenMai> ds_khuyenMai,Click_item_GioHang click_item_gioHang)
    {
        this.context = context;
        this.ds = ds;
        this.ds_khuyenMai = ds_khuyenMai;
        this.click_item_gioHang = click_item_gioHang;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_giohang,parent,false);
        return new ViewHolder(view);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(ds.get(position).getSanPham().getHinhs().size() > 0)
        {
            Glide.with(context).load(ds.get(position).getSanPham().getHinhs().get(0).getUrl()).into(holder.hinh_laptop);
        }else
        {
            Glide.with(context).load(context.getResources().getIdentifier("laptop","mipmap",context.getPackageName())).into(holder.hinh_laptop);
        }
        holder.khuyenMai.setVisibility(View.GONE);
        SanPhams sanPhams = ds.get(position).getSanPham();
        if(ds.get(position).getTriGia() < sanPhams.getGiaGoc())
        {
            holder.gia.setText(Convent_Money.money(ds.get(position).getTriGia()));
            holder.gia_goc.setText(Convent_Money.money(sanPhams.getGiaGoc()));
            holder.gia_goc.setPaintFlags(holder.gia_goc.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            holder.khuyenMai.setVisibility(View.VISIBLE);
        }else {
            holder.gia.setText(Convent_Money.money(ds.get(position).getTriGia()));
            holder.gia_goc.setText(Convent_Money.money(sanPhams.getGiaGoc()));
            holder.khuyenMai.setVisibility(View.GONE);
        }


        holder.ten_sp.setText(ds.get(position).getSanPham().getTenSanPham());
        holder.soluong.setText(String.valueOf(ds.get(position).getSoLuong()));
        holder.btn_cong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_item_gioHang.btn_tang(ds.get(holder.getAdapterPosition()));
            }
        });
        holder.btn_tru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_item_gioHang.btn_giam(ds.get(holder.getAdapterPosition()));
            }
        });
        holder.btn_xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click_item_gioHang.xoa(ds.get(holder.getAdapterPosition()).getMaSanPham());
            }
        });
        holder.list_car.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                Bundle build = new Bundle();
                build.putInt("MASANPHAM",ds.get(holder.getAdapterPosition()).getMaSanPham());
                intent.putExtras(build);
                intent.setAction("CHITIETSANPHAM");
                ((MainActivity)context).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hinh_laptop,btn_xoa;
        TextView ten_sp,gia,gia_goc,khuyenMai;
        ImageView btn_tru,btn_cong;
        EditText soluong;
        CardView list_car;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hinh_laptop = itemView.findViewById(R.id.hinh_laptop);
            ten_sp = itemView.findViewById(R.id.ten_sp);
            gia = itemView.findViewById(R.id.gia);
            btn_tru = itemView.findViewById(R.id.btn_tru);
            btn_cong = itemView.findViewById(R.id.btn_cong);
            soluong = itemView.findViewById(R.id.soluong);
            btn_xoa =itemView.findViewById(R.id.btn_xoa);
            click_item_gioHang.edit_soluong(soluong);
            list_car = itemView.findViewById(R.id.list_car);
            gia_goc = itemView.findViewById(R.id.gia_goc);
            khuyenMai = itemView.findViewById(R.id.khuyenMai);
        }
    }
}
