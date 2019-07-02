package com.kuyou.train.entity.req;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
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
 * BookOrderReq
 *
 * @author taokai3
 * @date 2018/11/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookReq {

    private String orderId;
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
    private boolean acceptNoSeat;

    private List<BookTicketReq> tickets;

    public List<String> ticketInfo() {
        return tickets.stream().map(ticket -> ticket.getName().toUpperCase().replaceAll("\\s","") + "#"
                + ticket.getTicketType()).sorted().collect(Collectors.toList());
    }

    public String trainInfo() {
        return String.format("%s#%s#%s#%s", trainCode, DateUtil.format(fromDate, DateFormat.DATE), fromStationCode,
                toStationCode);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
