package com.kuyou.train.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * SettingEnum
 *
 * @author taokai3
 * @date 2018/12/26
 */
@AllArgsConstructor
@Getter
public enum SettingEnum {

    /**
     * 机器人出票权重
     */
    CRALWER_WEIGHT("BOOKING_KYFW_WEIGHT"),

    /**
     * 航天华有出票权重
     */
    HTHY_WEIGHT("BOOKING_HTHY_WEIGHT");

    private String name;

}
