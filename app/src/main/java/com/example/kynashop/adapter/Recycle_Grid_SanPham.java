package com.example.kynashop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannedString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kynashop.R;
import com.example.kynashop.model.Convent_Money;
import com.example.kynashop.model.KhuyenMai;
import com.example.kynashop.model.SanPhams;
import com.example.kynashop.view.MainActivity;

import java.util.ArrayList;

public class Recycle_Grid_SanPham extends RecyclerView.Adapter<Recycle_Grid_SanPham.ViewHolder> {
    private Context context;
    private ArrayList<SanPhams> ds;
    private ArrayList<KhuyenMai> ds_khuyenmai;
    public Recycle_Grid_SanPham(Context context,ArrayList<SanPhams> ds,ArrayList<KhuyenMai> ds_khuyenmai )
    {
        this.context = context;
        this.ds = ds;
        this.ds_khuyenmai = ds_khuyenmai;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.itemsanpham,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(ds.get(position).getHinhs().size() > 0)
        {
            Glide.with(context).load(ds.get(position).getHinhs().get(0).getUrl()).into(holder.hinh_laotop);
        }else
        {
            Glide.with(context).load(context.getResources().getIdentifier("laptop","mipmap",context.getPackageName())).into(holder.hinh_laotop);
        }
        holder.ten_sp.setText(ds.get(position).getTenSanPham());
        holder.khuyenMai.setVisibility(View.GONE);
        if(ds.get(position).getMaKhuyenMai() != 0)
        {
            try {
                for(KhuyenMai khuyenMai : ds_khuyenmai)
                {
                    if(ds.get(position).getMaKhuyenMai() == khuyenMai.getMaKhuyenMai())
                    {
                        Double x = Double.valueOf(khuyenMai.getPhanTramKhuyenMai() + "") ;
                        Double gia = ds.get(position).getGiaGoc()*(Double)((100-x)/100);
                        holder.gia_ban.setText(Convent_Money.money(gia));
                        holder.gia_goc.setText(Convent_Money.money(Double.valueOf(ds.get(position).getGiaGoc())));
                        holder.gia_goc.setPaintFlags(holder.gia_ban.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.khuyenMai.setVisibility(View.VISIBLE);
                        holder.khuyenMai.setText(String.valueOf(khuyenMai.getPhanTramKhuyenMai())+"%");
                        break;
                    }
                }
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }else {
            holder.gia_ban.setText(Convent_Money.money(Double.valueOf(ds.get(position).getGiaGoc())));
            holder.gia_goc.setText(Convent_Money.money(Double.valueOf(ds.get(position).getGiaGoc())));
            holder.khuyenMai.setVisibility(View.GONE);
        }
        holder.grid_sanpham.setOnClickListener(new View.OnClickListener() {
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
        CardView grid_sanpham;
        ImageView hinh_laotop;
        TextView ten_sp,gia_ban,gia_goc,khuyenMai;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hinh_laotop = itemView.findViewById(R.id.hinh_laotop);
            ten_sp = itemView.findViewById(R.id.ten_sp);
            gia_ban = itemView.findViewById(R.id.gia_ban);
            gia_goc = itemView.findViewById(R.id.gia_goc);
            khuyenMai = itemView.findViewById(R.id.khuyenMai);
            grid_sanpham = itemView.findViewById(R.id.grid_sanpham);
        }
    }
}
