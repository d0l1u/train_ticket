package com.l9e.transaction.channel.request;

import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.Result;
import com.l9e.transaction.vo.Worker;

public interface IRequest {

	/**
	 * 查询订单
	 * @param orderbill
	 * @return
	 */
	
	
	public Result request(Order order,String weight);
	
	public  Account getAccount();
	
	public Worker getWorker();
}
