package com.l9e.transaction.job;


import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.common.TuniuConstant;
import com.l9e.transaction.service.TuniuChangeService;
import com.l9e.transaction.service.TuniuCommonService;
import com.l9e.transaction.service.TuniuNoticeService;
import com.l9e.transaction.service.TuniuOrderService;
import com.l9e.transaction.service.TuniuRefundService;
import com.l9e.transaction.vo.Notice;
import com.l9e.transaction.vo.TuniuChangeInfo;
import com.l9e.transaction.vo.TuniuOrder;
import com.l9e.transaction.vo.TuniuQueueOrder;
import com.l9e.transaction.vo.TuniuRefund;

/**
 * 途牛结果回调定时任务
 * @author licheng
 *
 */
@Component("tuniuCallbackJob")
public class TuniuCallbackJob {

	private static final Logger logger = Logger.getLogger(TuniuCallbackJob.class);
	
	@Resource
	private TuniuNoticeService tuniuNoticeService;
	
	@Resource
	private TuniuOrderService tuniuOrderService;
	
	@Resource
	private TuniuRefundService tuniuRefundService;
	
	@Resource 
	private TuniuChangeService tuniuChangeService;
	
	/**
	 * 回调占座结果
	 */
	public void book() {
		List<Notice> notices = tuniuNoticeService.findBookWaitCallbackNotifies(20);
		logger.info("[途牛占座]待通知数量为：" + notices.size());
		
		/* 更新乐观锁状态 */
		for (Notice notice : notices) {
			notice.setNotifyStatus(TuniuCommonService.NOTIFY_START);
			notice.setNotifyTime("notifyTime");
			tuniuNoticeService.updateBookNotice(notice);//开始通知
		}
			for (Notice notice : notices) {	
			logger.info("开始通知[途牛占座]：" + notice.getOrderId());
			TuniuOrder order = tuniuOrderService.getOrderById(notice.getOrderId(), true);
			tuniuOrderService.callbackBookOrder(order, notice);//通知途牛

		}
		
	}
	
	/**
	 * 回调出票或取消结果
	 */
	public void out() {
		List<Notice> notices = tuniuNoticeService.findOutWaitCallbackNotifies(20);
		logger.info("途牛回调出票或取消的通知数量为：" + notices.size());

		/* 更新乐观锁状态 */
		for (Notice notice : notices) {
			notice.setNotifyStatus(TuniuCommonService.NOTIFY_START);
			notice.setNotifyTime("notifyTime");
			tuniuNoticeService.updateOutNotice(notice);
		}
		
		for(Notice notice : notices) {
			logger.info("途牛回调出票或取消结果：" + notice.getOrderId());
			TuniuOrder order = tuniuOrderService.getOrderById(notice.getOrderId(), true);
			tuniuOrderService.callbackOutOrder(order, notice);
		}
	}
	
	/**
	 * 回调退款结果
	 */
	public void refund() {
		List<Notice> notices = tuniuNoticeService.findRefundWaitCallbackNotifies(20);
		logger.info("途牛回调退款的通知数量为：" + notices.size());

		/* 更新乐观锁状态 */
		for (Notice notice : notices) {
			notice.setNotifyStatus(TuniuCommonService.NOTIFY_START);
			notice.setNotifyTime("notifyTime");
			tuniuNoticeService.updateRefundNotice(notice);
		}
		
		for(Notice notice : notices) {
			logger.info("途牛回调退款 ：orderId=" + notice.getOrderId() + ",cpId=" + notice.getCpId());
			TuniuRefund refund = tuniuRefundService.getRefundById(notice.getRefundId());
//			TuniuPassenger passenger = tuniuOrderService.getPassengerById(notice.getCpId());
			
			tuniuRefundService.callbackRefundOrder(refund, notice);
		}
	}
	/**
	 * 改签结果通知
	 */
	public void changeNotice() {
		/*获取途牛回调通知列表*/
	
		List<TuniuChangeInfo> notifyList = tuniuChangeService.getNoticeChangeInfo(TuniuConstant.merchantId);
		
		if(notifyList!=null && notifyList.size()>0){
			logger.info("途牛改签回调准备开始");
			tuniuChangeService.callbackChangeNotice(notifyList);
		}else{
			logger.info("途牛改签回调信息为空");
		}
	}
	/**
	 * 排队结果通知
	 */
	public void queueNotice(){
		//获取排队订单
		List<TuniuQueueOrder>  queueOrderList = tuniuNoticeService.getQueueOrder();
		logger.info("排队订单回调信息数量为"+queueOrderList.size());
		if(queueOrderList!=null && queueOrderList.size()>0){
			for (TuniuQueueOrder notice : queueOrderList) {
				notice.setNotify_status(2);
				tuniuNoticeService.updateQueueNotice(notice);
			}
			
			for(TuniuQueueOrder notice : queueOrderList) {
				logger.info("途牛回调排队订单信息：" + notice.getOrder_id());
				tuniuOrderService.callbackQueueOrder(notice);
			}
			
		}
	}
}
