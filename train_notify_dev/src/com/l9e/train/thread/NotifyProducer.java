package com.l9e.train.thread;

import java.util.List;

import org.apache.log4j.Logger;

import com.l9e.train.notify.service.impl.NotifyServiceImpl;
import com.l9e.train.po.OrderBill;
import com.l9e.train.producerConsumer.distinct.DistinctProducer;


public class NotifyProducer extends DistinctProducer<OrderBill> {
	private NotifyServiceImpl dao = new NotifyServiceImpl();
	private Logger logger=Logger.getLogger(this.getClass());
	@Override
	public String getObjectKeyId(OrderBill orderbill) {
		return orderbill.getOrderId();
	}

	@Override
	public List<OrderBill> getProducts() {
		logger.info("start Get Notify list");
		
		//查询需要发送的类
		int ret=0;
		List<OrderBill> list = null;
		try {
			ret = dao.notifyOrderByList(50);
			
			if(ret == 0){
				list = dao.getNotify();
			}
			
			logger.info("end Get Notify list:"+list.size());
		} catch (Exception e) {
			logger.warn("Repate exception"+e);
		}
		return list;
	}

}
