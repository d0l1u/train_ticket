package com.train.system.center.service;

/**
 * HistoryService
 *
 * @author taokai3
 * @date 2018/6/25
 */
public interface HistoryService {

    int insertBookingHistory(String orderId, String message, String operator);

	int insertRefundHistory(String orderId, String cpId, String message, String operator);
	int insertChangeOrderHistory(String orderId, String cpId, String message, String operator);
}
