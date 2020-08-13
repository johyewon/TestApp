package com.hanix.myapplication.common.constants;

import com.hanix.myapplication.common.app.TestApplication;

public class URLApi {

    private static final String URL_DEV_SERVER = "";
    private static final String URL_REAL_SERVER = "";

    /** 서버 url **/
    // url 취득
    public static String getServerURL() {
        //Release 버전에서는 무조건 실서버를 바라본다
        if(TestApplication.getInstance().isDebuggable) {
            return URL_DEV_SERVER;
        }
        return URL_REAL_SERVER;
    }
}
