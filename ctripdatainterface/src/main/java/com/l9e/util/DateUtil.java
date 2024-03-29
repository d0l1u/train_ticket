package com.l9e.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 时间处理工具类
 *
 * @author 肖有标
 */
public class DateUtil {

    public static final String DATE_HM = "HH:mm";

    public static final String DATE_FMT1 = "yyyy-MM-dd";

    public static final String DATE_FMT2 = "yyyyMMddHHmmss";

    //刘毅
    public static final String DATE_FMT3 = "yyyy-MM-dd HH:mm:ss";

    public static final String DATE_FMT_YMD = "yyyyMMdd";

    //一天的毫秒数
    private static final long ND = 1000 * 24 * 60 * 60;
    //一分钟的毫秒数
    private static final long MD = 1000 * 60;

    /**
     * 把时间转换成字符串
     *
     * @param date      时间
     * @param formatStr 要转换的格式
     * @return
     */
    public static String dateToString(Date date, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(date);
    }

    public static Date stringToDate(String dateStr, String formatStr) {
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        Date date;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }

    public static Date dateAddDays(Date date, int addDays) {
        if (null == date) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_YEAR, addDays);
        return cal.getTime();
    }

    /**
     * @param date    时间
     * @param addDays 要增加的天数
     * @return
     * @author zuoyx
     */
    public static String dateAddDays(String date, String addDays) {
        if (null == date) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(stringToDate(date, DATE_FMT1));
        cal.add(Calendar.DAY_OF_YEAR, Integer.valueOf(addDays));
        return dateToString(cal.getTime(), DATE_FMT1);
    }

    /**
     * @param date    时间
     * @param addDays 要增加的天数
     * @return "yyyy-MM-dd HH:mm:ss"
     * @author zuoyx
     */
    public static String dateAddDaysFmt3(String date, String addDays) {
        if (null == date) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(stringToDate(date, DATE_FMT3));
        cal.add(Calendar.DAY_OF_YEAR, Integer.valueOf(addDays));
        return dateToString(cal.getTime(), DATE_FMT3);
    }

    /**
     * @param date  时间1
     * @param date2 时间2
     * @return long 相差几天
     * @author 刘毅
     */
    public static int dateDiff(Date d1, Date d2) {
        long n1 = d1.getTime() / ND;
        long n2 = d2.getTime() / ND;
        long diff = Math.abs(n1 - n2);
        return (int) diff + 1;
    }

    /**
     * @param date  时间1
     * @param date2 时间2
     * @return long 相差分钟
     * @author zuoyx
     */
    public static String minuteDiff(String min1, String min2) {
        Date d1 = stringToDate(min1, DATE_HM);
        Date d2 = stringToDate(min2, DATE_HM);
        long n1 = d1.getTime() / MD;
        long n2 = d2.getTime() / MD;
        return String.valueOf(Math.abs(n1 - n2));
    }


    /**
     * @param date  时间1
     * @param date2 时间2
     * @return long 相差分钟
     * @author zuoyx
     */
    public static long minuteDiff(Date min1, Date min2) {
        long n1 = min1.getTime() / MD;
        long n2 = min2.getTime() / MD;
        return Math.abs(n1 - n2);
    }

    /**
     * 分钟数值转换为HH:mm
     *
     * @param str 分钟
     * @return HH:mm字符串
     * @author zuoyx
     */
    public static String minuteFormat(String str) {
        int minute = Integer.valueOf(str);
        String result = "";
        int hours = (int) (minute / 60);
        int min = (int) (minute % 60);
        if (hours > 10) {
            result += hours + "";
        } else if (hours > 0) {
            result += "0" + hours;
        } else {
            result += "00";
        }
        if (min > 10) {
            result += ":" + min;
        } else {
            result += ":0" + min;
        }
        return result;
    }

    public static Date dateAddMin(Date date, int addDays) {
        if (null == date) {
            return null;
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.MINUTE, addDays);
        return cal.getTime();
    }


    /**
     * @param dateStr
     * @param formatStr
     * @return
     */
    public static Date stringToDate_1(String dateStr, String formatStr) {

        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+0")); //避免出现负数
        Date date;
        try {
            date = sdf.parse(dateStr);

           // System.out.println(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return date;
    }


    /**
     * @param min1
     * @param min2
     * @return yyyy-MM-dd HH:mm
     */
    public static String minuteDiff_1(String min1, String min2) {

        Date d1 = stringToDate_1(min1, "yyyy-MM-dd HH:mm");
        Date d2 = stringToDate_1(min2, "yyyy-MM-dd HH:mm");
        long n1 = d1.getTime() / (1000 * 60);
        long n2 = d2.getTime() / (1000 * 60);

        return String.valueOf(Math.abs(n2 - n1));

    }

    /**
     * @param starTime
     * @param endTime
     * @return 返回分钟数, 前后间隔1天的时间相减的分钟数
     * @author meizs
     */
    public static String minuteDiff_2(String arrTime, String starTime) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d1 = stringToDate_1(arrTime, DateUtil.DATE_HM);
        Date d2 = stringToDate_1(starTime, DateUtil.DATE_HM);
        Date now = new Date();
        long n1 = d1.getTime() / (1000 * 60);
        long n2 = d2.getTime() / (1000 * 60);
        String str = null;
        if (n1 > n2) {//新的计算
            Date star = DateUtil.dateAddDays(now, 1);
            String arrStr = sdf.format(now);
            String starStr = sdf.format(star);
            starStr = starStr.concat(" ").concat(starTime);
            arrStr = arrStr.concat(" ").concat(arrTime);
            str = minuteDiff_1(starStr, arrStr);
        } else if (n1 <= n2) {
            str = DateUtil.minuteDiff(arrTime, starTime);//原有的计算
        }
        return str;
    }


    //判断两个时间的大小
    public static int compare_date(String DATE1, String DATE2) throws Exception {

        DateFormat df = new SimpleDateFormat("HH:mm");//24小时制(HH)
        Date dt1 = df.parse(DATE1);
        Date dt2 = df.parse(DATE2);
        if (dt1.getTime() > dt2.getTime()) {
            return 1;
        } else if (dt1.getTime() < dt2.getTime()) {
            return -1;
        } else {
            return 0;
        }

    }

    public static String getDayWord(int day) {

        String dayWord = "";
        if (day == 1) {
            dayWord = "当日";
        } else if (day == 2) {
            dayWord = "第2日";
        } else if (day == 3) {
            dayWord = "第3日";
        } else if (day == 4) {
            dayWord = "第4日";
        } else if (day == 5) {
            dayWord = "第5日";
        }

        return dayWord;
    }


    public static void main(String[] args) {
        System.out.println(dateToString(dateAddMin(new Date(), -10), DATE_FMT3));
    }
}
