package com.kuyou.train.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CancelResp
 *
 * @author taokai3
 * @date 2018/11/26
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CancelResp {

    private boolean success;

    private String message;

    private String orderId;
    private boolean bookFlag;
    private Integer changeId;
}
