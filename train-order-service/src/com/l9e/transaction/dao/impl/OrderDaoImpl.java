package com.l9e.transaction.dao.impl;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderDao;
import com.l9e.transaction.vo.Order;

@Repository("orderDao")
public class OrderDaoImpl extends BaseDao implements OrderDao {

	@Override
	public int updateOrder(Order order) {
		return getSqlMapClientTemplate().update("order.updateOrder", order);
	}

}
