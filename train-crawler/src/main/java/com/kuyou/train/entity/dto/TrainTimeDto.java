package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * TrainTimeDto
 *
 * @author taokai3
 * @date 2018/11/15
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TrainTimeDto {

    @JSONField(ordinal = 1)
    private String trainCode;

    @JSONField(ordinal = 2)
    private String fromStationName;

    @JSONField(ordinal = 3)
    private String toStationName;

    @JSONField(ordinal = 4, format = "yyyy-MM-dd HH:mm")
    private Date fromTime;

    @JSONField(ordinal = 5, format = "yyyy-MM-dd HH:mm")
    private Date toTime;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
