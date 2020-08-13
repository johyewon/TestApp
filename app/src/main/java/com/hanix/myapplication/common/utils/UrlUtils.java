package com.hanix.myapplication.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

public class UrlUtils {

    public static String getUrlEncodeUTF8(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * Map 을 url query 형태로 변환
     * ex) p3=a+%26+b&p2=cat&p1=12
     */
    public static String getMap2UrlEncodeUTF8(Map<?,?> map) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?,?> entry : map.entrySet()) {
            if(entry.getValue() == null) continue;

            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s",
                    getUrlEncodeUTF8(entry.getKey().toString()),
                    getUrlEncodeUTF8(entry.getValue().toString())
            ));
        }
        return sb.toString();
    }

    /**
     * list 를 url 파라미터 형식으로 변형
     * @param list
     * @param key
     * @return
     */
    public static String getList2UrlEncodeUTF8(ArrayList<String> list, String key) {
        StringBuilder sb = new StringBuilder();
        sb.append(key).append("=");
        for(String temp : list) {
            sb.append(getUrlEncodeUTF8(temp)).append(",");
        }
        String result = sb.toString();
        return result.substring(0, result.length()-1);
    }
}
