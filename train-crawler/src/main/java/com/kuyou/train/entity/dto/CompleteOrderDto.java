package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.kuyou.train.common.enums.OrderStatusEnum;
import com.kuyou.train.common.util.DateFormat;
import com.kuyou.train.common.util.DateUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * CompleteOrderDto
 *
 * @author taokai3
 * @date 2018/11/15
 */
@Data
@Builder
@AllArgsConstructor
public class CompleteOrderDto {

    private String name;
    private String sequence;
    private String subSequence;
    private String status;
    private String trainCode;
    private String innerTrainCode;
    private String statusName;
    private String idcardType;
    private String idcardNo;
    private String batchNo;
    private String coachNo;
    private String coachName;
    private String seatNo;
    private String seatName;
    private String seatTypeName;
    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;
    private int price;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date fromTime;

    @JSONField(format = "yyyy-MM-dd")
    private Date fromDate;


    public String getTicketKey(String sequence) {
        return String.format("%s,%s,%s,%s,%s#", sequence, batchNo, coachNo, seatNo,
                DateUtil.format(fromTime, DateFormat.DATE_HM));
    }


    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

    public OrderStatusEnum getStatusEnum() {
        return OrderStatusEnum.getEnum(status);
    }
}
