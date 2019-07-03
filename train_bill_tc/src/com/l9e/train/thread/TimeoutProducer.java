package com.l9e.train.thread;

import java.util.List;

import org.apache.log4j.Logger;

import com.l9e.train.po.BillOrder;
import com.l9e.train.producerConsumer.nodistinct.NoDistinctProducer;
import com.l9e.train.service.impl.TrainServiceImpl;


public class TimeoutProducer extends NoDistinctProducer<BillOrder> {
	
	private Logger logger=Logger.getLogger(this.getClass());
	@Override
	public String getObjectKeyId(BillOrder billOrder) {
		return billOrder.getBillId() + "";
	}

	@Override
	public List<BillOrder> getProducts() {
		
		TrainServiceImpl dao = new TrainServiceImpl();
		 
		logger.info("同程结算生产者生产... ...");
		List<BillOrder> list = null;
		//查询需要发送的类
		int ret=-1;
		try {
			int num = 50;
			logger.info("一次通知财务结算记录数: "+num);
			
			ret = dao.orderbillByList(num);
			
			if(ret == 0){
				list = dao.getOrderbill();
			}
			
			logger.info("同程结算生产者生产结束,记录数为:"+list.size());
		} catch (Exception e) {
			logger.info("同程结算生产者生产异常,"+e);
		}
		return list;
	}
	
	

}
