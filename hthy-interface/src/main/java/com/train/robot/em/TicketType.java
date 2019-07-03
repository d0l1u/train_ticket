package com.train.robot.em;

/**
 * @ClassName: TicketType
 * @Description: 车票类型映射关系
 * @author: taokai
 * @date: 2017年7月20日 下午1:28:11
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
public enum TicketType {

    /**
     * 成人票
     */
    CHE_NGREN("0", "1", ""),

    /**
     * 儿童票
     */
    ER_TONG("1", "2", ""),

    /**
     * 学生票
     */
    XUE_SHENG("3", "3", ""),

    /**
     * 残军票
     */
    CAN_JUN("2", "4", "");

    private String ky;
    private String hthy;
    private String title;

    TicketType(String ky, String hthy, String title) {
        this.ky = ky;
        this.hthy = hthy;
        this.title = title;
    }

    public static TicketType getByKy(String ky) {
        for (TicketType p : values()) {
            if (p.ky.equals(ky)) {
                return p;
            }
        }
        return null;
    }


    public static TicketType getByHthy(String hthy) {
        for (TicketType p : values()) {
            if (p.hthy.equals(hthy)) {
                return p;
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
