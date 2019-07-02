package com.kuyou.train.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * RefundPo
 *
 * @author taokai3
 * @date 2018/11/9
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UnderRefundPo {

    private String cpId;

    private String myOrderId;

    private String supplierOrderId;

    private String orderId;

    private String subSequence;

    private String name;

    private String cardNo;

    private String cardType;

    private String sequence;

    private Date refundTime;

    private Date createTime;

    private Date modifyTime;

    private Date finishTime;

    private String status;

    private BigDecimal refundMoney;

    private String refundType;

    private String supplierType;

    private String operater;
}
