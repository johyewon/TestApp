package com.hanix.myapplication.common.utils;

import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.hanix.myapplication.common.app.GLog;

public class SnackBarUtil {

    private static Snackbar mSnackbar;

    public static void hideSnackbar () {
        if(mSnackbar != null) {
            GLog.i("Snackbar hide true!");
            mSnackbar.dismiss();
        }
    }

    public static void showSnackbar(final View view, final String msg, final int length) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                hideSnackbar();
                mSnackbar = Snackbar.make(view, msg, length);
                mSnackbar.show();
            }
        });
    }

   public static void showSnackbarClick (final View view, final String msg, final int length, String clickMsg,View.OnClickListener clickListener) {
        hideSnackbar();

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                hideSnackbar();
                if(clickListener == null) {
                    mSnackbar = Snackbar.make(view, msg, length).setAction(clickMsg, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { mSnackbar.dismiss(); }
                    });
                } else {
                    mSnackbar = Snackbar.make(view, msg, length).setAction(clickMsg, clickListener);
                }
                mSnackbar.show();
            }
        });
   }

}
