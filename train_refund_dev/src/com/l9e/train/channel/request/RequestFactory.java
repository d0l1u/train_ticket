package com.l9e.train.channel.request;

import org.apache.log4j.Logger;

import com.l9e.train.channel.request.impl.RobotRefundRequest;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.TrainServiceImpl;

public class RequestFactory {
	private Logger logger = Logger.getLogger(RequestFactory.class);

	private TrainServiceImpl impl = new TrainServiceImpl();

	public IRequest select(OrderCP order) {
		// 获取处理人和处理账号信息
		Worker worker = null;
		try {
			// start 订单为“开始退票”状态的处理：根据规则获取退票机器人。
			int ret = impl.selectPayAccountAndWorkerBy(order.getOrderId());
			logger.info("end select orderid:" + order.getOrderId() + " ret:" + ret);
			if (ret != 0) {
				logger.warn("get paycard account worker is null ret:" + ret);
				return null;
			}
			worker = impl.getWorker();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("select  worker:" + worker.getWorkerName());
		return new RobotRefundRequest(worker);
	}

}
