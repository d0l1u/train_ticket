package com.kuyou.train.dao;

import org.apache.ibatis.annotations.Param;

/**
 * ChangeCpMapper
 *
 * @author taokai3
 * @date 2018/10/28
 */
public interface NotifyMapper {

    /**
     * 预订占位通知
     *
     * @param orderId
     * @return
     */
    int updateBookOrder(@Param("orderId") String orderId);


    /**
     * 预订支付通知
     *
     * @param orderId
     * @return
     */
    int updateBookPay(@Param("orderId") String orderId);


    /**
     * 退票通知
     *
     * @param orderId
     * @param cpId
     * @return
     */
    int updateRefund(@Param("orderId") String orderId, @Param("cpId") String cpId);

    /**
     * 预订取消通知
     *
     * @param orderId
     * @return
     */
    int updateBookCancel(@Param("orderId") String orderId);

    /**
     * 改签占位通知
     *
     * @param changeId
     * @return
     */
    int updateChangeOrder(@Param("changeId") Integer changeId);

    /**
     * 改签取消通知
     *
     * @param changeId
     * @return
     */
    int updateChangeCancel(@Param("changeId") Integer changeId);

    /**
     * 改签支付通知
     *
     * @param changeId
     * @return
     */
    int updateChangePay(@Param("changeId") Integer changeId);
}