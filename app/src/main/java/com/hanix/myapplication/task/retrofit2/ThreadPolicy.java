package com.hanix.myapplication.task.retrofit2;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.StrictMode;

/**
 * 메인 UI 에서 쓰레드 쓸 경우 NetworkOnMainThreadException 에러 방지
 * 다중 이미지나 데이터가 큰 경우에 사용
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class ThreadPolicy {

    public ThreadPolicy() {
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);
    }
}
