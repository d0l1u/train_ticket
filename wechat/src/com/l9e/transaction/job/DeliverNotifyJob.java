package com.l9e.transaction.job;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.OrderService;

@Component("deliverNotifyJob")
public class DeliverNotifyJob {
	private static final Logger logger = Logger
			.getLogger(DeliverNotifyJob.class);
	@Resource
	private OrderService orderService;

	public void deliverNotify() {

	}
}
