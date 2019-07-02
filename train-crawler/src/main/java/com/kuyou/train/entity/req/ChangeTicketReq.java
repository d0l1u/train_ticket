package com.kuyou.train.entity.req;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * ChangeOrderTicketRo
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeTicketReq {

    /**
     * 乘客主键
     */
    private String cpId;

    /**
     * 车票号
     */
    private String oldSubSequence;

    private BigDecimal price;

    /**
     * 姓名
     */
    private String name;

    private String cardType;
    private String cardNo;

    private String ticketType;

    /**
     * 改签坐席
     */
    private String changeSeatType;


    public String getPassengerTicketStr(String seatType) {
        return String.format("%s,0,%s,%s,%s,%s,,Y_", seatType, ticketType, name, cardType, cardNo);
    }

    public String getOldPassengerStr() {
        return String.format("%s,%s,%s,%s_", name, cardType, cardNo, ticketType);
    }


    @JSONField(serialize = false)
    public String getKey() {
        return String.format("%s_%s", cardNo.toUpperCase().trim(), ticketType);
    }

}
