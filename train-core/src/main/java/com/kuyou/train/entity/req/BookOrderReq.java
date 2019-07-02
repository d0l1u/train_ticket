package com.kuyou.train.entity.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * BookOrderReq
 *
 * @author taokai3
 * @date 2018/11/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookOrderReq {

    private String orderId;
    private String myOrderId;
    private String username;
    private String password;
    private String trainCode;
    @JSONField(format = "yyyy-MM-dd")
    private Date fromDate;
    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;
    private String chooseSeats;
    private boolean acceptNoSeat;

    private List<BookOrderTicketReq> tickets;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
