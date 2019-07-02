package com.kuyou.train.entity.kyfw.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * OrderCacheTicketDto
 *
 * @author taokai3
 * @date 2018/11/12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderCacheTicketDto {

    private String seatTypeName;
    private String seatTypeCode;
    private String ticketTypeName;
    private String passengerName;
    private String passengerIdTypeName;
}
