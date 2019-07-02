package com.kuyou.train.entity.req;

import com.alibaba.fastjson.JSON;
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

    private Integer changeId;

    private String orderId;

    private String username;

    private String password;

    private String sequence;

    private boolean bookFlag;

    private List<String> subSequences;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
