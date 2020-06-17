package com.hanix.myapplication.common.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.widget.Button;

public class BackPressCloseHandler {

    private long backKeyPressedTime = 0;

    private Activity activity;
    private AlertDialog alertDialog;
    private Button appFinishY, appFinishN;

    public BackPressCloseHandler(Activity context)
    {
        this.activity = context;
    }

    public void onBackPressed()
    {
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            return;
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            activity.finish();
        }
    }

}
