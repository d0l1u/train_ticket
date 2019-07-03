
package com.l9e.transaction.channel.request.impl;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.Result;
import com.l9e.transaction.vo.Worker;

public class ManualRequest extends RequestImpl {
	private Logger logger = Logger.getLogger(this.getClass());

	public ManualRequest(Account account, Worker worker) {
		super(account, worker);
	}

	@Override
	public Result request(Order order, String weight) {

		result.setRetValue(Result.SUCCESS);

		return result;
	}

}
