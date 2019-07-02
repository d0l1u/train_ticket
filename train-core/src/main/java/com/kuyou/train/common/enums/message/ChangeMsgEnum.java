package com.kuyou.train.common.enums.message;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * ChangeMsgEnum
 *
 * @author taokai3
 * @date 2018/11/27
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum ChangeMsgEnum {

    YU_PIAO_BU_ZU("301", "余票不足"),


    //
    ;

    private String code;
    private String message;

}
