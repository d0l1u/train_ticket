package com.kuyou.train.common.enums;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

/**
 * @ClassName: SeatType
 * @Description: 坐席类型映射关系
 * @author: taokai
 * @date: 2017年7月20日 下午1:32:49
 * @Copyright: 2017 www.19e.cn Inc. All rights reserved.
 */
@Slf4j
@Getter
public enum SeatTypeEnum {

    /**
     * 商务座
     */
    SHANG_WU_ZUO("0", "9", 1),

    /**
     * 特等座
     */
    TE_DENG_ZUO("1", "P", 2),

    /**
     * 一等座
     */
    YI_DENG_ZUO("2", "M", 7),

    /**
     * 二等座
     */
    ER_DENG_ZUO("3", "O", 8),

    /**
     * 高级软卧：北京-南昌-Z133
     */
    GAO_JI_RUAN_WO("4", "6", 5),

    /**
     * 软卧
     */
    RUAN_WO("5", "4", 10),

    /**
     * 硬卧
     */
    YING_WO("6", "3", 11),

    /**
     * 软座
     */
    RUAN_ZUO("7", "2", 12),

    /**
     * 硬座
     */
    YING_ZUO("8", "1", 13),

    WU_ZUO("9", "无"),

    /**
     * 包厢硬卧：北京-二连-K23
     */
    BAO_XIANG_YING_WO("11", "5", 4),

    /**
     * 高级动卧: 上海虹桥站—深圳北-D941
     */
    GAO_JI_DONG_WO("16", "A", 6),

    /**
     * 动卧
     */
    DONG_WO("20", "F", 9),

    /**
     * 一人软包：北京-杭州-T31
     */
    YI_REN_RUAN_BAO("21", "H", 3);


    public static final List<String> WO_PU_LIST = Lists
            .newArrayList(GAO_JI_RUAN_WO.ky, RUAN_WO.ky, YING_WO.ky, BAO_XIANG_YING_WO.ky, GAO_JI_DONG_WO.ky,
                    GAO_JI_DONG_WO.ky, DONG_WO.ky);

    private String ky;
    private int sort;
    private String kyfw;

    SeatTypeEnum(String ky, String kyfw) {
        this.ky = ky;
        this.kyfw = kyfw;
    }

    SeatTypeEnum(String ky, String kyfw, int sort) {
        this.ky = ky;
        this.sort = sort;
        this.kyfw = kyfw;
    }

    public static SeatTypeEnum getByKy(String ky) {
        for (SeatTypeEnum st : values()) {
            if (st.ky.equals(ky)) {
                return st;
            }
        }
        return null;
    }

    public static SeatTypeEnum getByKyfw(String kyfw) {
        for (SeatTypeEnum st : values()) {
            if (st.kyfw.equals(kyfw)) {
                return st;
            }
        }
        return null;
    }

    /**
     * 计算无座
     *
     * @param seats
     * @return
     */
    public static SeatTypeEnum getWuZuoSources(String seats) {
        char[] seatCharArr = seats.toCharArray();
        int[] indexArr = new int[(int) 'z' + 1];
        //最大次数
        int maxNum = 0;
        //最大数
        char maxChar = '@';
        List<SeatTypeEnum> seatTypeEnums = Lists.newArrayList();
        for (char charStr : seatCharArr) {
            indexArr[charStr] = indexArr[charStr] + 1;
            if (indexArr[charStr] > maxNum) {
                maxNum = indexArr[charStr];
                maxChar = charStr;
            } else if (indexArr[charStr] == maxNum) {
                maxNum = indexArr[charStr];
                maxChar = '@';
            }
            seatTypeEnums.add(SeatTypeEnum.getByKyfw(String.valueOf(charStr)));
        }

        if ('@' != maxChar) {
            return SeatTypeEnum.getByKyfw(String.valueOf(maxChar));
        }

        seatTypeEnums.sort(new Comparator<SeatTypeEnum>() {
            @Override
            public int compare(SeatTypeEnum o1, SeatTypeEnum o2) {
                return o2.getSort() - o1.getSort();
            }
        });
        return seatTypeEnums.get(0);
    }


    @Override
    public String toString() {
        return "{\"name\":\"" + this.name() + "\", \"sort\":" + sort + ", \"kyfw\":" + kyfw + "}";
    }
}