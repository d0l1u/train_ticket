package com.kuyou.train.common.enums;

import lombok.AllArgsConstructor;

/**
 * UrlEnum
 *
 * @author taokai3
 * @date 2018/11/27
 */
@AllArgsConstructor
public enum UrlEnum {

    REFUND("http://%s:18000/train/refund"),

    CANCEL("http://%s:18000/train/cancel");


    private String url;


    public String format(String ip) {
        return String.format(this.url, ip);
    }

    public static void main(String[] args) {
        System.err.println(UrlEnum.REFUND.format("aaa"));
    }
}
