package com.l9e.train.channel.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.channel.request.impl.RobotPayRequest;
import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.TrainServiceImpl;

public class RequestFactory {
	private Logger logger = LoggerFactory.getLogger(RequestFactory.class);

	/**
	 * 获取支付账号，账号，机器人
	 * 
	 * @author: taoka
	 * @date: 2018年2月27日 下午4:09:07
	 * @param order
	 * @param logid
	 * @return IRequest
	 */
	public IRequest select(Order order, String logid) {
		TrainServiceImpl impl = new TrainServiceImpl();
		Account account = null;
		Worker worker = null;
		PayCard payCard = null;
		try {
			logger.info(logid + "Select Pay Worker、Account、Pay ...");
			int selectResult = impl.selectPayAccountAndWorkerBy(order, logid);
			logger.info(logid + "Selet Reuslt:" + selectResult + " [1:没有12306账号; 2:没有机器人; 3:没有支付账号]");
			if (selectResult != 0) {
				return null;
			}
			payCard = impl.getPayCard();
			account = impl.getAccount();
			worker = impl.getWorker();
		} catch (Exception e) {
			logger.info(logid + "【Select Pay Worker、Account、Pay Exception】", e);
		}
		return new RobotPayRequest(account, worker, payCard);
	}
}
