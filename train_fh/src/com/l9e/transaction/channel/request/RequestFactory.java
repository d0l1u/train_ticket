package com.l9e.transaction.channel.request;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.transaction.channel.request.impl.ManualRequest;
import com.l9e.transaction.channel.request.impl.RobotOrderRequest;
import com.l9e.transaction.service.impl.TrainServiceImpl;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.Worker;

public class RequestFactory {
	private  Logger logger=Logger.getLogger(RequestFactory.class);
	
	private  TrainServiceImpl impl = new TrainServiceImpl();
	
	public  IRequest select(Order order,String workerWeight) {
		// TODO 获取出票渠道，用户名，密码，所在地址
	
		//获取处理人和处理账号信息
		Account account = null;
		Worker worker = null;
		try {
			//start 订单为“开始出票”状态的处理：根据规则获取账号和订购人员信息，准备调用相应的方法进行火车票的订购。
			//目前支持人工和机器人订购。
			logger.info("start select:"+order.getOrderId()+" status:"+order.getOrderStatus());
			if("05".contains(order.getOrderStatus())){//消息队列中等待出票的订单状态
				int ret = 2;
				if(order.getChannel().equals("test")) {
					Map<String,String> map = new HashMap<String,String>();
					map.put("order_id", order.getOrderId());
					map.put("channel", order.getChannel());
					map.put("acc_id", order.getAcc_id()+"");
					
					//预登入 绑定机器人worker_id 
					logger.info("Get the init Worker, worker_id:"+order.getWorker_id());
					if(order.getWorker_id()>=0){
						map.put("worker_id",order.getWorker_id()+"");
					}
					
					logger.info("Get Account and Worker, orderid:"+order.getOrderId());
					ret = impl.selectOrderAccountAndWorkerBy(map);
				} else {
					logger.info("test_tongcheng -- start get account and worker ");
					ret = impl.selectAccountAndWorker(order,workerWeight);
				}
				logger.info(order.getOrderId()+" ret:"+ret);
			}else{
				logger.info("异常状态请求，请确定是否修改数据库？状态:"+order.getOrderStatus());
			}
			//end
			
			account = impl.getAccount();
			worker = impl.getWorker();
		} catch (Exception e) {
			logger.error("get account and worker is error!", e);
		}
		
		
		
		//start 账号和工人都不存在，系统重新获取相应的信息
		try{
			if(worker == null){
				logger.error(order.getOrderId()+" work is null error!");
				impl.insertHistory(order.getOrderId(), "没有空闲预定机器人，进入人工处理");
				return null;
			}else if(account ==null){
				logger.error(order.getOrderId()+" account is null error!");
				impl.insertHistory(order.getOrderId(), "没有空闲账号，进入人工处理");
				return null;
			}else{
				StringBuilder optlog = new StringBuilder();
				optlog.append("预定车票分配的帐号ID：").append(account.getId());
				optlog.append("分配的帐号：").append(account.getUsername());
				try {
					impl.insertHistory(order.getOrderId(), optlog.toString());
				} catch (Exception e) {
					logger.info(" insert log error",e);
				} 
			}
		}catch(Exception e){
			logger.error("insert history error!");
		}
		
		//end
		
		IRequest request= null;
		
		logger.info("select account:"+account.getId()+" workertype:"+worker.getWorkerType());
		
		if("05".contains(order.getOrderStatus())){//消息队列中等待的订单状态
			if(worker.getWorkerType().equals(Worker.TYPE_BOOK)){
				logger.info("RobotOrderRequest order:"+order.getOrderId());
				request=new RobotOrderRequest(account, worker);
			}else if(worker.getWorkerType().equals(Worker.TYPE_MANUAL)){
				logger.info("ManualRequest order:"+order.getOrderId());
				request=new ManualRequest(account, worker);
			}else{
				logger.warn("exception!!!!");
			}
		}else{
			logger.warn("exception!!!!");
		}
		
		return request;
	}
	
}
