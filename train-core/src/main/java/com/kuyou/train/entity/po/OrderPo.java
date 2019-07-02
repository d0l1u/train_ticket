package com.kuyou.train.entity.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ChangeCpPo
 *
 * @author taokai3
 * @date 2018/10/28
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderPo {

    private String orderId;

    private String myOrderId;

    private String supplierOrderId;

    private String orderName;

    private BigDecimal payMoney;

    private BigDecimal buyMoney;

    private String orderStatus;

    private Date createTime;

    private Date payTime;

    private Date outTicketTime;

    private Integer outTicketType;

    private String optRen;

    private String outTicketBillno;

    private String trainNo;

    private String fromCity;

    private String toCity;

    private Date fromTime;

    private Date toTime;

    private Date travelTime;

    private Integer seatType;

    private Integer accountId;

    private Integer workerId;

    private String outTicketAccount;

    private String bankPaySeq;

    private String errorInfo;

    private Date optionTime;

    private String channel;

    private String extSeattype;

    private String level;

    private String payType;

    private String isPay;

    private String returnOptlog;

    private String proBak2;

    private Date payLimitTime;

    private String manualOrder;

    private String waitForOrder;

    private Byte deviceType;

    private String from3c;

    private Integer accountFromWay;

    private String to3c;

    private BigDecimal ctripBxMoney;

    private String isClickButton;

    private String seatDetailType;

    private String chooseSeats;

    private Integer if12306Cutover;

    private String chooseSeatType;

    private Integer resendIdentify;

    private Integer resendNum;

    private Integer pinganStatus;

    private String supplierType;

    private Boolean canSwitchSupplier;

    private String delayOrder;

}