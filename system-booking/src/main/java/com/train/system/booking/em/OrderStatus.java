package com.train.system.booking.em;

/**
 * OrderStatus
 *
 * @author taokai3
 * @date 2018/6/18
 */
public class OrderStatus {

    //订单状态：00:开始占位, 01:占位重发, 02:队列中, 11:正在占位, 44:占位人工

    /** 00:开始占位 */
    public static final String BOOKING_INIT = "00";

    /** 01:占位重发 */
    public static final String BOOKING_RESEND = "01";

    /** 02:队列中 */
    public static final String BOOKING_QUEUE = "02";

    /** 订单失败:占位失败，取消成功*/
    public static final String ORDER_FAIL = "10";

    /** 11:正在占位 */
    public static final String BOOKING_ING = "11";

    /** 44:占位人工 */
    public static final String BOOKING_MANUAL  = "44";

    /** 45:等待支付 */
    public static final String PAY_WAIT  = "45";

    /** 55:开始支付 */
    public static final String BEGIN_WAIT  = "55";

    /** 84:准备取消 */
    public static final String CANCEL_INIT  = "84";
}
