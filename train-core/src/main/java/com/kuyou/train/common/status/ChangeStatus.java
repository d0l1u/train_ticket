package com.kuyou.train.common.status;

/**
 * ChangeStatus
 *
 * @author taokai3
 * @date 2018/10/26
 */
public class ChangeStatus {
    /** 11:等待任务重置状态 */
    public static final String CHANGE_INIT = "11";

    /** 10:等待机器改签 */
    public static final String CHANGE_WAIT = "10";

    /**
     * 12:开始机器改签
     */
    public static final String CHANGE_ING = "12";

    /** 13:人工改签 */
    public static final String CHANGE_MANAUL = "13";

    /** 14:改签待支付 */
    public static final String CHANGE_PAY = "14";

    /** 15:改签失败 */
    public static final String CHANGE_FAIL = "15";

    /** 21:等待改签取消 */
    public static final String CANCEL_INIT = "21";

    /** 22:改签取消中 */
    public static final String CANCEL_ING = "22";

    /** 23:取消成功 */
    public static final String CANCEL_SUCCESS = "23";

    /** 23:取消失败 */
    public static final String CANCEL_FAIL = "24";

    /** 31:等待支付 */
    public static final String PAY_WAIT = "31";

    /** 32:支付中 */
    public static final String PAY_ING = "32";

    /** 33:支付人工 */
    public static final String PAY_MANUAL = "33";

    /** 34:支付成功，改签成功 */
    public static final String PAY_SUCCESS = "34";

    /** 35:支付失败 */
    public static final String PAY_FAIL = "35";
}
