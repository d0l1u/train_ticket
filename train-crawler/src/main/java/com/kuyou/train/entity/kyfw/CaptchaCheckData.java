package com.kuyou.train.entity.kyfw;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * CaptchaCheckData
 *
 * @author sunshijun
 * @date 2018/9/29
 */

@Getter
@Setter
@ToString
public class CaptchaCheckData {

    /**
     * 用于透传打码坐标
     */
    private String code;

    /**
     * 打码结果
     * */
    private String result;
}
