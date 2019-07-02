package com.kuyou.train.common.status;

/**
 * RefundStatus
 *
 * @author taokai3
 * @date 2018/10/26
 */
public class RefundStatus {

    /**
     * 04:等待退票
     */
    public static final String REFUND_WAIT = "04";

    /**
     * 06:退票中
     */
    public static final String REFUND_ING = "06";

    /**
     * 07:人工退票
     */
    public static final String REFUND_MANUAL = "07";

    /**
     * 11:退票完成
     */
    public static final String REFUND_AUTO_OVER = "11";

    /**
     * 33:审核退票完成
     */
    public static final String REFUND_VERIFY_OVER = "33";

}
