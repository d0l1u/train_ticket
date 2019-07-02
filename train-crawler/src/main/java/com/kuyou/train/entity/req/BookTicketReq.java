package com.kuyou.train.entity.req;

import com.alibaba.fastjson.JSON;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * BookOrderTicketReq
 *
 * @author taokai3
 * @date 2018/11/20
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookTicketReq extends PassengerReq {
    private String cpId;
    private String seatType;


    @Override
    public boolean equals(Object o) {
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
