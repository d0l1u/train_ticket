package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * NoCompleteOrderTicketDto
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderTicketDto {

    private String name;
    private String cardType;
    private String cardNo;
    private String seatType;
    private String subSequence;
    private String coachNo;
    private String coachName;
    private String seatNo;
    private String seatName;
    private String ticketType;
    private Integer price;

    @JSONField(serialize = false)
    public String getKey() {
        return String.format("%s_%s", cardNo.toUpperCase().trim(), ticketType);
    }
}
