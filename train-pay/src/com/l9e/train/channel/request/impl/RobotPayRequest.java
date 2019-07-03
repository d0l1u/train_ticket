package com.l9e.train.channel.request.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.train.po.Account;
import com.l9e.train.po.Order;
import com.l9e.train.po.OrderCP;
import com.l9e.train.po.PayCard;
import com.l9e.train.po.Result;
import com.l9e.train.po.ReturnOptlog;
import com.l9e.train.po.ReturnVO;
import com.l9e.train.po.TicketEntrance;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.HttpUtil;
import com.l9e.train.util.MD5;
import com.l9e.train.util.MemcachedUtil;
import com.l9e.train.util.MobileMsgUtil;
import com.l9e.train.util.StrUtil;
import com.l9e.train.util.UrlFormatUtil;

public class RobotPayRequest extends RequestImpl {

	private Logger logger = LoggerFactory.getLogger(RobotPayRequest.class);

	public RobotPayRequest(Account account, Worker worker, PayCard payCard) {
		super(account, worker, payCard);
	}

	@Override
	public Result request(Order order, String logid) {

		String orderId = order.getOrderId();
		String sequence = order.getOutTicketBillNo();
		String buymoney = order.getBuymoney();
		logger.info(logid + "【开始支付订单:" + orderId + ", 12306订单:" + sequence + ", 支付金额:" + buymoney + "】...");

		TrainServiceImpl service = new TrainServiceImpl();
		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
		MemcachedUtil memcache = MemcachedUtil.getInstance();

		int resendNum = 0;
		String payResendKey = "pay_resend_times_" + orderId;
		Object payResendObj = memcache.getAttribute(payResendKey);
		if (payResendObj != null) {
			resendNum = Integer.valueOf(payResendObj.toString());
		}
		logger.info(logid + "pay_resend_times:" + resendNum);

		int failNum = 0;
		String payFailKey = "pay_fail_times_" + orderId;
		Object payFailObj = memcache.getAttribute(payFailKey);
		if (payFailObj != null) {
			failNum = Integer.valueOf(payFailObj.toString());
		}
		logger.info(logid + "pay_fail_times:" + failNum);

		int manualNum = 0;
		String payManualKey = "pay_manual_times_" + orderId;
		Object payManualObj = memcache.getAttribute(payManualKey);
		if (payManualObj != null) {
			manualNum = Integer.valueOf(payManualObj.toString());
		}
		logger.info(logid + "pay_manual_times:" + manualNum);

		// 参数
		Map<String, String> maps = new HashMap<String, String>();
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));
		maps.put("Timeout", "120000");
		maps.put("ParamCount", "1");

		String publicIp = worker.getPublicIp();
		String requestUrl = worker.getWorkerExt();

		JSONObject requestJson = new JSONObject(true);
		requestJson.put("orderId", orderId);
		requestJson.put("publicIp", publicIp);

		String kyfwUsername = account.getAccUsername();
		String kyfwPassword = account.getAccPassword();
		String payType = payCard.getPayType();
		String payClient = worker.getPay_device_type();
		logger.info(logid + "支付客户端payClient:" + payClient + " [0:PC端支付, 1:APP端支付]");

		StringBuffer sb = new StringBuffer();
		sb.append(kyfwUsername + "|");
		if ((StringUtils.isNotEmpty(payType)) && ("55".equals(payType))) {
			logger.info(logid + "支付宝支付。。。");
			if ((StringUtils.isNotBlank(payClient)) && ("1".equals(payClient))) {
				maps.put("ScriptPath", "appalipay.lua");
				sb.append(MD5.getMd5_UTF8(kyfwPassword) + "|");
			} else {
				maps.put("ScriptPath", "alipaynew.lua");
				sb.append(kyfwPassword + "|");
			}
		} else {
			logger.info(logid + "select ccb bank");
			maps.put("ScriptPath", "paymentnew.lua");
		}
		String cardNo = payCard.getCardNo();
		String cardPwd = payCard.getCardPwd();
		String cardPhone = payCard.getCardPhone();
		logger.info(logid + "支付账号:" + cardNo);

		sb.append(orderId + "|");
		sb.append(sequence + "|");
		sb.append(buymoney + "|");
		sb.append(cardNo + "|");
		sb.append(cardPwd + "|");
		sb.append(cardPhone + "|");
		sb.append(payType + "|");
		sb.append(payCard.getBankType() + "|");
		// 打码方式 [0:手动打码; 1:机器识别]
		// sysImpl.querySysVal("rand_code_type");
		sb.append("1");
		sb.append("|").append(order.getUserName());// 这个不知道什么卵用
		sb.append("|").append(worker.getWorkerId());

		JSONObject accountJson = new JSONObject(true);
		accountJson.put("username", kyfwUsername);
		accountJson.put("password", kyfwPassword);
		requestJson.put("account", accountJson);

		JSONObject alipayJson = new JSONObject(true);
		alipayJson.put("username", cardNo);
		alipayJson.put("password", "");
		alipayJson.put("payPassword", cardPwd);
		alipayJson.put("phone", cardPhone);
		alipayJson.put("payMoney", buymoney);
		requestJson.put("pay", alipayJson);

		/*
		 * 在此判断支付端是否是app端，pay_device_type 支付端类型 ： 0--PC端支付 1-APP端支付
		 * 如果走的是app端，则在Param1里加上设备号deviceNo
		 */
		if (StringUtils.isNotBlank(payClient) && "1".equals(payClient)) {
			sb.append("|").append(StrUtil.getRandomString(16));
			try {
				// 手机验证短信等待最大时间
				sysImpl.querySysVal("appPay_wait_second");
			} catch (Exception e) {
				e.printStackTrace();
			}
			String wait_second = sysImpl.getSysVal();
			if (StringUtils.isNotBlank(wait_second)) {
				sb.append("|").append(wait_second);
			}
		}

		// 支付宝打码:0:公司自己的打码动态库；1：第三方的打码动态库
		// codeEditor = service.getSysSettingValue("codeEditorSelect");
		sb.append("|").append("0");

		logger.info(logid + "计算订单时间...");
		String format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat sf = new SimpleDateFormat(format);
		String isPay = "";
		try {
			Date orderTime = order.getOutTicketTime();
			Date departureTime = order.getFromTime();
			Date nowTime = new Date();
			logger.info(logid + "订单开始支付时间为:" + sf.format(orderTime) + ", 列车发车时间为:" + sf.format(departureTime) + ", 当前时间:" + sf.format(nowTime));

			/*
			 * 在此判断订单支付时间是否超时，如果下单等待支付的开始时间与列车发车时间之间大于3小时，且等待支付时间不超过29分钟，
			 * 则isPay为1， 否则为0；
			 * 如果下单等待支付的开始时间与列车发车时间之间小于3小时，且等待支付时间不超过9分钟，则isPay为1，否则为0。
			 */
			long diffHours = 0L;
			long diffMinutes = 0L;
			DateUtil dateUtil = new DateUtil();
			if ((orderTime != null) && (departureTime != null)) {
				diffHours = dateUtil.twoDateDiffHours(orderTime, departureTime, format);
				diffMinutes = dateUtil.twoDateDiffMinutes(nowTime, orderTime, format);
			}
			logger.info(logid + "相差的小时数为:" + diffHours + ",相差的分钟数为:" + diffMinutes);
			if (diffHours >= 3L) {
				if (diffMinutes <= 29L) {
					isPay = "1";
				} else {
					isPay = "0";
				}
			} else if (diffMinutes <= 9L) {
				isPay = "1";
			} else {
				isPay = "0";
			}
			sb.append("|").append(isPay);
			logger.info(logid + "支付时间是否超时isPay:" + isPay + " [0:超时, 1:不超时]");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// 动态库或黑盒选择 0：动态库 1：黑盒
		// sysImpl.querySysVal("dynamicDepot_or_blackBox");
		sb.append("|").append("0");
		maps.put("Param1", sb.toString());

		String language = worker.getLanguage();
		logger.info(logid + "脚本语言:" + language + " [1:lua, 2:java]");

		String responseResult = "";
		if (language.equals("1")) {
			String param = "";
			try {
				param = UrlFormatUtil.createUrl("", maps);
			} catch (Exception e) {
				logger.info(logid + "【Lua Create Url Exception】", e);
			}
			logger.info(logid + "LUA Request Url:" + requestUrl);
			logger.info(logid + "LUA Request Param:" + param);
			responseResult = HttpUtil.httpPost(requestUrl, param, "UTF-8", 1000 * 60 * 5, false);
			logger.info(logid + "LUA Response Result :" + responseResult);
			responseResult = responseResult.replace("[\"{", "[{");
			responseResult = responseResult.replace("}\"]", "}]");
			responseResult = responseResult.replace("\\", "");
		} else {
			Order queryOrder = service.queryOrder(orderId);
			if (queryOrder == null) {
				result.setErrorInfo("获取订单信息异常，尚未进行支付，请开发查询错误日志!");
				result.setRetValue(Result.MANUAL);
				return result;
			}
			JSONObject ticketJson = new JSONObject(true);
			ticketJson.put("trainCode", queryOrder.getTrainno());
			ticketJson.put("sequence", queryOrder.outTicketBillNo);
			ticketJson.put("departureDate", queryOrder.getTranDate().split(" ")[0]);
			ticketJson.put("fromStationName", queryOrder.getFrom().trim());
			ticketJson.put("fromStationCode", queryOrder.getFrom3c());
			ticketJson.put("toStationName", queryOrder.getTo().trim());
			ticketJson.put("toStationCode", queryOrder.getTo3c());

			JSONArray array = new JSONArray();
			List<OrderCP> cps = queryOrder.getCps();
			Iterator<OrderCP> iterator = cps.iterator();
			while (iterator.hasNext()) {
				OrderCP cp = iterator.next();
				JSONObject cpJson = new JSONObject(true);
				String name = cp.getUsername();
				// 护照转大写
				name = name.toUpperCase().trim();
				// 新疆人
				name = name.replaceAll("\\.", "·");
				name = name.replaceAll("。", "·");
				cpJson.put("name", name);
				cpJson.put("cardType", cp.getCertType().toString());
				cpJson.put("cardNo", cp.getCertNo().toUpperCase().trim());
				cpJson.put("ticketType", cp.getTrainType().toString());
				cpJson.put("seatType", cp.getSeatType().toString());
				cpJson.put("boxName", cp.getTrainbox());
				cpJson.put("seatName", cp.getSeatNo());
				// cpJson.put("subSequence", cp.getSubSequence());
				array.add(cpJson);
			}
			ticketJson.put("passengers", array);
			requestJson.put("data", ticketJson);

			String url = "http://" + publicIp + ":18020/robot/pay/pc";
			String param = requestJson.toJSONString();
			logger.info(logid + "JAVA Request Url:" + url);
			logger.info(logid + "JAVA Request Param:" + param);
			responseResult = HttpUtil.httpPost(url, param, "UTF-8", 1000 * 60 * 5, true);
			logger.info(logid + "JAVA Response Result :" + responseResult);

			// 处理JAVA 结果
			if (responseResult.startsWith("{") && responseResult.endsWith("}")) {
				JSONObject responseJson = JSONObject.parseObject(responseResult);
				JSONObject errorJson = new JSONObject(true);
				errorJson.put("ErrorCode", 0);
				JSONObject infoJson = new JSONObject(true);
				infoJson.put("orderId", orderId);
				infoJson.put("robotNum", 1);
				infoJson.put("outTicketBillno", sequence);

				String status = responseJson.getString("status");
				String message = responseJson.getString("message");
				if (!status.equals("0000")) {
					infoJson.put("retValue", "failure");
					infoJson.put("retInfo", message);
					infoJson.put("balance", "");
					infoJson.put("paybillno", "");
					infoJson.put("ticketEntrances", new JSONArray());
				} else {
					JSONObject bodyJson = responseJson.getJSONObject("body");
					String balance = bodyJson.getString("balance");
					String paySequence = bodyJson.getString("paySequence");

					JSONArray entranceList = bodyJson.getJSONArray("entranceList");
					if (entranceList != null && !entranceList.isEmpty()) {
						JSONObject entranceJson = bodyJson.getJSONArray("entranceList").getJSONObject(0);
						String trainCode = entranceJson.getString("trainCode");
						String stationName = entranceJson.getString("stationName");
						String entrance = entranceJson.getString("entrance");

						JSONArray entranceArray = new JSONArray();
						JSONObject entranceInfo = new JSONObject(true);
						entranceInfo.put("stationName", stationName);
						entranceInfo.put("trainNum", trainCode);
						entranceInfo.put("entrance", entrance);
						entranceArray.add(entranceInfo);
						infoJson.put("ticketEntrances", entranceArray);
					}

					infoJson.put("retValue", "success");
					infoJson.put("retInfo", "");
					infoJson.put("balance", balance);
					infoJson.put("paybillno", paySequence);
				}
				JSONArray infoArray = new JSONArray();
				infoArray.add(infoJson);
				errorJson.put("ErrorInfo", infoArray);
				responseResult = errorJson.toJSONString();
			} else {
				// 不符合json格式，不做处理
			}
		}
		
		logger.info(logid + "Finally Response Result :" + responseResult);
		if (StringUtils.isBlank(responseResult)) {
			result.setErrorInfo("支付错误，没有返回结果！");
			if (resendNum >= 3) {
				result.setRetValue(Result.MANUAL);
				return result;
			}
			memcache.setAttribute(payResendKey, ++resendNum, 300000L);
			result.setRetValue(Result.RESEND);
			return result;
		}

		if (responseResult.contains("该订单不允许支付")) {
			String isClickButton = order.getIsClickButton();
			logger.info(logid + "isClickButton:" + isClickButton);
			if ("11".equals(isClickButton)) {
				result.setErrorInfo("该订单不允许支付");
				result.setRetValue(Result.FAILURE);
				return result;
			}
		}
		if (responseResult.equals(HttpUtil.TIME_OUT)//
				|| responseResult.equals(HttpUtil.CONNECT_ERROR)//
				|| responseResult.equals(HttpUtil.URL_ERROR)) {

			result.setErrorInfo("支付超时，通知管理员！");
			if (resendNum >= 3) {
				result.setRetValue(Result.MANUAL);
				return result;
			}
			memcache.setAttribute(payResendKey, ++resendNum, 300000L);
			result.setRetValue(Result.RESEND);
			return result;
		}

		if (responseResult.contains("\"ErrorCode\":9")) {
			result.setErrorInfo("机器人处理超时");
			if (resendNum >= 3) {
				result.setRetValue(Result.MANUAL);
				return result;
			}
			memcache.setAttribute(payResendKey, ++resendNum, 300000L);
			result.setRetValue(Result.RESEND);
			return result;
		}

		if (responseResult.contains("登录失败")) {
			result.setErrorInfo("机器人登录失败");
			if (resendNum >= 3) {
				result.setRetValue(Result.MANUAL);
				return result;
			}
			memcache.setAttribute(payResendKey, ++resendNum, 300000L);
			result.setRetValue(Result.RESEND);
			return result;
		}

		if (responseResult.contains("支付密码已被锁定")) {
			result.setErrorInfo("支付密码已被锁定，关闭机器人");
			try {
				sysImpl.stopWorker(worker.getWorkerId());

				sysImpl.queryRobotWarnPhone();
				String phones = sysImpl.getPhones();
				for (String phone : phones.split(",")) {
					MobileMsgUtil msg = new MobileMsgUtil();
					msg.send(phone, "支付机器人" + worker.getWorkerName() + ",支付密码已被锁定，已停用！");
				}
			} catch (Exception e) {
				logger.info(logid + "Stop Robot Error", e);
			}
			result.setRetValue(Result.MANUAL);
			return result;
		}

		ReturnVO resonse = null;
		try {
			resonse = new ObjectMapper().readValue(responseResult, ReturnVO.class);
		} catch (Exception e) {
			logger.info(logid + "【请求结果Json转Bean异常】", e);
			result.setErrorInfo("JSON分析异常:" + e.getClass().getSimpleName());
			if (resendNum >= 3) {
				result.setRetValue(Result.MANUAL);
				return result;
			}
			memcache.setAttribute(payResendKey, ++resendNum, 300000L);
			result.setRetValue(Result.RESEND);
			return result;
		}

		try {
			Integer spareThread = Integer.valueOf(resonse.getErrorInfo().get(0).getRobotNum());
			if (spareThread > 0) {
				sysImpl.updateWorkerSpareThread(worker.getWorkerId(), spareThread);
			}
		} catch (Exception e) {
			logger.info(logid + "【Update Pay_worker Spare_thread Exception】", e);
		}

		Integer errorCode = resonse.getErrorCode();
		// 失败
		if (!new Integer(0).equals(errorCode)) {
			result.setRetValue(Result.FAILURE);
			result.setErrorInfo(responseResult);
			return result;
		}

		Order errorInfo = resonse.getErrorInfo().get(0);
		String retInfo = errorInfo.getRetInfo();
		String retValue = errorInfo.getRetValue();

		String r_orderId = errorInfo.getOrderId();
		String r_sequence = errorInfo.getOutTicketBillNo();
		String paybillno = errorInfo.getPaybillno();
		String balance = errorInfo.getBalance();
		List<TicketEntrance> ticketEntrances = errorInfo.getTicketEntrances();

		result.setSelfId(orderId);
		result.setSheId(sequence);
		result.setPaybillno(paybillno);
		result.setBalance(balance);
		result.setTicketEntrances(ticketEntrances);

		// 再一次确认 订单号，12306订单号
		if (!orderId.equals(r_orderId) || !sequence.equals(r_sequence)) {
			// 人工
			logger.info(logid + "支付完成，订单号不一致，请人工核实。" + orderId + "-" + r_orderId + ", " + sequence + "-" + r_sequence);
			result.setRetValue(Result.MANUAL);
			result.setErrorInfo("支付完成，订单号不一致，请人工核实。" + orderId + "-" + r_orderId + ", " + sequence + "-" + r_sequence);
			return result;
		}

		String robotKey = "robot" + worker.getWorkerId();
		// 成功
		if (retValue.equals("success")) {
			result.setRetValue(Result.SUCCESS);
			return result;
		} else if (retValue.equals("manual")) {
			// 人工
			memcache.setAttribute(robotKey, Integer.valueOf(0));
			result.setRetValue(Result.MANUAL);
			result.setErrorInfo(retInfo);
			return result;
		} else if (retValue.equals("failure")) {
			// 失败
			result.setErrorInfo(retInfo);
			logger.info(logid + "错误信息为:" + retInfo);

			// 重发标识
			Integer isResend = 0;
			try {
				sysImpl.queryLogList();
			} catch (Exception e) {
				logger.info(logid + "【获取支付系统日志异常】", e);
			}
			List<ReturnOptlog> logList = sysImpl.getList_return();
			for (ReturnOptlog optlog : logList) {
				String return_name = optlog.getReturn_name();
				String return_id = optlog.getReturn_id();
				String return_type = optlog.getReturn_type();
				String return_join = optlog.getReturn_join();
				if (retInfo.contains(return_name)) {
					// logger.info(logid+"针对错误日志:"+return_name+ "/"+return_id);
					if ("00".equals(return_type)) {
						result.setReturnOptlog(return_id);
						if ("1".equals(return_join)) {
							isResend = Integer.valueOf(1);
							break;
						}
						isResend = Integer.valueOf(2);
						break;
					}
					if ("11".equals(return_type)) {
						result.setReturnOptlog(return_id);
						if ("1".equals(return_join)) {
							isResend = Integer.valueOf(3);
							break;
						}
						isResend = Integer.valueOf(4);
						break;
					}
					if ("22".equals(return_type)) {
						result.setReturnOptlog(return_id);
						if ("1".equals(return_join)) {
							isResend = Integer.valueOf(7);
							break;
						}
						isResend = Integer.valueOf(8);
						break;
					}
				}
			}

			if (isResend.intValue() != 0) {
				if (isResend.intValue() == 1) {
					logger.info(logid + "支付重发1");
					memcache.setAttribute(robotKey, 0);
					if (resendNum >= 3) {
						result.setRetValue(Result.MANUAL);
						return result;
					}
					memcache.setAttribute(payResendKey, ++resendNum, 90000L);
					result.setRetValue(Result.RESEND);
					return result;
				}

				if (isResend.intValue() == 2) {
					logger.info(logid + "支付重发2");
					result.setRetValue(Result.RESEND);
					return result;
				}

				String level = order.getLevel();
				if (isResend.intValue() == 3) {
					logger.info(logid + "支付重发3");
					memcache.setAttribute(robotKey, 0);
					if (failNum >= 3) {
						if (StringUtils.isNotBlank(level) && "10".equals(level)) {
							logger.info(logid + "联程订单1");
							result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再支付！</b>" + retInfo);
							result.setRetValue(Result.MANUAL);
							return result;
						}
						if (!Result.NOPASS.equals(result.getRetValue())) {
							result.setRetValue(Result.FAILURE);
						}
						return result;
					}
					memcache.setAttribute(payFailKey, ++failNum, 90000L);
					result.setRetValue(Result.RESEND);
					return result;
				}

				if (isResend.intValue() == 4) {
					logger.info(logid + "支付失败4");
					if (StringUtils.isNotBlank(level) && "10".equals(level)) {
						logger.info(logid + "联程订单2");
						result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再支付！</b>" + retInfo);
						result.setRetValue(Result.MANUAL);
						return result;
					}
					if (!Result.NOPASS.equals(result.getRetValue())) {
						result.setRetValue(Result.FAILURE);
					}
					return result;
				}

				if (isResend.intValue() == 7) {
					logger.info(logid + "支付人工1");
					memcache.setAttribute(robotKey, 0);
					if (manualNum >= 2) {
						result.setRetValue(Result.MANUAL);
						return result;
					}
					memcache.setAttribute(payManualKey, ++manualNum, 90000L);
					result.setRetValue(Result.RESEND);
					return result;
				}
				if (isResend.intValue() == 8) {
					logger.info(logid + "支付人工2");
					result.setRetValue(Result.MANUAL);
					return result;
				}
			}

			String resendLog = "";
			try {
				if (null != memcache.getAttribute("pay_resend_log")) {
					resendLog = String.valueOf(memcache.getAttribute("pay_resend_log"));
					logger.info(logid + "resend_log:" + resendLog);
				} else {
					sysImpl.querySysVal("pay_again");
					resendLog = sysImpl.getSysVal();
					memcache.setAttribute("pay_resend_log", resendLog, 5 * 60 * 1000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			String[] payLogArr = new String[10];
			if (StringUtils.isNotEmpty(resendLog)) {
				payLogArr = resendLog.split("@");
			}
			boolean ifResend = false;
			for (String str : payLogArr) {
				if (retInfo.contains(str)) {
					result.setRetValue(Result.RESEND);
					ifResend = true;
					break;
				}
			}
			if (ifResend) {
				memcache.setAttribute(robotKey, Integer.valueOf(0));
				if (resendNum >= 3) {
					result.setRetValue(Result.MANUAL);
					return result;
				}
				memcache.setAttribute(payResendKey, Integer.valueOf(++resendNum), 180000L);
				result.setRetValue(Result.RESEND);
				return result;
			}
			if ((retInfo.contains("已完成支付")) || (retInfo.contains("已出票"))) {
				result.setRetValue(Result.SUCCESS);
			} else {
				result.setRetValue(Result.MANUAL);
			}
			return result;
		}

		logger.info(logid + "支付异常，请检查机器人！[" + retInfo + "]");
		result.setErrorInfo("支付异常，请检查机器人！[" + retInfo + "]");
		result.setRetValue(Result.MANUAL);
		return result;

	}
}
