package com.hanix.myapplication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.hanix.myapplication.R;
import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.common.constants.AppConstants;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;


public class AppIntro extends AppCompatActivity {

    public SharedPreferences sf;

    private static final long SPLASHTIME = 2000;
    private static final int STOPSPLASH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_intro);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        Log.d("Key is", "key is " + getKeyHash(getApplicationContext()));

        // new VersionCheckTask(this, mVersionCheckTaskCallbackInterface).execute(); // 버전 체크scro

        sf = getSharedPreferences("Pref", MODE_PRIVATE);
    }

    public static String getKeyHash(final Context context) {
        Log.d("Key is", "im here");
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                GLog.e("Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent;
            boolean isFirstRun = sf.getBoolean(AppConstants.isFirstRun, true);
            switch (msg.what) {
                case STOPSPLASH:
                    intent = new Intent(AppIntro.this, MainActivity.class);
                    startActivity(intent);
                    overridePendingTransition(R.anim.fade_in_activity, R.anim.hold_activity);
                    finish();
                    break;

            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        Message msg = new Message();
        msg.what = STOPSPLASH;
        handler.sendMessageDelayed(msg, SPLASHTIME);
    }

    /**
     * 버전체크 콜백 공통처리
     */
//    public TaskCallbackInterface mVersionCheckTaskCallbackInterface = new TaskCallbackInterface() {
//        @Override
//        public void onPostProc(String respStr, CommonRestTask commonRestTask) {
//
//            if("ok".equals(respStr)) {
//                //버전 일치
//            }
//            else if("update".equals(respStr)) {
//                //버젼이 현재버젼이랑 다르다
//                DlgUtil.showConfirmDlg(AppIntro.this, "최신버젼이 있습니다.\n업데이트를 하셔야만 사용 가능 합니다.", false, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        try {
//                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(VersionCheckTask.marketUrl)));
//                            finish();
//                        } catch (ActivityNotFoundException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                });
//            }
//
//        }
//    };
}
