package com.l9e.train.thread;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.l9e.train.channel.request.RequestFactory;
import com.l9e.train.channel.request.IRequest;

import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;

import com.l9e.train.producerConsumer.distinct.DistinctConsumer;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class TimeoutConsumer extends DistinctConsumer<Order> {

	private Logger logger = Logger.getLogger(this.getClass());
	TrainServiceImpl service = new TrainServiceImpl();

	@Override
	public boolean consume(Order orderbill) {

		logger.info("start cancel selfid:" + orderbill.getOrderId() + " outTickerBillNo:" + orderbill.getOutTicketBillNo());

		try {

			logger.info("select account request=" + orderbill.getOrderId() + " start");
			// 选择账号和具体处理的人
			IRequest request = new RequestFactory().select(orderbill);

			logger.info("select account request=" + orderbill.getOrderId() + " end impl:" + request);
			if (request == null) {
				service.orderIsResend(orderbill, null);
				logger.info(orderbill.getOrderId() + " not Account");
				return false;
			}

			// 请求查询
			logger.info("cancel request=" + orderbill.getOrderId() + " start");
			Result result = request.request(orderbill);
			logger.info("cancel request=" + orderbill.getOrderId() + " end");

			logger.info("update orderbill cpid=" + orderbill.getOrderId() + " workerName:" + result.getWorker().getWorkerName() + " status:"
					+ result.getRetValue() + " start!");
			String optlog = "";

			// 对处理后的结果进行修改
			if (StringUtils.equals(result.getRetValue(), Result.SUCCESS)) {// 订单表改为成功，通知表改为正在通知
				logger.info(orderbill.getOrderId() + " find is success!");
				try {
					service.orderIsSuccess(orderbill, result);
				} catch (Exception e) {
					logger.error("更新取消成功时发生异常！e：" + e);
				}
				optlog = "12306网站的订单取消成功";
			} else if (StringUtils.equals(result.getRetValue(), Result.FAILURE)) {// 订单表和通知表不做调整
				logger.info(orderbill.getOrderId() + " find is resend!");
				service.orderIsFail(orderbill, result);
				optlog = "12306网站的订单取消失败，请人工核实";
			} else if (StringUtils.equals(result.getRetValue(), Result.RESEND)) {// 订单表和通知表不做调整
				logger.info(orderbill.getOrderId() + " find is resend!");
				service.orderIsResend(orderbill, result);
				optlog = "12306网站的订单取消流程重发";
			} else {// 异常
				logger.warn(result.getSelfId() + " find order is exception, restor find!");
				service.orderIsFail(orderbill, result);
				optlog = "12306网站的订单取消异常，请人工核实";
			}
			service.insertHistory(orderbill.getOrderId(), optlog);
			logger.info("update orderbill cpid=" + orderbill.getOrderId() + " status:" + result.getRetValue() + " end!");
		} catch (RepeatException e) {
			logger.warn("exception!!:" + e);
		} catch (DatabaseException e) {
			logger.warn("exception!!:" + e);
		}

		logger.info("end selfid:" + orderbill.getOrderId() + " outTickerBillNo:" + orderbill.getOutTicketBillNo());

		return true;
	}

	@Override
	public String getObjectKeyId(Order t) {
		// TODO Auto-generated method stub
		return t.getOrderId();
	}

}