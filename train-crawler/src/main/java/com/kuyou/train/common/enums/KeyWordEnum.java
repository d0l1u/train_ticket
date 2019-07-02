package com.kuyou.train.common.enums;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * KeyWordEnum
 *
 * @author taokai3
 * @date 2018/11/18
 */
@Slf4j
@Getter
public enum KeyWordEnum {

    UNKNOWN_MESSAGE(CodeEnum.UNKNOWN_MESSAGE, "未知错误信息", "未知错误信息:%s"),

    //========================= 登录
    LOGIN_CAPTCHA_CRACK_FAIL(CodeEnum.LOGIN_CAPTCHA_CRACK_FAIL, "登录打码失败", "登录打码失败:%s"),

    LOGIN_FAIL(CodeEnum.LOGIN_FAIL, "登录12306失败", "登录12306失败:%s"),

    USERNAME_NO_EXIST(CodeEnum.USERNAME_NO_EXIST, "登录名不存在", ""), EMAIL_NO_EXIST(CodeEnum.USERNAME_NO_EXIST, "该邮箱不存在"),

    USERNAME_OR_PASSWORD_ERROR(CodeEnum.USERNAME_OR_PASSWORD_ERROR, "密码输入错误"), USERNAME_OR_PASSWORD_EMPTY(
            CodeEnum.USERNAME_OR_PASSWORD_ERROR, "用户名或密码为空"), USERNAME_IS_LOCK(CodeEnum.USERNAME_OR_PASSWORD_ERROR,
            "您的用户已经被锁定"),

    USERINFO_COUNTERFEIT(CodeEnum.USERINFO_COUNTERFEIT, "您的用户信息被他人冒用，请重新在网上注册新的账户"),

    USERINFO__WAIT_VERIFY(CodeEnum.USERINFO__WAIT_VERIFY, "所属人身份信息未核验"),

    USER_MOBILE_WAIT_VERIFY(CodeEnum.USER_MOBILE_WAIT_VERIFY, "您的手机号码尚未进行核验，目前暂无法用于登录"),


    //================== 未完成订单
    OTHER_NO_ORDER(CodeEnum.OTHER_NO_ORDER, "未完成订单不一致", "未完成订单不一致:%s"),

    NOT_TRAVEL_ORDER_NOT_EXIST(CodeEnum.NOT_TRAVEL_ORDER_NOT_EXIST, "未出行订单不存在", "未出行订单不存在:%s"),


    //================== 余票查询
    LEFT_TICKET_EMPTY(CodeEnum.LEFT_TICKET_EMPTY, "余票查询为空"),

    TRAIN_NOT_EXIST(CodeEnum.TRAIN_NOT_EXIST, "车次不存在"),

    LEFT_TICKET_NOT_ENOUGH(CodeEnum.LEFT_TICKET_NOT_ENOUGH, "余票不足"),

    TRAIN_DIAGRAM_ADJUST(CodeEnum.TRAIN_DIAGRAM_ADJUST, "列车运行图调整"),

    TRAIN_STOP(CodeEnum.TRAIN_STOP, "列车停运"),

    NO_TIME_TO_SELL(CodeEnum.NO_TIME_TO_SELL, "未到起售时间"),


    //========================== 下单
    REQUEST_ORDER_PARAMETER_EMPTY(CodeEnum.TRAIN_STOP, "占位参数缺失", "占位参数缺失:%s"),


    //
    ;

    private CodeEnum codeEnum;
    private String message;
    private String keyword;

    KeyWordEnum(CodeEnum codeEnum, String keyword, String message) {
        this.codeEnum = codeEnum;
        this.keyword = keyword;
        this.message = message;
    }

    KeyWordEnum(CodeEnum codeEnum, String keyword) {
        this.codeEnum = codeEnum;
        this.keyword = keyword;
    }

    public static CodeEnum getCodeByKeyword(String message) {
        CodeEnum codeEnum = null;
        KeyWordEnum[] values = KeyWordEnum.values();
        for (KeyWordEnum keyWordEnum : values) {
            if (message.contains(keyWordEnum.keyword)) {
                codeEnum = keyWordEnum.getCodeEnum();
                break;
            }
        }
        if (codeEnum == null) {
            log.info("匹配关键字失败 message:{}", message);
            codeEnum = CodeEnum.UNKNOWN_MESSAGE;
        }

        return codeEnum;
    }

    public String format(String... info) {
        return String.format(this.message, info);
    }

    @Override
    public String toString() {
        return String.format("%s:%s", this.name(), this.message);
    }
}
