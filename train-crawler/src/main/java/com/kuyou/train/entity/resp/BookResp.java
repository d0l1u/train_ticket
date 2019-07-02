package com.kuyou.train.entity.resp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * BookOrderResp
 *
 * @author taokai3
 * @date 2018/11/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookResp {

    @JSONField(ordinal = 1)
    private boolean success;

    @JSONField(ordinal = 2)
    private String message;

    @JSONField(ordinal = 3)
    private String orderId;

    @JSONField(ordinal = 4)
    private String sequence;

    @JSONField(ordinal = 5)
    private BigDecimal totalPrice;

    @JSONField(ordinal = 6)
    private String trainCode;

    @JSONField(ordinal = 7, format = "yyyy-MM-dd")
    private Date fromDate;

    @JSONField(ordinal = 8, format = "yyyy-MM-dd HH:mm")
    private Date fromTime;

    @JSONField(ordinal =  9, format = "yyyy-MM-dd HH:mm")
    private Date toTime;

    @JSONField(ordinal = 10, format = "yyyy-MM-dd HH:mm:ss")
    private Date payLimitTime;

    @JSONField(ordinal = 11)
    private String fromStationName;

    @JSONField(ordinal = 12)
    private String fromStationCode;

    @JSONField(ordinal = 13)
    private String toStationName;

    @JSONField(ordinal = 14)
    private String toStationCode;

    @JSONField(ordinal = 15)
    private List<BookTicketResp> tickets;

    @JSONField(ordinal = 16)
    private String robotIp;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
