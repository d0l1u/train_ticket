package com.kuyou.train.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * AccountWayEnum
 *
 * @author taokai3
 * @date 2018/12/26
 */
@AllArgsConstructor
@Getter
public enum AccountWayEnum {

    KUYOU(0, "酷游"),

    CHANNEL(1, "分销商");

    public static final String message = "[0:自有账号, 1:分销账号]";

    private Integer value;
    private String name;


    public static AccountWayEnum getByVaule(Integer value) {
        for (AccountWayEnum st : values()) {
            if (st.value.equals(value)) {
                return st;
            }
        }
        return KUYOU;
    }
}
