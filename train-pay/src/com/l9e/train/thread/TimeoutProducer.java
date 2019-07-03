package com.l9e.train.thread;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.po.Order;
import com.l9e.train.producerConsumer.distinct.DistinctProducer;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.MemcachedUtil;
import com.l9e.train.util.WorkIdNum;

public class TimeoutProducer extends DistinctProducer<Order> {

	private Logger logger = LoggerFactory.getLogger(TimeoutProducer.class);
	
	public String getObjectKeyId(Order orderbill) {
		return orderbill.getOrderId();
	}

	public List<Order> getProducts(String logid) {
		MemcachedUtil memcached = MemcachedUtil.getInstance();
		TrainServiceImpl dao = new TrainServiceImpl();
		logger.info(logid + ">>> 开始获取订单资源...");
		int ret = -1;
		List<Order> list = null;
		try {
			int num = 10;
			if (memcached.getAttribute("robot_pay_product_num") == null) {
				num = dao.queryProductNum();
				memcached.setAttribute("robot_pay_product_num", Integer.valueOf(num), 120000L);
			} else {
				num = Integer.valueOf(String.valueOf(memcached.getAttribute("robot_pay_product_num"))).intValue();
			}
			logger.info(logid + "robot_pay_product_num：" + num);

			if (memcached.getAttribute("robot_app_book_num") == null) {
				WorkIdNum.book_num = Integer.valueOf(dao.queryProductBookNum());
				memcached.setAttribute("robot_app_book_num", WorkIdNum.book_num, 300000L);
			} else {
				WorkIdNum.book_num = Integer.valueOf(String.valueOf(memcached.getAttribute("robot_app_book_num")));
			}
			logger.info(logid + "robot_app_book_num：" + WorkIdNum.book_num);

			int time = 2;
			if (memcached.getAttribute("robot_pay_new_time") == null) {
				time = dao.queryProductNewTime();
				memcached.setAttribute("robot_pay_new_time", Integer.valueOf(time), 120000L);
			} else {
				time = Integer.valueOf(String.valueOf(memcached.getAttribute("robot_pay_new_time"))).intValue();
			}
			logger.info(logid + "robot_pay_time：" + time);

			ret = dao.orderbillByList(num, time);
			if (ret == 0) {
				list = dao.getOrderbill();
			}
			logger.info(logid + "End Get Timeout List Size:" + list.size());
		} catch (Exception e) {
			logger.info(logid + "【getProducts Exception】", e);
		}
		return list;
	}
}
