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
	private Logger logger = Logger.getLogger(RequestFactory.class);

	private TrainServiceImpl impl = new TrainServiceImpl();

	public IRequest select(Order order, String workerWeight) {
		// 获取出票渠道，用户名，密码，所在地址

		String uuid = order.getUuid();
		// 获取处理人和处理账号信息
		Account account = null;
		Worker worker = null;

		String orderId = order.getOrderId();
		String orderStatus = order.getOrderStatus();
		String channel = order.getChannel();
		int acc_id = order.getAcc_id();
		int worker_id = order.getWorker_id();

		logger.info(uuid + "获取处理人和处理账号信息...");
		logger.info(uuid + "orderStatus:" + orderStatus);
		logger.info(uuid + "channel:" + channel);
		logger.info(uuid + "acc_id:" + acc_id);
		logger.info(uuid + "worker_id:" + worker_id);
		try {
			// start 订单为“开始出票”状态的处理：根据规则获取账号和订购人员信息，准备调用相应的方法进行火车票的订购。
			// 目前支持人工和机器人订购。
			if ("05".contains(orderStatus)) {// 消息队列中等待出票的订单状态
				int ret = 2;
				if (channel.equals("test")) {
					Map<String, String> map = new HashMap<String, String>();
					map.put("order_id", orderId);
					map.put("channel", channel);
					map.put("acc_id", String.valueOf(acc_id));

					// 预登入 绑定机器人worker_id
					if (worker_id >= 0) {
						logger.info(uuid + "预登入绑定机器人worker_id...");
						map.put("worker_id", String.valueOf(worker_id));
					}

					logger.info(uuid + "获取账号和机器人");
					ret = impl.selectOrderAccountAndWorkerBy(map);
				} else {
					logger.info(uuid + "test_tongcheng -- start get account and worker ");
					ret = impl.selectAccountAndWorker(order, workerWeight);
				}
				logger.info(uuid + "ret:" + ret);
			} else {
				logger.info(uuid + "异常状态请求,请确定是否修改数据库？状态:" + orderStatus);
			}
			account = impl.getAccount();
			worker = impl.getWorker();
		} catch (Exception e) {
			logger.info(uuid + "【获取账号和机器人发生异常】", e);
		}

		// 判断 账号和工人都不存在,转人工,插入操作日志
		try {
			if (worker == null) {
				logger.info(uuid + "-获取机器人结果为空...");
				impl.insertHistory(uuid, "没有空闲预定机器人,进入人工处理");
				return null;
			} else if (account == null) {
				logger.info(orderId + "-获取账号结果为空...");
				impl.insertHistory(orderId, "没有空闲账号,进入人工处理");
				return null;
			} else {
				StringBuilder optlog = new StringBuilder();
				optlog.append("预定车票分配的帐号ID:").append(account.getId());
				optlog.append(" 分配的帐号:").append(account.getUsername());
				optlog.append(" 分配的帐号来源为:").append(order.getAccountFromWay());
				impl.insertHistory(order.getOrderId(), optlog.toString());
			}
		} catch (Exception e) {
			logger.info(uuid + "【插入历史订单信息发生异常】", e);
		}

		Integer id = account.getId();
		Integer workerType = worker.getWorkerType();
		logger.info(uuid + "accountId:" + id);
		logger.info(uuid + "workerType:" + workerType);
		IRequest request = null;
		if ("05".contains(orderStatus)) {// 消息队列中等待的订单状态
			if (workerType.equals(Worker.TYPE_BOOK)) {
				logger.info(uuid + "预定机器人");
				request = new RobotOrderRequest(account, worker);
			} else if (workerType.equals(Worker.TYPE_MANUAL)) {
				logger.info(uuid + "人工处理");
				request = new ManualRequest(account, worker);
			} else {
				logger.info(uuid + "出现未知机器人状态");
			}
		} else {
			logger.info(uuid + "出现未知订单状态");
		}
		return request;
	}

}
