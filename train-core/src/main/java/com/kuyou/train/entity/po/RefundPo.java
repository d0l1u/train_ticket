package com.kuyou.train.entity.po;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundPo {

    private String orderId;

    private String cpId;

    private String refundSeq;

    private String supplierOrderId;

    private String myOrderId;

    private String robotId;

    private String accountName;

    private String accountPwd;

    private BigDecimal buyMoney;

    private BigDecimal alterDiffMoney;

    private BigDecimal alterBuyMoney;

    private BigDecimal refundMoney;

    private BigDecimal refund12306Money;

    private String refundPercent;

    private String orderStatus;

    private Date createTime;

    private Date optionTime;

    private String optRen;

    private Date outTicketTime;

    private String outTicketBillno;

    private String trainNo;

    private String alterTrainNo;

    private String fromStation;

    private String arriveStation;

    private Date fromTime;

    private Date alterFromTime;

    private Date travelTime;

    private Date alterTravelTime;

    private Integer seatType;

    private Integer alterSeatType;

    private Integer ticketType;

    private String trainBox;

    private String alterTrainBox;

    private String seatNo;

    private String alterSeatNo;

    private Integer idsType;

    private String userIds;

    private String errorInfo;

    private String channel;

    private String returnOptlog;

    private String userName;

    private String refund12306Seq;

    private String userRemark;

    private String ourRemark;

    private String refuseReason;

    private Date verifyTime;

    private String proBak2;

    private String oldRefundSeq;

    private String level;

    private String refundType;

    private Boolean alterMyself;

    private Boolean canSwitchSupplier;

    private String supplierType;

    private String subOutTicketBillno;
}
