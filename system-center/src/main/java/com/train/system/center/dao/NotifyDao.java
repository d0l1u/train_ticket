package com.train.system.center.dao;

import org.apache.ibatis.annotations.Param;

/**
 * NotifyDao
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface NotifyDao {

	int updateBeginByOrderId(@Param("orderId") String orderId);

	int updateRefund(@Param("orderId") String orderId, @Param("cpId") String cpId);
}
