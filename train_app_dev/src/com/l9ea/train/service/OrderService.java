package com.l9ea.train.service;

import java.util.List;

import com.l9e.train.po.OrderVo;
import com.l9e.train.po.Passenger;


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
