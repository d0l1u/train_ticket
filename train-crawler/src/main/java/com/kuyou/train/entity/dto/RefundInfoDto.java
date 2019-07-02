package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * RefundInfoDto
 *
 * @author taokai3
 * @date 2018/11/22
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundInfoDto {

    /**
     * 总票价
     */
    @JSONField(ordinal = 1)
    private BigDecimal ticketPrice;

    /**
     * 实际退款金额
     */
    @JSONField(ordinal = 2)
    private BigDecimal returnPrice;

    /**
     * 手续费
     */
    @JSONField(ordinal = 3)
    private BigDecimal returnCost;

    /**
     * 退票手续费率
     */
    @JSONField(ordinal = 4)
    private String returnRate;

    @JSONField(ordinal = 5)
    private String refundNo;


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
