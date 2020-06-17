package com.hanix.myapplication.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.hanix.myapplication.common.beans.UserBean;

public class PrefUtil {

    private static final String PREF_NAME = "Pref";
    private static final String KEY_FCM_TOKEN_ID = "KEY_FCM_TOKEN_ID";
    private static final String KEY_USER_BEAN = "KEY_USER_BEAN";
    private static final String KEY_APP_RESET = "KEY_APP_RESET";

    private static SharedPreferences sf;
    private static PrefUtil instance ;
    private Gson gson;

    private PrefUtil() {
        gson = new Gson();
    }

    public static PrefUtil getInstance() {
        return instance;
    }

    public static PrefUtil getInstance(Context context) {

        if (sf == null) {
            sf = context.getSharedPreferences(PREF_NAME, Activity.MODE_PRIVATE);
        }

        if (instance == null) {
            instance = new PrefUtil();
        }
        return instance;
    }

    public static void setBoolean(Context context, String key, boolean value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public static boolean getBoolean(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getBoolean(key, false);
    }

    public static void setString(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getString(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getString(key, "");
    }

    public static void setInt(Context context, String key, int value) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getInt(key, 0);
    }

    /** 사용자 정보를 저장한다. */
    public void setUserBean(UserBean bean) {
        String encStr = "";
        try {
//            if(bean != null) {
//                String jsonStr = gson.toJson(bean);
//                //암호화
//                encStr = SecretUtils.getEncStr(jsonStr);
//            }
            //GLog.e(encStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(KEY_USER_BEAN, encStr);
        editor.commit();
    }

    /** 사용자 정보를 취득한다. */
    public UserBean getUserBean() {
        UserBean userBean = null;
        try {
//            String jsonStr = sf.getString(KEY_USER_BEAN, "");
//            String decStr = SecretUtils.getDecStr(jsonStr);
//            //GLog.e(decStr);
//            userBean = gson.fromJson(decStr, UserBean.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userBean;
    }

    /** FCM Token Id 값 설정 */
    public void setFcmTokenId(String tokenId) {
        SharedPreferences.Editor editor = sf.edit();
        editor.putString(KEY_FCM_TOKEN_ID, tokenId);
        editor.commit();
    }

    /** FCM Token Id 값을 취득한다. */
    public String getFcmTokenId() {
        return sf.getString(KEY_FCM_TOKEN_ID, "");
    }

}
