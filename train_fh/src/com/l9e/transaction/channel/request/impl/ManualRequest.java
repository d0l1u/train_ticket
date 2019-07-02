
package com.l9e.transaction.channel.request.impl;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.Result;
import com.l9e.transaction.vo.Worker;

public class ManualRequest extends RequestImpl {
	private Logger logger=Logger.getLogger(this.getClass());
	
	
	public ManualRequest(Account account, Worker worker) {
		// TODO Auto-generated constructor stub
		super(account, worker);
	}
	@Override
	public Result request(Order order,String weight) {
		// TODO Auto-generated method stub
		
		result.setRetValue(Result.SUCCESS);
		
		return result;
	}

	

}
