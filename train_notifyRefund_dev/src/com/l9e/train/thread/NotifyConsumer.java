package com.l9e.train.thread;



import org.apache.log4j.Logger;

import com.l9e.train.notify.request.NotifyRequest;
import com.l9e.train.notify.service.impl.NotifyServiceImpl;
import com.l9e.train.po.OrderBill;
import com.l9e.train.producerConsumer.distinct.DistinctConsumer;

/**
 * 
 * @author guobin
 *
 */
public class NotifyConsumer extends DistinctConsumer<OrderBill> {

	private Logger logger=Logger.getLogger(this.getClass());
	NotifyServiceImpl service = new NotifyServiceImpl();
	@Override
	public boolean consume(OrderBill orderbill) {
		//发送通知
		
		logger.info("start notify trainid="+orderbill.getOrderId());
		
		NotifyRequest request  = new NotifyRequest();
		
		int ret = 0;
		String optlog = "";
		try {
			logger.info("Post start trainid="+orderbill.getOrderId());
			String sRet = request.sending(orderbill);
			logger.info("Post end trainid="+orderbill.getOrderId()+" request value="+sRet);
			if (sRet.equals(NotifyRequest.FAILURE)) {// 如果通知失败
				logger.info("update trainid="+orderbill.getOrderId()+" restore");
				ret = service.restoreNotify(orderbill);// 逻辑重新发送
				optlog = "通知失败，重新通知";
			} else if (sRet.equals(NotifyRequest.SUCCESS)) {// 如果通知成功
				logger.info("update trainid="+orderbill.getOrderId()+" success");
				ret = service.successNotify(orderbill);// 更新发送成功
				optlog = "通知成功";
			} else if (sRet.equals(NotifyRequest.TIMEOUT)) {// 如果通知超时
				logger.info("update trainid="+orderbill.getOrderId()+" restore");
				ret = service.restoreNotify(orderbill);// 逻辑重新发送
				optlog = "通知超时";
			} else if(sRet.equals(NotifyRequest.EXCEPTION)){//如果异常，插入异常表
				logger.info("update trainid="+orderbill.getOrderId()+" restore");
				ret = service.restoreNotify(orderbill);
				optlog = "通知异常，重新通知；原因：订单表数据异常，请查看问题一";
			}
			
			service.insertHistory(orderbill, optlog);
			
		} catch (Exception e) {
			logger.error("getProducts Repate exception"+e);
		} 
		logger.info("end notify trainid="+orderbill.getOrderId());
		return true;
	}

	@Override
	public String getObjectKeyId(OrderBill orderbill) {
		return orderbill.getOrderId();
	}


}