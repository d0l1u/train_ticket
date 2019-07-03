
package com.l9e.transaction.channel.request.impl;

import com.l9e.transaction.channel.request.IRequest;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.Result;
import com.l9e.transaction.vo.Worker;

public class RequestImpl implements IRequest {
	protected Account account;
	protected Worker worker;
	protected Result result;

	protected RequestImpl(Account account, Worker worker) {
		// TODO Auto-generated constructor stub
		result = new Result();
		result.setAccount(account);
		result.setWorker(worker);

		this.account = account;
		this.worker = worker;
	}

	@Override
	public Result request(Order order, String weight) {

		return null;
	}

	@Override
	public Account getAccount() {
		return account;
	}

	@Override
	public Worker getWorker() {
		return worker;
	}

}
