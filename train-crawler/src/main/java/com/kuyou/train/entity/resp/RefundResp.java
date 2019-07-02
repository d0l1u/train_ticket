package com.kuyou.train.entity.resp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * RefundResp
 *
 * @author taokai3
 * @date 2018/11/21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundResp {

    @JSONField(ordinal = 1)
    @Builder.Default
    private boolean success;
    @JSONField(ordinal = 2)
    private String message = "请求成功";

    @JSONField(ordinal = 3)
    private String orderId;

    @JSONField(ordinal = 4)
    private String cpId;

    @JSONField(ordinal = 5)
    private String refundNo;

    @JSONField(ordinal = 6)
    private BigDecimal refundMoney;

    @JSONField(ordinal = 7)
    private String fee;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
