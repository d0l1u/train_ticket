package com.l9e.transaction.dao;

import com.l9e.transaction.vo.Order;

/**
 * 订单持久接口
 * @author licheng
 *
 */
public interface OrderDao {

	/**
	 * 更新订单信息
	 * @param order
	 */
	int updateOrder(Order order);
}
