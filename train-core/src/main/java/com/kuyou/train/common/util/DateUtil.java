package com.kuyou.train.common.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * DateUtil
 *
 * @author taokai3
 * @date 2018/10/30
 */
public class DateUtil {

    public static String format(Date date) {
        return format(date, DateFormat.DATE);
    }

    /**
     * 日期格式化
     *
     * @param date
     * @param dateFormat
     * @return
     */
    public static String format(Date date, DateFormat dateFormat) {
        return new DateTime(date).toString(dateFormat.getFormat());
    }

    public static String format(DateFormat dateFormat) {
        return format(new Date(), dateFormat);
    }


    public static int mathMinutes(String hhmmStr) {
        String[] split = hhmmStr.split(":");
        return Integer.valueOf(split[0]) * 60 + Integer.valueOf(split[1]);
    }

    public static Date parse(String dateStr) {
        return parse(dateStr, DateFormat.DATE);
    }

    /**
     * @param dateStr
     * @param dateFormat
     * @return
     */
    public static Date parse(String dateStr, DateFormat dateFormat) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(dateFormat.getFormat());
        DateTime dateTime = dateTimeFormatter.parseDateTime(dateStr);
        return dateTime.toDate();
    }

    /**
     * 当前时间增加
     *
     * @param add
     * @param timeUnit
     * @return
     */
    public static Date add(int add, TimeUnit timeUnit) {
        return add(new Date(), add, timeUnit);
    }

    public static Date add(Date date, Long add) {
        return new DateTime(date).plusMillis(add.intValue()).toDate();
    }

    /**
     * 指定时间增加
     *
     * @param date
     * @param add
     * @param timeUnit
     * @return
     */
    public static Date add(Date date, int add, TimeUnit timeUnit) {
        Long addMillis = timeUnit.toMillis(Long.valueOf(add));
        return new DateTime(date).plusMillis(addMillis.intValue()).toDate();
    }


    public static void main(String[] args) {
        System.err.println(add(-3, TimeUnit.DAYS));
    }
}
