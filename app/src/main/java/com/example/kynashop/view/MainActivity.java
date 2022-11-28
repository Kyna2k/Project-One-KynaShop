package com.example.kynashop.view;

import static com.example.kynashop.API.API_Services.BASE_Service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.example.kynashop.API.API_Services;
import com.example.kynashop.R;
import com.example.kynashop.adapter.MainLayoutViewPager2;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 main_layout;
    private BottomNavigationView menu;
    private MainLayoutViewPager2 mainLayoutViewPager2;
    private API_Services requestInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestInterface = new Retrofit.Builder()
                .baseUrl(BASE_Service)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(API_Services.class);
        main_layout = findViewById(R.id.main_layout);
        menu = findViewById(R.id.menu);
        mainLayoutViewPager2 = new MainLayoutViewPager2(this);
        main_layout.setAdapter(mainLayoutViewPager2);
        menu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.TrangChu:
                        main_layout.setCurrentItem(0);
                        break;
                    case R.id.Top:
                        main_layout.setCurrentItem(1);
                        break;
                    case R.id.GioHang:
                        main_layout.setCurrentItem(2);
                        break;
                    case R.id.DonHang:
                        main_layout.setCurrentItem(3);
                        break;
                    case R.id.ThongTin:
                        main_layout.setCurrentItem(4);
                        break;
                }
                return true;
            }
        });
        main_layout.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position)
                {
                    case 0:
                        menu.setSelectedItemId(R.id.TrangChu);
                        break;
                    case 1:
                        menu.setSelectedItemId(R.id.Top);
                        break;
                    case 2:
                        menu.setSelectedItemId(R.id.GioHang);
                        break;
                    case 3:
                        menu.setSelectedItemId(R.id.DonHang);
                        break;
                    case 4:
                        menu.setSelectedItemId(R.id.ThongTin);
                        break;
                }
            }
        });
    }
    public ViewPager2 viewPager2()
    {
        return main_layout;
    }
    public API_Services build_API()
    {
        return requestInterface;
    }
    //Nó là công thức trên stackoverflow cơ bản xử lý viewpager lướt ngang với thắng khưa recycle cũng lướt ngang
    //Dont fukin ask what happen in this shit
    public void controlHorizontalScrollingInViewPager2(RecyclerView recyclerView, ViewPager2 viewPager2) {
        RecyclerView.OnItemTouchListener onTouchListener = new RecyclerView.OnItemTouchListener() {
            int lastX = 0;

            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                switch (e.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) e.getX();
                        break;
                    case MotionEvent.ACTION_MOVE:
                        boolean isScrollingRight = e.getX() < lastX;
                        if ((isScrollingRight && ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition() == recyclerView.getAdapter().getItemCount() - 1) ||(!isScrollingRight && ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition() == 0)) {
                            viewPager2.setUserInputEnabled(true);
                        } else {
                            viewPager2.setUserInputEnabled(false);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        lastX = 0;
                        viewPager2.setUserInputEnabled(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        };
        recyclerView.addOnItemTouchListener(onTouchListener);
    }
}