package com.l9e.train.channel.request;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.l9e.train.channel.request.impl.CtripOrderRequest;
import com.l9e.train.channel.request.impl.JDOrderRobotRequest;
import com.l9e.train.channel.request.impl.ManualRequest;
import com.l9e.train.channel.request.impl.RobotOrderRequest;
import com.l9e.train.po.Account;
import com.l9e.train.po.CtripAcc;
import com.l9e.train.po.JdAcc;
import com.l9e.train.po.JdPrePayCard;
import com.l9e.train.po.Order;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.TrainServiceImpl;

public class RequestFactory {

	private Logger logger = LoggerFactory.getLogger(RequestFactory.class);

	private TrainServiceImpl trainService = new TrainServiceImpl();

	
	public IRequest select(Order order, String languageWeight, String logid) {
		String orderId = order.getOrderId();
		String orderStatus = order.getOrderStatus();
		// 获取处理人和处理账号信息
		Account account = null;
		Worker worker = null;
		try {
			// 订单为“开始出票”状态的处理：根据规则获取账号和订购人员信息，准备调用相应的方法进行火车票的订购。目前支持人工和机器人订购。
			int ret = 2;
			if ("00,01".contains(orderStatus)) {
				// 获取账号和机器人
				ret = trainService.selectAccountAndWorker(order, languageWeight, logid);
			} else {
				logger.info("{}异常状态请求，请确定是否修改数据库？状态:{}", logid, orderStatus);
			}
			logger.info("{}selectAccountAndWorker Ret:{}", logid, ret);
			account = trainService.getAccount();
			worker = trainService.getWorker();
		} catch (Exception e) {
			logger.info("{}【系统异常】:获取处理人和处理账号信息发生异常", logid, e);
		}

		// start 账号和工人都不存在，系统重新获取相应的信息
		try {
			if (worker == null) {
				logger.info("{}没有空闲预定机器人，进入人工处理", logid);
				trainService.insertHistory(orderId, "没有空闲预定机器人，进入人工处理");
				return null;
			} else if (account == null) {
				logger.info("{}没有空闲账号，进入人工处理");
				trainService.insertHistory(orderId, "没有空闲账号，进入人工处理");
				return null;
			} else {
				StringBuilder optlog = new StringBuilder();
				optlog.append("预订车票分配的帐号ID:").append(account.getId());
				optlog.append(",分配的帐号:").append(account.getUsername());
				optlog.append(",分配的帐号来源为:").append(order.getAccountFromWay()).append("[0:公司账号, 1:乘客自有]");
				trainService.insertHistory(orderId, optlog.toString());
			}
		} catch (Exception e) {
			logger.info("{}【系统异常】:系统重新获取处理人和处理账号信息发生异常", logid, e);
			try {
				trainService.insertHistory(orderId, "获取账号和机器人异常");
			} catch (Exception e2) {
				logger.info("{}insertHistory() Exception", logid, e);
			}
			return null;
		}

		IRequest request = null;
		String workerName = worker.getWorkerName();
		Integer workerType = worker.getWorkerType();
		logger.info("{}worker.getWorkerType():", logid, workerType);
		if (workerType.equals(Worker.TYPE_BOOK)) {
			logger.info("{}预订机器人", logid);
			request = new RobotOrderRequest(account, worker);
		} else if (workerType.equals(Worker.TYPE_MANUAL)) {
			logger.info("{}人工处理", logid);
			request = new ManualRequest(account, worker);
		} else {
			logger.info("{}{} 出现未知机器人状态{}", logid, workerName, workerType);
		}
		return request;
	}

	public IRequest select19e(Order order) {
		// 获取处理人和处理账号信息
		Account account = null;
		Worker worker = null;
		CtripAcc ctrip = null;
		try {
			// start 订单为“开始出票”状态的处理：根据规则获取账号和订购人员信息，准备调用相应的方法进行火车票的订购。
			// 目前支持人工和机器人订购。
			logger.info("ctrip start select:" + order.getOrderId() + " status:" + order.getOrderStatus());
			if ("00,01".contains(order.getOrderStatus())) {
				int ret = 1;
				Map<String, String> map = new HashMap<String, String>();
				map.put("order_id", order.getOrderId());
				map.put("channel", order.getChannel());
				map.put("acc_id", order.getAcc_id() + "");
				map.put("pay_money", order.getPaymoney());// 订单支付金额
				// ctrip_balance、ctrip_order_num_min、ctrip_order_num_max
				String ctrip_balance = trainService.getSysSettingValue("ctrip_balance");// 携程订单金额和余额最小差值
				String ctrip_order_num_min = trainService.getSysSettingValue("ctrip_order_num_min");// 携程已出票订单个数最小值
				String ctrip_order_num_max = trainService.getSysSettingValue("ctrip_order_num_max");// 携程已出票订单个数最大值
				map.put("ctrip_balance", ctrip_balance);
				map.put("ctrip_order_num_min", ctrip_order_num_min);
				map.put("ctrip_order_num_max", ctrip_order_num_max);

				// PC端和APP端获取携程账号时的标识
				map.put("manual_order", order.getManual_order());

				if (StringUtils.equals(order.getOrderStatus(), "00")) {
					logger.info("ctrip Get Account and Worker, orderid:" + order.getOrderId());
					ret = trainService.selectOrderAccountAndWorkerAndCtripAccBy(map, order);
				} else if (StringUtils.equals(order.getOrderStatus(), "01")) {
					logger.info("ctrip ReGet Account and Worker, orderid:" + order.getOrderId());
					ret = trainService.reGetAccountAndWorkerAndCtripAccBy(map);
				} else {
					logger.warn("exception info:" + order.getOrderId());
				}

				logger.info(order.getOrderId() + " ret:" + ret);
				if (ret == 1) {
					return null;
				} // end
			} else {
				logger.info("异常状态请求，请确定是否修改数据库？状态:" + order.getOrderStatus());
			}
			// end

			account = trainService.getAccount();
			worker = trainService.getWorker();
			ctrip = trainService.getCtripAcc();
		} catch (Exception e) {
			logger.error("get account and worker is error!", e);
		}

		// start 账号和工人都不存在，系统重新获取相应的信息
		try {
			if (worker == null) {
				logger.error(order.getOrderId() + " work is null error!");
				trainService.insertHistory(order.getOrderId(), "没有空闲预定机器人，进入人工处理");
				return null;
			} else if (account == null) {
				logger.error(order.getOrderId() + " account is null error!");
				trainService.insertHistory(order.getOrderId(), "没有空闲账号，进入人工处理");
				return null;
			} else if (ctrip == null) {
				logger.error(order.getOrderId() + " ctripAcc is null error!");
				trainService.insertHistory(order.getOrderId(), "没有空闲携程账号，进入人工处理");
				return null;
			} else {
				StringBuffer optlog = new StringBuffer();
				optlog.append("预定车票分配的帐号ID：").append(account.getId());
				optlog.append("分配的帐号：").append(account.getUsername());
				optlog.append("携程帐号：").append(ctrip.getCtrip_name());
				try {
					trainService.insertHistory(order.getOrderId(), optlog.toString());
				} catch (Exception e) {
					logger.info(" insert log error", e);
				}
			}
		} catch (Exception e) {
			logger.error("insert history error!");
		}

		// end

		IRequest request = null;

		logger.info("select account:" + account.getId() + " workertype:" + worker.getWorkerType());

		if ("00,01".contains(order.getOrderStatus())) {
			if (worker.getWorkerType().equals(Worker.TYPE_BOOK)) {
				logger.info("RobotOrderRequest order:" + order.getOrderId());
				request = new RobotOrderRequest(account, worker);
			} else if (worker.getWorkerType().equals(Worker.TYPE_MANUAL)) {
				logger.info("ManualRequest order:" + order.getOrderId());
				request = new ManualRequest(account, worker);
			} else if (worker.getWorkerType().equals(Worker.TYPE_CTRIP)) {
				logger.info("CtripOrderRequest order:" + order.getOrderId());
				request = new CtripOrderRequest(account, worker, ctrip);
			} else {
				logger.warn("exception!!!!");
			}
		} else {
			logger.warn("exception!!!!");
		}

		return request;
	}

	/**
	 * 获取京东账号，12306账号，京东预定机器人和京东预付卡信息
	 * 
	 * @param order
	 * @return
	 */
	public IRequest selectJD(Order order) {
		// TODO 获取出票渠道，用户名，密码，所在地址

		// 获取处理人和处理账号信息
		Account account = null;// 12306出票账号
		Worker worker = null;// 京东预定机器人
		JdAcc jdAcc = null;// 京东账号
		List<JdPrePayCard> jdPrePayCardList = null;// 匹配到的京东预付卡列表
		try {
			// start 订单为“开始出票”状态的处理：根据规则获取账号和订购人员信息，准备调用相应的方法进行火车票的订购。
			// 目前支持人工和机器人订购。
			logger.info("JD start select:" + order.getOrderId() + " status:" + order.getOrderStatus());
			if ("00,01".contains(order.getOrderStatus())) {
				int ret = 1;
				Map<String, String> map = new HashMap<String, String>();
				map.put("order_id", order.getOrderId());
				map.put("channel", order.getChannel());
				map.put("acc_id", order.getAcc_id() + "");
				map.put("pay_money", order.getPaymoney());// 订单支付金额
				map.put("manual_order", order.getManual_order());

				// 重发类型标识
				if (null != order.getResendIndetify()) {
					map.put("resend_identify", String.valueOf(order.getResendIndetify()));

					logger.info(order.getOrderId() + " 京东预定重发时，重发类型标识为：" + map.get("resend_identify"));
				}

				if (StringUtils.equals(order.getOrderStatus(), "00")) {
					logger.info("JD Get Account and Worker, orderid:" + order.getOrderId());
					ret = trainService.selectJDAllBookParams(map, order);
				} else if (StringUtils.equals(order.getOrderStatus(), "01")) {
					logger.info("JD ReGet Account and Worker, orderid:" + order.getOrderId());
					// 重发
					ret = trainService.reSelectJDAllBookParams(map);
				} else {
					logger.warn("exception info:" + order.getOrderId());
				}

				logger.info(order.getOrderId() + " ret:" + ret);
				if (ret == 1) {
					return null;
				} // end
			} else {
				logger.info("异常状态请求，请确定是否修改数据库？状态:" + order.getOrderStatus());
			}
			// end

			account = trainService.getAccount();
			worker = trainService.getWorker();
			jdAcc = trainService.getJdAcc();
			jdPrePayCardList = trainService.getJdPrePayCardList();
		} catch (Exception e) {
			logger.error("get account and worker is error!", e);
		}

		// start 账号和工人都不存在，系统重新获取相应的信息
		try {
			if (worker == null) {
				logger.error(order.getOrderId() + " work is null error!");
				trainService.insertHistory(order.getOrderId(), "没有空闲预定机器人，进入人工处理");
				return null;
			} else if (account == null) {
				logger.error(order.getOrderId() + " account is null error!");
				trainService.insertHistory(order.getOrderId(), "没有空闲账号，进入人工处理");
				return null;
			} else if (jdAcc == null) {
				logger.error(order.getOrderId() + " JD is null error!");
				trainService.insertHistory(order.getOrderId(), "没有空闲京东账号，进入人工处理");
				return null;
			} else if (jdPrePayCardList.size() == 0) {
				logger.error(order.getOrderId() + " JD_pre_paycard is null error!");
				trainService.insertHistory(order.getOrderId(), "没有空闲京东预付卡，进入人工处理");
				return null;
			} else {
				StringBuffer optlog = new StringBuffer();
				optlog.append("京东预定分配的帐号ID:").append(account.getId());
				optlog.append(" 分配的12306帐号:").append(account.getUsername());
				optlog.append(" 京东帐号:").append(jdAcc.getAccountName());
				optlog.append(" 京东帐号密码:").append(jdAcc.getAccountPwd());
				optlog.append(" 匹配到的优惠券编号:").append(jdAcc.getCouponNo());
				optlog.append(" 匹配到的京东预付卡个数为:").append(jdPrePayCardList.size());
				for (int i = 0; i < jdPrePayCardList.size(); i++) {
					optlog.append(" 第" + (i + 1) + "个京东预付卡卡号为:").append(jdPrePayCardList.get(i).getCardNo());
				}

				try {
					trainService.insertHistory(order.getOrderId(), optlog.toString());
				} catch (Exception e) {
					logger.info(" insert log error", e);
				}
			}
		} catch (Exception e) {
			logger.error("insert history error!");
		}

		// end

		logger.info("select account:" + account.getId() + " workertype:" + worker.getWorkerType());
		IRequest request = null;

		if ("00,01".contains(order.getOrderStatus())) {
			if (worker.getWorkerType().equals(Worker.TYPE_BOOK)) {
				// 京东预定机器人没有了，随机取一个预定机器人，走京东
				logger.info("JDOrderRequest order:" + order.getOrderId());
				request = new JDOrderRobotRequest(account, worker, jdAcc, jdPrePayCardList);
			} else if (worker.getWorkerType().equals(Worker.TYPE_MANUAL)) {
				logger.info("ManualRequest order:" + order.getOrderId());
				request = new ManualRequest(account, worker);
			} else if (worker.getWorkerType().equals(Worker.TYPE_JD)) {
				logger.info("JDOrderRequest order:" + order.getOrderId());
				request = new JDOrderRobotRequest(account, worker, jdAcc, jdPrePayCardList);
			} else {
				logger.warn("exception!!!!");
			}
		} else {
			logger.warn("exception!!!!");
		}

		return request;
	}

}
