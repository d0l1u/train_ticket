package com.l9e.train.channel.request.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONObject;
import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.HttpClientUtil;

/**
 * 机器人预定类
 * 
 * @author guobin
 *
 */
public class RobotCancelRequest extends RequestImpl {

	private Logger logger = Logger.getLogger(RobotCancelRequest.class);

	public RobotCancelRequest(Account account, Worker worker) {
		super(account, worker);
	}

	@Override
	public Result request(Order order) {
		TrainServiceImpl service = new TrainServiceImpl();

		String orderId = order.getOrderId();
		logger.info("订单号：" + orderId + ",订单状态：" + order.getOrderStatus() + ",开始请求取消机器人~~~");
		String accUsername = account.getAccUsername();
		String accPassword = account.getAccPassword();
		String outTicketBillNo = order.getOutTicketBillNo();
		logger.info(JSONObject.toJSON(worker));
		String scriptType = worker.getScriptType();
		String publicIp = worker.getPublicIp();
		String osType = worker.getOsType();

		String url = "http://" + publicIp + ":18020/";

		if (scriptType.equals("2")) {
			// if (osType.equals("")) {} else {}
			url = url + "robot/cancel/pc";
		} else {
			// TODO 暂无机器人
		}

		try {
			String workerName = worker.getWorkerName();
			service.insertHistory(orderId, workerName + "申请取消");
		} catch (Exception e) {
			e.printStackTrace();
		}

		JSONObject json = new JSONObject(true);
		json.put("username", accUsername);
		json.put("password", accPassword);
		json.put("sequence", outTicketBillNo);
		json.put("orderId", orderId);

		logger.info("JAVA 请求路径:" + url + "--" + json.toString());

		HttpClientUtil httpUtil = HttpClientUtil.getInstance();
		String responseStr = httpUtil.httpJson(url, 120 * 1000, json.toJSONString());
		logger.info("机器人返回结果:" + responseStr);

		String optlog = "";
		if (StringUtils.isBlank(responseStr)) {
			optlog = "机器人返回结果为空";
			logger.info(optlog);
			result.setRetValue(Result.RESEND);
			return result;
		}

		// {"status":"3001","message":"查询车次结果为空","startMillis":1525928697558,"endMillis":1525928714042}
		JSONObject responseJson = JSONObject.parseObject(responseStr);
		String status = responseJson.getString("status");
		String message = responseJson.getString("message");

		result.setSelfId(orderId);
		result.setSheId(outTicketBillNo);

		if ("0000".equals(status)) {
			optlog = "取消成功";
			result.setRetValue(Result.SUCCESS);
			logger.info(optlog);
		} else {
			optlog = "取消失败:" + message;
			result.setRetValue(Result.MANUAL);
			result.setErrorInfo(optlog);
		}
		try {
			service.insertHistory(orderId, optlog);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
}
