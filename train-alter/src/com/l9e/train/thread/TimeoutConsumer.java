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
	public boolean consume(Order order, String logid) {
		TrainServiceImpl service = new TrainServiceImpl();
		String orderId = order.getOrderId();
		String changeStatus = order.getChangeStatus();
		logger.info(logid + "Orderid:" + orderId + ", changeStatus:" + changeStatus);
		IRequest request = new RequestFactory().select(orderId, changeStatus, logid);
		try {
			if (request == null) {
				logger.info(logid + "没有可用 IRequest ...");
				service.payIsManual(order, null);
				for (OrderCP cp : order.getCps()) {
					service.insertHistory(orderId, cp.getCpId(), "没有空闲机器人");
				}
				return false;
			}

			// 机器人处理订单
			logger.info(logid + "机器人处理订单...");
			Result result = request.request(order, logid);
			String retValue = result.getRetValue();
			if (Result.MANUAL.equals(retValue)) {
				logger.info(logid + "人工处理,目前不知道这一步什么作用...");
				// service.payIsManual(order, result);
			}
		} catch (Exception e) {
			logger.info(logid + "【consume() Exception】", e);
			e.printStackTrace();
		}
		return true;
	}

	@Override
	public String getObjectKeyId(Order t) {
		return t.getOrderId();
	}

}