package com.kuyou.train.common.status;

/**
 * AccountStatus
 *
 * @author taokai3
 * @date 2018/12/5
 */
public class AccountStatus {

    /*
-- 00:正在下单
-- 01:队列中
-- 22:账号停用
-- 33:账号空闲
-- 44:临时停用
-- 55:核验用户信息流程
-- 66:占座中
-- 77:核验手机流程
-- 78:正在核验手机
-- 79:核验手机失败
-- 90:更新白名单流程
     */
    public static final String WORK_ING = "00";
    public static final String WORK_ING2 = "66";
    public static final String QUEUE_ING = "01";
    public static final String STOP = "22";
    public static final String FREE = "33";
    public static final String TEMP_STOP = "44";
    public static final String VERIFY_USER = "55";
    public static final String VERIFY_PHONE = "77";
    public static final String VERIFY_PHONE_ING = "78";
    public static final String VERIFY_PHONE_FAIL = "79";
    public static final String UPDATE_INIT = "90";
    public static final String UPDATE_ING = "91";
    public static final String JD = "jd";
}
