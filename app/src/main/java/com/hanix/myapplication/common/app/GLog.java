package com.hanix.myapplication.common.app;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hanix.myapplication.common.constants.AppConstants;

/**
 * 로그 통합 관리
 */
public class GLog {

    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static boolean IsLogSaveOnOff = false;


    /**
     * 현재 디버그 모드여부를 리턴
     * @param context
     * @return
     */
    public static boolean isDebuggable (Context context) {
        boolean debuggable = false;

        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo appinfo = pm.getApplicationInfo(context.getPackageName(), 0);
            debuggable = (0 != (appinfo.flags & ApplicationInfo.FLAG_DEBUGGABLE));
        } catch (PackageManager.NameNotFoundException e) {
            /* debuggable variable will remain false */
        }

        return debuggable;
    }

    private static synchronized void dLong (String theMsg, int logType) {
        final int MAX_INDEX = 2000;

        if(theMsg == null) return;

        if(theMsg.length() > MAX_INDEX) {
            String theSubString = theMsg.substring(0, MAX_INDEX);
            int theIndex = MAX_INDEX;

            theSubString = getWithMethodName(theSubString);

            switch(logType) {
                case 1: //i
                    Log.i(AppConstants.TAG, theSubString);
                    break;
                case 2: //d
                    Log.d(AppConstants.TAG, theSubString);
                    break;
                case 3: //e
                    Log.e(AppConstants.TAG, theSubString);
                    break;
            }
            dLong(theMsg.substring(theIndex), theIndex);
        } else {
            theMsg = getWithMethodName(theMsg);

            switch (logType) {
                case 1: //i
                    Log.i(AppConstants.TAG, theMsg);
                    break;
                case 2: //d
                    Log.d(AppConstants.TAG, theMsg);
                    break;
                case 3: //e
                    Log.e(AppConstants.TAG, theMsg);
                    break;
            }
        }
    }

    private static String getWithMethodName (String log) {
        try {
            StackTraceElement ste = Thread.currentThread().getStackTrace()[5];
            StringBuffer sb = new StringBuffer();
            sb.append("[");
            sb.append(ste.getFileName().replace(".java", ""));
            sb.append("::");
            sb.append(ste.getMethodName());
            sb.append("]");
            sb.append(log);
            return sb.toString();
        } catch (Throwable e) {
            e.printStackTrace();
            return log;
        }
    }

    public static synchronized void i (String msg) {
        if(TestApplication.getInstance().isDebuggable) dLong(msg, 1);
    }

    public static synchronized void d (String msg) {
        if(TestApplication.getInstance().isDebuggable) dLong(msg, 2);
    }

    public static synchronized void e (String msg) {
        if(TestApplication.getInstance().isDebuggable) dLong(msg, 3);
    }

    public static void e (String msg, Exception e) {
        if(TestApplication.getInstance().isDebuggable) Log.e(AppConstants.TAG, msg, e);
    }
}
