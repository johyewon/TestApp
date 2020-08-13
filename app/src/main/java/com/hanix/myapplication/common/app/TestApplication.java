package com.hanix.myapplication.common.app;

import android.app.Activity;

import androidx.annotation.Nullable;
import androidx.multidex.MultiDexApplication;

import com.kakao.auth.ApprovalType;
import com.kakao.auth.AuthType;
import com.kakao.auth.IApplicationConfig;
import com.kakao.auth.ISessionConfig;
import com.kakao.auth.KakaoAdapter;
import com.kakao.auth.KakaoSDK;

import java.io.File;

/*
 * 공유 Application
 */
public class TestApplication extends MultiDexApplication {

    private static TestApplication instance;

    public static TestApplication getInstance() {
        return instance;
    }

    //로그 표시를 위한 디버그 모드인지를 판별한다.
    public boolean isDebuggable = false;
    //앱 Crash 에러 로그가 저장된 경로(cache 디렉토리)
    public String logFilePathAppCrashError = "";
    //GLog(전체로그) 로그가 저장되는 파일이름(cache 디렉토리)
    public String logFileFileNameGLog = "";
    //로그에서 사용되는 패키지명
    public String logForPkgName = "";


    @Override
    public void onCreate() {
        super.onCreate();
        TestApplication.instance = this;
        isDebuggable = GLog.isDebuggable(this);

        //앱 Crash 에러 로그가 저장된 경로(cache 디렉토리)
        logFilePathAppCrashError = new File(this.getCacheDir(), "crash_log.txt").getAbsolutePath();
        //GLog(전체로그) 로그가 저장되는 파일이름(cache 디렉토리)
        logFileFileNameGLog = new File(this.getCacheDir(), "payme_glog.txt").getAbsolutePath();


        logForPkgName = this.getPackageName();

        KakaoSDK.init(new KakaoSDKAdapter());


    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }

    public void finishApp(Activity activity) {
        if (activity.isTaskRoot()) {
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } else {
            activity.finishAffinity();
        }
    }

    public static class KakaoSDKAdapter extends KakaoAdapter {

        @Override
        public ISessionConfig getSessionConfig() {
            return new ISessionConfig() {
                @Override
                public AuthType[] getAuthTypes() {
                    return new AuthType[]{AuthType.KAKAO_LOGIN_ALL};
                }

                @Override
                public boolean isUsingWebviewTimer() {
                    return false;
                }

                @Override
                public boolean isSecureMode() {
                    return false;
                }

                @Nullable
                @Override
                public ApprovalType getApprovalType() {
                    return ApprovalType.INDIVIDUAL;
                }

                @Override
                public boolean isSaveFormData() {
                    return true;
                }
            };
        }

        @Override
        public IApplicationConfig getApplicationConfig() {
            return TestApplication::getInstance;
        }
    }
}
