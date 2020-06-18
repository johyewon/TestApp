package com.hanix.myapplication.view.event;

import android.os.SystemClock;
import android.view.View;

public abstract class OnSingleClickListener implements View.OnClickListener{

    private static final long MIN_CLICK_INTERVAL = 300;
    private long mLastClick = 0;

    public abstract void onSingleClick(View v);

    @Override
    public void onClick(View v) {
        long currentClickTime = SystemClock.uptimeMillis();
        long elapsedTime = currentClickTime - mLastClick;
        mLastClick = currentClickTime;

        // 중복 클릭이 아닌 경우
        if(elapsedTime > MIN_CLICK_INTERVAL) {
            onSingleClick(v);
        }
    }
}
