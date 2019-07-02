package com.train.system.center.em;

/**
 * OrderStatus
 *
 * @author taokai3
 * @date 2018/6/18
 */
public class OrderStatus {

	// 订单状态：00:开始占位, 01:占位重发, 02:队列中, 11:正在占位, 44:占位人工

	/** 00:开始占位 */
	public static final String BOOKING_INIT = "00";

	/** 01:占位重发 */
	public static final String BOOKING_RESEND = "01";

	/** 02:队列中 */
	public static final String BOOKING_QUEUE = "02";

	/** 04:等待退票 */
	public static final String REFUND_WAIT = "04";

	/** 06:退票中 */
	public static final String REFUND_ING = "06";

	/** 07:人工退票 */
	public static final String REFUND_MANUAL = "07";

	/** 订单失败:占位失败，取消成功 */
	public static final String ORDER_FAIL = "10";

	/** 11:正在占位 */
	public static final String BOOKING_ING = "11";

	/** 11:退票完成 */
	public static final String REFUND_AUTO_OVER = "11";

	/** 33:审核退票完成 */
	public static final String REFUND_VERIFY_OVER = "33";

	/** 44:占位人工 */
	public static final String BOOKING_MANUAL = "44";

	/** 45:等待支付 */
	public static final String PAY_WAIT = "45";

	/** 55:开始支付 */
	public static final String PAY_BEGIN = "55";

	/** 61:支付人工 */
	public static final String PAY_MANUAL = "61";
	/** 66:支付中 */
	public static final String PAY_INT = "66";

	/** 77:取消失败 */
	public static final String CANCEL_FAIL = "77";

	/** 83:正在取消 */
	public static final String CANCEL_ING = "83";

	/** 84:准备取消 */
	public static final String CANCEL_INIT = "84";

	/** 85:开始取消 */
	public static final String CANCEL_START = "85";

	/** 85:开始取消 */
	public static final String ORDER_SUCCESS = "99";
}
