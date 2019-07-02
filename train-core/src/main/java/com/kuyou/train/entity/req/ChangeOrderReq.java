package com.kuyou.train.entity.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * ChangeDto
 *
 * @author taokai3
 * @date 2018/11/6
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangeOrderReq {

    private Integer changeId;
    private String orderId;
    private String myOrderId;
    private String supplierOrderId;
    private String sequence;

    private String username = "";
    private String password = "";

    private String trainCode;

    @JSONField(format = "yyyy-MM-dd")
    private Date fromDate;

    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;
    private String chooseSeats;
    private Boolean isChangeTo;

    private List<ChangeOrderTicketReq> tickets;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
