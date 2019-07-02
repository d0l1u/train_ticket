package com.kuyou.train.dao;

import org.apache.ibatis.annotations.Param;

/**
 * HistoryMapper
 *
 * @author taokai3
 * @date 2018/11/5
 */
public interface HistoryMapper {

    /**
     * 插入预订日志
     *
     *
     * @param opt
     * @param orderId
     * @param log
     * @return
     */
    int insertBookLog(@Param("orderId") String orderId, @Param("opt")String opt, @Param("log") String log);

    /**
     * 插入改签日志
     *
     * @param orderId
     * @param changeId
     * @param log
     * @return
     */
    int insertChangeLog(@Param("orderId") String orderId, @Param("changeId") Integer changeId,
            @Param("log") String log);

    /**
     * 插入退票日志
     *
     * @param orderId
     * @param cpId
     * @param log
     * @return
     */
    int insertRefundLog(@Param("orderId") String orderId, @Param("cpId") String cpId, @Param("log") String log);
}
