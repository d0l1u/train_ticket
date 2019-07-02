package com.kuyou.train.common.enums;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.EnumSet;
import java.util.List;

/**
 * CardType
 *
 * @author taokai3
 * @date 2018/7/5
 */
@Getter
public enum CardTypeEnum {

    /** 二代身份证 */
    SHEN_FEN("2", "1","二代身份证"),

    /** 台湾通行证 */
    TAI_BAO("4", "G","台湾通行证"),

    /** 护照 */
    HU_ZHAO("5", "B","护照"),

    /** 港澳通行证 */
    GANG_AO("3", "C","港澳通行证"),

    /** 外国人永久居留身份证 */
    WAI_GUO_JU_ZHU("6", "H","外国人永久居留身份证"),

    /** 港澳台居民居住证 */
    GANG_AO_TAI_JU_ZHU("7", "1","港澳台居民居住证");


    /**
     * 中国居民身份证
     */
    public static final EnumSet<CardTypeEnum> TWO_OPEN = EnumSet.of(SHEN_FEN, GANG_AO_TAI_JU_ZHU, WAI_GUO_JU_ZHU);


    /**
     * 身份证通过核验状态:中国居民身份证(一代身份证、二代身份证)，港澳台居民居住证，外国人永久居留身份证
     */
    public static final List<String> TWO_OPEN_STATUS_LIST = Lists.newArrayList("93", "95", "97", "99");

    /**
     * 其他通过核验状态: 除去 TWO_OPEN_LIST
     */
    public static final List<String> OTHER_OPEN_STATUS_LIST = Lists.newArrayList("91", "93", "98", "99", "95", "97");


    private String ky;
    private String kyfw;
    private String title;


    public static CardTypeEnum getByKyfw(String kyfw) {
        for (CardTypeEnum st : values()) {
            if (st.kyfw.equals(kyfw)) {
                return st;
            }
        }
        return null;
    }

    public static CardTypeEnum getByKy(String ky) {
        for (CardTypeEnum st : values()) {
            if (st.ky.equals(ky)) {
                return st;
            }
        }
        return null;
    }

    CardTypeEnum(String ky, String kyfw, String title) {
        this.ky = ky;
        this.kyfw = kyfw;
        this.title = title;
    }

    public boolean canBuyTicket(String status) {
        if (TWO_OPEN.contains(this)) {
            if (TWO_OPEN_STATUS_LIST.contains(status)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (OTHER_OPEN_STATUS_LIST.contains(status)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
