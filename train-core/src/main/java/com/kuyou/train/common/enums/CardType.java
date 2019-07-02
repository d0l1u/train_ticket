package com.kuyou.train.common.enums;

import lombok.Getter;

/**
 * CardType
 *
 * @author taokai3
 * @date 2018/7/5
 */
@Getter
public enum CardType {

    /**
     * 二代身份证
     */
    SHEN_FEN("2", "1", "二代身份证"),

    /**
     * 台湾通行证
     */
    TAI_BAO("4", "G", "台湾通行证"),

    /**
     * 护照
     */
    HU_ZHAO("5", "B", "护照"),

    /**
     * 港澳通行证
     */
    GANG_AO("3", "C", "港澳通行证"),

    /**
     * 外国人永久居留身份证
     */
    WAI_GUO_JU_ZHU("6", "H", "外国人永久居留身份证"),

    /**
     * 港澳台居民居住证
     */
    GANG_AO_TAI_JU_ZHU("7", "1", "港澳台居民居住证");

    private String ky;
    private String kyfw;
    private String title;


    public static CardType getByKyfw(String kyfw) {
        for (CardType st : values()) {
            if (st.kyfw.equals(kyfw)) {
                return st;
            }
        }
        return null;
    }

    public static CardType getByKy(String ky) {
        for (CardType st : values()) {
            if (st.ky.equals(ky)) {
                return st;
            }
        }
        return null;
    }


    CardType(String ky, String kyfw, String title) {
        this.ky = ky;
        this.kyfw = kyfw;
        this.title = title;
    }


}
