package com.kuyou.train.entity.resp;

import com.alibaba.fastjson.annotation.JSONField;
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
public class ChangeTicketResp {

    @JSONField(ordinal = 1)
    private String cpId;

    @JSONField(ordinal = 2)
    private String name;

    @JSONField(ordinal = 3)
    private String ticketType;

    @JSONField(ordinal = 4)
    private String oldSubSequence;

    @JSONField(ordinal = 5)
    private String newSubSequence;

    @JSONField(ordinal = 6)
    private BigDecimal price;

    @JSONField(ordinal = 7)
    private String coachNo;
    @JSONField(ordinal = 7)
    private String coachName;

    @JSONField(ordinal = 8)
    private String seatNo;
    @JSONField(ordinal = 8)
    private String seatName;
}
