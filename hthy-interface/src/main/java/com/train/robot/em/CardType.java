package com.train.robot.em;

/**
 * CardType
 *
 * @author taokai3
 * @date 2018/7/5
 */
public enum  CardType {

    /** 二代身份证 */
    SHEN_FEN("2", "1", "二代身份证"),

    /** 台湾通行证 */
    TAI_BAO("4", "G", "台湾通行证"),

    /** 护照 */
    HU_ZHAO("5", "B", "护照"),

    /** 港澳通行证 */
    GANG_AO("3", "C", "港澳通行证");

    private String ky;
    private String hthy;
    private String title;


    public static CardType getByHthy(String hthy) {
        for (CardType st : values()) {
            if (st.hthy.equals(hthy)) {
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


    CardType(String ky, String hthy, String title) {
        this.ky = ky;
        this.hthy = hthy;
        this.title = title;
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
