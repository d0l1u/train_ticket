package com.l9e.transaction.thread;

import java.util.Date;

import org.apache.log4j.Logger;

import com.l9e.common.Consts;
import com.l9e.transaction.service.NoticeService;
import com.l9e.transaction.service.OrderService;
import com.l9e.transaction.vo.DBNoticeVo;
import com.l9e.transaction.vo.DBOrderInfo;


public class BookResultNoticeThread implements Runnable {

	
	private NoticeService noticeService;
	
	private OrderService orderService;

	private DBOrderInfo orderInfo ;

	private static final Logger logger = Logger.getLogger(BookResultNoticeThread.class);

	public BookResultNoticeThread(DBOrderInfo orderInfo,NoticeService noticeService,OrderService orderService ) {
		this.orderInfo = orderInfo ;
		this.noticeService=noticeService;
		this.orderService=orderService;
	}

	@Override
	public void run() {
		Date begin = new Date();
		try {
			if("elong".equals(orderInfo.getChannel())){
				//发送请求给elong
				long start=System.currentTimeMillis();
				DBNoticeVo vo=new DBNoticeVo();
				vo.setChannel("elong");
				vo.setOrder_id(orderInfo.getOrder_id());
				vo.setBook_notify_num(0);
				orderService.addBookResultNotice(vo);
				logger.info("send to elong the booked resuld,"+orderInfo.getOrder_id()+",lose time:"+(System.currentTimeMillis()-start)+"ms");
			}else if("tongcheng".equals(orderInfo.getChannel())){
				String book_notify=noticeService.selectBookNoticeStatus(orderInfo.getOrder_id());
				if(Consts.NOTICE_OVER.equals(book_notify)){
					//区分二次预订通知以后 不更新通知表
				}else{
					long start=System.currentTimeMillis();
					DBNoticeVo vo=noticeService.queryNoticeInfoById(orderInfo.getOrder_id());
					logger.info("同程首次预订结果通知前notice status：" + vo.getBook_notify_status());
					vo.setChannel("tongcheng");
					vo.setOrder_id(orderInfo.getOrder_id());
					vo.setBook_notify_num(0);
					orderService.addBookResultNotice(vo);
					logger.info("send to tongcheng the booked resuld,"+orderInfo.getOrder_id()+",lose time:"+(System.currentTimeMillis()-start)+"ms"+",url"+vo.getBook_notify_url());
				}
			}
			Date end = new Date();
			logger.info("通知预订结果,orderId:"+orderInfo.getOrder_id()+"耗时"+(end.getTime()-begin.getTime()));

		} catch (Exception e) {
			logger.info("通知预定结果,orderId:"+orderInfo.getOrder_id()+"出现异常");
			e.printStackTrace();
		}
	}

	
}
