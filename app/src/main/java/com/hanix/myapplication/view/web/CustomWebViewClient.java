package com.hanix.myapplication.view.web;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.hanix.myapplication.common.app.GLog;
import com.hanix.myapplication.view.MainActivity;

import java.net.URISyntaxException;


public class CustomWebViewClient extends WebViewClient {

    private MainActivity mActivity;
    private boolean mIsFirstLoading;
    private WebViewInterface mWebViewInterface;
    private boolean mIsFirstLogout;

    public CustomWebViewClient (MainActivity activity, WebViewInterface webViewInterface){
        this.mActivity = activity;
        this.mIsFirstLoading = true;
        this.mIsFirstLogout = true;
        this.mWebViewInterface = webViewInterface;
    }

    // Load the url
    public boolean shouldOverrideUrlLoading(WebView view, String url) {

        GLog.e("shouldOverrideUrlLoading: " + url);

        if(url.startsWith("tel:")) {
            Intent dial = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
            mActivity.startActivity(dial);
            return true;
        }
        //키보드 암호화 호출처리
        else if (url.startsWith("intent://")) {
            Intent intent = null;
            try {
                intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
                if (intent != null) {
                    //앱실행
                    mActivity.startActivity(intent);
                }
            } catch (URISyntaxException e) {
                //URI 문법 오류 시 처리 구간
                e.printStackTrace();
            } catch (ActivityNotFoundException e) {
                String packageName = intent.getPackage();
                if (!packageName.equals("")) {
                    // 앱이 설치되어 있지 않을 경우 구글마켓 이동
                    mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                }
            }

            return true;
        }
        else {
            view.loadUrl(url);
            return true;
        }
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        GLog.e("page start: " + url);
    }

    /**
     * 페이지가 로딩이 된 이후
     * @param view
     * @param url
     */
    @Override
    public void onPageFinished(final WebView view, String url) {
//        mActivity.resetBottomBtns();
        GLog.e("onPageFinished: " + url);

    }
}
