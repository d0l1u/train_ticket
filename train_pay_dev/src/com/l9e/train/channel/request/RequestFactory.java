package com.l9e.train.channel.request;


import org.apache.log4j.Logger;

import com.l9e.train.channel.request.impl.RobotPayRequest;


import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class RequestFactory {
	private  Logger logger=Logger.getLogger(RequestFactory.class);
	
	
	private  TrainServiceImpl impl = new TrainServiceImpl();
	
	public  IRequest select(Order order) {
		// TODO 获取出票渠道，用户名，密码，所在地址
	
		//获取处理人和处理账号信息
		Account account = null;
		Worker worker = null;
		PayCard payCard = null;
		
		try {
			//start 订单为“开始出票”状态的处理：根据规则获取账号和订购人员信息，准备调用相应的方法进行火车票的订购。
			//目前支持人工和机器人订购。
			logger.info("start select orderid:"+order.getOrderId()+" status:"+order.getOrderStatus());
			int ret =  impl.selectPayAccountAndWorkerBy(order);
			logger.info("end select orderid:"+order.getOrderId()+" ret:"+ret);
			
			if(ret!=0){
				logger.warn("get paycard account worker is null ret:"+ret);
				return null;
			}
			//end
			
			
			payCard = impl.getPayCard();
			account = impl.getAccount();
			worker = impl.getWorker();
		} catch (RepeatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (DatabaseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		logger.info("select account:"+account.getAccUsername()+" worker:"+worker.getWorkerName()+" cardno:"+payCard.getCardNo());
		
		return new RobotPayRequest(account, worker, payCard);
	}

}
