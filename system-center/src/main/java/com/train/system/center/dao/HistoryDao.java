package com.train.system.center.dao;

import org.apache.ibatis.annotations.Param;

/**
 * HistoryDao
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface HistoryDao {

    int insertBookingHistory(@Param("orderId") String orderId, @Param("message") String message,
            @Param("operator") String operator);
    
    
    int insertRefundHistory(@Param("orderId") String orderId, @Param("cpId") String cpId, @Param("message") String message,
            @Param("operator") String operator);


	int insertChangeOrderHistory(@Param("orderId") String orderId, @Param("cpId") String cpId, @Param("message") String message,
            @Param("operator") String operator);

}
