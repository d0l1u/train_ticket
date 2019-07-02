package com.l9e.train.channel.request;

import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;

public interface IRequest {

	/**
	 * 查询订单
	 * @param orderbill
	 * @return
	 */
	
	
	Result request(Order order);
	
	Worker getWorker();
	
	PayCard getPayCard();
}
