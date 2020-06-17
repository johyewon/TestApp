package com.hanix.myapplication.view.web;

import android.app.Activity;
import android.os.Handler;
import android.text.TextUtils;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.hanix.myapplication.common.beans.UserBean;
import com.hanix.myapplication.common.utils.DeviceUtil;

public class WebViewInterface {

    public static final String NAME_JS_INTERFACE = "Android";
    private WebView mWebView;
    private Activity mContext;

    /**
     * 생성자.
     * @param activity : context
     * @param view : 적용될 웹뷰
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
        if( TextUtils.isEmpty(mobileNo) || TextUtils.isEmpty(pw) ) return;

        UserBean userBean = new UserBean();
        //기기에 저장 -- 나중에 정의
//        PrefUtil.getInstance(mContext).setUserBean(userBean);
    }



    /**
     * 현재 앱버전 확인요청
     * JS -> App
     */
    @JavascriptInterface
    public void appGetVersion(){
        // Thread를 생성한다.
        new Thread(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl("javascript:appGetVersionResponse('"+ DeviceUtil.getSwVerName(mContext) +"')");
                    }
                });
            }
        }).start();
    }


    /**
     * 로그아웃 호출
     * App -> JS
     */
    public void setUserSessionLogout(){
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // runOnUiThread를 추가하고 그 안에 UI작업을 한다.
                mContext.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mWebView.loadUrl("javascript:doLogout()");
                    }
                });
            }
        }, 500);
    }


}
