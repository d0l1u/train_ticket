package com.l9e.train.channel.request.impl;

import com.l9e.train.channel.request.IRequest;
import com.l9e.train.po.Order;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;

public class RequestImpl implements IRequest {
	protected Worker worker;
	protected PayCard payCard;
	protected Result result;

	protected RequestImpl(Worker worker, PayCard payCard) {
		result = new Result();
		result.setWorker(worker);
		this.worker = worker;
		this.payCard = payCard;
	}

	@Override
	public Result request(Order order, String logid) {
		return null;
	}
}
