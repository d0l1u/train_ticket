package com.kuyou.train.entity.po;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
public class OrderCpPo {

    private String cpId;

    private String orderId;

    private String userName;

    private Integer ticketType;

    private Integer certType;

    private String certNo;

    private String telephone;

    private String createTime;

    private String payMoney;

    private String buyMoney;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date modifyTime;

    private Integer seatType;

    private String trainBox;

    private String seatNo;

    private String checkStatus;

    private String ctripBxPrice;

    private Byte isWhiteList;

    private String subOutticketBillno;

    @JSONField(format = "yyyy-MM-dd")
    private Date validDateEnd;

    @JSONField(format = "yyyy-MM-dd")
    private Date borthDate;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}