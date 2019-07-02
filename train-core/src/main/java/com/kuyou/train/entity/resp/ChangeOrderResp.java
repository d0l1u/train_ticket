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
public class ChangeOrderResp {
    private String code;
    private String message;
    private Integer changeId;
    private String orderId;
    private String sequence;
    private String robotIp;
    @JSONField(format = "yyyy-MM-dd")
    private Date fromDate;
    private String fromStationCode;
    private String fromStationName;
    private String toStationCode;
    private String toStationName;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date fromTime;
    @JSONField(format = "yyyy-MM-dd HH:mm")
    private Date toTime;
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    private Date payLimitTime;

    /**
     * 1:高->低，0:平价，-1:低->高
     */
    private int changeType;
    private BigDecimal totalPrice;
    private List<ChangeOrderTicketResp> tickets;

    @Override
    public String toString() {
        return JSON.toJSONString(this, SerializerFeature.SortField);
    }
}
