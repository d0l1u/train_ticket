package com.l9e.transaction.job;

import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.service.TuniuNoticeService;
import com.l9e.transaction.service.TuniuOrderService;
import com.l9e.transaction.service.TuniuRefundService;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.TuniuOrder;
import com.l9e.transaction.vo.TuniuRefund;

/**
 * 途牛通知出票系统定时任务
 * 
 * @author licheng
 * 
 */
@Component("tuniuOutTicketJob")
public class TuniuOutTicketJob {

	private static final Logger logger = Logger
			.getLogger(TuniuOutTicketJob.class);

	@Resource
	private TuniuOrderService tuniuOrderService;

	@Resource
	private TuniuRefundService tuniuRefundService;

	@Resource
	private TuniuNoticeService tuniuNoticeService;

	/**
	 * 通知出票系统占座
	 */
	public void book() {
		//cp_notify_status 00 和 11 ，<6次 
		List<Notice> notices = tuniuNoticeService.findBookWaitOutTicketNotifies(20);
		logger.info("待通知[出票系统占座]数量：" + notices.size());

		for (Notice notice : notices) {
			notice.setCpNotifyStatus(TuniuCommonService.NOTIFY_START);
			notice.setCpNotifyTime("cpNotifyTime");
			//修改通知表-->开始通知
			tuniuNoticeService.updateBookNotice(notice);
			
			logger.info("开始通知[出票系统占座]：" + notice.getOrderId());
			TuniuOrder order = tuniuOrderService.getOrderById(notice.getOrderId(), true);
			tuniuOrderService.sendBookOrder(order, notice);//发送请求并修改通知完成

		}
		
	}

	/**
	 * 通知出票系统出票
	 */
	public void out() {
		List<Notice> notices = tuniuNoticeService.findOutWaitOutTicketNotifies(20);
		logger.info("途牛通知出票/取消的通知数量为：" + notices.size());

		/* 更新乐观锁状态 */
		for (Notice notice : notices) {
			notice.setCpNotifyStatus(TuniuCommonService.NOTIFY_START);
			notice.setCpNotifyTime("cpNotifyTime");
			tuniuNoticeService.updateOutNotice(notice);
		}
		
		for(Notice notice : notices) {
			logger.info("途牛通知出票系统出票/取消：" + notice.getOrderId());
			TuniuOrder order = tuniuOrderService.getOrderById(notice.getOrderId(), true);
			String status = order.getOrderStatus();
			if(status.equals(TuniuOrderService.STATUS_CANCEL_ING)) {
				tuniuOrderService.sendCancelOrder(order, notice);
			} else {
				tuniuOrderService.sendOutOrder(order, notice);
			}
		}
	}

	/**
	 * 通知出票系统退票
	 */
	public void refund() {
		List<Notice> notices = tuniuNoticeService.findRefundWaitOutTicketNotifies(20);
		logger.info("途牛通知退款的通知数量为：" + notices.size());

		/* 更新乐观锁状态 */
		for (Notice notice : notices) {
			notice.setCpNotifyStatus(TuniuCommonService.NOTIFY_START);
			notice.setCpNotifyTime("cpNotifyTime");
			tuniuNoticeService.updateRefundNotice(notice);
		}
		
		for(Notice notice : notices) {
			logger.info("途牛通知出票系统退款 ：orderId=" + notice.getOrderId() + ",cpId=" + notice.getCpId());
			TuniuRefund refund = tuniuRefundService.getRefundById(notice.getRefundId());
//			TuniuPassenger passenger = tuniuOrderService.getPassengerById(notice.getCpId());
			
			refund.setRefundStatus(TuniuRefundService.STATUS_START_ROBOT_ALTER);
			tuniuRefundService.updateRefund(refund);
			tuniuRefundService.sendRefundOrder(refund, notice);
		}
	}
}
