package com.hanix.myapplication.common.constants;

import com.hanix.myapplication.common.app.TestApplication;
import com.hanix.myapplication.common.beans.UserBean;
import com.hanix.myapplication.common.utils.UrlUtils;

import java.util.HashMap;
import java.util.Map;

public class URLApi {

    private static final String URL_DEV_SERVER = "";
    private static final String URL_REAL_SERVER = "";

    public static final String KEY_URL = "keyUrl";
    public static final String KEY_QUERY = "keyQuery";


    /** 서버 url **/
    // url 취득
    public static String getServerURL() {
        //Release 버전에서는 무조건 실서버를 바라본다
        if(TestApplication.getInstance().isDebuggable) {
            return URL_DEV_SERVER;
        }
        return URL_REAL_SERVER;
    }

    /**
            * 앱 FCM 토큰 업데이트
     */
    public static Map<String, String> updateAppToken(UserBean userBean) {
        Map<String, String> pMap = new HashMap();

        String url = getServerURL() + "";

        //파라미터 생성
        Map<String, String> mkMap = new HashMap();
        mkMap.put("osType", "1"); //1:android, 2:iOS
//        mkMap.put("andToken", userBean.fcmToken);
//        mkMap.put("mobileNo", userBean.mobileNo);
        String query = UrlUtils.getMap2UrlEncodeUTF8(mkMap);

        pMap.put(KEY_URL, url);
        pMap.put(KEY_QUERY, query);

        return pMap;
    }

}
