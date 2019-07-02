package com.kuyou.train.service;

/**
 * HistoryService
 *
 * @author taokai3
 * @date 2018/11/5
 */
public interface HistoryService {

    /**
     * 插入预订日志
     *
     * @param orderId
     * @param log
     * @return
     */
    int insertBookLog(String orderId, String log);

    /**
     * 插入日志
     * @param orderId
     * @param opt
     * @param log
     * @return
     */
    int insertBookLog(String orderId, String opt, String log);

    /**
     * 插入改签日志
     *
     * @param orderId
     * @param changeId
     * @param log
     * @return
     */
    int insertChangeLog(String orderId, Integer changeId, String log);

    /**
     * 插入退票日志
     *
     * @param orderId
     * @param cpId
     * @param log
     * @return
     */
    int insertRefundLog(String orderId, String cpId, String log);


}
