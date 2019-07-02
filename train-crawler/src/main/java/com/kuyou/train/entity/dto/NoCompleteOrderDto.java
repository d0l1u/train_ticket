package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuyou.train.common.enums.OrderStatusEnum;
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
 * NoCompleteOrderDto
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NoCompleteOrderDto {

    private String sequence;
    private String trainCode;
    private String innerTrainCode;
    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;

    @JSONField(format = "yyyy-MM-dd")
    private Date fromDate;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date fromTime;
    private String status;
    private int waitTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date payLimitTime;
    private int totalPrice;

    private String message;
    private List<OrderTicketDto> tickets;

    public OrderStatusEnum getStatusEnum() {
        return OrderStatusEnum.getEnum(status);
    }

    public String trainInfo() {
        return String.format("%s#%s#%s#%s", trainCode, DateUtil.format(fromDate, DateFormat.DATE), fromStationCode,
                toStationCode);
    }

    public List<String> ticketInfo() {
        return tickets.stream().map(ticket -> ticket.getName().toUpperCase().replaceAll("\\s","")+ "#"
                + ticket.getTicketType()).sorted().collect(Collectors.toList());
    }

    public List<String> ticketSeqInfo() {
        return tickets.stream().map(OrderTicketDto::getSubSequence).distinct().sorted().collect(Collectors.toList());
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
