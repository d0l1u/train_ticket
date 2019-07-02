package com.kuyou.train.common.enums;

import lombok.Getter;

/**
 * CodeEnum
 *
 * @author taokai3
 * @date 2018/11/18
 */
@Getter
public enum CodeEnum {

    SUCCESS("0000", "请求成功", false),

    UNKNOWN_MESSAGE("0001", "未知错误信息", false),

    SYSTEM_EXCEPTION("0002", "系统异常", false),

    //======================= 登录
    LOGIN_CAPTCHA_CRACK_FAIL("1001", "登录打码失败", true),

    LOGIN_FAIL("1002", "登录12306失败", true),

    USERNAME_NO_EXIST("1003", "登录名不存在", false),

    USERNAME_OR_PASSWORD_ERROR("1004", "密码输入错误", false),

    USERINFO_COUNTERFEIT("1005", "您的用户信息被他人冒用，请重新在网上注册新的账户", false),

    USERINFO__WAIT_VERIFY("1006", "所属人身份信息未核验", false),

    USER_MOBILE_WAIT_VERIFY("1007", "您的手机号码尚未进行核验，目前暂无法用于登录", false),


    //=============== 未完成订单
    OTHER_NO_ORDER("2001", "未完成订单不一致", false),

    NOT_TRAVEL_ORDER_NOT_EXIST("2002", "未出行订单不存在", false),


    //================== 余票查询,
    LEFT_TICKET_EMPTY("3001", "余票查询为空", false),

    TRAIN_NOT_EXIST("3002", "车次不存在", false),

    LEFT_TICKET_NOT_ENOUGH("3003", "余票不足", false),

    TRAIN_DIAGRAM_ADJUST("3004", "列车运行图调整", false),

    TRAIN_STOP("3005", "列车停运", false),

    NO_TIME_TO_SELL("3006", "未到起售时间", false),

    //=========================== 下单
    REQUEST_ORDER_PARAMETER_EMPTY("5001", "占位参数缺失", false),

    //
    ;

    private String code;
    private String message;
    private boolean isRetry;

    CodeEnum(String code, String message, boolean isRetry) {
        this.code = code;
        this.message = message;
        this.isRetry = isRetry;
    }
}
