package com.kuyou.train.service;

/**
 * NotifyService
 *
 * @author taokai3
 * @date 2018/11/9
 */
public interface NotifyService {

    /**
     * 预订占位通知
     *
     * @param orderId
     * @return
     */
    int updateBookOrder(String orderId);

    /**
     * 预订取消通知
     *
     * @param orderId
     * @return
     */
    int updateBookCancel(String orderId);


    /**
     * 预订支付通知
     *
     * @param orderId
     * @return
     */
    int updateBookPay(String orderId);


    /**
     * 退票通知
     *
     * @param orderId
     * @param cpId
     * @return
     */
    int updateRefund(String orderId, String cpId);

    /**
     * 改签占位
     *
     * @param changeId
     * @return
     */
    int updateChangeOrder(Integer changeId);


    /**
     * 改签取消
     *
     * @param changeId
     * @return
     */
    int updateChangeCancel(Integer changeId);

    /**
     * 改签支付
     *
     * @param changeId
     * @return
     */
    int updateChangePay(Integer changeId);
}
