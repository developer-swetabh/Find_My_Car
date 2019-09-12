package com.example.findmycar.utils;

import android.support.design.widget.Snackbar;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Utils {
    public static void showSnackBarAlert(View view, String msg){
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    public static String covertToDate(long millis){
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy:HH:mm", Locale.US);
        return sdf.format(millis);
    }
}
