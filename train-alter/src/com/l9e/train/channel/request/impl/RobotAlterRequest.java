package com.l9e.train.channel.request.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.train.common.TrainAlertBase;
import com.l9e.train.common.TrainConsts;
import com.l9e.train.po.AlterResultEntity;
import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.ReturnAlterInfo;
import com.l9e.train.po.ReturnAlterPasEntity;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.AlterServiceImpl;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.HttpUtil;

//配置robot参数
public class RobotAlterRequest extends RequestImpl {

	private Logger logger = LoggerFactory.getLogger(RobotAlterRequest.class);

	AlterServiceImpl alertServiceImpl = new AlterServiceImpl();
	TrainServiceImpl trainServiceImpl = new TrainServiceImpl();
	TrainAlertBase tab = new TrainAlertBase();

	public RobotAlterRequest(Worker worker, PayCard payCard) {
		super(worker, payCard);
	}

	@Override
	public Result request(Order order, String logid) {
		String orderId = order.getOrderId();
		String changeId = order.getChangeId();
		logger.info(logid + "等待改签 ORDER_ID:" + orderId + ", CHANAGE_ID:" + changeId);
		logger.info(logid + "Worker:" + worker);

		Integer isChangeTo = order.getIsChangeTo();
		logger.info(logid + "操作类型:" + isChangeTo + "[0:改签， 1:变更到站]");

		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();

		// 判断改签方式。0:人工改退 ，1:机器改退
		String refundAndAlert = "0";
		try {
			sysImpl.querySysVal("refund_and_alert");
			if (StringUtils.isNotEmpty(sysImpl.getSysVal())) {
				refundAndAlert = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			logger.info(logid + "查询改签方式 Exception！", e);
		}
		logger.info(logid + "改签方式:" + refundAndAlert + "(0:人工改退 ，1:机器改退)");

		List<OrderCP> cps = order.getCps();
		BigDecimal total = new BigDecimal(0);
		logger.info(logid + "###........... ");
		for (OrderCP cp : cps) {
			BigDecimal cpMoney = new BigDecimal(cp.getBuyMoney());
			logger.info(logid + "### cpMoney:" + cpMoney);
			total = total.add(cpMoney);
		}
		logger.info(logid + "### totalMoney:" + total);
		order.setBuy_money(total.toString());

		Map<String, String> map = new HashMap<String, String>();
		map.put("logid", logid);
		map.put("order_id", orderId);
		map.put("type", "ALL");
		// 0、人工改退
		if (refundAndAlert.equals("0")) {
			logger.info(logid + "后台为人工改签，直接修改为‘机器改签失败03--人工改签’状态！");
			String opt_log = "后台为人工改签，直接修改为‘机器改签失败03--人工改签’状态";
			for (OrderCP cp : cps) {
				try {
					map.put("order_status", Order.RESIGNING); // 机器正在改签
					map.put("new_order_status", Order.ARTIFICIAL_RESIGN); // 人工改签

					trainServiceImpl.updateOrderStatus(map);// 更新退款状态
					trainServiceImpl.insertHistory(orderId, cp.getCpId(), opt_log);// 插入日志
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			result.setRetValue(Result.MANUAL);// 人工操作
			return result;
		}

		/*
		 * 然而不知道有什么卵用 int resend_num = 0; if (null !=
		 * MemcachedUtil.getInstance().getAttribute("alter_resend_times_" +
		 * order.getOrderId())) { resend_num = Integer.valueOf(String
		 * .valueOf(MemcachedUtil.getInstance().getAttribute(
		 * "alter_resend_times_" + order.getOrderId()))); } logger.info(
		 * "start RobotOrderRequest orderid:" + order.getOrderId());
		 */

		// 打码方式。0:手动打码， 1:机器识别
		String randCodeorderype = "0";
		try {
			sysImpl.querySysVal("rand_code_type");
			if (StringUtils.isNotEmpty(sysImpl.getSysVal())) {
				randCodeorderype = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(logid + "打码方式:" + refundAndAlert + "(0:人工 ，1:机器)");
		order.setInputCode(randCodeorderype);

		try {
			// 查询车票是否出票
			String oldTravelTime = order.getTravelTime();
			String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
			logger.info(logid + "原乘车日期为:" + oldTravelTime + ", 当前时间为:" + nowDate);

			String changeStatus = order.getChangeStatus();
			logger.info(logid + "changeStatus:" + changeStatus);

			if (Order.RESIGNING.equals(changeStatus) && oldTravelTime.compareTo(nowDate) < 0) {
				logger.info(logid + "乘车日期小于当前日期，发车时间已过期，必须人工处理");
				trainServiceImpl.insertHistory(orderId, null, "发车时间已过期，必须人工处理，辛苦辛苦！");
				// map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				// map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
				result.setRetValue(Result.MANUAL);// 人工处理
				return result;
			}

			String channel = order.getChannel();
			String oldTrainNo = order.getTrainNo();
			String oldSeatType = order.getSeatType();
			String newTravelTime = order.getChangeTravelTime();
			String newTrainNo = order.getChangeTrainNo();
			String newSeatType = order.getChangeSeatType();
			String chooseSeat = order.getChooseSeat();
			logger.info(logid + "选座参数:" + chooseSeat);

			// 重置改签后坐席，仅针对卧铺
			order.setChangeSeatType(seatTypeTurn(newSeatType));
			// 重置改签前坐席,仅针对卧铺
			order.setSeatType(seatTypeTurn(oldSeatType));

			logger.info(logid + "channel::::" + channel + " old----new");
			logger.info(logid + "trainNo::::" + oldTrainNo + "----" + newTrainNo);
			logger.info(logid + "travelTime:" + oldTravelTime + "----" + newTravelTime);
			logger.info(logid + "seatType:::" + oldSeatType + "----" + newSeatType);

			// 根据坐席类型判断是否支持批量改签(卧铺不支持批量改签)。
			String seatType = order.getSeatType();
			int cpSize = cps.size();
			if (StringUtils.isBlank(seatType) && cpSize == 0) {
				// 证明不是最新的改签订单
				logger.info(logid + "不是最新的改签订单,置为失败...");
				// TODO
			}

			logger.info(logid + "seatType:" + seatType + ", cpSize:" + cpSize);
			boolean canBatch = false;
			if (!seatType.contains("4") && !seatType.contains("5") && !seatType.contains("6") && cpSize >= 2) {
				canBatch = true;
			}
			logger.info(logid + "是否可以批量改签:" + canBatch);

			// 获取车站简码
			String fromCity = order.getFromCity();
			String toCity = order.getToCity();
			String fromCity3c = "";
			String toCity3c = "";
			if ((null != fromCity && "" != fromCity) && (null != toCity && "" != toCity)) {
				// 查询出包含站点城市和三字码的order实体
				try {
					trainServiceImpl.queryOrderCity3c(fromCity, toCity);
				} catch (Exception e) {
					logger.info("Query Station 3C Exception !", e);
				}
				Order orderCity3c = trainServiceImpl.getOrder3c();
				if (null != orderCity3c) {
					fromCity3c = orderCity3c.getFromCity_3c();
					toCity3c = orderCity3c.getToCity_3c();
				}
			}
			logger.info(logid + "From 3C:" + fromCity3c + ", To 3C:" + toCity3c);
			order.setFromCity_3c(fromCity3c);
			order.setToCity_3c(toCity3c);

			String script = worker.getScript();
			logger.info(logid + "本次改签请求脚本类型:" + script);

			String workerExt = worker.getWorkerExt();
			String publicIp = worker.getPublicIp();
			logger.info(logid + "publicIp:" + publicIp + ", workerExt:" + workerExt);

			String requestResult = "";
			// old Ticket
			String sequence = order.getOutTicketBillno();
			String username = order.getAccountName();
			String password = order.getAccountPwd();
			String trainCode = order.getTrainNo();
			String departureDate = order.getTravelTime();
			// new Ticket
			String newTrainCode = order.getChangeTrainNo();
			String newDepartureDate = order.getChangeTravelTime();

			JSONObject requestJson = new JSONObject();
			requestJson.put("orderId", orderId);
			requestJson.put("privateIp", "");
			requestJson.put("publicIp", publicIp);

			JSONObject accountJson = new JSONObject();
			accountJson.put("username", username);
			accountJson.put("password", password);
			requestJson.put("account", accountJson);

			// old Ticket
			JSONObject ticketJson = new JSONObject();
			ticketJson.put("sequence", sequence);
			ticketJson.put("trainCode", trainCode);
			ticketJson.put("departureDate", departureDate);
			// new Ticket
			ticketJson.put("newTrainCode", newTrainCode);
			ticketJson.put("newDepartureDate", newDepartureDate);
			ticketJson.put("newFromStationName", fromCity);
			ticketJson.put("newToStationName", toCity);
			ticketJson.put("newFromStationCode", fromCity3c);
			ticketJson.put("newToStationCode", toCity3c);
			ticketJson.put("chooseSeats", chooseSeat);

			if (canBatch) {
				JSONArray passengers = new JSONArray();
				for (OrderCP cp : cps) {
					JSONObject json = new JSONObject();
					json.put("passengerNo", cp.getCpId());
					json.put("name", cp.getUserName());
					json.put("subSequence", "");
					json.put("cardType", cp.getIdsType());
					json.put("cardNo", cp.getUserIds().toUpperCase().trim());
					json.put("ticketType", cp.getTicketType());
					json.put("seatType", cp.getSeatType());
					json.put("boxName", cp.getTrainBox());
					json.put("seatName", cp.getSeatNo());
					json.put("newSeatType", cp.getChangeSeatType());
					passengers.add(json);
				}
				ticketJson.put("passengers", passengers);

				requestJson.put("data", ticketJson);
				String jsonParam = requestJson.toJSONString();
				logger.info(logid + "JAVA 请求参数:" + jsonParam);

				if ("2".equals(script)) {
					trainServiceImpl.insertHistory(orderId, null, "JAVA-PC批量改签:" + publicIp);

					String pcUrl = "";
					// 判断是改签还是变更到站
					if (isChangeTo == null || isChangeTo == 0) {
						// 改签
						logger.info(logid + "执行批量改签...");
						pcUrl = "http://" + publicIp + ":18040/robot/change/pc";
					} else {
						// 变更到站
						logger.info(logid + "执行批量变更到站...");
						pcUrl = "http://" + publicIp + ":18040/robot/alter/pc";
					}

					logger.info(logid + "PC-批量改签请求路径和参数:" + pcUrl);
					requestResult = HttpUtil.postJson(pcUrl, "UTF-8", jsonParam);
					logger.info(logid + "PC-批量改签返回结果为-BEFORE:" + requestResult);
					requestResult = parseNewResult(requestResult, order);
					logger.info(logid + "PC-批量改签返回结果为-AFTER:" + requestResult);
				} else {
					String appUrl = workerExt;
					trainServiceImpl.insertHistory(orderId, null, "JAVA-APP批量改签:" + appUrl);
					// 组装路径和参数
					appUrl = tab.getAlterAllUrl(order, cps, appUrl);
					logger.info(logid + "APP-批量改签请求路径和参数:" + appUrl);

					// isInstanceIdNull如果为true,则表明请求结果中不包含
					// 获取WL-Instance-Id为空；如果为false，则表明下面的请求结果中包含
					// 获取WL-Instance-Id为空，需要继续请求
					boolean isInstanceIdNull = true;
					do {
						requestResult = HttpUtil.sendByGet(appUrl, "UTF-8", "200000", "200000");// 调用接口
						if (requestResult.contains("获取WL-Instance-Id为空")) {
							// 如果WL-Instance-Id为空，则重新调用一次请求
							isInstanceIdNull = false;
						} else {
							isInstanceIdNull = true;
						}
						// logger.info(prefix + "调用APP批量改签请求后，返回结果为:" +
						// requestResult);
						requestResult = requestResult.replace("\\\"", "\"").replace("[\"{", "[{").replace("}\"]", "}]");
					} while (!isInstanceIdNull);
					logger.info(logid + "APP-批量改签返回结果为:" + requestResult);
				}

				// 处理批量改签结果
				executeResult(order, cps, requestResult, true, logid);
			} else {
				for (int i = 1; i <= cpSize; i++) {
					OrderCP cp = cps.get(i - 1);
					logger.info(logid + "......单独改签 INDEX:" + i);
					order.setBuy_money(cp.getBuyMoney());
					JSONArray passengers = new JSONArray();
					JSONObject json = new JSONObject();
					json.put("passengerNo", cp.getCpId());
					json.put("name", cp.getUserName());
					json.put("subSequence", "");
					json.put("cardType", cp.getIdsType());
					json.put("cardNo", cp.getUserIds().toUpperCase().trim());
					json.put("ticketType", cp.getTicketType());
					json.put("seatType", cp.getSeatType());
					json.put("boxName", cp.getTrainBox());
					json.put("seatName", cp.getSeatNo());
					json.put("newSeatType", cp.getChangeSeatType());
					passengers.add(json);
					ticketJson.put("passengers", passengers);

					requestJson.put("data", ticketJson);
					String jsonParam = requestJson.toJSONString();
					logger.info(logid + "JAVA 请求参数:" + jsonParam);

					if ("2".equals(script)) {
						trainServiceImpl.insertHistory(orderId, cp.getCpId(), "JAVA-PC单独改签:" + publicIp);
						String pcUrl = "";
						// 判断是改签还是变更到站
						if (isChangeTo == null || isChangeTo == 0) {
							// 改签
							logger.info(logid + "执行单独改签...");
							pcUrl = "http://" + publicIp + ":18040/robot/change/pc";
						} else {
							// 变更到站
							logger.info(logid + "执行单独变更到站...");
							pcUrl = "http://" + publicIp + ":18040/robot/alter/pc";
						}

						logger.info(logid + "PC-单独改签请求路径和参数:" + pcUrl);

						requestResult = HttpUtil.postJson(pcUrl, "UTF-8", jsonParam);
						logger.info(logid + "PC-单独改签返回结果为-BEFORE:" + requestResult);
						requestResult = parseNewResult(requestResult, order);
						logger.info(logid + "PC-单独改签返回结果为-AFTER:" + requestResult);
					} else {
						String appUrl = tab.getAlterUrl(order, cp, workerExt);
						trainServiceImpl.insertHistory(orderId, cp.getCpId(), "JAVA-APP单独改签:" + workerExt);
						logger.info(logid + "APP-单独改签请求路径和参数:" + appUrl);
						// isInstanceIdNull如果为true,则表明请求结果中不包含,获取WL-Instance-Id为空；如果为false，则表明下面的请求结果中包含，获取WL-Instance-Id为空，需要继续请求
						boolean isInstanceIdNull = true;
						do {
							requestResult = HttpUtil.sendByGet(appUrl, "UTF-8", "200000", "200000");// 调用接口
							if (requestResult.contains("获取WL-Instance-Id为空")) {
								// 如果WL-Instance-Id为空，则重新调用一次请求
								isInstanceIdNull = false;
							} else {
								isInstanceIdNull = true;
							}
						} while (!isInstanceIdNull);

						requestResult = requestResult.replace("\\\"", "\"").replace("[\"{", "[{").replace("}\"]", "}]");
						logger.info(logid + "APP-单独改签返回结果为:" + requestResult);
					}
					// 处理单独改签结果
					executeResult(order, cps, requestResult, false, logid);
				}
			}
		} catch (Exception e) {
			logger.info(logid + "系统异常发起机器改签失败！转为人工处理", e);
			try {
				trainServiceImpl.insertHistory(orderId, null, orderId + ":系统异常发起机器改签失败！转为人工处理");
				// 转为人工处理
				map.put("type", "SINGLE");
				map.put("change_id", order.getChangeId());
				map.put("order_status", Order.RESIGNING); // 机器正在改签
				map.put("new_order_status", Order.ARTIFICIAL_RESIGN); // 人工改签
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			result.setRetValue(Result.MANUAL);// 人工处理
		}
		return result;
	}

	public String parseNewResult(String jsonStr, Order order) {

		String oldMoney = order.getBuy_money();
		String orderId = order.getOrderId();

		JSONObject error = new JSONObject();
		error.put("ErrorCode", 0);

		JSONArray errorInfo = new JSONArray();
		JSONObject info = new JSONObject();

		if (StringUtils.isBlank(jsonStr) || !jsonStr.startsWith("{")) {
			info.put("contactsnum", 0);
			info.put("outTicketBillno", "");
			info.put("orderId", orderId);
			info.put("retInfo", "JAVA-PC机器人返回结果不符合JSON格式");
			info.put("arrivetime", "");
			info.put("summoney", "");
			info.put("old_ticket_price", oldMoney);
			info.put("from", "");
			info.put("seattime", "");
			info.put("trainno", "");
			info.put("retValue", "failure");
			info.put("to", "");
			info.put("insertCaptcha", false);
			info.put("to", "");
			info.put("cps", new JSONArray());
		} else {
			JSONObject resultJson = JSONObject.parseObject(jsonStr);
			String status = resultJson.getString("status");
			if ("0000".equals(status)) {
				JSONObject body = resultJson.getJSONObject("body");
				info.put("contactsnum", 0);
				info.put("outTicketBillno", body.getString("sequence"));
				info.put("orderId", body.getString("orderId"));
				info.put("retInfo", "");
				info.put("retValue", "success");
				info.put("arrivetime", body.getString("newArrivalTime"));
				info.put("from", body.getString("newFromStationName"));
				info.put("seattime", body.getString("newDepartureTime"));
				info.put("trainno", body.getString("newTrainCode"));
				info.put("to", body.getString("newToStationName"));
				info.put("insertCaptcha", false);
				info.put("old_ticket_price", oldMoney);
				info.put("pay_limit_time", body.getJSONArray("passengers").getJSONObject(0).getString("loseTime"));

				// cps
				BigDecimal total = new BigDecimal(0);
				JSONArray cps = new JSONArray();
				JSONArray passengers = body.getJSONArray("passengers");
				for (int i = 0; i < passengers.size(); i++) {
					JSONObject passenger = passengers.getJSONObject(i);
					JSONObject cp = new JSONObject();
					cp.put("resign_flag", "");
					cp.put("return_flag", "");
					cp.put("msg", "改签待支付");
					cp.put("trainbox", passenger.getString("newBoxName"));
					cp.put("cpId", passenger.getString("passengerNo"));
					cp.put("id", passenger.getString("cardNo"));
					cp.put("seattype", passenger.getString("newSeatType"));
					cp.put("seatNo", passenger.getString("newSeatName"));
					cp.put("name", passenger.getString("name"));
					cp.put("idtype", passenger.getString("cardType"));
					cp.put("status", "05");
					cp.put("tickettype", passenger.getString("ticketType"));
					BigDecimal price = new BigDecimal(passenger.getDouble("newPrice"));
					cp.put("paymoney", price.toString());
					total = total.add(price);
					cps.add(cp);
				}
				info.put("cps", cps);
				info.put("summoney", total.toString());
			} else {
				String message = resultJson.getString("message");

				info.put("contactsnum", 0);
				info.put("outTicketBillno", "");
				info.put("orderId", orderId);
				info.put("retInfo", message);
				info.put("arrivetime", "");
				info.put("summoney", "");
				info.put("old_ticket_price", oldMoney);
				info.put("from", "");
				info.put("seattime", "");
				info.put("trainno", "");
				info.put("retValue", "failure");
				info.put("to", "");
				info.put("insertCaptcha", false);
				info.put("to", "");
				info.put("cps", new JSONArray());
			}
		}

		errorInfo.add(info);
		error.put("ErrorInfo", errorInfo);
		return error.toJSONString();
	}

	public void stractToRefund(Order order) {
		try {
			for (OrderCP cp : order.getCps()) {
				alertServiceImpl.updateElongAlterToRefund(order, cp.getCpId());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
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
	 * 批量改签返回结果处理--java版本的处理
	 * 
	 * @param order
	 * @param cpList
	 * @param jsonStr
	 * @param isBatch
	 * @param uuid
	 * @throws Exception
	 */
	public void executeResult(Order order, List<OrderCP> cpList, String jsonStr, boolean isBatch, String uuid) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String orderId = order.getOrderId();
		map.put("order_id", orderId);
		map.put("change_id", order.getChangeId());
		if (isBatch) {
			// 批量改签
			logger.info(uuid + "批量改签结果返回处理...");
			map.put("type", "ALL");
		} else {
			// 单独改签
			logger.info(uuid + "单独改签结果返回处理...");
			map.put("type", "SINGLE");
		}
		map.put("statistical", "true");
		map.put("channel", order.getChannel());

		// 改造:如果返回结果中出发和到达时间有值，则把出发，到达时间更新到数据库
		ObjectMapper mapper = new ObjectMapper();
		ReturnAlterInfo alterInfo = mapper.readValue(jsonStr, ReturnAlterInfo.class);
		AlterResultEntity alterEntity = alterInfo.getErrorInfo().get(0);

		// 新增支付截止时间
		String payLimitTime = alterEntity.getPay_limit_time();
		map.put("pay_limit_time", payLimitTime);

		// 只有订单状态不为改签待支付，才更新车次出发，到达时间
		String changeFromTime = alterEntity.getSeattime(); // 改签后的出发时间
		String changeToTime = alterEntity.getArrivetime();// 改签后的到达时间
		map.put("change_from_time", changeFromTime);
		map.put("change_to_time", changeToTime);

		if (jsonStr.contains("\"retValue\":\"failure\"")) {
			if (jsonStr.contains("没有查到该列车") || jsonStr.contains("未找到要改签的车次")) {
				logger.info(uuid + "发起机器改签失败,未找到要改签的车次，改签自动失败");
				trainServiceImpl.insertHistory(orderId, null, "未找到要改签的车次，请确认该列车出发与否或者现在是否允许订票，改签自动失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_UNFINDTRAINNO);
			} else if (jsonStr.contains("已出票")) {
				logger.info(uuid + "已出票,无法改签，改签自动失败");
				trainServiceImpl.insertHistory(orderId, null, " 已出票,无法改签，改签自动失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_YETOUTTICKET);
			} else if (jsonStr.contains("已改签")) {
				logger.info(uuid + "已改签,无法改签，改签自动失败");
				trainServiceImpl.insertHistory(orderId, null, " 已改签,不可改签，改签自动失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_YETALTERTICKET);
			} else if (jsonStr.contains("行程冲突")) {
				logger.info(uuid + "行程冲突，改签失败");
				trainServiceImpl.insertHistory(orderId, null, " 本次购票与其它订单行程冲突，改签失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_TRAVELCONFLICT);
			} else if (jsonStr.contains("您还有未处理的订单") || jsonStr.contains("存在未完成订单")) {
				logger.info(uuid + "存在未完成订单,转为人工处理");
				trainServiceImpl.insertHistory(orderId, null, "该帐号还有未处理(未支付)的订单，转为人工处理！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
				map.put("fail_reason", TrainConsts.FAILCODE_HAVEUNFINISHEDORDER);
			} else if (jsonStr.contains("没有余票") || jsonStr.contains("对应坐席已无票")) {
				logger.info(uuid + "没有余票或者对应坐席已无票，改签失败");
				trainServiceImpl.insertHistory(orderId, null, "没有余票或者对应坐席已无票，改签失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_NOTICKET);
			} else if (jsonStr.contains("系统错误")) {
				logger.info(uuid + "系统错误，转为人工处理");
				trainServiceImpl.insertHistory(orderId, null, "系统错误，转为人工处理！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
				map.put("fail_reason", TrainConsts.FAILCODE_SYSTEMERROR);
			} else if (jsonStr.contains("高消费")) {
				logger.info(uuid + "法院依法限制高消费，禁止乘坐列车**座位，改签失败");
				trainServiceImpl.insertHistory(orderId, null, "法院依法限制高消费，禁止乘坐列车**座位，改签失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_HIGHCONSUME);
			} else if (jsonStr.contains("未定义的12306错误")) {
				logger.info(uuid + "未定义的12306错误，转为人工处理");
				trainServiceImpl.insertHistory(orderId, null, "未定义的12306错误，转为人工处理！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
				map.put("fail_reason", TrainConsts.FAILCODE_UNDEFINEDERROR);
			} else if (jsonStr.contains("账号登陆失败")) {
				logger.info(uuid + "账号登陆失败，转为人工处理");
				trainServiceImpl.insertHistory(orderId, null, "账号登陆失败，转为人工处理！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
				map.put("fail_reason", TrainConsts.FAILCODE_LOGINFAIL);
			} else if (jsonStr.contains("距离开车时间太近")) {
				logger.info(uuid + "距离开车时间太近，改签失败");
				trainServiceImpl.insertHistory(orderId, null, "距离开车时间太近，改签失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_DRIVETIMECLOSE);
			} else if (jsonStr.contains("取消改签次数超过上限")) {
				logger.info(uuid + "取消改签次数超过上限，改签失败");
				trainServiceImpl.insertHistory(orderId, null, "取消改签次数超过上限，改签失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_CANCELUPTOLIMIT);
			} else if (jsonStr.contains("旅游票")) {
				logger.info(uuid + "旅游票，请到车站办理改签，改签失败");
				trainServiceImpl.insertHistory(orderId, null, "旅游票，请到车站办理改签，改签失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_TRAVELTICKET);
			} else if (jsonStr.contains("不允许改签到指定时间的车票")) {
				logger.info(uuid + "不允许改签到指定时间的车票，改签失败");
				trainServiceImpl.insertHistory(orderId, null, "不允许改签到指定时间的车票，改签失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_APPOINTTIME);
			} else if (jsonStr.contains("已退票")) {
				logger.info(uuid + "已退票，改签失败");
				trainServiceImpl.insertHistory(orderId, null, "已退票，改签失败！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.RESIGN_FAILURE);
				map.put("fail_reason", TrainConsts.FAILCODE_YETRETURNTICKET);
			} else if (jsonStr.contains("排队购票人数过多")) {
				logger.info(uuid + "排队购票人数过多，转为人工处理");
				trainServiceImpl.insertHistory(orderId, null, "排队购票人数过多，转为人工处理！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
				map.put("fail_reason", TrainConsts.FAILCODE_LISTTOOMUCH);
			} else if (jsonStr.contains("查询到的车次信息不完全")) {
				logger.info(uuid + "发起机器改签失败,转为人工处理");
				trainServiceImpl.insertHistory(orderId, null, "查询到的车次信息不完全，转为人工处理！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
			} else {
				logger.info(uuid + "机器改签发生其它异常，转为人工处理");
				trainServiceImpl.insertHistory(orderId, null, alterInfo.getErrorInfo().get(0).getRetInfo());
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
			}
			// 更新改签订单状态
			logger.info(uuid + "更新改签订单状态 map:" + map.toString());
			trainServiceImpl.updateOrderStatus(map);
		} else {

			List<ReturnAlterPasEntity> cps = alterEntity.getCps();
			if (cps.size() == 0) {
				logger.info(uuid + "机器改签返回空值！请人工确认并处理");
				trainServiceImpl.insertHistory(orderId, null, "机器改签结果返回空！请人工确认并处理！");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
				return;
			}
			// TODO 无座处理
			Integer hasSeat = order.getHasSeat();
			logger.info(uuid + "改签是否接受无座票:" + hasSeat + " [1:不改到无座票, 0:允许改到无座票]");

			for (OrderCP cp : cpList) {
				String cpId = cp.getCpId();
				String changeSeatType = cp.getChangeSeatType();

				for (ReturnAlterPasEntity entity : cps) {
					String entityCpId = entity.getCpId();
					String entitySeatNo = entity.getSeatNo();
					if (!cpId.equals(entityCpId)) {
						continue;
					}
					logger.info(uuid + "...........");
					logger.info(uuid + entityCpId + " 处理改签占位无座情况...");
					logger.info(uuid + "申请改签坐席简码:" + changeSeatType + " 9为无座");
					logger.info(uuid + "改签后的坐席:" + entitySeatNo);

					// if(!changeSeatType.equals("9")){
					// logger.info(uuid + "改签坐席不为无座，不做处理...");
					// break;
					// }

					if (StringUtils.isNotBlank(entitySeatNo) && entitySeatNo.contains("无座")) {
						logger.info(uuid + "改签申请占位无座，判断订单是否接受无座票");
						if (new Integer(0).equals(hasSeat)) {
							logger.info(uuid + "接受无座票...");
						} else {
							logger.info(uuid + "不接受无座票，返回人工改签...");
							trainServiceImpl.insertHistory(orderId, null, "改签申请不为无座，占位无座，订单不接受无座票");
							map.put("order_status", Order.RESIGNING);
							map.put("new_order_status", Order.ARTIFICIAL_RESIGN);
							trainServiceImpl.updateOrderStatus(map);
							return;
						}
					}
				}
			}

			// 是否改签成功待支付，并更新表状态的标识
			boolean ifSuccess = false;
			for (ReturnAlterPasEntity entity : cps) {
				String status = entity.getStatus();
				logger.info(uuid + "改签返回的status为:" + status);
				if ("05".equals(status)) {
					ifSuccess = true;
					// 更新车票表改签后信息
					Map<String, String> alterMap = new HashMap<String, String>();
					alterMap.put("order_id", orderId);
					alterMap.put("cp_id", entity.getCpId());
					alterMap.put("alter_train_no", order.getChangeTrainNo());
					alterMap.put("alter_seat_type", TrainConsts.getQunarSeatType().get(entity.getSeattype()));
					alterMap.put("alter_seat_no", entity.getSeatNo());
					alterMap.put("alter_train_box", entity.getTrainbox());
					alterMap.put("alter_pay_money", entity.getPaymoney());
					alterMap.put("alter_travel_time", order.getChangeTravelTime());
					alterMap.put("alter_from_time", entity.getSeattime());

					logger.info(uuid + "更新车票表改签后信息 alterMap:" + alterMap.toString());
					trainServiceImpl.updateOrderCp(alterMap);

					StringBuffer sb_success = new StringBuffer();
					sb_success.append("车票:").append(entity.getCpId())//
							.append("改签成功，改签后车次为:").append(order.getChangeTrainNo())//
							.append(";改签后日期:").append(order.getChangeTravelTime())//
							.append(";坐席为:").append(order.getChangeSeatType())//
							.append(";改签后票价为:").append(entity.getPaymoney());
					logger.info(uuid + sb_success.toString());
					trainServiceImpl.insertHistory(orderId, entity.getCpId(), sb_success.toString());
				}
			}

			if (ifSuccess) {
				/**
				 * 通过改签前总价格和改签后总价格判断支付类型
				 */
				String finalMoney = alterEntity.getSummoney();// 最终总价格
				logger.info(uuid + "改签成功时返回的最终价格finalMoney为:" + finalMoney);
				// TODO
				String oldMoney = alterEntity.getOld_ticket_price();// 改签前的总价格
				if (StringUtils.isBlank(oldMoney)) {
					logger.info(uuid + "alterEntity oldMoney empty...");
					oldMoney = order.getBuy_money();
				}
				logger.info(uuid + "改签成功时返回的改签前的价格oldMoney为:" + oldMoney);

				Double finalMoneyD = Double.valueOf(finalMoney);
				Double oldMoneyD = Double.valueOf(oldMoney);

				// 改签支付类型 1:平改 2:高改低 3:低改高
				Integer alterPayType = null;
				if (finalMoneyD > oldMoneyD) {
					alterPayType = TrainConsts.ALTER_PAY_TYPETHREE;
				} else if (finalMoneyD < oldMoneyD) {
					alterPayType = TrainConsts.ALTER_PAY_TYPESECOND;
				} else {
					alterPayType = TrainConsts.ALTER_PAY_TYPEONE;
				}

				logger.info(uuid + "改签成功时改签支付类型为:" + alterPayType + " [ 1:平改, 2:高改低, 3:低改高]");
				map.put("alter_pay_type", String.valueOf(alterPayType));

				/**
				 * 在此把待支付的总金额更新到库里 只有低改高时才更新
				 */
				Double changeReceiveMoney = null;
				if (alterPayType == TrainConsts.ALTER_PAY_TYPETHREE) {
					changeReceiveMoney = finalMoneyD;// 待支付的总金额
					logger.info(uuid + "改签成功时~低改高~待支付的总金额为:" + changeReceiveMoney);
					map.put("change_receive_money", String.valueOf(changeReceiveMoney));
				}
				map.put("type", "SINGLE");
				map.put("bookFlag", "1");
				map.put("order_status", Order.RESIGNING);
				map.put("new_order_status", Order.WAITING_CONFIRM);// 等待同程网确认
				logger.info(uuid + "update map:" + map);
				trainServiceImpl.updateOrderStatus(map);// 更新改签状态
			}
		}
	}
}
