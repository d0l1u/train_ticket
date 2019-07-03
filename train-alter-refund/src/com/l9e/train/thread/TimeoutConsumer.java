package com.l9e.train.thread;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.channel.request.IRequest;
import com.l9e.train.channel.request.RequestFactory;
import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.Result;
import com.l9e.train.producerConsumer.distinct.DistinctConsumer;
import com.l9e.train.service.impl.TrainServiceImpl;

public class TimeoutConsumer extends DistinctConsumer<Order> {

	private Logger logger = LoggerFactory.getLogger(TimeoutConsumer.class);

	@Override
	public boolean consume(Order orderbill) {
		TrainServiceImpl service = new TrainServiceImpl();

		try {
			// start 选择处理机器人信息
			logger.info("select request=" + orderbill.getOrderId() + " start");
			IRequest request = new RequestFactory().select(orderbill);
			if (request == null) {
				service.payIsManual(orderbill);
				logger.error(orderbill.getOrderId() + " not robot");
				for (OrderCP cp : orderbill.getCps()) {
					service.insertHistory(orderbill.getOrderId(), cp.getCpId(), "没有空闲机器人，请进行人工核对后进行处理。");
				}
				return false;
			}
			// end 选择处理机器人信息

			// start 利用处理类和其它信息进行订单的处理
			logger.info("request=" + orderbill.getOrderId() + " start");
			Result result = request.request(orderbill);
			// end

		} catch (Exception e) {
			logger.warn("exception!!:" + e);
		}
		return true;
	}

	@Override
	public String getObjectKeyId(Order t) {
		// TODO Auto-generated method stub
		return t.getOrderId();
	}

}