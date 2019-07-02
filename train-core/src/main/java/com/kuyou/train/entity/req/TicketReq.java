package com.kuyou.train.entity.req;

import com.alibaba.fastjson.JSON;
import lombok.*;

/**
 * TicketReq
 *
 * @author taokai3
 * @date 2018/12/13
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketReq {

    private String fromDate;
    private String fromStationName;
    private String fromStationCode;
    private String toStationName;
    private String toStationCode;
    private String ticketType;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
