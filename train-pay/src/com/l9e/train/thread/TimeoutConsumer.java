package com.l9e.train.thread;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.channel.request.IRequest;
import com.l9e.train.channel.request.RequestFactory;
import com.l9e.train.po.Order;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.TicketEntrance;
import com.l9e.train.po.Worker;
import com.l9e.train.producerConsumer.distinct.DistinctConsumer;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

public class TimeoutConsumer extends DistinctConsumer<Order> {

	private Logger logger = LoggerFactory.getLogger(TimeoutConsumer.class);

	public boolean consume(Order order, String logid) {
		TrainServiceImpl service = new TrainServiceImpl();
		String orderId = order.getOrderId();
		String buymoney = order.getBuymoney();
		String orderStatus = order.getOrderStatus();
		logger.info(logid + "订单ID:" + orderId + ", 订单状态:" + orderStatus + ", 总价:" + buymoney);

		try {
			String optlog = null;
			if ("55".contains(orderStatus)) {
				optlog = "train_pay进入正在支付，选择支付方式";
			} else {
				optlog = "支付状态异常，状态为：" + orderStatus;
			}

			// 插入日志
			service.insertHistory(orderId, optlog);

			// 查询订单信息
			IRequest request = new RequestFactory().select(order, logid);
			if (request == null) {
				service.payIsManual(order, null, logid);
				logger.info(logid + "没有空闲卡号、工号和账号，请进行人工核对后进行处理。");
				service.insertHistory(orderId, "没有空闲卡号、工号和账号，请进行人工核对后进行处理。");
				return false;
			}

			Result result = null;
			Integer workerReportId = null;
			try {
				workerReportId = service.startWorkerReport(request.getWorker(), order, "2");
			} catch (Exception e) {
				logger.info(logid + "【startWorkerReport() Exception】", e);
			}
			try {
				result = request.request(order, logid);
			} catch (Exception e) {
				logger.info(logid + "【request() Exception】" + e.getClass().getSimpleName(), e);
			} finally {
				// 机器人结束记录
				service.endWorkerReport(workerReportId);
			}

			String retValue = result.getRetValue();
			String sequence = order.getOutTicketBillNo();
			String paybillno = order.getPaybillno();
			String errorInfo = result.getErrorInfo();
			PayCard payCard = result.getPayCard();
			Worker worker = result.getWorker();
			String workerName = worker.getWorkerName();

			List<TicketEntrance> ticketEntrances = result.getTicketEntrances();
			order.setPaybillno(paybillno);

			if (retValue.equals(Result.SUCCESS)) {
				// 查询一人多单
				int querySameOutTicketBillno = service.querySameOutTicketBillno(sequence);
				if (querySameOutTicketBillno > 1) {
					optlog = workerName + " 支付成功，可能存在一人多单，或者往返票赔款的情况，转入人工处理";
					logger.info(logid + optlog);
					service.payIsManual(order, ticketEntrances, logid);
				} else {
					// TODO
					optlog = workerName + " 支付成功，进入核对流程！";
					logger.info(logid + optlog);
					service.payIsSuccess(order, ticketEntrances, payCard, logid);
					service.balance4PayCard(request.getPayCard(), result);
				}
			} else if (retValue.equals(Result.FAILURE)) {
				optlog = workerName + " 支付失败！[" + errorInfo + "]";
				logger.info(logid + optlog);
				service.payIsManual(order, null, logid);
			} else if (retValue.equals(Result.MANUAL)) {
				if (paySuccessNoOrder(service) && errorInfo.contains("已成功付款,但是没有查询到完成订单")) {
					optlog = workerName + " 支付成功，未找到完成订单！[" + errorInfo + "]";
					logger.info(logid + optlog);
					service.payIsSuccess(order, ticketEntrances, payCard, logid);
					service.order2find(orderId);
				} else {
					optlog = workerName + " 支付人工！[" + errorInfo + "]";
					logger.info(logid + optlog);
					service.payIsManual(order, null, logid);
				}
			} else if (retValue.equals(Result.RESEND)) {
				optlog = workerName + " 支付重发！[" + errorInfo + "]";
				logger.info(logid + optlog);
				service.payIsResend(order, logid);
			} else {
				optlog = workerName + " 支付异常！";
				logger.info(logid + "支付异常!!!");
			}
			service.insertHistory(orderId, optlog);

			if (StringUtils.isNotBlank(errorInfo) && errorInfo.contains("您的操作频率过快")) {
				logger.info(logid + "机器人被封停。停用机器人 workerId : " + worker.getWorkerId());
				service.stopWorker(worker);
			}
		} catch (Exception e) {
			logger.info(logid + "【consume() Exception】", e);
		}
		return true;
	}

	public String getObjectKeyId(Order t) {
		return t.getOrderId();
	}

	private boolean paySuccessNoOrder(TrainServiceImpl service) {
		boolean flag = false;
		try {
			int ret = service.isPaySuccessNoOrder();
			if (ret == 1) {
				flag = true;
			}
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		}
		return flag;
	}
}
