package com.kuyou.train.common.enums;

import lombok.Getter;

/**
 * @ClassName: TicketType
 * @Description: 车票类型映射关系
 * @author: taokai
 * @date: 2017年7月20日 下午1:28:11
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
@Getter
public enum TicketType {

    /**
     * 成人票
     */
    CHE_NGREN("0", "1"),

    /**
     * 儿童票
     */
    ER_TONG("1", "2"),

    /**
     * 学生票
     */
    XUE_SHENG("3", "3"),

    /**
     * 残军票
     */
    CAN_JUN("2", "4");

    private String ky;
    private String kyfw;

    TicketType(String ky, String kyfw) {
        this.ky = ky;
        this.kyfw = kyfw;
    }

    public static TicketType getByKy(String ky) {
        for (TicketType p : values()) {
            if (p.ky.equals(ky)) {
                return p;
            }
        }
        return null;
    }


    public static TicketType getByKyfw(String kyfw) {
        for (TicketType p : values()) {
            if (p.kyfw.equals(kyfw)) {
                return p;
            }
        }
        return null;
    }


}
