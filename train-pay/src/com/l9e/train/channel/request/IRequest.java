package com.l9e.train.channel.request;

import com.l9e.train.po.Order;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;

public abstract interface IRequest {
	public abstract Result request(Order paramOrder, String logid);

	public abstract Worker getWorker();

	public abstract PayCard getPayCard();
}
