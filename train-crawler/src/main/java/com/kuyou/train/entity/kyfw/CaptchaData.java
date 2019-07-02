package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * captcha4LoginData
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CaptchaData {

    private String result_message;
    private String result_code;
    private String image;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
