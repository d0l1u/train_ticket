package com.l9e.train.thread;

import java.util.List;

import org.apache.log4j.Logger;

import com.l9e.train.po.OrderCP;
import com.l9e.train.producerConsumer.distinct.DistinctProducer;
import com.l9e.train.service.impl.TrainServiceImpl;


public class TimeoutProducer extends DistinctProducer<OrderCP> {
	
	private Logger logger=Logger.getLogger(this.getClass());
	@Override
	public String getObjectKeyId(OrderCP orderbill) {
		return orderbill.getCpId();
	}

	@Override
	public List<OrderCP> getProducts() {
		
		TrainServiceImpl dao = new TrainServiceImpl();
		String logid = String.valueOf(System.currentTimeMillis());
		logid = "["+logid.substring(logid.length()-6)+"] ";
		
		logger.info(logid+"Get Refund List Start");
		
		//查询需要发送的类
		int ret=-1;
		List<OrderCP> list = null;
		try {
			int num = 1;
			ret = dao.orderbillByList(num,logid);
			if(ret == 0){
				list = dao.getOrderbill();
			}
			logger.info(logid+"End Get Timeout List:"+list.size());
		} catch (Exception e) {
			logger.info(logid+"Get Products Repate Exception:"+e.getClass().getSimpleName(),e);
		}
		return list;
	}

}
