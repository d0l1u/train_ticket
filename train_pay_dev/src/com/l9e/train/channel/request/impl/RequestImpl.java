package com.l9e.train.channel.request.impl;

import com.l9e.train.channel.request.IRequest;
import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;

public class RequestImpl implements IRequest{
	protected Account account;
	protected Worker worker;
	protected PayCard payCard;
	protected Result result;
	
	protected RequestImpl(Account account, Worker worker, PayCard payCard) {
		result = new Result();
		result.setAccount(account);
		result.setWorker(worker);
		result.setPayCard(payCard);
		
		this.account = account;
		this.worker = worker;
		this.payCard = payCard;
	}
	

	@Override
	public Result request(Order order){
		
		return null;
	}


	@Override
	public Worker getWorker() {
		return this.worker;
	}


	@Override
	public PayCard getPayCard() {
		return this.payCard;
	}


	

}
