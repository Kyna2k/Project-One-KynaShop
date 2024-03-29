package com.example.kynashop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
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
import com.example.kynashop.model.NhaSanXuat;
import com.example.kynashop.model.SanPhamTop10;
import com.example.kynashop.model.SanPhams;
import com.example.kynashop.view.MainActivity;

import java.util.ArrayList;

public class Recycle_List_SanPhams extends RecyclerView.Adapter<Recycle_List_SanPhams.ViewHolder> {
    private Context context;
    private ArrayList<SanPhamTop10> ds;
    private ArrayList<KhuyenMai> ds_khuyenmai;

    public Recycle_List_SanPhams(Context context,ArrayList<SanPhamTop10> ds,ArrayList<KhuyenMai> ds_nhasanxuat)
    {
        this.context = context;
        this.ds = ds;
        this.ds_khuyenmai = ds_nhasanxuat;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view  = layoutInflater.inflate(R.layout.item_top,parent,false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(ds.get(position).getHinhs().size() > 0)
        {
            Glide.with(context).load(ds.get(position).getHinhs().get(0).getUrl()).into(holder.hinh_laptop);
        }else
        {
            Glide.with(context).load(context.getResources().getIdentifier("laptop","mipmap",context.getPackageName())).into(holder.hinh_laptop);
        }
        holder.ten_sp.setText(ds.get(position).getTenSanPham());
        holder.giamgia.setVisibility(View.GONE);
        String dem  = String.valueOf(position+1);
        holder.xephang.setText(dem);
        holder.xephang.setTextColor(Color.parseColor("#000000"));
        holder.xephang.setTextSize(18);
        if(dem.equals("1"))
        {
            holder.xephang.setTextColor(Color.parseColor("#E6A519"));
            holder.xephang.setTextSize(35);
        }else if(dem.equals("2")){
            holder.xephang.setTextColor(Color.parseColor("#A9A9A9"));
            holder.xephang.setTextSize(30);
        }else if(dem.equals("3"))
        {
            holder.xephang.setTextColor(Color.parseColor("#cd7f32"));
            holder.xephang.setTextSize(25);
        }

        holder.soluong.setText("Tống sản phẩm đã bán: "+String.valueOf(ds.get(position).getTong()));
        holder.ten_nsx.setText(ds.get(position).getTenNhaSanXuat());
        if(ds.get(position).getMaKhuyenMai() != null)
        {
            for(KhuyenMai khuyenMai : ds_khuyenmai)
            {
                if(ds.get(position).getMaKhuyenMai() == khuyenMai.getMaKhuyenMai())
                {
                    Double x = Double.valueOf(khuyenMai.getPhanTramKhuyenMai() + "") ;
                    Double gia = ds.get(position).getGiaGoc()*(Double)((100-x)/100);
                    holder.gia.setText(Convent_Money.money(gia));
                    holder.giamgia.setVisibility(View.VISIBLE);
                    holder.giamgia.setText("-"+String.valueOf(khuyenMai.getPhanTramKhuyenMai())+"%");
                    break;
                }else {
                    holder.gia.setText(Convent_Money.money(Double.valueOf(ds.get(position).getGiaGoc())));
                    holder.giamgia.setVisibility(View.GONE);
                }
            }
        }else {
            holder.gia.setText(Convent_Money.money(Double.valueOf(ds.get(position).getGiaGoc())));
            holder.giamgia.setVisibility(View.GONE);
        }
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
        TextView xephang,ten_sp,ten_nsx,gia,soluong,giamgia;
        ImageView hinh_laptop;
        CardView list_car;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            xephang = itemView.findViewById(R.id.xephang);
            ten_sp = itemView.findViewById(R.id.ten_sp);
            ten_nsx = itemView.findViewById(R.id.ten_nsx);
            gia = itemView.findViewById(R.id.gia);
            soluong = itemView.findViewById(R.id.soluong);
            giamgia = itemView.findViewById(R.id.giamgia);
            hinh_laptop = itemView.findViewById(R.id.hinh_laptop);
            list_car = itemView.findViewById(R.id.list_car);


        }
    }

}
