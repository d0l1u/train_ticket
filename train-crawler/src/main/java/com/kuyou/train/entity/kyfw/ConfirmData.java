package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * confirmData
 *
 * @author taokai3
 * @date 2018/11/16
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmData {

    private Boolean submitStatus;
    private String errMsg;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
