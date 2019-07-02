package com.kuyou.train.entity.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuyou.train.entity.resp.ChangeOrderTicketResp;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeCpPo {
    private String cpId;
    private String newCpId;
    private String orderId;
    private Integer changeId;
    private BigDecimal buyMoney;
    private BigDecimal fee;
    private BigDecimal diffrate;
    private BigDecimal changeBuyMoney;
    private String tcSeatType;
    private String seatType;
    private String changeSeatType;
    private String tcTicketType;
    private String ticketType;
    private String trainBox;
    private String changeTrainBox;
    private String tcChangeSeatType;
    private String seatNo;
    private String changeSeatNo;
    private String tcIdsType;
    private String tnSeatType;
    private String tnTicketType;
    private String tnChangeSeatType;
    private String tnIdsType;
    private String idsType;
    private String userIds;
    private String userName;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    private String isChanged;
    private String mtChangeSeatType;
    private String subSequence;
    private String oldSubSequence;

    public ChangeCpPo(ChangeOrderTicketResp ticket, Integer changeId) {
        this.changeBuyMoney = ticket.getPrice();
        this.changeTrainBox = ticket.getCoachName();
        this.changeSeatNo = ticket.getSeatName();
        this.subSequence = ticket.getNewSubSequence();
        //this.newCpId = ticket.getCpId();
        //this.changeId = changeId;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public static void main(String[] args) {
        System.err.println(new BigDecimal(600).divide(new BigDecimal(100)).setScale(2, BigDecimal.ROUND_HALF_UP));
    }
}