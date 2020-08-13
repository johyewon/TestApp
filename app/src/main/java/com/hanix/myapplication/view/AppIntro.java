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
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.hanix.myapplication.R;
import com.hanix.myapplication.common.app.GLog;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.kakao.util.helper.Utility.getPackageInfo;

public class AppIntro extends AppCompatActivity {

    public SharedPreferences sf;

    private static final long SPLASH_TIME = 2000;
    private static final int STOP_SPLASH = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_app_intro);
        getIntent().addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        GLog.d(getKeyHash(getApplicationContext()));
        sf = getSharedPreferences("Pref", MODE_PRIVATE);
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Intent intent;
            if (msg.what == STOP_SPLASH) {
                intent = new Intent(AppIntro.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.fade_in_activity, R.anim.hold_activity);
                finish();
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();

        Message msg = new Message();
        msg.what = STOP_SPLASH;
        handler.sendMessageDelayed(msg, SPLASH_TIME);
    }

    public static String getKeyHash(final Context context) {
        PackageInfo packageInfo = getPackageInfo(context, PackageManager.GET_SIGNATURES);
        if (packageInfo == null)
            return null;

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                return Base64.encodeToString(md.digest(), Base64.NO_WRAP);
            } catch (NoSuchAlgorithmException e) {
                GLog.e( "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
        return null;
    }

}
