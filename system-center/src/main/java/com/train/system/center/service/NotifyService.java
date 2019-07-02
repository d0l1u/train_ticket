package com.train.system.center.service;

/**
 * NotifyService
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface NotifyService {

	int updateBeginByOrderId(String orderId);

	int updateRefund(String orderId, String cpId);

}
