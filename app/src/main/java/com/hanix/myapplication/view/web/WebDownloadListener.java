package com.hanix.myapplication.view.web;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.hanix.myapplication.R;
import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.view.MainActivity;

import java.net.URLDecoder;

public class WebDownloadListener implements DownloadListener {

    private DownloadManager dm;
    private DownloadManager.Request request;
    private MainActivity activity;

    public WebDownloadListener(MainActivity activity) {
        this.activity = activity;
    }

    private String[] getPermission() {
        return new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
        try  {

            request = new DownloadManager.Request(Uri.parse(url));
            dm = (DownloadManager) activity.getSystemService(Context.DOWNLOAD_SERVICE);

            contentDisposition = URLDecoder.decode(contentDisposition,"UTF-8"); //디코딩
  //          String fileName = contentDisposition.replace("attachment; filename=", ""); //attachment; filename*=UTF-8''뒤에 파일명이있는데 파일명만 추출하기위해 앞에 attachment; filename*=UTF-8''제거
            String fileName = URLUtil.guessFileName(url, contentDisposition, mimetype);

            request.setMimeType(mimetype);
            request.addRequestHeader("User-Agent", userAgent);
            request.setDescription("Downloading File");
            request.setAllowedOverMetered(true);
            request.setAllowedOverRoaming(true);
            request.setTitle(fileName);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                request.setRequiresCharging(false);
            }
            request.allowScanningByMediaScanner();
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);
            dm.enqueue(request);

            Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.file_downloading), Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            GLog.e(e.getMessage(), e);
            if(ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.file_download_permission), Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(activity, getPermission(), 100);
                } else {
                    Toast.makeText(activity.getApplicationContext(), activity.getResources().getString(R.string.file_download_permission), Toast.LENGTH_LONG).show();
                    ActivityCompat.requestPermissions(activity, getPermission(), 100);
                }
            }
        }
    }
}
