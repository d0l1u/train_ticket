package com.l9e.train.channel.request;

import org.apache.log4j.Logger;

import com.l9e.train.channel.request.impl.RobotCancelRequest;
import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.Worker;
import com.l9e.train.service.TrainService;
import com.l9e.train.service.impl.TrainServiceTCCImpl;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;




public class RequestFactory {
	private  Logger logger=Logger.getLogger(RequestFactory.class);
	
	public  IRequest select(Order order) {
		TrainService impl = new TrainServiceTCCImpl();
		// 获取出票渠道，用户名，密码，所在地址
		Account account = null;
		Worker worker = null;
		try {
			int ret = impl.findAccountAndWorker(order);
			
			logger.info("findAccount Ret:"+ret);
			
			account = impl.getAccount();
			worker  = impl.getWorker();
			
			
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		if(account==null && worker == null){
			return null;
		}
		
		
		
		return new RobotCancelRequest(account,worker);
	}

}
