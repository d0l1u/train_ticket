package com.train.robot.em;

/**
 * @ClassName: SeatType
 * @Description: 坐席类型映射关系
 * @author: taokai
 * @date: 2017年7月20日 下午1:32:49
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
public enum SeatType {

    /**
     * 商务座
     */
    BUSINESS_SEAT("0", "9", "商务座"),

    /**
     * 特等座
     */
    SUPER_SEAT("1", "P", "特等座"),

    /**
     * 一等座
     */
    FIRST_SEAT("2", "M", "一等座"),

    /**
     * 二等座
     */
    SECOND_SEAT("3", "O", "二等座"),

    /**
     * 高级软座
     */
    SENIORSOFT_SEAT("4", "6", "高级软卧"),

    /**
     * 软卧
     */
    SOFT_SLEEPER("5", "4", "软卧"),

    /**
     * 硬卧
     */
    HARD_SLEEPER("6", "3", "硬卧"),

    /**
     * 软座
     */
    SOFT_SEAT("7", "2", "软座"),

    /**
     * 硬座
     */
    HARD_SEAT("8", "1", "硬座"),

    /**
     * 无座 动车/高铁(C,G,D)无座为二等座
     */
    GAOTIE_NO_SEAT("9", "O", "二等座"),

    /**
     * 无座 其他无座为硬座座
     */
    COMMONLY_NO_SEAT("9", "1", "硬座"),

    /**
     * 包厢硬卧
     */
    HARD_SLEEPER_BOX("11", "5", ""),

    /**
     * 高级动卧
     */
    GAO_JI_DONG_WO("16", "A", ""),

    /**
     * 动卧
     */
    DONG_WO("20", "F", "");

    private String ky;
    private String hthy;
    private String title;

    SeatType(String ky, String hthy, String title) {
        this.ky = ky;
        this.hthy = hthy;
        this.title = title;
    }

    public static SeatType getByKy(String ky) {
        for (SeatType st : values()) {
            if (st.ky.equals(ky)) {
                return st;
            }
        }
        return null;
    }

    public static SeatType getByHthy(String hthy) {
        for (SeatType st : values()) {
            if (st.hthy.equals(hthy)) {
                return st;
            }
        }
        return null;
    }


    public String getKy() {
        return ky;
    }

    public void setKy(String ky) {
        this.ky = ky;
    }

    public String getHthy() {
        return hthy;
    }

    public void setHthy(String hthy) {
        this.hthy = hthy;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}