package com.hanix.myapplication.view.web;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import androidx.annotation.Nullable;


import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.view.MainActivity;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CustomWebChromeClient extends WebChromeClient {

    // 카메라 시작

    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;

    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    private Uri mCaptureImageURI = null;
    private String mCameraPhotoPath;

    // 카메라 끝

    private MainActivity mActivity;
    private WebView mWebView;

    public CustomWebChromeClient(MainActivity activity, WebView webView){
        this.mActivity = activity;
        this.mWebView = webView;
    }

    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        GLog.e("onJsAlert : " + message);
        return super.onJsAlert(view, url, message, result);
    }

    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        GLog.e("onJsConfirm : " + message);
        return super.onJsConfirm(view, url, message, result);
    }

    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        GLog.e(consoleMessage.message() + '\n' + consoleMessage.messageLevel() + '\n' + consoleMessage.sourceId());
        return super.onConsoleMessage(consoleMessage);
    }

    @Override
    public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, FileChooserParams fileChooserParams) {
//        return super.onShowFileChooser(webView, filePathCallback, fileChooserParams);

        if(mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePath;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePictureIntent.resolveActivity(mActivity.getPackageManager()) != null ) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (IOException e) {
                GLog.e(e.getMessage(), e);
            }
        }

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("image/*");

        Intent[] intentArray;
        if(takePictureIntent != null) {
            intentArray = new Intent[] {takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        mActivity.startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);

        return true;
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {

        mUploadMessage = uploadMsg;

        File imageStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Base Project");

        if(!imageStorageDir.exists()) {
            imageStorageDir.mkdir();
        }

        File file = new File(imageStorageDir +  File.separator  + "IMG_" + String.valueOf(System.currentTimeMillis()) + ".jpg");

        final Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCameraPhotoPath);

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("image/*");

        Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Parcelable[] {captureIntent});

        mActivity.startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg)  {
        openFileChooser(uploadMsg, "");
    }

    public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
        openFileChooser(uploadMsg, acceptType);
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir  = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK) && mWebView.canGoBack()) {
            mWebView.goBack();
            return true;
        }
        return mActivity.onKeyDown(keyCode, event);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // TODO : 웹뷰 업로드 및 다운로드 시 onActivityResult에 추가해야 함
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            if(requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
//                super.onActivityResult(requestCode, resultCode, data);
//                return;
//            }
//        }
//
//        Uri[] results = null;
//
//        if(resultCode == Activity.RESULT_OK) {
//            if (data != null) {
//                if (mCameraPhotoPath != null) {
//                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
//                } else {
//                    String dataString = data.getDataString();
//                    if (dataString != null) {
//                        results = new Uri[]{Uri.parse(dataString)};
//                    }
//                }
//            }
//
//            mFilePathCallback.onReceiveValue(results);
//            mFilePathCallback = null;
//
//        } else if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//
//            if(requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
//                super.onActivityResult(requestCode, resultCode, data);
//                return;
//            }
//
//            if(requestCode == FILECHOOSER_RESULTCODE) {
//                if(null == this.mUploadMessage) {
//                    return;
//                }
//
//                Uri result = null;
//
//                try {
//                    if(resultCode != Activity.RESULT_OK) {
//                        result = null;
//                    } else {
//                        result = data == null ? mCaptureImageURI : data.getData();
//                    }
//                } catch (Exception e) {
//                    GLog.e(e.getMessage(), e);
//                }
//
//                mUploadMessage.onReceiveValue(result);
//                mUploadMessage = null;
//            }
//        }
//        return;
    }
}
