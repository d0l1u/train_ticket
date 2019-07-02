package com.kuyou.train.service;

/**
 * TicketEntranceService
 *
 * @author taokai3
 * @date 2018/11/9
 */
public interface TicketEntranceService {

    /**
     * 插入预订支付检票口
     *
     * @param orderId
     * @param trainNo
     * @param fromCity
     * @param ticketEntrance
     * @return
     */
    int insertBook(String orderId, String trainNo, String fromCity, String ticketEntrance);


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
    int insertChange(String orderId, Integer changeId, String trainNo, String fromCity, String ticketEntrance);
}
