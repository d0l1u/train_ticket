package com.kuyou.train.entity.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * CancelOrderReq
 *
 * @author taokai3
 * @date 2018/11/24
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelReq {

    @JSONField(ordinal = 1)
    private Integer changeId;
    
    @JSONField(ordinal = 1)
    private String orderId;

    @JSONField(ordinal = 2)
    private String supplierOrderId;

    @JSONField(ordinal = 3)
    private String username;

    @JSONField(ordinal = 4)
    private String password;

    @JSONField(ordinal = 5)
    private String sequence;

    @JSONField(ordinal = 6)
    private boolean bookFlag;

    @JSONField(ordinal = 7)
    private List<String> subSequences;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
