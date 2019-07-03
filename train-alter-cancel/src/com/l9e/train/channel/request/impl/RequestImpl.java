package com.l9e.train.channel.request.impl;

import com.l9e.train.channel.request.IRequest;
import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;

public class RequestImpl implements IRequest{
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
	public Result request(Order order){
		
		return null;
	}

}
