package com.kuyou.train.common.enums;

/**
 * @ClassName: TicketStatus
 * @Description: 12306车票状态
 * @author: taokai
 * @date: 2017年8月3日 下午1:50:37
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
public enum OrderStatusEnum {

    /**
     * fail:占位失败，自定义
     */
    BOOK_SEAT_FAIL("fail", "占位失败"),

    /**
     * wait:订单排队中，自定义
     */
    WAIT_ING("wait", "排队中"),

    /**
     * a: 已支付
     */
    PAY_FINISH("a", "已支付"),

    /**
     * b: 已出票
     */
    OUT_FINISH("b", "已出票"),

    /**
     * c: 已退票
     */
    REFUND_FINISH("c", "已退票"),

    /**
     * d:已改签
     */
    CHANGE_FINISH("d", "已改签"),

    /**
     * e:改签中
     */
    CHANGE_ING("e", "改签中"),

    /**
     * f: 改签票
     */
    CHANGE_TICKET("f", "改签票"),
    /** g */
    /** h */

    /**
     * i:待支付
     */
    WAIT_PAY("i", "待支付"),

    /**
     * j:改签待支付
     */
    CHANGE_WAIT_PAY("j", "改签待支付"),

    /** k */
    /** l */

    /**
     * m:已出站
     */
    OUT_STATION_FINISH("m", "已出站"),

    /** n */
    /** o */

    /**
     * p: 已变更到站
     */
    ALTER_FINISH("p", "已变更到站"),

    /**
     * q: 变更到站中
     */
    ALTER_ING("q", "变更到站中"),

    /**
     * r: 变更到站票
     */
    ALTER_TICKET("r", "变更到站票"),

    /**
     * s:变更到站待支付
     */
    ALTER_WAIT_PAY("s", "变更到站待支付"),

    /** t */
    ;

    private String code;
    private String title;

    OrderStatusEnum(String code, String title) {
        this.code = code;
        this.title = title;
    }

    public static OrderStatusEnum getEnum(String code) {
        for (OrderStatusEnum st : values()) {
            if (st.code.equals(code)) {
                return st;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
