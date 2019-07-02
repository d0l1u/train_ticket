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

    /**
     * 日期格式化，增量
     *
     * @param incr
     * @param timeUnit
     * @param dateFormat
     * @return
     */
    public static String format4Incr(int incr, TimeUnit timeUnit, DateFormat dateFormat) {
        return format4Incr(new Date(), incr, timeUnit, dateFormat);
    }

    /**
     * 日期格式化，增量
     *
     * @param date
     * @param incr
     * @param timeUnit
     * @param dateFormat
     * @return
     */
    public static String format4Incr(Date date, int incr, TimeUnit timeUnit, DateFormat dateFormat) {
        return format(new DateTime(date).plus(timeUnit.toMillis(incr)).toDate(), dateFormat);
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

    public static String format(Date date, String format) {
        return new DateTime(date).toString(format);
    }

    /**
     * 日期格式化
     *
     * @param dateFormat
     * @return
     */
    public static String format(DateFormat dateFormat) {
        return format(new Date(), dateFormat);
    }

    public static String format(Date date) {
        return format(date, DateFormat.DATE);
    }


    /**
     * 计算分钟HH:mm
     *
     * @param hhmmStr
     * @return
     */
    public static int mathMinutes(String hhmmStr) {
        String[] split = hhmmStr.split(":");
        return Integer.valueOf(split[0]) * 60 + Integer.valueOf(split[1]);
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
    public static Date incr(int add, TimeUnit timeUnit) {
        return incr(new Date(), add, timeUnit);
    }

    public static Date incr(Date date, Long add) {
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
    public static Date incr(Date date, int add, TimeUnit timeUnit) {
        Long addMillis = timeUnit.toMillis(Long.valueOf(add));
        return new DateTime(date).plusMillis(addMillis.intValue()).toDate();
    }


    public static void main(String[] args) {
        //
        System.err.println(DateUtil.format4Incr(-1, TimeUnit.DAYS, DateFormat.DATE));
        //
        System.err.println(format(parse("03:11", DateFormat.TIME_HM), DateFormat.DATE_HM));
    }
}
