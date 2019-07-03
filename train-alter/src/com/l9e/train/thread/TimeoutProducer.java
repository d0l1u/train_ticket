package com.l9e.train.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.Order;
import com.l9e.train.producerConsumer.distinct.DistinctProducer;
import com.l9e.train.service.impl.TrainServiceImpl;

public class TimeoutProducer extends DistinctProducer<Order> {

	private Logger logger = LoggerFactory.getLogger(TimeoutProducer.class);

	@Override
	public String getObjectKeyId(Order orderbill) {
		return orderbill.getOrderId();
	}

	@Override
	public List<Order> getProducts() {

		TrainServiceImpl dao = new TrainServiceImpl();

		logger.info("alert_get Alter list start");

		// 查询需要发送的类
		int ret = -1;
		List<Order> list = null;
		try {
			int num = 10;// 改签机器人一次处理订单量
			logger.info("one time Alter orders: " + num);
			ret = dao.orderbillByList(num);
			if (ret == 0) {
				list = dao.getOrderbill();
			}
			logger.info("end get Timeout list:" + list.size());
		} catch (Exception e) {
			logger.error("getProducts Repate exception" + e);
			e.printStackTrace();
		}

		return list;
	}

}
