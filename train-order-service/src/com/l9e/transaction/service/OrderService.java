package com.l9e.transaction.service;

import com.l9e.transaction.vo.Order;

/**
 * 订单业务接口
 * @author licheng
 *
 */
public interface OrderService {

	/**
	 * 修改订单信息
	 * @param order
	 */
	Boolean updateOrder(Order order);
	
}
