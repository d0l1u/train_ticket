package com.kuyou.train.entity.resp;

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
public class BookOrderResp {

    private Boolean success;
    private String message;
    private String orderId;
    private String robotIp;
    private String sequence;
    private BigDecimal totalPrice;

    private String trainCode;
    @JSONField(format = "yyyy-MM-dd")
    private Date fromDate;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date fromTime;

    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date toTime;

    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date payLimitTime;

    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;

    private List<BookOrderTicketResp> tickets;

}
