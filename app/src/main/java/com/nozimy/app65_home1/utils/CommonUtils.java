package com.nozimy.app65_home1.utils;

import android.content.Context;
import android.widget.Toast;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observable;

public class CommonUtils {
    public static void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static <T> Flowable<T> zipWithTimer(Flowable<T> observable) {
        return Flowable.zip(observable,
                Flowable.timer(1000, TimeUnit.MILLISECONDS), (t, timerValue) -> t);
    }
}
