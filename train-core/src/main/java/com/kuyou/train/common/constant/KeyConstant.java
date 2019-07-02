package com.kuyou.train.common.constant;

import org.joda.time.DateTime;

import java.util.Date;

/**
 * KeyConstant
 *
 * @author taokai3
 * @date 2018/10/26
 */
public class KeyConstant {

    // =======================  订单 ==========================
    /**
     * 预订占位
     */
    public static final String BOOK_REQ = "BOOK_REQ";
    public static final String BOOK_RESP = "BOOK_RESP";

    /**
     * 改签占位
     */
    public static final String CHANGE_REQ = "CHANGE_REQ";
    public static final String CHANGE_RESP = "CHANGE_RESP";

    /**
     * 白名单
     */
    public static final String WHITE_lIST = "WHITE_lIST";



    // =======================  基础数据 ==========================
    /**
     * 车站数据 name 映射 code
     */
    public static final String STATION_NAME2CODE = "STATION_NAME2CODE";
    public static final String STATION_CODE2NAME = "STATION_CODE2NAME";

    /**
     * 学校数据name 映射 code
     */
    public static final String SCHOOL_NAME2CODE = "SCHOOL_NAME2CODE";
    public static final String SCHOOL_CODE2NAME = "SCHOOL_CODE2NAME";


    /**
     * 城市数据name 映射 code
     */
    public static final String CITY_NAME2CODE = "CITY_NAME2CODE";
    public static final String CITY_CODE2NAME = "CITY_CODE2NAME";

    /**
     * 车次，出发时间 key:%s_%s_%s value:{HH:mm，历时 x分钟}
     */
    public static final String TRAIN_TIME = "%s_%s_%s";


    // =======================  效率统计 ==========================
    /**
     * 预订占位效率统计
     */
    private static final String BOOK_TIME_STATISTICS_PRE = "BOOK_TIME_";

    /**
     * 取消效率统计
     */
    private static final String CANCEL_TIME_STATISTICS_PRE = "CANCEL_TIME_";

    /**
     * 改签占位效率统计
     */
    private static final String CHANGE_TIME_STATISTICS_PRE = "CHANGE_TIME_";

    /**
     * 支付效率统计
     */
    private static final String PAY_TIME_STATISTICS_PRE = "PAY_TIME_";

    /**
     * 退票效率统计
     */
    private static final String REFUND_TIME_STATISTICS_PRE = "REFUND_TIME_";

    /**
     * 获取预订占位效率统计key
     *
     * @param now
     * @return
     */
    public static String getBookTimeStatistics(Date now) {
        return BOOK_TIME_STATISTICS_PRE + new DateTime(now).toString();
    }

    /**
     * 获取取消效率统计key
     *
     * @param now
     * @return
     */
    public static String getCancelTimeStatistics(Date now) {
        return CANCEL_TIME_STATISTICS_PRE + new DateTime(now).toString();
    }

    /**
     * 获取改签占位效率统计key
     *
     * @param now
     * @return
     */
    public static String getChangeTimeStatistics(Date now) {
        return CHANGE_TIME_STATISTICS_PRE + new DateTime(now).toString();
    }

    /**
     * 获取支付效率统计key
     *
     * @param now
     * @return
     */
    public static String getPayTimeStatistics(Date now) {
        return PAY_TIME_STATISTICS_PRE + new DateTime(now).toString();
    }

    /**
     * 获取退票效率统计key
     *
     * @param now
     * @return
     */
    public static String getRefundTimeStatistics(Date now) {
        return REFUND_TIME_STATISTICS_PRE + new DateTime(now).toString();
    }

    public static String getKey(String keyFormat, String... values) {
        return String.format(keyFormat, values);
    }
}
