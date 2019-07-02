package com.kuyou.train.entity.kyfw;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderWaitData
 *
 * @author taokai3
 * @date 2018/11/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderWaitData {

    private boolean queryOrderWaitTimeStatus;
    private int count;
    private int waitTime;
    private long requestId;
    private int waitCount;
    private String tourFlag;
    private String errorcode;
    private String msg;
    private String orderId;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
