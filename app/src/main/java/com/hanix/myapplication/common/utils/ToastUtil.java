package com.hanix.myapplication.common.utils;

import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.widget.Toast;

import com.hanix.myapplication.common.app.GLog;


public class ToastUtil {

    private static Toast mToast;

    public static void showToastS(final Context context, final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                hideToast();
                mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                mToast.show();
            }
        });
    }

    public static void showwToastL(final Context context, final String msg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                hideToast();
                mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
                mToast.show();
            }
        });
    }

    public static void hideToast() {
        if(mToast != null) {
            GLog.i("Toast hide true!");
            mToast.cancel();
        }
    }

    public static void showToastPresent(final Context context, final String msg, final boolean isHtml) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                if(isHtml) {
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Toast.makeText(context, Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY), Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
