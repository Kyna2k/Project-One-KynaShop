package com.example.kynashop.model;

import android.icu.text.NumberFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Locale;

public class Convent_Money {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static String money(Double money)
    {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.US);
        String tien = nf.format(money);
        return tien.replace("$","").substring(0,tien.indexOf(".")-1)+ "\u20AB";
    }
}
