package com.l9e.train.channel.request.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.train.common.QueryTrainBase;
import com.l9e.train.common.TrainAlertBase;
import com.l9e.train.common.TrainConsts;
import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.Result;
import com.l9e.train.po.ReturnAlterInfo;
import com.l9e.train.po.ReturnAlterPasEntity;
import com.l9e.train.po.WaitAlterEntity;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.AlterServiceImpl;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.AmountUtil;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.HttpUtil;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

//配置robot参数
public class RobotAlterRequest extends RequestImpl {

	private static Logger logger = LoggerFactory.getLogger(RobotAlterRequest.class);

	AlterServiceImpl alertServiceImpl = new AlterServiceImpl();
	TrainServiceImpl trainServiceImpl = new TrainServiceImpl();
	TrainAlertBase tab = new TrainAlertBase();
	QueryTrainBase query = new QueryTrainBase();

	public RobotAlterRequest(Worker worker) {
		super(worker);
	}

	@Override
	public Result request(Order order) {
		String orderId = order.getOrderId();
		String channel = order.getChannel();
		String prefix = "[";
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			prefix = prefix + random.nextInt(9);
		}
		prefix = prefix + "] ";
		logger.info(prefix + "------ 改签退票 ORDER_ID:" + orderId + ", CHANNEL:" + channel + " ------");
		List<OrderCP> cps = order.getCps();
		logger.info(prefix + "订单中车票张数为: " + cps.size());

		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();

		// start 改退方式:0、人工改退 1、机器改退
		String refundAndAlert = "0";
		try {
			sysImpl.querySysVal("refund_and_alert");
			if (StringUtils.isNotEmpty(sysImpl.getSysVal())) {
				refundAndAlert = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			logger.info(prefix + "获取改退方式异常", e);
		}
		logger.info(prefix + "改退方式-refundAndAlert:" + refundAndAlert + " [0:人工改退, 1:机器改退]");

		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", orderId);
		map.put("type", "ALL");
		String optlog = "";
		if (refundAndAlert.equals("0")) {
			logger.info(prefix + "后台配置为人工改签,直接修改为‘机器改签失败03--人工改签’状态!");
			String opt_log = "后台配置为人工改签,直接修改为‘机器改签失败03--人工改签’状态!";
			for (OrderCP cp : cps) {
				try {
					map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
					map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
					trainServiceImpl.updateOrderStatus(map);// 更新退款状态
					trainServiceImpl.insertHistory(orderId, cp.getCpId(), opt_log);// 插入日志
				} catch (Exception e) {
					logger.info(prefix + "更新乘客状态异常", e);
				}
			}
			result.setRetValue(Result.MANUAL);// 人工操作
			return result;
		}

		// 设置--打码方式 0:手动打码,1:机器识别
		String randCodeorderype = "0";
		try {
			sysImpl.querySysVal("rand_code_type");
			if (StringUtils.isNotEmpty(sysImpl.getSysVal())) {
				randCodeorderype = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			logger.info(prefix + "获取打码方式异常", e);
		}
		order.setInputCode(randCodeorderype);
		logger.info(prefix + "打码方式-randCodeorderype:" + randCodeorderype + " [0:手动打码, 1:机器识别]");

		// 开始机器改签start
		Map<String, String> logMap = new HashMap<String, String>();

		String jsonStr = "";
		try {
			// 查询车票是否出票
			String oldSeatType = order.getSeatType();
			String oldTravelTime = order.getTravelTime();
			String nowDate = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);
			String oldFromTime = order.getFromTime();
			String nowTime = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT3);
			logger.info(prefix + "nowDate:" + nowDate + ", oldFromTime:" + oldFromTime + ", nowTime:" + nowTime);

			if (oldTravelTime.compareTo(nowDate) < 0 || oldFromTime.compareTo(nowTime) < 0) {
				logger.info(prefix + "发车时间已过期,必须人工处理,辛苦辛苦!");
				trainServiceImpl.insertHistory(orderId, null, "发车时间已过期,必须人工处理,辛苦辛苦!");
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
				result.setRetValue(Result.MANUAL);// 人工处理
				return result;
			}

			// 自有账号
			try {
				trainServiceImpl.ishaveOwnAccount(order);
			} catch (Exception e) {
				logger.info(prefix + "获取自有账号异常", e);
			}
			String accountFromWay = order.getAccount_from_way();
			logger.info(prefix + "是否是自有账号-accountFromWay:" + accountFromWay + " [1:YES]");

			if ("1".equals(accountFromWay)) {// 1:12306自带账号
				for (OrderCP cp : cps) {
					logger.info(prefix + "12306自带账号,直接转为机器退票!");
					optlog = "12306自带账号,直接转为机器退票!";
					trainServiceImpl.insertHistory(orderId, cp.getCpId(), optlog);// 插入日志
					// 转为机器直接退票
					map.put("type", "SINGLE");
					map.put("cp_id", cp.getCpId());
					map.put("stract", "refund");
					map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
					map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS04);
					trainServiceImpl.updateOrderStatus(map);// 更新退款状态
				}
				result.setRetValue(Result.MANUAL);// 人工操作
				return result;
			}

			/*
			 * if(new
			 * Date().after(DateUtil.stringToDate(order.getFrom_time_feh(),
			 * "yyyy-MM-dd HH:mm:ss"))){
			 * trainServiceImpl.insertHistory(order.getOrderId(),null,
			 * "48小时之内，转为人工处理，辛苦辛苦！");//插入日志 map.put("order_status",
			 * TrainConsts.ROBOT_REFUND_STATUS02); map.put("new_order_status",
			 * TrainConsts.ROBOT_REFUND_STATUS03);
			 * trainServiceImpl.updateOrderStatus(map);//更新退款状态
			 * result.setRetValue(Result.MANUAL);//人工处理 return result; }
			 */

			// 获取需要筛选的车次集合
			// WaitAlterEntity entity = queryWaitFilterTrainList(order, logMap);

			// 春运期间，不改签，直接退票改造，明年春运还需改造 add 2016-12-19
			Date date = new Date();
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			int year = cal.get(Calendar.YEAR);// 获取年份
			int month = cal.get(Calendar.MONTH);// 获取月份
			int day = cal.get(Calendar.DATE);// 获取日
			if (month == 12) {
				month = 1;
			} else {
				month = month + 1;
			}
			logger.info(prefix + "当前年月日:" + year + "-" + month + "-" + day + ", 春节区间 01-01 -- 03-31");
			// 1-1
			int minMonth = 1;
			int minDay = 1;
			// 3-31
			int maxMonth = 3;
			int maxDay = 31;
			boolean bool = false;
			if ((month >= minMonth) && (month <= maxMonth)) {
				if (month == minMonth) {
					if (day >= minDay) {
						bool = true;
					}
				} else if (month == maxMonth) {
					if (day <= maxDay) {
						bool = true;
					}
				} else {
					bool = true;
				}
			}

			// 获取需要筛选的车次集合
			WaitAlterEntity entity = null;
			if (bool) {
				logger.info(prefix + "春运期间,不进行改签直接退票,entity为空!");
				entity = null;
				trainServiceImpl.insertHistory(orderId, null, "春运期间,不进行改签直接退票!");
			} else if (channel.equals("301030")) {
				logger.info(prefix + "高铁渠道退票不做改签,已被投诉 T_T~~~");
				entity = null;
				trainServiceImpl.insertHistory(orderId, null, "高铁渠道退票不做改签,直接退票!");
			} else if (channel.equals("tuniu")) {
				logger.info(prefix + "途牛渠道退票不做改签,已被投诉 T_T~~~");
				entity = null;
				trainServiceImpl.insertHistory(orderId, null, "途牛渠道退票不做改签,直接退票!");
			} else {
				logger.info(prefix + "不是春运期间,先进行改签在退票!");
				// 获取需要筛选的车次集合
				entity = queryWaitFilterTrainList(order, logMap, prefix);
				trainServiceImpl.insertHistory(orderId, null, "不是春运期间,先进行改签在退票!");
			}

			if (entity != null) {
				Double handPrice = entity.getHand_price();
				boolean wea48 = entity.isWea_48();
				logger.info(prefix + "手续费:" + handPrice);
				logger.info(prefix + "是否是48小时内:" + wea48);

				if (Double.valueOf(10000).equals(handPrice)) {
					if (wea48) {
						stractToRefund(order);
						logMap.put("no_refund", "no_refund");
						logger.info(prefix + "48小时内没有查出符合条件的车次进行改签,直接进行机器退票,请悉知!");
						trainServiceImpl.insertHistory(orderId, null, "48小时内没有查出符合条件的车次进行改签,直接进行机器退票,请悉知!");// 插入日志
					} else {
						logger.info(prefix + "没有查出符合条件的车次进行改签,建议人工处理,辛苦辛苦!");
						trainServiceImpl.insertHistory(orderId, null, "没有查出符合条件的车次进行改签,建议人工处理,辛苦辛苦!");
						// 转为人工处理
						map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
						map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
						trainServiceImpl.updateOrderStatus(map);// 更新退款状态
						result.setRetValue(Result.MANUAL);// 人工处理
						return result;
					}
				} else {
					double basePrice = 0.0;
					Date fromTimeFifd = DateUtil.stringToDate(order.getFrom_time_fifd(), "yyyy-MM-dd");
					Date fromTimeFeh = DateUtil.stringToDate(order.getFrom_time_feh(), "yyyy-MM-dd HH:mm:ss");
					Date fromTimetTfh = DateUtil.stringToDate(order.getFrom_time_tfh(), "yyyy-MM-dd HH:mm:ss");
					Double buyMoney = Double.valueOf(order.getBuy_money());

					logger.info(prefix + "fromTimeFifd:" + fromTimeFifd);
					logger.info(prefix + "fromTimeFeh:" + fromTimeFeh);
					logger.info(prefix + "fromTimetTfh:" + fromTimetTfh);
					logger.info(prefix + "buyMoney:" + buyMoney);
					date = new Date();
					if (date.before(fromTimeFifd)) {
						logger.info(prefix + "发车前15天之外");
						basePrice = buyMoney;
					} else if (date.before(fromTimeFeh)) {
						logger.info(prefix + "发车前15天-48小时");
						basePrice = buyMoney * 0.05;
					} else if (date.before(fromTimetTfh)) {
						logger.info(prefix + "发车前48小时-24小时");
						basePrice = buyMoney * 0.1;
					} else {
						logger.info(prefix + "发车前24小时之内");
						basePrice = buyMoney * 0.2;
					}

					// 手续费最少扣2块钱
					if (basePrice < 2.0) {
						basePrice = 2.0;
					}
					basePrice = Double.valueOf(String.format("%.1f", basePrice));
					Double hand_price = Double.valueOf(String.format("%.1f", handPrice));

					// 手续费最少扣2块钱
					if (hand_price < 2.0) {
						hand_price = 2.0;
					}

					String travelTime = entity.getTravel_time();
					String trainNo = entity.getTrain_no();
					logger.info(prefix + "当前车次的手续费:" + basePrice + " [" + travelTime + "]");
					logger.info(prefix + "筛选车次的手续费:" + hand_price);

					String log = "筛选出的车次手续费用:" + hand_price + " [" + trainNo + "/" + travelTime + "], 当前车次手续费:" + basePrice;
					trainServiceImpl.insertHistory(orderId, null, log);// 插入日志

					if (hand_price > basePrice) {
						if (wea48) {
							logger.info(prefix + "48小时内筛选出的车次手续费用大于等于当前车次手续费,直接进行机器退票,请悉知!");
							stractToRefund(order);
							logMap.put("no_refund", "no_refund");
							trainServiceImpl.insertHistory(orderId, null, "48小时内筛选出的车次手续费用大于等于当前车次手续费,直接进行机器退票,请悉知!");// 插入日志
						} else {
							// 转为人工处理
							logger.info(prefix + "筛选出的车次手续费用大于当前车次手续费,转为人工处理!");
							trainServiceImpl.insertHistory(orderId, null, "筛选出的车次手续费用大于当前车次手续费,转为人工处理!");// 插入日志
							map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
							map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
							trainServiceImpl.updateOrderStatus(map);// 更新退款状态
							result.setRetValue(Result.MANUAL);// 人工处理
						}
					} else if (hand_price.equals(basePrice)) {
						logger.info(prefix + "筛选出的车次手续费用等于当前车次手续费,直接进行机器退票,请悉知!");
						stractToRefund(order);
						logMap.put("no_refund", "no_refund");
						trainServiceImpl.insertHistory(orderId, null, "筛选出的车次手续费用等于当前车次手续费,直接进行机器退票,请悉知!");// 插入日志
					} else {
						String orderTrainNo = order.getTrainNo();
						logger.info(prefix + "orderTrainNo:" + orderTrainNo);
						String[] stains = orderTrainNo.split("/");
						String seatType = entity.getSeat_type();
						for (String train : stains) {
							if (oldTravelTime.equals(travelTime) && train.equals(trainNo) && oldSeatType.equals(seatType)) {
								stractToRefund(order);
								logMap.put("no_refund", "no_refund");
							}
						}
					}
				}
			} else if (entity == null) {
				// 没有筛选出合适的车次，直接进行机器退票
				stractToRefund(order);
				logMap.put("no_refund", "no_refund");
				logger.info("没有筛选出合适的车次,直接进行机器退票,请悉知!");
				// for(OrderCP cp : order.getCps()){//插入日志
				// trainServiceImpl.insertHistory(order.getOrderId(),
				// cp.getCpId(), optlog);
				// //转为人工处理
				// map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				// map.put("new_order_status",
				// TrainConsts.ROBOT_REFUND_STATUS03);
				// trainServiceImpl.updateOrderStatus(map);//更新退款状态
				// }
				// result.setRetValue(Result.MANUAL);//人工处理
				// return result;
			}

			Date fromTimeFifd = DateUtil.stringToDate(order.getFrom_time_fifd(), "yyyy-MM-dd");
			Date fromTimeFeh = DateUtil.stringToDate(order.getFrom_time_feh(), "yyyy-MM-dd HH:mm:ss");

			logger.info(prefix + "fromTimeFifd:" + fromTimeFifd);
			logger.info(prefix + "fromTimeFeh:" + fromTimeFeh);
			date = new Date();
			if ("no_refund".equals(logMap.get("no_refund"))) {
				if (entity == null) {
					optlog = "没有筛选出合适的车次,直接进行机器退票,请悉知!";
				} else if (date.before(fromTimeFifd)) {
					optlog = "15天以后发车,直接转为机器退票!";
				} else if (date.before(fromTimeFeh)) {
					optlog = "48小时内--15天以内发车,直接转为机器退票!";
				} else {
					optlog = "不用改签,直接转为机器退票!";
				}
				logger.info(prefix + optlog);

				for (OrderCP cp : cps) {
					logger.info(prefix + "orderId:" + orderId + " 开始更新退款状态,订单中车票张数为:" + cps.size());
					trainServiceImpl.insertHistory(orderId, cp.getCpId(), optlog);// 插入日志
					// 转为机器直接退票
					map.put("type", "SINGLE");
					map.put("cp_id", cp.getCpId());
					map.put("stract", "refund");
					map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
					map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS04);
					trainServiceImpl.updateOrderStatus(map);// 更新退款状态
				}
				result.setRetValue(Result.MANUAL);// 人工操作
				return result;
			}

			order.setAlterTrainNo(entity.getTrain_no());
			order.setAlterSeatType(entity.getSeat_type());
			order.setAlterTravelTime(entity.getTravel_time());
			for (OrderCP cp : cps) {
				cp.setAlterBuyMoney(entity.getSeat_price());
			}

			// 筛选出订单内成人票价，跟改签后票价进行比较
			String buyMoney = order.getBuy_money();
			double payMoney = Double.valueOf(buyMoney);
			logger.info(prefix + "payMoney:" + payMoney);
			for (OrderCP cp : cps) {
				String alterBuyMoneyStr = cp.getAlterBuyMoney();
				logger.info(prefix + "...........");
				logger.info(prefix + "alterBuyMoneyStr:" + alterBuyMoneyStr);
				if (StringUtils.isNotBlank(alterBuyMoneyStr)) {
					Double alterBuyMoney = Double.valueOf(cp.getAlterBuyMoney());
					if (payMoney < alterBuyMoney) {
						StringBuffer slog = new StringBuffer();
						slog.append(" 原车票车次为:").append(order.getTrainNo()).append(" 改签后车次为:" + order.getAlterTrainNo());
						slog.append("要改签的车次票价").append(alterBuyMoneyStr).append("大于目前车次的票价").append(payMoney).append(",转为人工处理!");
						optlog = slog.toString();
						logger.info(prefix + optlog);

						map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
						map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
						trainServiceImpl.updateOrderStatus(map);// 更新退款状态
						result.setRetValue(Result.MANUAL);// 人工处理
						trainServiceImpl.insertHistory(orderId, null, optlog);
						return result;
					}
				}
			}

			// 重置改签后坐席，仅针对卧铺
			order.setAlterSeatType(seatTypeTurn(order.getAlterSeatType()));

			StringBuffer sb = new StringBuffer();
			sb.append("改签的订单号:").append(orderId);
			sb.append(" 原车票发车日期为:").append(order.getTravelTime());
			sb.append(" 原车票车次为:").append(order.getTrainNo());
			sb.append(" 原车票坐席为:").append(TrainConsts.getSeatType().get(order.getSeatType()));
			sb.append(" 原车票票价为:").append(payMoney);
			sb.append(" 改签后发车日期为:").append(order.getAlterTravelTime());
			sb.append(" 改签后车次为:").append(order.getAlterTrainNo());
			sb.append(" 改签后坐席为:").append(TrainConsts.getSeatType().get(order.getAlterSeatType()));
			sb.append(" 改签后票价为:").append(order.getCps().get(0).getAlterBuyMoney());
			logger.info(prefix + "channel:" + channel + ":" + sb.toString());
			// 重置改签前坐席,仅针对卧铺
			order.setSeatType(seatTypeTurn(order.getSeatType()));

			// 需要改签的乘客信息
			List<OrderCP> pas_list = order.getCps();
			logger.info(prefix + "cps size:" + pas_list.size());
			// TODO 判断是不是同城的车票，同城不支持批量改签
			String seatType = order.getSeatType();
			logger.info(prefix + "seatType:" + seatType);

			String url = "";
			String workerExt = worker.getWorkerExt();
			String publicIp = worker.getPublicIp();
			String script = worker.getScript();
			logger.info(prefix + ">>> -------------");
			logger.info(prefix + ">>> workerExt:" + workerExt);
			logger.info(prefix + ">>> script:" + script);

			boolean canBath = false;
			if (!seatType.contains("4") && !seatType.contains("5") && !seatType.contains("6") && pas_list.size() >= 2) {
				canBath = true;
			}

			// 计算金额
			BigDecimal total = new BigDecimal(0);
			logger.info(prefix + "###........... ");
			for (OrderCP cp : cps) {
				BigDecimal cpMoney = new BigDecimal(cp.getBuyMoney());
				logger.info(prefix + "### cpMoney:" + cpMoney);
				total = total.add(cpMoney);
			}
			logger.info(prefix + "### totalMoney:" + total);
			order.setBuy_money(total.toString());

			// 组装JAVA-PC 参数
			// old Ticket
			String sequence = order.getOutTicketBillno();
			String username = order.getAccountName();
			String password = order.getAccountPwd();
			String trainCode = order.getTrainNo();
			String departureDate = order.getTravelTime();
			// new Ticket
			String newTrainCode = order.getAlterTrainNo();
			String newDepartureDate = order.getAlterTravelTime();

			JSONObject requestJson = new JSONObject();
			requestJson.put("orderId", orderId);

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
			ticketJson.put("newFromStationName", order.getFromStation());
			// ticketJson.put("newToStationName", toCity);
			ticketJson.put("newToStationName", order.getArriveStation());
			// ticketJson.put("newToStationCode", toCity3c);

			if (canBath) {
				JSONArray passengers = new JSONArray();
				for (OrderCP cp : cps) {
					JSONObject json = new JSONObject();
					json.put("passengerNo", cp.getCpId());
					json.put("name", cp.getUserName());
					json.put("subSequence", "");
					json.put("cardType", cp.getIdsType());
					json.put("cardNo", cp.getUserIds().toUpperCase().trim());
					json.put("seatType", order.getSeatType());
					json.put("ticketType", cp.getTicketType());
					json.put("boxName", cp.getTrainBox());
					json.put("seatName", cp.getSeatNo());
					json.put("newSeatType", order.getAlterSeatType());
					passengers.add(json);
				}
				ticketJson.put("passengers", passengers);
				requestJson.put("data", ticketJson);

				if (StringUtils.isNotBlank(script) && script.equals("2")) {
					String pcUrl = "http://" + publicIp + ":18040/robot/change/pc";
					trainServiceImpl.insertHistory(orderId, null, "JAVA-PC批量改签URL:" + pcUrl);
					logger.info(prefix + "JAVA-PC批量改签退票请求参数:" + pcUrl + ":" + requestJson.toJSONString());
					jsonStr = HttpUtil.postJson(pcUrl, "UTF-8", requestJson.toJSONString());
					logger.info(prefix + "PC-批量改签返回结果为-BEFORE:" + jsonStr);
					jsonStr = parseNewResult(jsonStr, order);
					logger.info(prefix + "PC-批量改签返回结果为-AFTER:" + jsonStr);
				} else {
					url = workerExt;
					trainServiceImpl.insertHistory(orderId, null, "LUA批量改签URL:" + url);
					// 调用改签接口
					url = tab.getAlterAllUrl(order, pas_list, workerExt);
					logger.info(prefix + "LUA批量改签退票请求参数:" + url);
					jsonStr = HttpUtil.sendByGet(url, "UTF-8", "200000", "200000");// 调用接口
					jsonStr = jsonStr.replace("\\\"", "\"").replace("[\"{", "[{").replace("}\"]", "}]");
				}
				logger.info(prefix + "批量改签返回结果:" + jsonStr);
				executeAlterAllResult(order, pas_list, jsonStr, 0, prefix);
			} else {
				for (int i = 1; i <= cps.size(); i++) {
					OrderCP cp = cps.get(i - 1);
					logger.info(prefix + "......单独改签 INDEX:" + i);
					order.setBuy_money(cp.getBuyMoney());
					JSONArray passengers = new JSONArray();
					JSONObject json = new JSONObject();
					json.put("passengerNo", cp.getCpId());
					json.put("name", cp.getUserName());
					json.put("subSequence", "");
					json.put("cardType", cp.getIdsType());
					json.put("cardNo", cp.getUserIds().toUpperCase().trim());
					json.put("ticketType", cp.getTicketType());
					json.put("seatType", order.getSeatType());
					json.put("boxName", cp.getTrainBox());
					json.put("seatName", cp.getSeatNo());
					json.put("newSeatType", order.getAlterSeatType());
					passengers.add(json);
					ticketJson.put("passengers", passengers);
					requestJson.put("data", ticketJson);

					if (StringUtils.isNotBlank(script) && script.equals("2")) {
						String pcUrl = "http://" + publicIp + ":18040/robot/change/pc";
						trainServiceImpl.insertHistory(orderId, cp.getCpId(), "JAVA-PC单独改签:" + pcUrl);
						logger.info(prefix + "PC-单独改签请求路径和参数:" + pcUrl + ":" + requestJson.toJSONString());
						jsonStr = HttpUtil.postJson(pcUrl, "UTF-8", requestJson.toJSONString());
						logger.info(prefix + "PC-单独改签返回结果为-BEFORE:" + jsonStr);
						jsonStr = parseNewResult(jsonStr, order);
						logger.info(prefix + "PC-单独改签返回结果为-AFTER:" + jsonStr);
					} else {
						// 调用改签接口
						url = tab.getAlterUrl(order, cp, workerExt);
						logger.info(prefix + "LUA单独改签退票请求参数:" + url);
						optlog = "改签机器人URL:" + workerExt;
						trainServiceImpl.insertHistory(orderId, cp.getCpId(), optlog);
						jsonStr = HttpUtil.sendByGet(url, "UTF-8", "200000", "200000");// 调用接口
						jsonStr = jsonStr.replace("\\\"", "\"").replace("[\"{", "[{").replace("}\"]", "}]");
					}
					logger.info(prefix + "改签返回结果:" + jsonStr);
					executeAlterResult(order, cp.getCpId(), pas_list, jsonStr, 0, prefix);
				}
				return result;

			}
		} catch (Exception e) {
			logger.info(prefix + "系统异常发起机器改签失败！转为人工处理", e);
			try {
				trainServiceImpl.insertHistory(orderId, null, "系统异常发起机器改签失败！转为人工处理" + jsonStr);
				// 转为人工处理
				for (OrderCP cp : order.getCps()) {
					map.put("type", "SINGLE");
					map.put("cp_id", cp.getCpId());
					map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
					map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
					trainServiceImpl.updateOrderStatus(map);// 更新退款状态
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			result.setRetValue(Result.MANUAL);// 人工处理

		}
		// 开始机器改签end
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

	// 查询需要进行票价筛选的车次信息集合
	public WaitAlterEntity queryWaitFilterTrainList(Order alter, Map<String, String> logMap, String prefix) throws Exception {
		// 调用余票查询
		Map<String, String> map = new HashMap<String, String>();
		map.put("order_id", alter.getOrderId());
		map.put("train_no", alter.getTrainNo());
		map.put("from_station", alter.getFromStation());
		map.put("arrive_station", alter.getArriveStation());
		map.put("buy_money", alter.getBuy_money());
		map.put("seat_type", alter.getSeatType());
		String travel_time = DateUtil.stringToString(alter.getFromTime(), DateUtil.DATE_FMT1);
		String now_date = DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1);// 今天
		String tom_date = DateUtil.dateAddDaysFmt3(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1), "1");// 明天
		String sec_date = DateUtil.dateAddDaysFmt3(DateUtil.dateToString(new Date(), DateUtil.DATE_FMT1), "2");// 后天

		if (DateUtil.weatherChunYun(travel_time)) {
			stractToRefund(alter);
			logger.info(prefix + "[2016春运期间发车]直接退票");
			logMap.put("no_refund", "no_refund");
			return null;
		}

		if (now_date.equals(travel_time)) {// 当天出发的车次--------直接退票
			stractToRefund(alter);
			logger.info(prefix + "[当天发车]直接退票");
			logMap.put("no_refund", "no_refund");
			return null;
		} else if (new Date().before(DateUtil.stringToDate(alter.getFrom_time_fifd(), "yyyy-MM-dd"))) {// 15天之后
			stractToRefund(alter);
			logger.info(prefix + "直接退票");
			logMap.put("no_refund", "no_refund");
			return null;
		} else if (new Date().before(DateUtil.stringToDate(alter.getFrom_time_feh(), "yyyy-MM-dd HH:mm:ss"))) {// 48小时以后---15天之内
			// 48小时以后，15天以内，改签到15天以后的最低票价车次
			/*
			 * String alter_day = DateUtil.exceptionDay(15);
			 * map.put("travel_time",alter_day);
			 */

			// 改签新规定:48小时以后，15天以内-----直接退票
			stractToRefund(alter);
			logger.info(prefix + "[48小时以后，15天以内]" + alter.getOrderId() + "直接退票");
			logMap.put("no_refund", "no_refund");
			return null;
		} else if (new Date().before(DateUtil.stringToDate(alter.getFrom_time_tfh(), "yyyy-MM-dd HH:mm:ss"))) {// 24小时以后---48小时之内
			if (tom_date.equals(travel_time)) {// 48小时内，明天发车的----直接退票
				stractToRefund(alter);
				logger.info(prefix + "[48小时以内，明天发车]" + alter.getOrderId() + "直接退票");
				logMap.put("no_refund", "no_refund");
				return null;
			} else {
				map.put("alter_time_limit", "48hour_inner");
				map.put("travel_time", travel_time);
			}
		} else {// 24小时之内
				// 48小时以内，改签到当天的最低票价车次
			if (now_date.equals(travel_time)) {// 24小时之内，当天发车
				stractToRefund(alter);
				logger.info(prefix + "[24小时以内，当天发车]" + alter.getOrderId() + "直接退票");
				logMap.put("no_refund", "no_refund");
				return null;
			} else {
				map.put("alter_time_limit", "24hour_inner");
				map.put("travel_time", travel_time);
			}
		}
		return query.newQueryData(map);
	}

	public void stractToRefund(Order order) {
		try {
			logger.info(order.getOrderId() + ":stractToRefund~订单中车票张数为: " + order.getCps().size());
			for (OrderCP cp : order.getCps()) {
				alertServiceImpl.updateElongAlterToRefund(order, cp.getCpId());
			}
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
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

	// 批量改签返回结果处理
	public void executeAlterAllResult(Order order, List<OrderCP> pasList, String jsonStr, int weaCon, String prefix) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String orderId = order.getOrderId();
		String channel = order.getChannel();

		map.put("order_id", orderId);
		map.put("type", "ALL");
		map.put("statistical", "true");
		map.put("channel", channel);
		// 新增是否是咱们自己改签的标识
		map.put("alter_myself", "1");// 是否是咱们自己改签的订单 0:否 1:是

		String alter_fail_cp = "";
		if (jsonStr.contains("\"retValue\":\"failure\"")) {
			/*
			 * trainServiceImpl.insertHistory(orderId, null, "发起机器改签失败,转为人工处理！"
			 * + jsonStr); logger.error("发起机器改签失败,转为人工处理---" + jsonStr); //
			 * 转为人工处理 map.put("order_status",
			 * TrainConsts.ROBOT_REFUND_STATUS02); map.put("new_order_status",
			 * TrainConsts.ROBOT_REFUND_STATUS03); //更新退款状态
			 * trainServiceImpl.updateOrderStatus(map);
			 */

			if (jsonStr.contains("没有查到该列车")) {
				logger.info(prefix + "没有查到该列车，请确认该列车出发与否或者现在是否允许订票，转为人工处理！");
				trainServiceImpl.insertHistory(orderId, null, "没有查到该列车，请确认该列车出发与否或者现在是否允许订票，转为人工处理！");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);
			} else if (jsonStr.contains("已出票")) {
				logger.info(prefix + "已出票,无法改签，转为人工退票");
				trainServiceImpl.insertHistory(orderId, null, "已出票,无法改签，转为人工退票！");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS07);
				map.put("return_optlog", TrainConsts.RETURN_G1);
				trainServiceImpl.updateOrderStatus(map);
			} else if (jsonStr.contains("已改签")) {
				logger.info(prefix + "已改签,无法改签，转为人工退票");
				trainServiceImpl.insertHistory(orderId, null, "已改签,请人工确认是否乘客要求改签过该车票，转为人工改签！");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				map.put("return_optlog", TrainConsts.RETURN_G2);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态、
			} else if (jsonStr.contains("改签票")) {
				logger.info(prefix + "改签票,无法改签，转为人工改签");
				trainServiceImpl.insertHistory(orderId, null, "改签票,无法改签，转为人工改签！");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				map.put("return_optlog", TrainConsts.RETURN_G7);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else if (jsonStr.contains("旅游旺季")) {
				logger.info(prefix + "旅游旺季,无法改签，转为人工改签");
				trainServiceImpl.insertHistory(orderId, null, "旅游旺季,无法改签，转为人工改签！");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				map.put("return_optlog", TrainConsts.REFUNN_G8);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else if (jsonStr.contains("与本次购票行程冲突")) {
				logger.info(prefix + "行程冲突，转为人工退票");
				trainServiceImpl.insertHistory(orderId, null, "与本次购票行程冲突，转为人工改签！");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				map.put("return_optlog", TrainConsts.RETURN_G6);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else if (jsonStr.contains("您还有未处理的订单")) {
				logger.info(prefix + "该帐号还有未处理(未支付)的订单，转为人工处理！");
				trainServiceImpl.insertHistory(orderId, null, "该帐号还有未处理(未支付)的订单，转为人工处理！");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else {
				logger.info(prefix + "发起机器改签失败,转为人工处理" + jsonStr);
				trainServiceImpl.insertHistory(orderId, null, "发起机器改签失败,转为人工处理！" + jsonStr);
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			}
		} else {
			ObjectMapper mapper = new ObjectMapper();
			ReturnAlterInfo alterResult = mapper.readValue(jsonStr, ReturnAlterInfo.class);
			List<ReturnAlterPasEntity> list_pas = alterResult.getErrorInfo().get(0).getCps();
			if (list_pas.size() == 0) {
				logger.info(prefix + "机器改签返回空值！请人工确认并处理");
				trainServiceImpl.insertHistory(orderId, null, "机器改签结果返回空！请人工确认并处理！");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
				return;
			}
			boolean repeatPay = false;
			for (ReturnAlterPasEntity pape : list_pas) {
				if ("05".equals(pape.getStatus())) {
					repeatPay = true;
					// 更新车票表改签后信息
					Map<String, String> alter_map = new HashMap<String, String>();
					alter_map.put("order_id", orderId);
					alter_map.put("cp_id", pape.getCpId());
					alter_map.put("alter_train_no", order.getAlterTrainNo());
					alter_map.put("alter_seat_type", TrainConsts.getQunarSeatType().get(pape.getSeattype()));
					alter_map.put("alter_seat_no", pape.getSeatNo());
					alter_map.put("alter_train_box", pape.getTrainbox());
					alter_map.put("alter_pay_money", pape.getPaymoney());// 改签车次的票价
					alter_map.put("alter_travel_time", order.getAlterTravelTime());
					alter_map.put("alter_from_time", pape.getSeattime());

					// 新增是否是咱们自己改签的标识
					alter_map.put("alter_myself", "1");// 是否是咱们自己改签的订单 0:否 1:是
					// alter_diff_money = (buy_money-alter_buy_money)*(1-手续费)
					// ----差额
					String refund_percent = "";// 手续费百分比
					double feePercent = 0.0;
					if (new Date().before(DateUtil.stringToDate(order.getFrom_time_fifd(), "yyyy-MM-dd"))) {// 发车前15天
						refund_percent = "0";
						feePercent = 0.0;
					} else if (new Date().before(DateUtil.stringToDate(order.getFrom_time_feh(), "yyyy-MM-dd HH:mm:ss"))) {// 发车前48小时
						refund_percent = "5%";
						feePercent = 0.05;
					} else if (new Date().before(DateUtil.stringToDate(order.getFrom_time_tfh(), "yyyy-MM-dd HH:mm:ss"))) {// 发车前24小时
						refund_percent = "10%";
						feePercent = 0.1;
					} else {
						refund_percent = "20%";
						feePercent = 0.2;
					}
					alter_map.put("refund_percent", refund_percent);
					// 计算退票金额
					double alter_diff_money = 0.0, alter_diff_money_12306 = 0.0, sxf = 0;
					double buy_money = Double.parseDouble(order.getBuy_money());
					double alter_buy_money = Double.parseDouble(pape.getPaymoney());
					alter_diff_money_12306 = (buy_money - alter_buy_money) * feePercent;// 12306收取的改签差额手续费

					if (feePercent != 0.0) {
						alter_diff_money_12306 = AmountUtil.quarterConvert(alter_diff_money_12306);// 手续费
					}

					alter_diff_money = buy_money - alter_buy_money - alter_diff_money_12306;

					// alter_diff_money =
					// (Double.parseDouble(alter.getBuy_money())-Double.parseDouble(pape.getPaymoney()))-alter_diff_money_12306;
					alter_diff_money = Double.valueOf(String.format("%.1f", alter_diff_money));
					// alter_diff_money改签差额的金额小于2元，则置为0
					if (alter_diff_money <= 2.0) {
						alter_diff_money = 0.0;
					}
					alter_map.put("alter_diff_money", alter_diff_money + "");
					logger.info(prefix + "【批量改签】order_id=" + order.getOrderId() + "|refund_percent=" + order.getRefund_percent() + "|alter_diff_money="
							+ alter_diff_money);

					trainServiceImpl.updateOrderAlter(alter_map);// 更改改签的车次信息

					trainServiceImpl.updateOrderAlterMoney(alter_map);// 更改改签差额和手续费百分比

					// map.put("cp_id", pape.getCpId());
					// map.put("type", "SINGLE");
					// map.put("order_status",
					// TrainConsts.ROBOT_REFUND_STATUS02);
					// map.put("new_order_status",
					// TrainConsts.ROBOT_REFUND_STATUS04);
					// trainServiceImpl.updateOrderStatus(map);//更新退款状态

					StringBuffer sb_success = new StringBuffer();
					sb_success.append("车票:").append(pape.getCpId()).append("改签成功，改签后车次为:").append(order.getAlterTrainNo()).append(";改签后日期:")
							.append(order.getAlterTravelTime()).append(";坐席为:").append(TrainConsts.getSeatType().get(order.getAlterSeatType()))
							.append(";改签后票价为:").append(pape.getPaymoney());
					logger.info(prefix + sb_success.toString());
					trainServiceImpl.insertHistory(orderId, pape.getCpId(), sb_success.toString());
				} else if ("04".equals(pape.getStatus())) {
					if (pape.getMsg().contains("改签中") || pape.getMsg().contains("改签待支付")) {
						if (weaCon == 0) {
							trainServiceImpl.insertHistory(orderId, null, "改签中或者改签待支付，调用继续支付机器人");
							repeatPay = true;
							break;
						} else {
							trainServiceImpl.insertHistory(orderId, null, "调用继续支付机器人,支付失败，请人工处理");
							map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
							map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
							map.put("return_optlog", TrainConsts.RETURN_G5);
							trainServiceImpl.updateOrderStatus(map);// 更新退款状态
							return;
						}
					} else {
						if ("N".equals(pape.getResign_flag())) {
							alter_fail_cp += "车票号:" + pape.getCpId() + pape.getMsg() + "，12306无法改签，请主银出手，么么哒";
						} else {
							alter_fail_cp += "车票号:" + pape.getCpId() + pape.getMsg() + "改签失败，请主银出手，么么哒";
						}
						logger.info(alter_fail_cp);
						trainServiceImpl.insertHistory(orderId, pape.getCpId(), alter_fail_cp);
					}
				}

				// else if("05".equals(pape.getStatus())){
				// if(wea_con == 0){
				// trainServiceImpl.insertHistory(order_id, null,
				// "改签中或者改签待支付，调用继续支付机器人");
				// repeatPay = true;
				// break;
				// }else{
				// trainServiceImpl.insertHistory(order_id, null,
				// "调用继续支付机器人,支付失败，请人工处理");
				// map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				// map.put("new_order_status",
				// TrainConsts.ROBOT_REFUND_STATUS03);
				// trainServiceImpl.updateOrderStatus(map);//更新退款状态
				// return;
				// }
				// }
			}
			if (repeatPay && weaCon == 0) {
				for (ReturnAlterPasEntity pape : list_pas) {
					for (OrderCP pas : pasList) {
						if (pas.getCpId().equals(pape.getCpId())) {
							pas.setTrainBox((pape.getTrainbox()));
							order.setAlterSeatType((TrainConsts.getQunarSeatType().get(pape.getSeattype())));
							pas.setSeatNo(pape.getSeatNo());
						}
					}
				}

				// 重新支付
				String url = tab.getContinuePayUrl(order, pasList, worker.getWorkerExt());
				String repay_jsonStr = HttpUtil.sendByGet(url, "UTF-8", "30000", "30000");// 调用接口
				repay_jsonStr = repay_jsonStr.replace("\\\"", "\"").replace("[\"{", "[{").replace("}\"]", "}]");
				logger.info(prefix + "继续支付返回结果:" + repay_jsonStr);
				trainServiceImpl.insertHistory(orderId, null, "调用继续支付机器人！");
				executeAlterPayAllResult(order, pasList, repay_jsonStr);
			}
		}
	}

	// 改签返回结果处理
	public void executeAlterResult(Order alter, String cp_id, List<OrderCP> pas_list, String jsonStr, int wea_con, String prefix) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		String order_id = alter.getOrderId();
		map.put("order_id", order_id);
		map.put("cp_id", cp_id);
		map.put("type", "SINGLE");
		map.put("statistical", "true");
		map.put("channel", alter.getChannel());
		// 新增是否是咱们自己改签的标识
		map.put("alter_myself", "1");// 是否是咱们自己改签的订单 0:否 1:是

		String alter_fail_cp = "";
		if (jsonStr.contains("\"retValue\":\"failure\"")) {
			// trainServiceImpl.insertHistory(order_id, null,
			// "发起机器改签失败,转为人工处理！");
			// logger.error("发起机器改签失败,转为人工处理---"+jsonStr);
			// //转为人工处理
			// map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
			// map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
			// trainServiceImpl.updateOrderStatus(map);//更新退款状态
			if (jsonStr.contains("没有查到该列车")) {
				trainServiceImpl.insertHistory(order_id, null, "没有查到该列车，请确认该列车出发与否或者现在是否允许订票，转为人工处理！");
				logger.error("发起机器改签失败,转为人工处理---" + jsonStr);
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else if (jsonStr.contains("已出票")) {
				trainServiceImpl.insertHistory(order_id, null, " 已出票,无法改签，转为人工退票！");
				logger.error("已出票,无法改签，转为人工退票--" + jsonStr);
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS07);
				map.put("return_optlog", TrainConsts.RETURN_G1);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else if (jsonStr.contains("已改签")) {
				trainServiceImpl.insertHistory(order_id, null, " 已改签,请人工确认是否乘客要求改签过该车票，转为人工改签！");
				logger.error("已改签,无法改签，转为人工退票--" + jsonStr);
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				map.put("return_optlog", TrainConsts.RETURN_G2);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else if (jsonStr.contains("改签票")) {
				trainServiceImpl.insertHistory(order_id, null, " 改签票,,无法改签，转为人工改签！");
				logger.error("改签票,,无法改签，转为人工改签--" + jsonStr);
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				map.put("return_optlog", TrainConsts.RETURN_G7);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else if (jsonStr.contains("旅游旺季")) {
				trainServiceImpl.insertHistory(order_id, null, "旅游旺季,无法改签，转为人工改签！");
				logger.error("旅游旺季,无法改签，转为人工改签--" + jsonStr);
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				map.put("return_optlog", TrainConsts.REFUNN_G8);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else if (jsonStr.contains("与本次购票行程冲突")) {
				trainServiceImpl.insertHistory(order_id, null, " 与本次购票行程冲突，转为人工改签！");
				logger.error("行程冲突，转为人工退票--" + jsonStr);
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				map.put("return_optlog", TrainConsts.RETURN_G6);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else if (jsonStr.contains("您还有未处理的订单")) {
				trainServiceImpl.insertHistory(order_id, null, "该帐号还有未处理(未支付)的订单，转为人工处理！");
				logger.error("发起机器改签失败,转为人工处理---" + jsonStr);
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			} else {
				trainServiceImpl.insertHistory(order_id, null, "发起机器改签失败,转为人工处理！");
				logger.error("发起机器改签失败,转为人工处理---" + jsonStr);
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			}
		} else {
			ObjectMapper mapper = new ObjectMapper();
			ReturnAlterInfo alterResult = mapper.readValue(jsonStr, ReturnAlterInfo.class);
			List<ReturnAlterPasEntity> list_pas = alterResult.getErrorInfo().get(0).getCps();
			if (list_pas.size() == 0) {
				trainServiceImpl.insertHistory(order_id, null, "机器改签结果返回空！请人工确认并处理！");
				logger.info(alter.getOrderId() + "机器改签返回空值！请人工确认并处理");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
				return;
			}
			boolean repeatPay = false;
			for (ReturnAlterPasEntity pape : list_pas) {
				if ("05".equals(pape.getStatus())) {
					repeatPay = true;
					// 更新车票表改签后信息
					Map<String, String> alter_map = new HashMap<String, String>();
					alter_map.put("order_id", order_id);
					alter_map.put("cp_id", cp_id);
					alter_map.put("alter_train_no", alter.getAlterTrainNo());
					alter_map.put("alter_seat_type", TrainConsts.getQunarSeatType().get(pape.getSeattype()));
					alter_map.put("alter_seat_no", pape.getSeatNo());
					alter_map.put("alter_train_box", pape.getTrainbox());
					alter_map.put("alter_pay_money", pape.getPaymoney());
					alter_map.put("alter_travel_time", alter.getAlterTravelTime());
					alter_map.put("alter_pay_money", pape.getPaymoney());
					// 新增是否是咱们自己改签的标识
					alter_map.put("alter_myself", "1");// 是否是咱们自己改签的订单 0:否 1:是

					// alter_diff_money = (buy_money-alter_buy_money)*(1-手续费)
					// ----差额
					String refund_percent = "";// 手续费百分比
					double feePercent = 0.0;
					if (new Date().before(DateUtil.stringToDate(alter.getFrom_time_fifd(), "yyyy-MM-dd"))) {// 发车前15天
						refund_percent = "0";
						feePercent = 0.0;
					} else if (new Date().before(DateUtil.stringToDate(alter.getFrom_time_feh(), "yyyy-MM-dd HH:mm:ss"))) {// 发车前48小时
						refund_percent = "5%";
						feePercent = 0.05;
					} else if (new Date().before(DateUtil.stringToDate(alter.getFrom_time_tfh(), "yyyy-MM-dd HH:mm:ss"))) {// 发车前24小时
						refund_percent = "10%";
						feePercent = 0.1;
					} else {
						refund_percent = "20%";
						feePercent = 0.2;
					}
					alter_map.put("refund_percent", refund_percent);
					// 计算退票金额
					double alter_diff_money = 0.0, alter_diff_money_12306 = 0.0, sxf = 0;
					double buy_money = Double.parseDouble(alter.getBuy_money());
					double alter_buy_money = Double.parseDouble(pape.getPaymoney());
					alter_diff_money_12306 = (buy_money - alter_buy_money) * feePercent;// 12306收取的改签差额手续费

					if (feePercent != 0.0) {
						alter_diff_money_12306 = AmountUtil.quarterConvert(alter_diff_money_12306);// 手续费
					}

					alter_diff_money = buy_money - alter_buy_money - alter_diff_money_12306;

					// alter_diff_money =
					// (Double.parseDouble(alter.getBuy_money())-Double.parseDouble(pape.getPaymoney()))-alter_diff_money_12306;
					alter_diff_money = Double.valueOf(String.format("%.1f", alter_diff_money));
					// alter_diff_money改签差额的金额小于2元，则置为0
					if (alter_diff_money <= 2.0) {
						alter_diff_money = 0.0;
					}
					alter_map.put("alter_diff_money", alter_diff_money + "");
					logger.info(
							"【批量改签】order_id=" + alter.getOrderId() + "|refund_percent=" + alter.getRefund_percent() + "|alter_diff_money=" + alter_diff_money);

					trainServiceImpl.updateOrderAlter(alter_map);
					trainServiceImpl.updateOrderAlterMoney(alter_map);// 更改改签差额和手续费百分比

					// map.put("order_status",
					// TrainConsts.ROBOT_REFUND_STATUS02);
					// map.put("new_order_status",
					// TrainConsts.ROBOT_REFUND_STATUS04);
					// trainServiceImpl.updateOrderStatus(map);//更新退款状态

					StringBuffer sb_success = new StringBuffer();
					sb_success.append("车票:").append(cp_id).append("改签成功，改签后车次为:").append(alter.getAlterTrainNo()).append(";改签后日期:")
							.append(alter.getAlterTravelTime()).append(";坐席为:").append(TrainConsts.getSeatType().get(alter.getAlterSeatType()))
							.append(";改签后票价为:").append(pape.getPaymoney());
					logger.info(alter.getOrderId() + sb_success.toString());
					trainServiceImpl.insertHistory(order_id, cp_id, sb_success.toString());
				} else if ("04".equals(pape.getStatus())) {
					if (pape.getMsg().contains("改签中") || pape.getMsg().contains("改签待支付")) {
						if (wea_con == 0) {
							trainServiceImpl.insertHistory(order_id, null, "改签中或者改签待支付，调用继续支付机器人");
							repeatPay = true;
							break;
						} else {
							trainServiceImpl.insertHistory(order_id, null, "调用继续支付机器人,支付失败，请人工处理");
							map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
							map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
							map.put("return_optlog", TrainConsts.RETURN_G5);
							trainServiceImpl.updateOrderStatus(map);// 更新退款状态
							return;
						}
					} else {
						if ("N".equals(pape.getResign_flag())) {
							alter_fail_cp += "车票号:" + pape.getCpId() + pape.getMsg() + "，12306无法改签，请主银出手，么么哒";
						} else {
							alter_fail_cp += "车票号:" + pape.getCpId() + pape.getMsg() + "改签失败，请主银出手，么么哒";
						}
						logger.info(alter_fail_cp);
						trainServiceImpl.insertHistory(order_id, pape.getCpId(), alter_fail_cp);
						// 转为人工处理
						map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
						map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
						trainServiceImpl.updateOrderStatus(map);// 更新退款状态
						return;
					}

				}
				// else if("05".equals(pape.getStatus())){
				// repeatPay = true;
				// if(wea_con!=0){
				// //转为人工处理
				// map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				// map.put("new_order_status",
				// TrainConsts.ROBOT_REFUND_STATUS03);
				// trainServiceImpl.updateOrderStatus(map);//更新退款状态
				// return;
				// }
				// }

				if (repeatPay && wea_con == 0) {
					for (ReturnAlterPasEntity pape1 : list_pas) {
						for (OrderCP pas : pas_list) {
							if (pas.getCpId().equals(pape1.getCpId())) {
								pas.setTrainBox((pape1.getTrainbox()));
								alter.setAlterSeatType((TrainConsts.getQunarSeatType().get(pape1.getSeattype())));
								pas.setSeatNo(pape1.getSeatNo());
							}
						}
					}

					// 重新支付
					String url = tab.getContinuePayUrl(alter, pas_list, worker.getWorkerExt());
					String repay_jsonStr = HttpUtil.sendByGet(url, "UTF-8", "30000", "30000");// 调用接口
					repay_jsonStr = repay_jsonStr.replace("\\\"", "\"").replace("[\"{", "[{").replace("}\"]", "}]");
					logger.info(order_id + "；继续支付返回结果:" + repay_jsonStr);
					trainServiceImpl.insertHistory(order_id, cp_id, "调用继续支付机器人！");
					executeAlterPayResult(alter, cp_id, pas_list, repay_jsonStr);
				}
			}
		}
	}

	// 批量改签支付返回结果处理
	public void executeAlterPayAllResult(Order alter, List<OrderCP> pas_list, String jsonStr)
			throws JsonParseException, JsonMappingException, IOException, RepeatException, DatabaseException {

		logger.info(alter.getOrderId() + "批量改签支付结果处理开始！");
		Map<String, String> map = new HashMap<String, String>();
		String order_id = alter.getOrderId();
		map.put("order_id", order_id);
		map.put("statistical", "true");
		map.put("channel", alter.getChannel());

		ObjectMapper mapper = new ObjectMapper();
		ReturnAlterInfo alterResult = mapper.readValue(jsonStr, ReturnAlterInfo.class);
		if (!jsonStr.contains("\"ErrorCode\":0")) {
			// 脚本错误
			Map<String, Object> resultMap = mapper.readValue(jsonStr, Map.class);
			String errorInfoMap = resultMap.get("ErrorInfo").toString();
			logger.info(alter.getOrderId() + ";结果为脚本错误的信息为" + errorInfoMap);
			trainServiceImpl.insertHistory(order_id, null, errorInfoMap);
			map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
			map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);// 人工改签

			trainServiceImpl.updateOrderStatus(map);// 更新退款状态

		} else if (jsonStr.contains("\"retValue\":\"failure\"")) {

			Map<String, Object> resultMap = mapper.readValue(jsonStr, Map.class);
			Map<String, Object> errorInfoMap = ((List<Map<String, Object>>) resultMap.get("ErrorInfo")).get(0);
			logger.info(alter.getOrderId() + ";改签支付失败时ErrorInfo的map对象为:" + errorInfoMap);
			String retInfo = errorInfoMap.get("retInfo").toString();
			logger.info(alter.getOrderId() + ";改签支付失败时的错误信息retInfo为:" + retInfo);

			trainServiceImpl.insertHistory(order_id, null, retInfo);
			logger.info(alter.getOrderId() + ";改签支付失败，转为人工改签---" + jsonStr);
			// 改签支付转人工
			map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
			map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);

			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
		} else {

			List<ReturnAlterPasEntity> list_pas = alterResult.getErrorInfo().get(0).getCps();
			if (list_pas.size() == 0) {
				trainServiceImpl.insertHistory(order_id, null, "机器改签结果返回空！请人工确认并处理！");
				logger.info(alter.getOrderId() + "机器改签返回空值！请人工确认并处理");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
				return;
			}

			logger.info(alter.getOrderId() + "批量改签支付结果处理，开始更新订单状态为开始机器退票！");
			for (ReturnAlterPasEntity pape : list_pas) {
				map.put("cp_id", pape.getCpId());
				map.put("type", "SINGLE");
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS04);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			}
			logger.info(alter.getOrderId() + "批量改签支付完成，更新订单状态为开始机器退票完成！");
		}

	}

	// 改签支付返回结果处理
	public void executeAlterPayResult(Order alter, String cp_id, List<OrderCP> pas_list, String jsonStr)
			throws JsonParseException, JsonMappingException, IOException, RepeatException, DatabaseException {

		logger.info(alter.getOrderId() + "单独改签支付结果处理开始！");
		Map<String, String> map = new HashMap<String, String>();
		String order_id = alter.getOrderId();
		map.put("order_id", order_id);
		map.put("cp_id", cp_id);
		map.put("type", "SINGLE");
		map.put("statistical", "true");
		map.put("channel", alter.getChannel());

		ObjectMapper mapper = new ObjectMapper();
		ReturnAlterInfo alterResult = mapper.readValue(jsonStr, ReturnAlterInfo.class);

		if (!jsonStr.contains("\"ErrorCode\":0")) {
			// 脚本错误
			Map<String, Object> resultMap = mapper.readValue(jsonStr, Map.class);
			String errorInfoMap = resultMap.get("ErrorInfo").toString();
			logger.info(alter.getOrderId() + ";结果为脚本错误的信息为" + errorInfoMap);
			trainServiceImpl.insertHistory(order_id, null, errorInfoMap);
			map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
			map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);// 人工改签

			trainServiceImpl.updateOrderStatus(map);// 更新退款状态

		} else if (jsonStr.contains("\"retValue\":\"failure\"")) {

			Map<String, Object> resultMap = mapper.readValue(jsonStr, Map.class);
			Map<String, Object> errorInfoMap = ((List<Map<String, Object>>) resultMap.get("ErrorInfo")).get(0);
			logger.info(alter.getOrderId() + ";改签支付失败时ErrorInfo的map对象为:" + errorInfoMap);
			String retInfo = errorInfoMap.get("retInfo").toString();
			logger.info(alter.getOrderId() + ";改签支付失败时的错误信息retInfo为:" + retInfo);

			trainServiceImpl.insertHistory(order_id, null, retInfo);
			logger.info(alter.getOrderId() + ";改签支付失败，转为人工改签---" + jsonStr);
			// 改签支付转人工
			map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
			map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);

			trainServiceImpl.updateOrderStatus(map);// 更新改签订单状态
		} else {
			List<ReturnAlterPasEntity> list_pas = alterResult.getErrorInfo().get(0).getCps();
			if (list_pas.size() == 0) {
				trainServiceImpl.insertHistory(order_id, null, "机器改签结果返回空！请人工确认并处理！");
				logger.info(alter.getOrderId() + "机器改签返回空值！请人工确认并处理");
				// 转为人工处理
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS03);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
				return;
			}

			logger.info(alter.getOrderId() + "单独改签支付结果处理，开始更新订单状态为开始机器退票！");
			for (ReturnAlterPasEntity pape : list_pas) {
				map.put("order_status", TrainConsts.ROBOT_REFUND_STATUS02);
				map.put("new_order_status", TrainConsts.ROBOT_REFUND_STATUS04);
				trainServiceImpl.updateOrderStatus(map);// 更新退款状态
			}

			logger.info(alter.getOrderId() + "单独改签支付完成，更新订单状态为开始机器退票完成！");
		}

	}

}
