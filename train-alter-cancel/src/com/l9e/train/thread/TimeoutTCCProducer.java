package com.l9e.train.thread;

import java.util.List;

import org.apache.log4j.Logger;

import com.l9e.train.po.Order;
import com.l9e.train.producerConsumer.distinct.DistinctProducer;
import com.l9e.train.service.TrainService;
import com.l9e.train.service.impl.TrainServiceTCCImpl;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class TimeoutTCCProducer extends DistinctProducer<Order> {
	
	private Logger logger=Logger.getLogger(this.getClass());
	private TrainService dao = new TrainServiceTCCImpl();

	@Override
	public String getObjectKeyId(Order orderbill) {
		return orderbill.getOrderId();
	}

	@Override
	public List<Order> getProducts() {
		
		 
		logger.info("tongcheng改签取消生产...");
		
		//查询需要发送的类
		int ret=-1;
		try {
			ret = dao.orderbillByList(10);
		} catch (RepeatException e) {
			// TODO Auto-generated catch block
			logger.error("getProducts Repate exception"+e);
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			logger.error("getProducts database exception "+e);
		}
		
		
		List<Order> list = null;
		
		if(ret == 0){
			list = dao.getOrderbill();
		}
		
		logger.info("end get Timeout list:"+list.size());
		
		return list;
	}

}
