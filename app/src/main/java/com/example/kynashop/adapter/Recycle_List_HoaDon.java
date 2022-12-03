package com.example.kynashop.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kynashop.R;
import com.example.kynashop.model.Convent_Money;
import com.example.kynashop.model.HoaDon;

import java.util.ArrayList;

public class Recycle_List_HoaDon extends RecyclerView.Adapter<Recycle_List_HoaDon.ViewHolder> {
    private Context context;
    private ArrayList<HoaDon> ds;

    public Recycle_List_HoaDon(Context context, ArrayList<HoaDon> ds)
    {
        this.context = context;
        this.ds = ds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.item_hoadon,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.mahoadon.setText(String.valueOf(ds.get(position).getMaHoaDon()));
        String value_nv = (ds.get(position).getMaNhanVien() > 0)? String.valueOf(ds.get(position).getMaNhanVien()) : "";
        String value_ngay = (ds.get(position).getNgayXuatHoaDon() == null)? "" : Convent_Money.ngay(ds.get(position).getNgayXuatHoaDon());
        holder.maNhaVien.setText("Mã nhân viên: "+value_nv);
        holder.makhachhang.setText("Mã khách hàng: "+ ds.get(position).getMaKhachHang());
        holder.ngayxuathoadon.setText(value_ngay);
        switch (ds.get(position).getTrangThai())
        {
            case -1:
                holder.trangthai.setText("Đã Hủy");
                holder.trangthai.setTextColor(Color.parseColor("#44494D"));
                holder.trangthai.setBackgroundResource(R.drawable.layvogiau);

                break;
            case 1:
                holder.trangthai.setText("Chờ xác nhận");
                holder.trangthai.setTextColor(Color.parseColor("#FF4545"));
                break;
            case 2:
                holder.trangthai.setText("Đã xác nhận");
                holder.trangthai.setTextColor(Color.parseColor("#11998e"));
                break;
            case 3:
                holder.trangthai.setText("Đánh giá");
                holder.trangthai.setTextColor(Color.parseColor("#11998e"));
                break;
            case 4:
                holder.trangthai.setText("Đã Đánh giá");
                holder.trangthai.setTextColor(Color.parseColor("#11998e"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return ds.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView mahoadon,maNhaVien,makhachhang,trangthai,ngayxuathoadon;
        private CardView item;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mahoadon = itemView.findViewById(R.id.mahoadon);
            maNhaVien = itemView.findViewById(R.id.maNhaVien);
            makhachhang = itemView.findViewById(R.id.makhachhang);
            trangthai = itemView.findViewById(R.id.trangthai);
            ngayxuathoadon = itemView.findViewById(R.id.ngayxuathoadon);
            item = itemView.findViewById(R.id.item);
        }
    }
}
