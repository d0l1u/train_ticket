package com.kuyou.train.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * ChangeOrderTicketResp
 *
 * @author taokai3
 * @date 2018/11/19
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangeOrderTicketResp {
    private String cpId;
    private String name;
    private String ticketType;
    private String oldSubSequence;
    private String newSubSequence;
    private BigDecimal price;
    private String coachNo;
    private String coachName;
    private String seatNo;
    private String seatName;


    public String getCoachName() {
        return coachName == null ? "" : coachName;
    }

    public String getSeatName() {
        return seatName == null ? "" : seatName;
    }
}
