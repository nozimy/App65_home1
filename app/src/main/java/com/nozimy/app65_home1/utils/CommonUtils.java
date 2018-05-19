package com.nozimy.app65_home1.utils;

import android.content.Context;
import android.widget.Toast;

public class CommonUtils {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
