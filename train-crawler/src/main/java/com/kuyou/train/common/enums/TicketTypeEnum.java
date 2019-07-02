package com.kuyou.train.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import retrofit2.http.GET;

/**
 * TicketType
 *
 * @author taokai3
 * @date 2018/11/11
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public enum TicketTypeEnum {

    /**
     * 成人票
     */
    ADULT("ADULT"),

    /**
     * 学生票
     */
    STUDENT("0X00");

    private String value;

}
