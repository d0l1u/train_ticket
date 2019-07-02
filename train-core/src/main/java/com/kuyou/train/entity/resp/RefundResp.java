package com.kuyou.train.entity.resp;

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

    private boolean success;
    private String message;
    private String orderId;
    private String cpId;
    private String refundNo;
    private BigDecimal refundMoney;
    private String fee;

}
