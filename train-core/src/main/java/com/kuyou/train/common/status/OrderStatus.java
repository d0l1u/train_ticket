package com.kuyou.train.common.status;

/**
 * BookStatus
 *
 * @author taokai3
 * @date 2018/10/26
 */
public class OrderStatus {

    /**
     * 00:等待机器人进行占位
     */
    public static final String BOOK_INIT = "00";

    /**
     * 01:占位重发
     */
    public static final String BOOK_RESEND = "01";

    /**
     * 02:队列中
     */
    public static final String BOOK_QUEUE = "02";

    /**
     * 订单失败:占位失败，取消成功
     */
    public static final String ORDER_FAIL = "10";

    /**
     * 11:正在占位
     */
    public static final String BOOK_ING = "11";

    /**
     * 44:占位人工
     */
    public static final String BOOK_MANUAL = "44";

    /**
     * 45:等待支付
     */
    public static final String PAY_WAIT = "45";

    /**
     * 55:开始支付
     */
    public static final String PAY_BEGIN = "55";

    /**
     * 61:支付人工
     */
    public static final String PAY_MANUAL = "61";
    /**
     * 66:支付中
     */
    public static final String PAY_ING = "66";

    /**
     * 77:取消失败
     */
    public static final String CANCEL_FAIL = "77";

    /**
     * 83:正在取消
     */
    public static final String CANCEL_ING = "83";

    /**
     * 85:准备取消
     */
    public static final String CANCEL_INIT = "85";

    /**
     * 85:开始取消
     */
    public static final String ORDER_SUCCESS = "99";
}
