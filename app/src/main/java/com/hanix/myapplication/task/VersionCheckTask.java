package com.hanix.myapplication.task;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.view.View;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.ActivityResult;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.common.interfaces.TaskCallbackInterface;
import com.hanix.myapplication.view.event.OnSingleClickListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.regex.Pattern;

//public class VersionCheckTask extends AsyncTask<Void, String, Boolean>  {
//
//    public static boolean isVersionCheckFinished = false;
//    public static String marketUrl = "";
//    private static final String URL = "https://play.google.com/store/apps/details?id=";
//    private Activity mActivity;
//    private String packageName;
//    private String marketVersion;
//    public String curAppVersion;
//    public TaskCallbackInterface mTaskCallBackInterface;
//
//    public VersionCheckTask (Activity context, TaskCallbackInterface taskCallBackInterface) {
//        mActivity = context;
//        mTaskCallBackInterface = taskCallBackInterface;
//        packageName = context.getPackageName();
//        marketUrl = URL + packageName;
//        try {
//            curAppVersion = context.getPackageManager().getPackageInfo(packageName, 0 ).versionName;
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//    }
//
//
//    @Override
//    protected Boolean doInBackground(Void... voids) {
//        boolean result = false;
//
//        try {
//            Document doc = Jsoup.connect(marketUrl).get();
//            GLog.d(" 버전 체크 중 : " + marketUrl);
//
//            Elements Version = doc.select(".htlgb");
//
//            for(int i = 0; i < Version.size(); i++) {
//                marketVersion = Version.get(i).text();
//                if(Pattern.matches("^[0-9]{1}.[0-9]{1}.[0-9]{1}$", marketVersion)) {
//                    result = true;
//                    break;
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//            GLog.e(e.getMessage());
//            GLog.e("문서 오류");
//            e.printStackTrace();
//        } catch (RuntimeException runTimeEx) {
//            GLog.e(runTimeEx.getMessage());
//            GLog.e("버전가져오기 에러");
//        }
//
//        return result;
//    }
//
//    @Override
//    protected void onPostExecute(Boolean result) {
//
//        isVersionCheckFinished = true;
//
//        if(result && !TextUtils.isEmpty(curAppVersion) &&
//                    !TextUtils.equals(curAppVersion, marketVersion)) {
//            mTaskCallBackInterface.onPostProc("update", null);
//        } else {
//            mTaskCallBackInterface.onPostProc("ok", null);
//        }
//
//    }
//}
public class VersionCheckTask {

    public static final int MY_REQUEST_CODE = 0111;
    AppUpdateManager appUpdateManager;
    Task<AppUpdateInfo> appUpdateInfoTask;

    Context context;
    Activity activity;

    public VersionCheckTask(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
        appUpdateManager = AppUpdateManagerFactory.create(this.context);
        appUpdateManager.registerListener(installStateUpdatedListener);

        appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if(appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                        && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                startUpdate(appUpdateInfo);
            } else if(appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else {
                GLog.e("checkForAppUpdateAvailability : something else");
            }
        });
    }

    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        @Override
        public void onStateUpdate(InstallState state) {
            if(state.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate();
            } else if(state.installStatus() == InstallStatus.INSTALLED) {
                if(appUpdateManager != null)
                    appUpdateManager.unregisterListener(installStateUpdatedListener);
            } else {
                GLog.i("installStateUpdatedListener : state : " + state.installStatus());
            }
        }
    };

    private void startUpdate(AppUpdateInfo appUpdateInfo) {
        try {
            if(appUpdateManager != null && appUpdateInfoTask != null) {
                appUpdateManager.startUpdateFlowForResult(
                        appUpdateInfo,
                        AppUpdateType.IMMEDIATE,
                        activity,
                        MY_REQUEST_CODE
                );
            }
        } catch (IntentSender.SendIntentException e) {
            GLog.e(e.getMessage());
        }
    }

    private void popupSnackBarForCompleteUpdate() {

        Snackbar snackbar = Snackbar.make(activity.getWindow().getDecorView().getRootView(),
                "새로운 업데이트가 있습니다.", Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("설치", new OnSingleClickListener() {
            @Override
            public void onSingleClick(View v) {
                if(appUpdateManager != null)
                    appUpdateManager.completeUpdate();
            }
        });

        snackbar.show();

    }

    public void updateResult(int resultCode, int requestCode) {
        if(requestCode == MY_REQUEST_CODE) {
            if(resultCode == ActivityResult.RESULT_IN_APP_UPDATE_FAILED) {
                GLog.e("RESULT_IN_APP_UPDATE_FAILED : " + resultCode);
            }
        }
    }
}
