package com.l9e.train.channel.request.impl;

import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;

public class ManualRequest extends RequestImpl {
	
	
	public ManualRequest(Account account, Worker worker) {
		super(account, worker);
	}
	@Override
	public Result request(Order order,String weight, String logid) {
		result.setRetValue(Result.SUCCESS);
		return result;
	}
}
