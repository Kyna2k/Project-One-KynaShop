package com.example.kynashop.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.kynashop.R;
import com.example.kynashop.model.Comment;

import java.util.ArrayList;

public class Recycle_List_Comment extends RecyclerView.Adapter<Recycle_List_Comment.ViewHolder> {
    private Context context;
    private ArrayList<Comment> ds;

    public Recycle_List_Comment(Context context,ArrayList<Comment> ds)
    {
        this.context = context;
        this.ds = ds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_comments,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(ds.get(position).getKhach().getAvatar() == null)
        {
            Glide.with(context).load(R.mipmap.man).circleCrop().into(holder.avartar);
        }else {
            Glide.with(context).load(ds.get(position).getKhach().getAvatar()).circleCrop().into(holder.avartar);
        }
        holder.ten_khachhang.setText(ds.get(position).getKhach().getTenKhachHang());
        holder.date.setText(ds.get(position).getNgay());
        holder.noidung.setText(ds.get(position).getNoiDung());
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView  avartar;
        TextView ten_khachhang,noidung,date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avartar = itemView.findViewById(R.id.avartar);
            ten_khachhang = itemView.findViewById(R.id.ten_khachhang);
            noidung = itemView.findViewById(R.id.noidung);
            date = itemView.findViewById(R.id.date);
        }
    }
}
