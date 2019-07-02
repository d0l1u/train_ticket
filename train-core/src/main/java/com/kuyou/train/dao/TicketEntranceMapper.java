package com.kuyou.train.dao;

import org.apache.ibatis.annotations.Param;

/**
 * TicketEntranceMapper
 *
 * @author taokai3
 * @date 2018/11/9
 */
public interface TicketEntranceMapper {

    /**
     * 插入预订支付检票口
     *
     * @param orderId
     * @param trainNo
     * @param fromCity
     * @param ticketEntrance
     * @return
     */
    int insertBook(@Param("orderId") String orderId, @Param("trainNo") String trainNo,
            @Param("fromCity") String fromCity, @Param("ticketEntrance") String ticketEntrance);

    /**
     * 插入改签支付检票口
     *
     * @param orderId
     * @param changeId
     * @param trainNo
     * @param fromCity
     * @param ticketEntrance
     * @return
     */
    int insertChange(@Param("orderId") String orderId, @Param("changeId") Integer changeId,
            @Param("trainNo") String trainNo, @Param("fromCity") String fromCity,
            @Param("ticketEntrance") String ticketEntrance);
}
