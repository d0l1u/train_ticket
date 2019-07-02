package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RequestData
 *
 * @author taokai3
 * @date 2018/11/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestData {

    private String existError;
    private String errorMsg;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
