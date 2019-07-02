package com.l9e.train.thread;

import java.util.List;

import org.apache.log4j.Logger;

import com.l9e.train.po.Order;
import com.l9e.train.producerConsumer.distinct.DistinctProducer;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.MemcachedUtil;
import com.l9e.train.util.WorkIdNum;


public class TimeoutProducer extends DistinctProducer<Order> {
	
	private Logger logger=Logger.getLogger(this.getClass());
	@Override
	public String getObjectKeyId(Order orderbill) {
		return orderbill.getOrderId();
	}

	@Override
	public List<Order> getProducts() {
		
		TrainServiceImpl dao = new TrainServiceImpl();
		 
		logger.info("pay_get Pay list start");
		
		//查询需要发送的类
		int ret=-1;
		List<Order> list = null;
		try {
			int num = 10;
			if(null == MemcachedUtil.getInstance().getAttribute("robot_pay_product_num")){
				num = dao.queryProductNum();
				MemcachedUtil.getInstance().setAttribute("robot_pay_product_num", num, 2*60*1000);
			}else{
				num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("robot_pay_product_num")));
			}
			logger.info("robot_pay_product_num: "+num);
			
			if(null == MemcachedUtil.getInstance().getAttribute("robot_app_book_num")){
				WorkIdNum.book_num = dao.queryProductBookNum();
				MemcachedUtil.getInstance().setAttribute("robot_app_book_num", WorkIdNum.book_num, 5*60*1000);
			}else{
				WorkIdNum.book_num = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("robot_app_book_num")));
			}
			
			logger.info("robot_app_book_num: "+WorkIdNum.book_num);
			
			/**
			//train_pay
			int time = 3;
			if(null == MemcachedUtil.getInstance().getAttribute("robot_pay_time")){
				time = dao.queryProductTime();
				MemcachedUtil.getInstance().setAttribute("robot_pay_time", time, 2*60*1000);
			}else{
				time = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("robot_pay_time")));
			}
			*/
			  
			//train_pay_new
			int time = 2;
			if(null == MemcachedUtil.getInstance().getAttribute("robot_pay_new_time")){
				time = dao.queryProductNewTime();
				MemcachedUtil.getInstance().setAttribute("robot_pay_new_time", time, 2*60*1000);
			}else{
				time = Integer.valueOf(String.valueOf(MemcachedUtil.getInstance().getAttribute("robot_pay_new_time")));
			}
			logger.info("robot_pay_time: "+time);
			
			ret = dao.orderbillByList(num,time);
			if(ret == 0){
				list = dao.getOrderbill();
			}
			logger.info("end get Timeout list:"+list.size());
		} catch (Exception e) {
			logger.error("getProducts Repate exception"+e);
		}
		return list;
	}

}
