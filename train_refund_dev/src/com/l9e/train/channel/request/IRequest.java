package com.l9e.train.channel.request;

import java.util.List;

import com.l9e.train.po.OrderCP;
import com.l9e.train.po.ResultCP;

public interface IRequest {

	/**
	 * 查询订单
	 * @param orderbill
	 * @return
	 */
	
	
	public ResultCP request(OrderCP order, String weight);
	

}
