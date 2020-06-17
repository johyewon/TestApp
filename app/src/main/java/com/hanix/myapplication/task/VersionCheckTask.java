package com.hanix.myapplication.task;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;


import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.common.interfaces.TaskCallbackInterface;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;

public class VersionCheckTask extends AsyncTask<Void, String, Boolean>  {

    public static boolean isVersionCheckFinished = false;
    public static String marketUrl = "";
    private static final String URL = "https://play.google.com/store/apps/details?id=";
    private Activity mActivity;
    private String packageName;
    private String marketVersion;
    public String curAppVersion;
    public TaskCallbackInterface mTaskCallBackInterface;

    public VersionCheckTask (Activity context, TaskCallbackInterface taskCallBackInterface) {
        mActivity = context;
        mTaskCallBackInterface = taskCallBackInterface;
        packageName = context.getPackageName();
        marketUrl = URL + packageName;
        try {
            curAppVersion = context.getPackageManager().getPackageInfo(packageName, 0 ).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Boolean doInBackground(Void... voids) {
        boolean result = false;

        try {
            Document doc = Jsoup.connect(marketUrl).get();
            GLog.d(" 버전 체크 중 : " + marketUrl);

            Elements Version = doc.select(".htlgb");

            for(int i = 0; i < Version.size(); i++) {
                marketVersion = Version.get(i).text();
                if(Pattern.matches("^[0-9]{1}.[0-9]{1}.[0-9]{1}$", marketVersion)) {
                    result = true;
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            GLog.e(e.getMessage());
            GLog.e("문서 오류");
            e.printStackTrace();
        } catch (RuntimeException runTimeEx) {
            GLog.e(runTimeEx.getMessage());
            GLog.e("버전가져오기 에러");
        }

        return result;
    }

    @Override
    protected void onPostExecute(Boolean result) {

        isVersionCheckFinished = true;

        if(result && !TextUtils.isEmpty(curAppVersion) &&
                    !TextUtils.equals(curAppVersion, marketVersion)) {
            mTaskCallBackInterface.onPostProc("update", null);
        } else {
            mTaskCallBackInterface.onPostProc("ok", null);
        }

    }
}
