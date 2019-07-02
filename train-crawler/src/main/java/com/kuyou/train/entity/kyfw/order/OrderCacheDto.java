package com.kuyou.train.entity.kyfw.order;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * OrderCacheDto
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCacheDto {

    private long requestId;
    private long userId;
    private int number;
    private String tourFlag;
    private long queueOffset;
    private String queueName;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date trainDate;

    @JSONField(format = "HH:mm")
    private Date startTime;
    private String stationTrainCode;
    private String fromStationCode;
    private String fromStationName;
    private String toStationCode;
    private String toStationName;
    private int status;
    private Message message;
    private List<OrderCacheTicketDto> tickets;
    private int waitTime;
    private int waitCount;
    private int ticketCount;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date startTimeString;

    public String message() {
        return message.getMessage();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
