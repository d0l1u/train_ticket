package com.kuyou.train.entity.resp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * BookOrderTicketResp
 *
 * @author taokai3
 * @date 2018/11/20
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookOrderTicketResp {

    private String cpId;
    private String name;
    private String subSequence;
    private BigDecimal price;
    private String coachNo;
    private String coachName;
    private String seatNo;
    private String seatName;
}
