package com.l9e.train.channel.request.impl;

import com.l9e.train.channel.request.IRequest;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.Result;
import com.l9e.train.po.ResultCP;
import com.l9e.train.po.Worker;

public class RequestImpl implements IRequest {
	protected Worker worker;
	protected Result result;

	protected RequestImpl(Worker worker) {
		result = new Result();
		result.setWorker(worker);
		this.worker = worker;
	}

	@Override
	public ResultCP request(OrderCP order, String weight) {
		return null;
	}

}
