package com.kuyou.train.common.constant;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.List;
import java.util.Map;

/**
 * OptlogConstant
 *
 * @author taokai3
 * @date 2018/12/27
 */
public class OptlogConstant {

    /** 需要切换账号 */
    public static final List<String> SWITCH_ACCOUNT = Lists.newArrayList("A0", "A1", "A2", "A3", "A4",
            "A5", "A6", "A7", "A8", "A9", "B0", "B1", "B2", "B3", "B4", "B5", "D9", "H1", "J1");

    public static Map<String, String> STOP_MAP = null;
    static {

        /*
        停用原因:3:联系人达上限; 4:账号冒用; 6:账号不存在; 7:账号密码错误; 8:手机未核验; 11:身份信息待核验
         */
        STOP_MAP = Maps.newHashMap();
        //登录名不存在
        STOP_MAP.put("A0", "6");
        STOP_MAP.put("A1", "6");
        STOP_MAP.put("H1", "6");

        //密码错误
        STOP_MAP.put("A2", "7");
        STOP_MAP.put("A3", "7");
        STOP_MAP.put("A4", "7");
        STOP_MAP.put("J1", "7");

        //冒用
        STOP_MAP.put("A5", "4");
        STOP_MAP.put("B2", "4");
        STOP_MAP.put("B4", "4");

        //账号核验
        STOP_MAP.put("A6", "11");
        STOP_MAP.put("B5", "11");

        //手机核验
        STOP_MAP.put("A7", "8");
        STOP_MAP.put("B1", "8");
        STOP_MAP.put("B3", "8");

        //联系人达上限
        STOP_MAP.put("A8", "3");

    }
}
