package com.l9e.train.channel.request;

import org.apache.log4j.Logger;

import com.l9e.train.channel.request.impl.RobotCancelRequest;
import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;




public class RequestFactory {
	private  Logger logger=Logger.getLogger(RequestFactory.class);
	
	
	private  TrainServiceImpl impl = new TrainServiceImpl();
	
	public  IRequest select(Order order) {
		// TODO 获取出票渠道，用户名，密码，所在地址
		Account account = null;
		Worker worker = null;
		try {
			int ret = impl.findAccountAndWorker(order);
			
			logger.info("findAccount Ret:"+ret);
			
			account = impl.getAccount();
			worker  = impl.getWorker();
			
			
		} catch (RepeatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(account==null && worker == null){
			return null;
		}
		
		
		
		return new RobotCancelRequest(account,worker);
	}

}
