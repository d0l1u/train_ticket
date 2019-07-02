package com.kuyou.train.entity.dto;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ConfirmDto
 *
 * @author taokai3
 * @date 2018/11/16
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmDto {

    private String isAsync;
    private String tourFlag;
    private String submitToken;
    private String innerTrainCode;
    private String trainCode;
    private String fromStationCode;
    private String toStationCode;
    private String leftTicket;
    private String purposeCodes;
    private String trainLocation;
    private String keyCheckIsChange;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
