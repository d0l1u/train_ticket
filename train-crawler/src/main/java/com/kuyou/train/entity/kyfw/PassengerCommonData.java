package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import lombok.*;

/**
 * PassengerKyfwData
 *
 * @author liujia33
 * @date 2018/9/29
 */
@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PassengerCommonData {

    private Boolean flag;

    private String message;

    /**
     * 是否存在错误
     */
    private String existError;

    /**
     * 信息错误
     */
    private String MessageInfo;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
