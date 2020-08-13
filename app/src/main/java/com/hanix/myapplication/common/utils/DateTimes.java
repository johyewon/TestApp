package com.hanix.myapplication.common.utils;

import android.annotation.SuppressLint;
import android.text.TextUtils;

import com.hanix.myapplication.common.app.GLog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateTimes {

    public static final SimpleDateFormat DF_YYYY_MM_DD = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat DF_YYYYMMDDHHMMSS = new SimpleDateFormat("yyyyMMddHHmmss");

    /**
     * @param inForm  입력 형식(ex : yyyy-MM-dd)
     * @param outForm 출력 형식(ex : yyyy.MM.dd)
     * @param inDate  입력일자
     * @return 출력일자
     */
    public static String parseDate(String inForm, String outForm, String inDate) {

        SimpleDateFormat sf = new SimpleDateFormat(inForm);
        SimpleDateFormat sf2 = new SimpleDateFormat(outForm);
        Date date;
        String ret;

        try {
            date = sf.parse(inDate);
            ret = sf2.format(date);
        } catch (Exception e) {
            ret = inDate;
        }

        return ret;
    }

    /**
     * 입력받은 일자가 현재 일자보다 이전인지 여부를 확인
     *
     * @param date 만료일자(YYYY-MM-DD)
     * @return true : 지남
     */
    public static int remainDay(String date) {
        int ret = -1;

        if (date == null || date.length() > 10) {
            return ret;
        }

        try {
            SimpleDateFormat sf2 = new SimpleDateFormat(date);

            String form1 = DF_YYYY_MM_DD.format(new Date()); // 오늘일자
            String form2 = sf2.format(new Date());

            long diff = DF_YYYY_MM_DD.parse(form2).getTime() - DF_YYYY_MM_DD.parse(form1).getTime();
            ret = (int) (diff / 1000 / 60 / 60 / 24);

        } catch (Exception e) {
            ret = -1;
        }

        return ret;
    }

    /**
     * 1 minute = 60 seconds
     * 1 hour = 60 x 60 = 3600
     * 1 day = 3600 x 24 = 86400
     * <p>
     * SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hhmmss")
     * Date startDate = simpleDateFormat.parse("2019-04-02 173010")
     * Date endDate = simpleDateFormat.parse("2019-04-02 240000")
     * <p>
     * ex)printDifference(startDate, endDate)
     */
    public static void printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);
    }


    /**
     * 현재날짜 기준으로 startDate 와 endDate 가 유효한 데이터 기간안에 있는지를 확인한다.
     *
     * @param fromDateStr ex) 20190322000000 (YYYYMMDDHHMMSS)
     * @param toDateStr   ex) 20190625235959 (YYYYMMDDHHMMSS)
     * @return true: 기간안에 있어 실행가능함.  false: 기간안에 없어 실행 불가능함.
     */
    public static boolean getIsExecutableDate(String fromDateStr, String toDateStr) {
        boolean rtnBool = false;

        if (TextUtils.isEmpty(fromDateStr) || TextUtils.isEmpty(toDateStr)) {
            return false;
        }

        try {
            long diffMilli;
            String todayStr = DF_YYYYMMDDHHMMSS.format(new Date());
            Date todayDt = DF_YYYYMMDDHHMMSS.parse(todayStr);
            Date fromDt = DF_YYYYMMDDHHMMSS.parse(fromDateStr);
            Date toDt = DF_YYYYMMDDHHMMSS.parse(toDateStr);

            //1.fromDate 는 오늘날짜보다 작거나 같아야 한다.
            diffMilli = todayDt.getTime() - fromDt.getTime();
            if (diffMilli >= 0) {
                rtnBool = true;
            }

            //2.toDate 는 오늘날자보다 크커나 같아야 한다.
            diffMilli = toDt.getTime() - todayDt.getTime();
            if (diffMilli >= 0) {
                rtnBool = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
            rtnBool = false;
        }

        return rtnBool;
    }

    /**
     * @param format 출력일자 폼 (yyyy-MM-dd)
     * @param offset 금일기준 옵셋값(-1:하루전 0:금일 1:내일)
     * @return
     */
    public static String moveDate(String format, int offset) {
        if (format == null) {
            format = "yyyy.MM.dd";
        }

        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DATE, offset);

        return sdf.format(cal.getTime());
    }

    /**
     * 파라미터로 넘겨준 날짜의 하루가 끝나는 시간을 넘겨준다.
     *
     * @param date
     * @return
     */
    public static Date atEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 파라미터로 넘겨준 날짜의 하루가 시작하는 시간을 넘겨준다.
     *
     * @param date
     * @return
     */
    public static Date atStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 주어진 milliseconds 의 남은 시간을 표시한다.
     */
    public static String printRemainTime(long remainMilli) {
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = remainMilli / daysInMilli;
        long diffMilli11 = remainMilli % daysInMilli;

        long elapsedHours = diffMilli11 / hoursInMilli;
        long diffMilli22 = diffMilli11 % hoursInMilli;

        long elapsedMinutes = diffMilli22 / minutesInMilli;
        long diffMilli33 = diffMilli22 % minutesInMilli;

        long elapsedSeconds = diffMilli33 / secondsInMilli;

        String msg = elapsedDays + " days, " + elapsedHours + " hours, "
                + elapsedMinutes + " minutes, " + elapsedSeconds + " seconds";
        GLog.i("다음작업 남은시간 ===> " + msg);

        return msg;
    }
}
