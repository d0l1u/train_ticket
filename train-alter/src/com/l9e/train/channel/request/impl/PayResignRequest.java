package com.l9e.train.channel.request.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.train.common.TrainConsts;
import com.l9e.train.po.AlterResultEntity;
import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.ReturnAlterInfo;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.HttpUtil;

public class PayResignRequest extends RequestImpl {
	private Logger logger = LoggerFactory.getLogger(PayResignRequest.class);
	TrainServiceImpl trainService = new TrainServiceImpl();

	public PayResignRequest(Worker worker, PayCard payCard) {
		super(worker, payCard);
	}

	public Result request(Order order, String logid) {
		String orderId = order.getOrderId();
		String changeId = order.getChangeId();
		logger.info("{}开始支付改签订单ID:{}, CHANGE_ID:{}", logid, orderId, changeId);
		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();

		// 改退方式：0、人工改退 1、机器改退
		String refundAndAlert = "0";
		try {
			sysImpl.querySysVal("refund_and_alert");
			if (StringUtils.isNotEmpty(sysImpl.getSysVal())) {
				refundAndAlert = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			logger.info(logid + "Select ‘hc_system_setting’ Value ‘refund_and_alert’ Exception", e);
		}
		logger.info(logid + "‘hc_system_setting’ Value ‘refund_and_alert’:" + refundAndAlert + " [0:人工改退, 1:机器改退]");

		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", orderId);
		map.put("type", "ALL");
		// 人工改退
		if (refundAndAlert.equals("0")) {
			logger.info(logid + "后台配置为人工改签，不进行机器改签...");
			String opt_log = "后台配置为人工改签，不进行机器改签";
			for (OrderCP cp : order.getCps()) {
				try {
					map.put("order_status", Order.PAYING); // 机器正在支付
					map.put("new_order_status", Order.ARTIFICIAL_PAY); // 人工支付
					trainService.updateOrderStatus(map);// 更新退款状态
					trainService.insertHistory(orderId, cp.getCpId(), opt_log);// 插入日志
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			result.setRetValue(Result.MANUAL);// 人工操作
			return result;
		}

		// 重置改签前坐席,仅针对卧铺
		String seatType = order.getSeatType();
		logger.info(logid + "转换改签前坐席-BEFORE:" + seatType);
		seatType = seatTypeTurn(seatType);
		logger.info(logid + "转换改签前坐席-AFTER:" + seatType);
		order.setSeatType(seatType);

		// 重置改签后坐席，仅针对卧铺
		String changeSeatType = order.getChangeSeatType();
		logger.info(logid + "转换改签后坐席-BEFORE:" + changeSeatType);
		changeSeatType = seatTypeTurn(changeSeatType);
		logger.info(logid + "转换改签后坐席-AFTER:" + changeSeatType);
		order.setChangeSeatType(changeSeatType);

		List<OrderCP> cps = order.getCps();// 需要改签的乘客信息
		String publicIp = worker.getPublicIp();
		String workerName = worker.getWorkerName();

		// 组装参数
		// 组装ticket主体信息
		JSONObject requestJson = new JSONObject();
		requestJson.put("orderId", orderId);
		requestJson.put("publicIp", publicIp);

		JSONObject accountJson = new JSONObject();
		accountJson.put("username", order.getAccountName());
		accountJson.put("password", order.getAccountPwd());
		requestJson.put("account", accountJson);

		if (payCard != null) {
			JSONObject payJson = new JSONObject();
			payJson.put("username", payCard.getCardNo());
			payJson.put("payPassword", payCard.getCardPwd());
			requestJson.put("pay", payJson);
		}

		JSONObject ticketJson = new JSONObject();
		ticketJson.put("sequence", order.getOutTicketBillno());
		ticketJson.put("fromStationName", order.getFromCity());
		ticketJson.put("toStationName", order.getToCity());
		ticketJson.put("trainCode", order.getChangeTrainNo());
		ticketJson.put("departureDate", order.getChangeTravelTime());

		JSONArray passengerArr = new JSONArray();
		// 组装乘客信息
		for (OrderCP cp : cps) {
			JSONObject passenger = new JSONObject();
			passenger.put("passengerNo", cp.getCpId());
			passenger.put("subSequence", "");
			passenger.put("name", cp.getUserName());
			passenger.put("ticketType", cp.getTicketType());
			passenger.put("cardType", cp.getIdsType());
			passenger.put("cardNo", cp.getUserIds().toUpperCase().trim());
			passenger.put("seatType", cp.getChangeSeatType());
			passenger.put("boxName", cp.getChangeTrainBox());
			passenger.put("seatName", cp.getChangeSeatNo());
			passengerArr.add(passenger);
		}
		ticketJson.put("passengers", passengerArr);
		requestJson.put("data", ticketJson);

		Integer payType = order.getAlterPayType();
		logger.info(logid + "改签票支付方式:" + payType + " [1:平改, 2:高改低, 3:低改高]");
		try {
			String payResult = "";
			String url = "";
			if (TrainConsts.ALTER_PAY_TYPETHREE.equals(payType)) {
				// 低改高
				url = "http://" + publicIp + ":18020/robot/pay/pc";
				trainService.insertHistory(orderId, null, "低改高支付，调用支付机器人:" + workerName);
			} else {
				// 改签支付时，平改和高改低走这个
				url = "http://" + publicIp + ":18020/robot/changePay/pc";
				trainService.insertHistory(orderId, null, "平改OR高改低支付，调用支付机器人:" + workerName);
			}

			logger.info(logid + "PC-改签支付路径和参数:" + url + ":" + requestJson.toJSONString());
			payResult = HttpUtil.postJson(url, "UTF-8", requestJson.toJSONString());
			logger.info(logid + "PC-改签支付返回结果-BEFORE:" + payResult);

			// 解析新版JAVA
			JSONObject error = new JSONObject();
			error.put("ErrorCode", 0);

			JSONArray errorInfoList = new JSONArray();
			JSONObject errorInfo = new JSONObject();
			JSONObject resultJson = null;

			// 检票口数据
			JSONObject entranceInfo = null;
			try {
				resultJson = JSONObject.parseObject(payResult);
				String status = resultJson.getString("status");
				// 成功
				if ("0000".equals(status)) {
					JSONObject bodyJson = resultJson.getJSONObject("body");
					String balance = bodyJson.getString("balance");
					String sequence = bodyJson.getString("sequence");
					String paySequence = bodyJson.getString("paySequence");
					errorInfo.put("balance", balance);
					errorInfo.put("paybillno", paySequence);
					errorInfo.put("outTicketBillno", sequence);
					errorInfo.put("orderId", orderId);
					errorInfo.put("insertCaptcha", false);
					errorInfo.put("retInfo", "");
					errorInfo.put("retValue", "success");

					JSONArray jsonArray = bodyJson.getJSONArray("entranceList");
					if (jsonArray != null && !jsonArray.isEmpty()) {
						entranceInfo = jsonArray.getJSONObject(0);
					}
				} else {
					// 失败
					String message = resultJson.getString("message");
					errorInfo.put("balance", "");
					errorInfo.put("paybillno", "");
					errorInfo.put("outTicketBillno", "");
					errorInfo.put("orderId", orderId);
					errorInfo.put("insertCaptcha", false);
					errorInfo.put("retInfo", message);
					errorInfo.put("retValue", "failure");
				}
			} catch (Exception e) {
				errorInfo.put("retInfo", "JAVA-PC机器人返回结果不符合JSON格式");
				errorInfo.put("balance", "");
				errorInfo.put("paybillno", "");
				errorInfo.put("outTicketBillno", "");
				errorInfo.put("orderId", orderId);
				errorInfo.put("insertCaptcha", false);
				logger.info(logid + "【解析机器人结果异常】", e);
			}
			errorInfoList.add(errorInfo);
			error.put("ErrorInfo", errorInfoList);
			payResult = error.toJSONString();
			logger.info(logid + "PC-改签支付返回结果-AFTER:" + payResult);
			executePayResult(order, payResult, payCard, entranceInfo, logid);
		} catch (Exception e) {
			logger.info(logid + "系统异常发起机器改签支付失败！转为人工处理", e);
			try {
				trainService.insertHistory(orderId, null, "系统异常发起机器改签支付失败！转为人工处理");
				// 转为人工处理
				map.put("type", "SINGLE");
				map.put("change_id", order.getChangeId());
				map.put("order_status", Order.PAYING);
				map.put("new_order_status", Order.ARTIFICIAL_PAY);
				trainService.updateOrderStatus(map);// 更新退款状态
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			result.setRetValue(Result.MANUAL);// 人工处理
		}

		return result;
	}

	// 列车席位转换--针对卧铺
	public static String seatTypeTurn(String seat_type) {
		if (seat_type != null && !"".equals(seat_type)) {
			return seat_type.contains("6") ? "6" : seat_type.contains("5") ? "5" : seat_type.contains("4") ? "4" : seat_type;
		} else {
			return seat_type;
		}
	}

	/**
	 * @author: taoka
	 * @date: 2018年4月12日 上午10:57:50
	 * @param order
	 *            订单
	 * @param resultJson
	 *            返回结果
	 * @param payCard
	 *            支付信息
	 * @param entranceInfo
	 *            检票口
	 * @param logid
	 * @throws Exception
	 */
	public void executePayResult(Order order, String resultJson, PayCard payCard, JSONObject entranceInfo, String logid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String orderId = order.getOrderId();
		map.put("order_id", orderId);
		map.put("change_id", order.getChangeId());
		map.put("type", "SINGLE");
		map.put("statistical", "true");
		map.put("channel", order.getChannel());
		ReturnAlterInfo resultVo = new ObjectMapper().readValue(resultJson, ReturnAlterInfo.class);

		AlterResultEntity errorInfo = resultVo.getErrorInfo().get(0);
		String retValue = errorInfo.getRetValue();

		Integer payType = order.getAlterPayType();
		// 只有低改高的情况下，才会调用支付账户，并更新余额
		if (TrainConsts.ALTER_PAY_TYPETHREE == payType) {
			// 低改高的情况下，需要把支付流水号更新到elong_orderinfo_change中
			String paybillno = errorInfo.getPaybillno();// 支付流水号
			logger.info(logid + "改签支付的银行流水号为:" + paybillno);
			map.put("bankPaySeq", paybillno);

			// 更新支付账户的余额
			String balance = errorInfo.getBalance();// 支付账户余额
			logger.info(logid + "改签支付成功后，支付账户余额为:" + balance);
			trainService.updatePayCardBalance(payCard, balance);
		}

		if (retValue.equals("failure")) {
			String retInfo = errorInfo.getRetInfo();
			logger.info(logid + "改签支付失败时的错误信息retInfo为:" + retInfo);
			if (retInfo.contains("不存在改签待支付订单")) {
				logger.info(logid + "不存在改签待支付订单，请人工核实");
				trainService.insertHistory(orderId, null, "不存在改签待支付订单，请人工核实");
				// 改签支付转人工
				map.put("order_status", Order.PAYING);
				map.put("new_order_status", Order.ARTIFICIAL_PAY);
				map.put("fail_reason", TrainConsts.FAILCODE_NOPAYMENTORDER);
				trainService.updateOrderStatus(map);// 更新改签订单状态
			} else {
				logger.info(logid + "改签支付失败，" + retInfo + "，转为人工处理");
				trainService.insertHistory(orderId, null, retInfo);
				// 改签支付转人工
				map.put("order_status", Order.PAYING);
				map.put("new_order_status", Order.ARTIFICIAL_PAY);
				trainService.updateOrderStatus(map);// 更新改签订单状态
			}
		} else if (retValue.equals("success")) {
			logger.info(logid + "改签支付成功,开始更新表状态为34！");
			map.put("order_status", Order.PAYING);
			map.put("new_order_status", Order.PAY_FINISH);

			if (entranceInfo != null) {
				logger.info("{}更新检票口信息 ...", logid);
				// 更新检票口数据 TODO
				int insertEntrance = trainService.insertEntrance(order, entranceInfo,logid);
				logger.info("{}检票口更新结果:{}", logid, insertEntrance);
			}

			trainService.updateOrderStatus(map);
			trainService.insertHistory(orderId, null, "改签支付成功！");
		}
	}
}
