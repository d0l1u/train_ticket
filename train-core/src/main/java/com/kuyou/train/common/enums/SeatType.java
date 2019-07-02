package com.kuyou.train.common.enums;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.List;

/**
 * @ClassName: SeatType
 * @Description: 坐席类型映射关系
 * @author: taokai
 * @date: 2017年7月20日 下午1:32:49
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
@Getter
public enum SeatType {

    /**
     * 商务座
     */
    SHANG_WU_ZUO("0", "9", "商务座"),

    /**
     * 特等座
     */
    TE_DENG_ZUO("1", "P", "特等座"),

    /**
     * 一等座
     */
    YI_DENG_ZUO("2", "M", "一等座"),

    /**
     * 二等座
     */
    ER_DENG_ZUO("3", "O", "二等座"),

    /**
     * 高级软卧：北京-南昌
     */
    GAO_JI_RUAN_WO("4", "6", "高级软卧"),

    /**
     * 软卧
     */
    RUAN_WO("5", "4", "软卧"),

    /**
     * 硬卧
     */
    YING_WO("6", "3", "硬卧"),

    /**
     * 软座
     */
    RUAN_ZUO("7", "2", "软座"),

    /**
     * 硬座
     */
    YING_ZUO("8", "1", "硬座"),

    /**
     * 无座 动车/高铁(C,G,D)无座为二等座
     */
    GAO_TIE_WU_ZUO("9", "O", "二等座"),

    /**
     * 无座 其他无座为硬座座
     */
    QIT_TA_WU_ZUO("9", "1", "硬座"),

    /**
     * 包厢硬卧：北京-二连
     */
    BAO_XIANG_YING_WO("11", "5", "包厢硬卧"),

    /**
     * 高级动卧
     */
    GAO_JI_DONG_WO("16", "A", "高级动卧"),

    /**
     * 一等卧
     */
    YI_DENG_SHUANG_RUAN("12", "一等双软"),

    /**
     * ，二等卧
     */
    ER_DENG_SHUANG_RUAN("13", "二等双软"),


    /**
     * 动卧
     */
    DONG_WO("20", "F", "动卧"),

    /**
     * 一人软包：北京-杭州
     */
    YI_REN_RUAN_BAO("21", "H", "一人软包");


    public static final List<String> WO_PU_LIST = Lists
            .newArrayList(GAO_JI_RUAN_WO.l9e, RUAN_WO.l9e, YING_WO.l9e, BAO_XIANG_YING_WO.l9e,
                    GAO_JI_DONG_WO.l9e, GAO_JI_DONG_WO.l9e, DONG_WO.l9e);

    private String l9e;
    private String kyfw;
    private String title;

    SeatType(String l9e, String kyfw) {
        this.l9e = l9e;
        this.kyfw = kyfw;
    }

    SeatType(String l9e, String kyfw, String title) {
        this.l9e = l9e;
        this.kyfw = kyfw;
        this.title = title;
    }

    public static SeatType getKyfwByl9e(String l9e) {
        for (SeatType st : values()) {
            if (st.l9e.equals(l9e)) {
                return st;
            }
        }
        return null;
    }
}