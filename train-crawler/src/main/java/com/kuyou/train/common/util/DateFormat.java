package com.kuyou.train.common.util;

import lombok.Getter;

/**
 * DateFormat
 *
 * @author taokai3
 * @date 2018/10/30
 */
@Getter
public enum DateFormat {

    DATE("yyyy-MM-dd"),

    DATE_HM("yyyy-MM-dd HH:mm"),

    DATE_YYYY("yyyy"),

    DATE_HMS("yyyy-MM-dd HH:mm:ss"),

    DATE_HMS2("yyyyMMddHHmmss"),

    DATE_HMS3("yyyyMMddHHmmsss"),

    DATE_HMSSS("yyyy-MM-dd HH:mm:sss"),

    TIME("HH:mm"),

    TIME_HMS("HH:mm:ss"),

    TIME_HM("HH:mm"),;

    private String format;

    DateFormat(String format) {
        this.format = format;
    }
}
