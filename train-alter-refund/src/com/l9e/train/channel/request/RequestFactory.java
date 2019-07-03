package com.l9e.train.channel.request;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.channel.request.impl.RobotAlterRequest;
import com.l9e.train.po.Order;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.TrainServiceImpl;


public class RequestFactory {

	private static Logger logger = LoggerFactory.getLogger(RequestFactory.class);
	
	private  TrainServiceImpl impl = new TrainServiceImpl();
	
	public  IRequest select(Order order) {
		// TODO 获取出票渠道，用户名，密码，所在地址
	
		//获取处理机器人和处理12306账号信息
		Worker worker = null;
		
		try {
			//start 订单状态为“开始机器改签02”状态的处理：根据规则获取账号和订购人员信息，准备调用相应的方法进行火车票的订购
			//目前支持人工和机器人订购
			logger.info("start select orderid:"+order.getOrderId());
			int ret =  impl.selectPayAccountAndWorkerBy(order.getOrderId());
			logger.info("end select orderid:"+order.getOrderId()+" ret:"+ret);
			
			if(ret!=0){
				logger.warn("get ALTER account and worker is null ret:"+ret);
				return null;
			}
			//end
			
			worker = impl.getWorker();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		logger.info("select worker:"+worker.getWorkerName());
		
		return new RobotAlterRequest(worker);
	}

}
