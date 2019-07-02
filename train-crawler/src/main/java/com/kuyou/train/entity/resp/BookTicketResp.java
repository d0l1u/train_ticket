package com.kuyou.train.entity.resp;

import com.alibaba.fastjson.annotation.JSONField;
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
public class BookTicketResp {

    @JSONField(ordinal = 1)
    private String cpId;

    @JSONField(ordinal = 2)
    private String name;

    @JSONField(ordinal = 3)
    private String subSequence;

    @JSONField(ordinal = 4)
    private BigDecimal price;

    @JSONField(ordinal = 5)
    private String coachNo;

    @JSONField(ordinal = 6)
    private String coachName;

    @JSONField(ordinal = 7)
    private String seatNo;

    @JSONField(ordinal = 8)
    private String seatName;

}
