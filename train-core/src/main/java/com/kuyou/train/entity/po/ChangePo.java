package com.kuyou.train.entity.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

/**
 * ChangePo
 *
 * @author taokai3
 * @date 2018/10/28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePo {

    private Integer changeId;

    private String orderId;

    private String myOrderId;

    private String supplierOrderId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    private String oldTicketChangeSerial;

    private String newTicketChangeSerial;

    private String ticketPriceDiffChangeSerial;

    private BigDecimal changeDiffMoney;

    private BigDecimal changeRefundMoney;

    private BigDecimal diffrate;

    private BigDecimal totalpricediff;

    private BigDecimal fee;

    private BigDecimal changeReceiveMoney;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date bookTicketTime;

    private String failMsg;

    private String failReason;

    private String trainNo;

    private String changeTrainNo;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date fromTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date changeFromTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date changeToTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date travelTime;

    private String fromCity;

    private String toCity;

    private String outTicketBillno;

    private String bankPaySeq;

    private Integer workerId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date payTime;

    private String accountId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date optionTime;

    private String optRen;

    private String changeStatus;

    private String isasync;

    private String callbackurl;

    private String reqtoken;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date changeTravelTime;

    private String changeNotifyStatus;

    private Integer changeNotifyCount;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date changeNotifyTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date changeNotifyFinishTime;

    private String fromStationCode;

    private String toStationCode;

    private Boolean ischangeto;

    private String merchantId;

    private Boolean hasseat;

    private Integer alterPayType;

    private String mtChangeId;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date payLimitTime;

    private String serialnumber;

    private Boolean ischooseseats;

    private String chooseseats;

    private String supplierType;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}