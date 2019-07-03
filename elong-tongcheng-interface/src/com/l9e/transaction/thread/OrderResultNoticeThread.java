package com.l9e.transaction.thread;

import java.util.Date;

import org.apache.log4j.Logger;

import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBOrderInfo;


public class OrderResultNoticeThread implements Runnable {

	
	private NoticeService noticeService;
	
	private OrderService orderService;

	private DBOrderInfo orderInfo ;
	
	private boolean reOrder;

	private static final Logger logger = Logger.getLogger(OrderResultNoticeThread.class);

	public OrderResultNoticeThread(DBOrderInfo orderInfo,boolean reOrder,NoticeService noticeService,OrderService orderService ) {
		this.orderInfo = orderInfo ;
		this.reOrder= reOrder;
		this.noticeService = noticeService;
		this.orderService =orderService;
	}

	@Override
	public void run() {
		Date begin = new Date();
		try {
			if("elong".equals(orderInfo.getChannel())){
				DBNoticeVo vo=new DBNoticeVo();
				vo.setChannel("elong");
				vo.setOrder_id(orderInfo.getOrder_id());
				vo.setOut_notify_num(0);
				orderService.addOrderResultNotice(vo);
			}else if("tongcheng".equals(orderInfo.getChannel())){
				if(reOrder){
					noticeService.insertRemedyNotice(orderInfo.getOrder_id());
				}else{
					DBNoticeVo vo=new DBNoticeVo();
					vo.setChannel("tongcheng");
					vo.setOrder_id(orderInfo.getOrder_id());
					vo.setOut_notify_num(0);
					orderService.addOrderResultNotice(vo);
				}
			}
			Date end = new Date();
			logger.info("通知出票结果,orderId:"+orderInfo.getOrder_id()+"耗时"+(end.getTime()-begin.getTime()));
		} catch (Exception e) {
			logger.info("通知出票结果,orderId:"+orderInfo.getOrder_id()+"出现异常");
			e.printStackTrace();
		}
	}

	
}
