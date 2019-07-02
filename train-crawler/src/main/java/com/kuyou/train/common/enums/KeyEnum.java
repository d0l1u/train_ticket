package com.kuyou.train.common.enums;

import lombok.Getter;

/**
 * KeyEnum
 *
 * @author taokai3
 * @date 2018/11/18
 */
@Getter
public enum KeyEnum {

    PC_COOKIE("PC_%s"),

    WHITE_lIST("WHITE_lIST"),

    BOOK_REQ("BOOK_REQ"),

    BOOK_RESP("BOOK_RESP"),

    CHANGE_REQ("CHANGE_REQ"),

    CHANGE_RESP("CHANGE_RESP"),

    LOGIN_SWITCH("LOGIN_SWITCH"),

    IP_CHECK("IP_CHECK"),

    IP_ORDER("IP_ORDER_"),

    //
    ;

    private String value;

    KeyEnum(String value) {
        this.value = value;
    }

    public String format(Object... info) {
        return String.format(this.value, info);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", this.name(), this.value);
    }
}
