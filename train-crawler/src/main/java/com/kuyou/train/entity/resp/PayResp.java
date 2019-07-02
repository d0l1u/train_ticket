package com.kuyou.train.entity.resp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CancelResp
 *
 * @author taokai3
 * @date 2018/11/27
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PayResp {

    @JSONField(ordinal = 1)
    private boolean success;

    @Builder.Default
    @JSONField(ordinal = 2)
    private String message = "";

    @JSONField(ordinal = 3)
    private String orderId;

    @JSONField(ordinal = 4)
    private Integer changeId;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
