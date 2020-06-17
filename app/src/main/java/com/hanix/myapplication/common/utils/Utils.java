package com.hanix.myapplication.common.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.List;

public class Utils {

    /**
     * 대문자로 변환
     * @param s
     * @return
     */
    private static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    /**
     * 문자 -> 숫자 (오류일 경우 -1 반환)
     * @param strInt
     * @return
     */
    public static int getInt(String strInt) {
        try {
            return Integer.parseInt(strInt.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 문자열 배열 --> 버블소트 --> int 배열
     * @param strArr
     * @return
     */
    public static int[] getBubbleSort(String[] strArr) throws Exception {

        int intArr[] = new int[strArr.length];

        //str -> int
        for(int i=0; i<strArr.length; i++) {
            int naNum = 0;
            try {
                naNum = Integer.parseInt( strArr[i].trim() );
            } catch (Exception e) { }
            intArr[i] = naNum;
        }

        int temp;
        for(int i=intArr.length; i>0; i--) {
            for (int j=0; j<i-1; j++) {

                if(intArr[j] > intArr[j+1]) {
                    temp = intArr[j];
                    intArr[j] = intArr[j+1];
                    intArr[j+1] = temp;
                }
            }
        }

        return intArr;
    }

    /**
     * 패키지명 -> app ID 반환
     * @param packageName
     * @return
     */
    @SuppressLint("NewApi")
    public static int getPackageUid(Context context, String packageName) {
        int uid = -1;
        try {
            uid = context.getPackageManager().getPackageUid(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            //e.printStackTrace();
        }

        return uid;
    }

    /**
     * 현재앱의 버젼을 반환한다. ex)1.0.0
     * @return 1.0.0
     */
    public static String getAppVersionName(Context context) {
        String curAppVersion = "";
        try {
            curAppVersion = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return curAppVersion;
    }

    /**
     *  List<Integer> -> int[] 변환
     * @param list
     * @return
     */
    public static int[] toIntArray(List<Integer> list)  {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list)
            ret[i++] = e;
        return ret;
    }

    /**
     *  HashSet<Integer> -> int[] 변환
     * @param list
     * @return
     */
    public static int[] toIntArray(HashSet<Integer> list)  {
        int[] ret = new int[list.size()];
        int i = 0;
        for (Integer e : list)
            ret[i++] = e;
        return ret;
    }

    /**
     * 비디오의 Thumbnail 이미지를 Bitmap 으로 취득한다.
     * @param videoPath
     * @return
     * @throws Throwable
     */
    public static Bitmap retriveVideoFrameFromVideo(String videoPath) throws Throwable {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        } catch (Exception e) {
            e.printStackTrace();
            throw new Throwable("Exception in retriveVideoFrameFromVideo(String videoPath)" + e.getMessage());
        } finally {
            if (mediaMetadataRetriever != null) {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    /**
     * KB. MB, GB, TB 단위변환
     *
     * @param size
     * @return
     */
    public static Useage measureUnit(int size){
        String unit = "";
        int k = size;

        double m = size/1024;
        double g = size/1048576;
        double t = size/1073741824;

        Useage useage = new Useage();

        DecimalFormat dec = new DecimalFormat("0.0");

        if (k >= 0) {
            useage.unit = "KB";
            useage.useage = dec.format(k);
        } if (m > 0) {
            useage.unit = "MB";
            useage.useage = dec.format(m);
        } if (g > 0) {
            useage.unit = "GB";
            useage.useage = dec.format(g);
        } if (t > 0) {
            useage.unit = "TB";
            useage.useage = dec.format(t);
        }

        return useage;
    }

    public static class Useage {
        public String useage;
        public String unit;
    }

    /** 앱이 사용가능한 스크린 사이즈 취득 */
    public static Point getAppUsableScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

    /** 화면 전체 스크린 사이즈 취득 */
    public static Point getRealScreenSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();

        if (Build.VERSION.SDK_INT >= 17) {
            display.getRealSize(size);
        } else if (Build.VERSION.SDK_INT >= 14) {
            try {
                size.x = (Integer) Display.class.getMethod("getRawWidth").invoke(display);
                size.y = (Integer) Display.class.getMethod("getRawHeight").invoke(display);
            } catch (IllegalAccessException e) {} catch (InvocationTargetException e) {} catch (NoSuchMethodException e) {}
        }

        return size;
    }

    /** 타이틀바 높이취득 */
    public static int getTitleBarHeight(Activity activity) {
        Rect rect = new Rect();
        Window win = activity.getWindow();
        win.getDecorView().getWindowVisibleDisplayFrame(rect);
        int statusBarHeight = rect.top;
        int contentViewTop = win.findViewById(Window.ID_ANDROID_CONTENT).getTop();
        int titleBarHeight = contentViewTop - statusBarHeight;

        return titleBarHeight;
    }


    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String byteArrayToHexString(byte[] bytes){
        StringBuilder sb = new StringBuilder();
        for(byte b : bytes){
            sb.append(String.format("%02X", b&0xff));
        }
        return sb.toString();
    }

    /**
     * 블럭 사이즈 내에서 부족한 바이트를 zero 패딩
     */
    public static byte[] addPadding(byte[] source, int blockSize) {
        int paddingCnt = source.length % blockSize;
        byte[] paddingResult = null;

        if (paddingCnt != 0) {
            paddingResult = new byte[source.length + (blockSize - paddingCnt)];
            System.arraycopy(source, 0, paddingResult, 0, source.length);

            // 패딩 0x00 값을 추가한다.
            int addPaddingCnt = blockSize - paddingCnt;
            for (int i = 0; i < addPaddingCnt; i++) {
                paddingResult[source.length + i] = (byte) 0x00;
            }
        } else {
            paddingResult = source;
        }

        return paddingResult;
    }

    public static String addPaddingStr(String orgStr, int len) {
        if(orgStr != null) {
            int calcLen = orgStr.length() - len;
            if(calcLen < 0) {
                calcLen = Math.abs(calcLen);
                for(int i=0; i<calcLen; i++) {
                    orgStr += " ";
                }
                return  orgStr;
            } else {
                return orgStr.substring(0, len);
            }
        }
        return "";
    }

}
