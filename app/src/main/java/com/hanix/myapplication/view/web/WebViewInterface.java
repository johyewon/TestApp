package com.hanix.myapplication.view.web;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.hanix.myapplication.common.utils.DeviceUtil;

public class WebViewInterface {

    public static final String NAME_JS_INTERFACE = "Android";
    private final WebView mWebView;
    private final Activity mContext;

    /**
     * 생성자.
     *
     * @param activity : context
     * @param view     : 적용될 웹뷰
     */
    public WebViewInterface(Activity activity, WebView view) {
        mContext = activity;
        mWebView = view;
    }

    /**
     * 로그인 정보저장
     * JS - App
     */
    @JavascriptInterface
    public void appSetLoginInfo(String mobileNo, String pw) {
        if (TextUtils.isEmpty(mobileNo) || TextUtils.isEmpty(pw)) return;

//        UserBean userBean = new UserBean();
    }


    /**
     * 현재 앱버전 확인요청
     * JS -> App
     */
    @JavascriptInterface
    public void appGetVersion() {
        // Thread 를 생성한다.
        new Thread(() -> {
            // runOnUiThread 를 추가하고 그 안에 UI 작업을 한다.
            mContext.runOnUiThread(() -> mWebView.loadUrl("javascript:appGetVersionResponse('" + DeviceUtil.getSwVerName(mContext) + "')"));
        }).start();
    }


    /**
     * 로그아웃 호출
     * App -> JS
     */
    public void setUserSessionLogout() {
        new Handler().postDelayed(() -> {
            // runOnUiThread 를 추가하고 그 안에 UI 작업을 한다.
            mContext.runOnUiThread(() -> mWebView.loadUrl("javascript:doLogout()"));
        }, 500);
    }


}
