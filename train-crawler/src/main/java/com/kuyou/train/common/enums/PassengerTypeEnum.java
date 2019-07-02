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
public enum PassengerTypeEnum {

    /**
     * 成人票
     */
    CHE_NGREN("0", "1", "成人票"),

    /**
     * 儿童票
     */
    ER_TONG("1", "2", "儿童票"),

    /**
     * 学生票
     */
    XUE_SHENG("3", "3", "学生票"),

    /**
     * 残军票
     */
    CAN_JUN("2", "4", "残军票");

    private String ky;
    private String kyfw;
    private String title;

    PassengerTypeEnum(String ky, String hthy, String title) {
        this.ky = ky;
        this.kyfw = hthy;
        this.title = title;
    }

    public static PassengerTypeEnum getByKy(String ky) {
        for (PassengerTypeEnum p : values()) {
            if (p.ky.equals(ky)) {
                return p;
            }
        }
        return null;
    }


    public static PassengerTypeEnum getByKyfw(String kyfw) {
        for (PassengerTypeEnum p : values()) {
            if (p.kyfw.equals(kyfw)) {
                return p;
            }
        }
        return null;
    }

    public static PassengerTypeEnum getByTitle(String title) {
        for (PassengerTypeEnum p : values()) {
            if (p.title.equals(title)) {
                return p;
            }
        }
        return null;
    }


}
