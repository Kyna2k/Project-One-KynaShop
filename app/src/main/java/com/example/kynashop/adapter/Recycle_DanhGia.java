package com.example.kynashop.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kynashop.Interfaces.getValueDanhGia;
import com.example.kynashop.R;
import com.example.kynashop.model.ChiTietHoaDon;
import com.example.kynashop.model.Convent_Money;
import com.example.kynashop.model.SanPhams;

import java.util.ArrayList;

public class Recycle_DanhGia extends RecyclerView.Adapter<Recycle_DanhGia.ViewHolder> {
    private Context context;
    private ArrayList<ChiTietHoaDon> ds;
    private getValueDanhGia get;
    public Recycle_DanhGia(Context context, ArrayList<ChiTietHoaDon> ds,getValueDanhGia get)
    {
        this.context = context;
        this.ds = ds;
        this.get = get;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_danhgia,parent,false);
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
        holder.soluong.setText( "X"+String.valueOf(ds.get(position).getSoLuong()));
        holder.noidung.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                get.getThongTin(ds.get(holder.getAdapterPosition()).getMaSanPham(),holder.getAdapterPosition(),holder.noidung.getText().toString());
            }
        });

    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hinh_laptop;
        TextView ten_sp,gia,gia_goc,khuyenMai,soluong;
        CardView list_car;
        EditText noidung;
        View include;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            include =itemView.findViewById(R.id.include);
            noidung =itemView.findViewById(R.id.noidung);

            hinh_laptop = include.findViewById(R.id.hinh_laptop);
            ten_sp = include.findViewById(R.id.ten_sp);
            gia = include.findViewById(R.id.gia);
            gia_goc = include.findViewById(R.id.gia_goc);
            khuyenMai = include.findViewById(R.id.khuyenMai);
            soluong = include.findViewById(R.id.soluong);
        }
    }
}
