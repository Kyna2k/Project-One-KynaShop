package com.example.kynashop.adapter;

import android.app.Activity;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kynashop.Interfaces.click_nhaphathanh;
import com.example.kynashop.R;
import com.example.kynashop.model.NhaSanXuat;

import java.util.ArrayList;

public class Recycle_NhaSanXuat extends RecyclerView.Adapter<Recycle_NhaSanXuat.ViewHolder> {
    private Context context;
    private ArrayList<NhaSanXuat> ds;
    private click_nhaphathanh click;
    public Recycle_NhaSanXuat(Context context, ArrayList<NhaSanXuat> ds, click_nhaphathanh click)
    {
        this.context = context;
        this.ds = ds;
        this.click = click;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_nhasanxuat,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(ds.get(position).getHinh()).into(holder.hinh_nsx);
        holder.click_nsx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click.click(ds.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView hinh_nsx;
        CardView click_nsx;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hinh_nsx = itemView.findViewById(R.id.hinh_nsx);
            click_nsx = itemView.findViewById(R.id.click_nsx);
        }
    }
}
