package com.kuyou.train.entity.resp;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * ChangeResp
 *
 * @author taokai3
 * @date 2018/11/18
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeResp {

    @JSONField(ordinal = 1)
    private String code;

    @JSONField(ordinal = 2)
    private String message;

    @JSONField(ordinal = 3)
    private Integer changeId;

    @JSONField(ordinal = 4)
    private String orderId;

    @JSONField(ordinal = 5)
    private String sequence;

    @JSONField(ordinal = 6)
    private String robotIp;

    @JSONField(ordinal = 7, format = "yyyy-MM-dd")
    private Date fromDate;

    @JSONField(ordinal = 8)
    private String fromStationCode;
    @JSONField(ordinal = 9)
    private String fromStationName;
    @JSONField(ordinal = 10)
    private String toStationCode;
    @JSONField(ordinal = 11)
    private String toStationName;

    @JSONField(ordinal = 12, format = "yyyy-MM-dd HH:mm:ss")
    private Date fromTime;

    @JSONField(ordinal = 13, format = "yyyy-MM-dd HH:mm:ss")
    private Date toTime;

    @JSONField(ordinal = 14, format = "yyyy-MM-dd HH:mm:ss")
    private Date payLimitTime;

    /**
     * 1:高->低，0:平价，-1:低->高
     */
    @JSONField(ordinal = 15)
    private int changeType;

    @JSONField(ordinal = 16)
    private BigDecimal totalPrice;

    @JSONField(ordinal = 17)
    private List<ChangeTicketResp> tickets;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.SortField);
    }
}
