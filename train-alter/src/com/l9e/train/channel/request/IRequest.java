package com.l9e.train.channel.request;

import com.l9e.train.po.Order;
import com.l9e.train.po.Result;

public interface IRequest {

	/**
	 * 查询订单
	 * 
	 * @param orderbill
	 * @return
	 */
	public Result request(Order order, String logid);

}
