package com.kuyou.train.entity.req;

import com.alibaba.fastjson.JSON;
import com.kuyou.train.common.util.DateFormat;
import com.kuyou.train.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * ChangeOrderRo
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeReq {

    private Integer changeId;
    private String orderId;
    private String sequence;

    private String username;
    private String password;

    private String trainCode;
    private Date fromDate;
    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;
    private String chooseSeats;
    private Boolean isChangeTo;

    private List<ChangeTicketReq> tickets;


    public String trainInfo() {
        if (orderId.startsWith("tn")) {
            //兼容途牛订单
            return String.format("%s#%s", trainCode, DateUtil.format(fromDate, DateFormat.DATE));
        } else {
            return String.format("%s#%s#%s#%s", trainCode, DateUtil.format(fromDate, DateFormat.DATE), fromStationCode,
                    toStationCode);
        }
    }

    public List<String> ticketInfo() {
        return tickets.stream().map(ticket -> ticket.getName().toUpperCase().replaceAll("\\s","") + "#"
                + ticket.getTicketType()).sorted().collect(Collectors.toList());
    }


    @Override

    public String toString() {
        return JSON.toJSONString(this);
    }


}
