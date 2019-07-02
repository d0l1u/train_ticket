package com.l9e.transaction.controller;

import org.apache.log4j.Logger;

import com.l9e.common.Consts;
import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBOrderInfo;

public class BookResultThread implements Runnable {
	private static final Logger logger = Logger.getLogger(BookResultThread.class);
	
	DBOrderInfo orderInfo;
	NoticeService noticeService;
	OrderService orderService;
	
	public BookResultThread(DBOrderInfo orderInfo, NoticeService noticeService, OrderService orderService){
		this.orderInfo = orderInfo;
		this.noticeService = noticeService;
		this.orderService = orderService;
	}
	
	@Override
	public void run() {
		if ("meituan".equals(orderInfo.getChannel())) {
			String book_notify = noticeService
					.selectBookNoticeStatus(orderInfo.getOrder_id());
			if (Consts.NOTICE_OVER.equals(book_notify)) {
				// 区分二次预订通知以后 不更新通知表
			} else {
				long start = System.currentTimeMillis();
				DBNoticeVo vo = noticeService.queryNoticeInfoById(orderInfo.getOrder_id());
				vo.setChannel(Consts.CHANNEL_MEITUAN);
				vo.setOrder_id(orderInfo.getOrder_id());
				vo.setBook_notify_num(0);
				//美团火车票出票前代理商回调美团校验
				orderService.addBookResultNotice(vo);
				logger.info("send to [美团] the booked result,"
						+ orderInfo.getOrder_id() + ",lose time:"
						+ (System.currentTimeMillis() - start)
						+ "ms" + ",url" + vo.getBook_notify_url());
			}
		}
	}
	
}
