package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.OrderVo;
import com.l9e.transaction.vo.Passenger;

/**
 * 订单接入服务
 * @author licheng
 *
 */
public interface OrderService {

	/**
	 * 查询订单所有乘客-车票信息
	 * @param orderId
	 * @return
	 */
	List<Passenger> getPassengersByOrderId(String orderId);
	
	/**
	 * 更新订单信息
	 * @param order
	 */
	void updateOrder(OrderVo order);
	
}
