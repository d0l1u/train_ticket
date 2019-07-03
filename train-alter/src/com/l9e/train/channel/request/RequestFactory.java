package com.l9e.train.channel.request;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.channel.request.impl.PayResignRequest;
import com.l9e.train.channel.request.impl.RobotAlterRequest;
import com.l9e.train.po.Order;
import com.l9e.train.service.impl.TrainServiceImpl;

public class RequestFactory {

	private Logger logger = LoggerFactory.getLogger(RequestFactory.class);

	TrainServiceImpl trainService = new TrainServiceImpl();

	/**
	 * @author: taoka
	 * @date: 2018年3月6日 下午2:38:38
	 * @param orderId
	 *            订单ID
	 * @param changeStatus
	 *            改签状态
	 * @param logid
	 *            日志ID
	 * @return IRequest
	 */
	public IRequest select(String orderId, String changeStatus, String logid) {
		if (Order.WAITING_PAY.equals(changeStatus)) {
			logger.info(logid + "等待支付，获取支付机器人...");
			try {
				int returnValue = trainService.selectChangePayWorker(orderId, logid);
				logger.info(logid + "Selet Reuslt:" + returnValue + " [0:成功; 1:没有机器人; 2:没有支付账号]");
				if (returnValue == 0) {
					return new PayResignRequest(trainService.worker, trainService.payCard);
				}
			} catch (Exception e) {
				logger.info(logid + "【selectPayWorkerByOrder() Exception】", e);
			}
		} else if (Order.WAITING_RESIGN.equals(changeStatus)) {
			logger.info(logid + "等待改签，获取改签机器人...");
			try {
				int returnValue = trainService.selectChangeWorker(orderId, logid);
				logger.info(logid + "Selet Reuslt:" + returnValue + " [0:查询成功; 1:没有可用机器人]");
				if (returnValue == 0) {
					return new RobotAlterRequest(trainService.worker, null);
				}
			} catch (Exception e) {
				logger.info(logid + "【selectChangeWorker() Exception】", e);
			}
		}
		// 走到这里 要么异常，要么机器人为空
		return null;
	}
}
