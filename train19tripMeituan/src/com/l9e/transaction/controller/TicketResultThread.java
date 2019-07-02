package com.l9e.transaction.controller;

import org.apache.log4j.Logger;

import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBOrderInfo;

public class TicketResultThread implements Runnable {
	private static final Logger logger = Logger.getLogger(TicketResultThread.class);
	
	DBOrderInfo orderInfo;
	OrderService orderService;
	
	public TicketResultThread(DBOrderInfo orderInfo, OrderService orderService){
		this.orderInfo = orderInfo;
		this.orderService = orderService;
	}
	
	@Override
	public void run() {// 出票结果通知
		if ("meituan".equals(orderInfo.getChannel())) {
			long start = System.currentTimeMillis();
			DBNoticeVo vo = new DBNoticeVo();
			vo.setChannel("meituan");
			vo.setOrder_id(orderInfo.getOrder_id());
			vo.setOut_notify_num(0);
			orderService.addOrderResultNotice(vo);
			logger.info("send to [美团] the out ticket result,"
					+ orderInfo.getOrder_id() + ",lose time:"
					+ (System.currentTimeMillis() - start)
					+ "ms" + ",url" + vo.getOut_notify_url());
		} 
	}
	
}
