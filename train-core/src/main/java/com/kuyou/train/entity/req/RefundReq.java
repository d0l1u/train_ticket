package com.kuyou.train.entity.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * RefundReq
 *
 * @author taokai3
 * @date 2018/11/21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundReq {

    @JSONField(ordinal = 1)
    private String orderId;

    @JSONField(ordinal = 2)
    private String myOrderId;

    @JSONField(ordinal = 2)
    private String supperOrderId;

    @JSONField(ordinal = 3)
    private String cpId;

    @JSONField(ordinal = 4)
    private String username;

    @JSONField(ordinal = 5)
    private String password;

    @JSONField(ordinal = 6)
    private String sequence;

    @JSONField(ordinal = 7)
    private String name;

    @JSONField(ordinal = 8)
    private String subSequence;

    @JSONField(ordinal = 9)
    private String cardNo;

    @JSONField(ordinal = 10)
    private String cardType;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
