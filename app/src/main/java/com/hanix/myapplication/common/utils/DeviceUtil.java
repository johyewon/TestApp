package com.hanix.myapplication.common.utils;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.security.KeyChain;
import android.telephony.TelephonyManager;
import android.text.TextUtils;


import com.hanix.myapplication.common.app.GLog;

import java.util.Locale;


public class DeviceUtil {

    public static enum DEVICE_MAKER {
        SAMSUNG, LGE, ETC
    }

    /**
     * 현재 기기의 모델명을 취득
     */
    public static String getDeviceModelName() { return Build.MODEL; }

    /**
     * 현재 App의 build.gradle 의 VersionName 항목값을 가져온다
     */
    public static String getSwVerName(Context context) {
        String ver = "";
        try {
            ver = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ver;
    }

    /**
     * 현재 기기의 Binary version 을 취득
     */
    public static String getBinaryVersionName(Context context) {
        String rdoVer = Build.getRadioVersion();
        if(!TextUtils.isEmpty(rdoVer) && rdoVer.length() > 32) {
            rdoVer = rdoVer.substring(0,32);
        }
        return rdoVer;
    }

    /**
     * 삼성, 엘지 디바이스 체크
     */
    public static DEVICE_MAKER getDeviceMaker() {
        if( Build.MANUFACTURER.toUpperCase().indexOf("SAMSUNG") >= 0 ) {
            return DEVICE_MAKER.SAMSUNG;
        }
        else if( Build.MANUFACTURER.toUpperCase().indexOf("LGE") >= 0 ) {
            return DEVICE_MAKER.LGE;
        }
        return DEVICE_MAKER.ETC;
    }

    /**
     * 인터넷 접속 여부 체크
     */
    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            return (netInfo != null && netInfo.isConnected());
        } catch (NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 현재 네트워크망의 종류를 파악한다.
     * To get device consuming netowkr type is 2g,3g,4g
     *
     * @return LTE 일때는 true, 나머지는 false
     */
    public static boolean getNetworkTypeAvail(Context context) {
        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        int networkType = mTelephonyManager.getNetworkType();
        String netTypeStr = "";
        boolean is4GOver = true;

        switch (networkType) {
            case TelephonyManager.NETWORK_TYPE_GPRS:
            case TelephonyManager.NETWORK_TYPE_EDGE:
            case TelephonyManager.NETWORK_TYPE_CDMA:
            case TelephonyManager.NETWORK_TYPE_1xRTT:
            case TelephonyManager.NETWORK_TYPE_IDEN:
                netTypeStr = "2g";
                is4GOver = false;
                break;
            case TelephonyManager.NETWORK_TYPE_UMTS:
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                /**
                 From this link https://en.wikipedia.org/wiki/Evolution-Data_Optimized ..NETWORK_TYPE_EVDO_0 & NETWORK_TYPE_EVDO_A
                 EV-DO is an evolution of the CDMA2000 (IS-2000) standard that supports high data rates.

                 Where CDMA2000 https://en.wikipedia.org/wiki/CDMA2000 .CDMA2000 is a family of 3G[1] mobile technology standards for sending voice,
                 data, and signaling data between mobile phones and cell sites.
                 */
            case TelephonyManager.NETWORK_TYPE_HSDPA:
            case TelephonyManager.NETWORK_TYPE_HSUPA:
            case TelephonyManager.NETWORK_TYPE_HSPA:
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
            case TelephonyManager.NETWORK_TYPE_EHRPD:
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                //Log.d("Type", "3g");
                //For 3g HSDPA , HSPAP(HSPA+) are main  networktype which are under 3g Network
                //But from other constants also it will 3g like HSPA,HSDPA etc which are in 3g case.
                //Some cases are added after  testing(real) in device with 3g enable data
                //and speed also matters to decide 3g network type
                //https://en.wikipedia.org/wiki/4G#Data_rate_comparison
                netTypeStr = "3g";
                is4GOver = false;
                break;
            case TelephonyManager.NETWORK_TYPE_LTE:
                //case TelephonyManager.NETWORK_TYPE_NR: ==> android Q support
                //No specification for the 4g but from wiki
                //I found(LTE (Long-Term Evolution, commonly marketed as 4G LTE))
                //https://en.wikipedia.org/wiki/LTE_(telecommunication)
                netTypeStr = "4g";
                is4GOver = true;
                break;
            default:
                netTypeStr = "Notfound";
        }

        GLog.e("    현재 네트워크 연결상태 =====> :" + netTypeStr);

        return is4GOver;
    }

    /**
     * 현재 Wifi 접속중인지를 검색한다.
     * @return true: wifi 접속중.  false: wifi 접속중 아님.
     */
    public static boolean isWifiConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network network = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                network = connectivityManager.getActiveNetwork();
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

            if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 현재 Cellular 접속중인지를 검색한다.
     * @return true: celluar 접속중.  false: Cellular 접속중 아님.
     */
    public static boolean isCellularConnected(Context context) {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            Network network = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                network = connectivityManager.getActiveNetwork();
            }
            NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);

            if (capabilities != null && capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 유니크한 단말번호 - AndroidId 사용
     * - 대신 공장 초기화 시 사라질 수 있음
     * @param resolver
     * @return string
     */
    public static String getAndroidId(ContentResolver resolver) {
        @SuppressLint("HardwareIds") String androidId =  Settings.Secure.getString(resolver, Settings.Secure.ANDROID_ID);
        return androidId;
    }

    /**
     * iOS 에서 키체인 쓰는 것과 비슷
     * @return
     */
    public static String getAndroidKeyChain() {
        return KeyChain.EXTRA_KEY_ALIAS;
    }

    /**
     * 국가 코드를 가져와준다
     * - ex) KR, US, UK ...
     * @param context
     * @return
     */
    public static String getAndroidCountry(Context context) {
        Locale locale;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            locale = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            locale = context.getResources().getConfiguration().locale;
        }
        return locale.getCountry();
    }

    // 태블릿인지 아닌지 구분
    public static boolean IsTablet(Context context) {
        //화면 사이즈 종류 구하기
        int screenSizeType = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if(screenSizeType==Configuration.SCREENLAYOUT_SIZE_XLARGE || screenSizeType==Configuration.SCREENLAYOUT_SIZE_LARGE) {
            return true;
        }
        return false;
    }

    public static boolean isPhone(Context context) {
        int screenSizeType = context.getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;

        if(screenSizeType == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            GLog.d("SCREENLAYOUT_SIZE_NORMAL");
        } else if(screenSizeType == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            GLog.d("SCREENLAYOUT_SIZE_SMALL");
        } else if(screenSizeType == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            GLog.d("SCREENLAYOUT_SIZE_LARGE");
        } else if(screenSizeType == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            GLog.d("SCREENLAYOUT_SIZE_XLARGE");
        }

        if(screenSizeType== Configuration.SCREENLAYOUT_SIZE_NORMAL || screenSizeType==Configuration.SCREENLAYOUT_SIZE_SMALL){
            return true;
        }
        return false;

    }
}

