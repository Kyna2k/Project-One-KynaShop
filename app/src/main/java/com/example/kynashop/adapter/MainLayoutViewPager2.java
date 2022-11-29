package com.example.kynashop.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.kynashop.fragment.Fragment_GioHang;
import com.example.kynashop.fragment.Fragment_Top10;
import com.example.kynashop.fragment.Fragment_TrangChu;

public class MainLayoutViewPager2 extends FragmentStateAdapter {


    public MainLayoutViewPager2(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return Fragment_TrangChu.getInstance();
            case 1:
                return Fragment_Top10.getInstance();
            case 2:
                return Fragment_GioHang.getInstance();

            default:
                return Fragment_TrangChu.getInstance();
        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
