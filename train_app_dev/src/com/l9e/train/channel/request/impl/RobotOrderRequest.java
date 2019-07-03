package com.l9e.train.channel.request.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sound.midi.Sequence;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.train.main.App;
import com.l9e.train.po.Account;
import com.l9e.train.po.ErrorInfo;
import com.l9e.train.po.Order;
import com.l9e.train.po.Result;
import com.l9e.train.po.ReturnOptlog;
import com.l9e.train.po.ReturnVO;
import com.l9e.train.po.Worker;
import com.l9e.train.service.impl.SysSettingServiceImpl;
import com.l9e.train.service.impl.TrainServiceImpl;
import com.l9e.train.util.ConfigUtil;
import com.l9e.train.util.DateUtil;
import com.l9e.train.util.HttpUtil;
import com.l9e.train.util.MD5;
import com.l9e.train.util.MemcachedUtil;
import com.l9e.train.util.PostRequestUtil;
import com.l9e.train.util.StrUtil;
import com.l9e.train.util.UrlFormatUtil;
import com.train.commons.jedis.SingleJedisClient;
import com.unlun.commons.exception.DatabaseException;
import com.unlun.commons.exception.RepeatException;

/**
 * 机器人预定类
 * 
 * @author guobin
 *
 */
public class RobotOrderRequest extends RequestImpl {

	private Logger logger = LoggerFactory.getLogger(RobotOrderRequest.class);

	public RobotOrderRequest(Account account, Worker worker) {
		super(account, worker);
	}

	@Override
	public Result request(Order order, String weight, String logid) {
		String orderId = order.getOrderId();
		TrainServiceImpl service = new TrainServiceImpl();

		// 生成一个随机的16位的字符串(设备号）
		String deviceNo = StrUtil.getRandomString(16);
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");// 6:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");// 23:00
		if (date.before(begin) || date.after(end)) {
			logger.info("{}【向机器人发送预订请求】发送时间在23:00~06:00之间，不予发送！", logid);
			result.setRetValue(Result.MANUAL);
			return result;
		}

		// lua 参数
		Map<String, String> maps = new HashMap<String, String>();

		// 新版java 预订机器人，发送post请求，参数格式 json
		JSONObject requestJson = new JSONObject();
		requestJson.put("orderId", orderId);

		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.queryLogList();
		} catch (Exception e) {
			logger.info("{}获取出票系统日志异常！", logid, e);
		}
		List<ReturnOptlog> returnLogList = sysImpl.getList_return();
		MemcachedUtil memcached = MemcachedUtil.getInstance();
		int resendNum = 0;
		String resendKey = "resend_times_" + orderId;
		Object attribute = memcached.getAttribute(resendKey);
		if (null != attribute) {
			resendNum = Integer.valueOf(String.valueOf(attribute));
		}
		logger.info("{}resend_num:{}", logid, resendNum);

		int failNum = 0;
		String key = "fail_times_" + orderId;
		attribute = memcached.getAttribute(key);
		if (null != attribute) {
			failNum = Integer.valueOf(String.valueOf(attribute));
		}
		logger.info("{}fail_num:{}", logid, failNum);

		int manualNum = 0;
		key = "manual_times_" + orderId;
		attribute = memcached.getAttribute(key);
		if (null != attribute) {
			manualNum = Integer.valueOf(String.valueOf(attribute));
		}
		logger.info("{}manual_num:{}", logid, manualNum);

		int waitNum = 0;
		key = "wait_times_" + orderId;
		attribute = memcached.getAttribute(key);
		if (null != attribute) {
			waitNum = Integer.valueOf(String.valueOf(attribute));
		}
		logger.info("{}wait_num:{}", logid, waitNum);

		Integer workerLangType = worker.getWorkerLangType();
		logger.info("{}机器脚本语言类型为:{} [1:lua脚本语言, 2:java脚本语言, 3:Python]", logid, workerLangType);
		String workerExt = worker.getWorkerExt();

		if ("0".equals(String.valueOf(workerLangType)) || "1".equals(String.valueOf(workerLangType))) {
			logger.info("{}执行LUA脚本12306new.lua", logid);
			// String weight = deviceWeight(service);
			String scriptPath = "12306new.lua";
			logger.info("{}出票模式权重:{}", logid, weight);
			maps.put("ScriptPath", scriptPath);
			try {
				// LUA 全部PC出票
				service.updateDevice(orderId, 0);
				service.insertHistory(orderId, "本次预订使用设备:LUA PC");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		maps.put("commond", "trainOrder");
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));
		maps.put("Timeout", "240000");
		maps.put("ParamCount", String.valueOf(order.getOrderCps().size() + 1));

		// 机器人返回信息改造
		StringBuffer sb = new StringBuffer();
		String username = account.getUsername();
		String password = account.getPassword();
		// 各个操作的睡眠时间JSON串
		String timeIntervalParam = timeIntervalParam(service);
		// tn17101342036412|香坊|海林|2017-10-16|K1451
		String orderstr = order.getOrderstr();
		Integer workerId = worker.getWorkerId();
		sb.append(username).append("|")//
				.append(password).append("|")//
				.append(orderstr).append("|")//
				// 打码方式：0、手动打码 1、机器识别 sysImpl.querySysVal("rand_code_type");
				.append("1").append("|")//
				.append(workerId).append("|")//
				.append(timeIntervalParam).append("|")//
				.append(deviceNo);

		String[] orderArr = orderstr.split("\\|");

		// 账号
		JSONObject accountJson = new JSONObject();
		accountJson.put("username", username);
		accountJson.put("password", password);
		requestJson.put("account", accountJson);

		// 车票
		JSONObject ticketJson = new JSONObject();
		ticketJson.put("departureDate", orderArr[3]);
		ticketJson.put("trainCode", orderArr[4]);

		// 在此获取订单中出发城市和到达城市的三字码 add by wangsf
		String fromCity = order.getFrom();
		String fromCity3c = order.getFromCity_3c();
		logger.info("{}订单中出发城市为:{}", logid, fromCity);
		logger.info("{}订单中出发城市三字码为:{}", logid, fromCity3c);

		String toCity = order.getTo();
		String toCity3c = order.getToCity_3c();
		logger.info("{}订单中到达城市为:{}", logid, toCity);
		logger.info("{}订单中到达城市三字码为:{}", logid, toCity3c);

		if (StringUtils.isNotBlank(fromCity3c) && StringUtils.isNotBlank(toCity3c)) {
			sb.append("|").append(fromCity3c);
			sb.append("|").append(toCity3c);
		} else {
			// 获取简码
			try {
				Order orderCity3c = null;
				if (StringUtils.isBlank(fromCity3c)) {
					logger.info("{}Redis中获取出发站简码三字码...", logid);
					Object city3cObj = App.redisDao.getCacheVal(MD5.getMd5_UTF8(fromCity));
					if (city3cObj != null) {
						fromCity3c = city3cObj.toString();
					} else {
						logger.info("{}DB中获取出发站简码三字码...", logid);
						service.queryOrderCity3c(fromCity, toCity);
						orderCity3c = service.getOrder3c();
						if (orderCity3c != null) {
							fromCity3c = orderCity3c.getFromCity_3c();
						}
					}
				}

				if (StringUtils.isBlank(toCity3c)) {
					logger.info("{}Redis中获取到达站简码三字码...", logid);
					Object city3cObj = App.redisDao.getCacheVal(MD5.getMd5_UTF8(toCity));
					if (city3cObj != null) {
						toCity3c = city3cObj.toString();
					} else {
						logger.info("{}DB中获取出发站简码三字码...", logid);
						if (orderCity3c != null) {
							toCity3c = orderCity3c.getFromCity_3c();
						} else {
							service.queryOrderCity3c(fromCity, toCity);
							orderCity3c = service.getOrder3c();
							if (orderCity3c != null) {
								toCity3c = orderCity3c.getFromCity_3c();
							}
						}
					}
				}
			} catch (Exception e) {
				logger.info("{}【Get City 3C code Exception】", logid, e);
			}
		}
		logger.info("{}最终的出发城市的三字码为:{}", logid, fromCity3c);
		logger.info("{}最终的到达城市的三字码为:{}", logid, toCity3c);

		ticketJson.put("fromStationName", fromCity.trim());
		ticketJson.put("fromStationCode", fromCity3c);
		ticketJson.put("toStationName", toCity.trim());
		ticketJson.put("toStationCode", toCity3c);

		// 新增动态库或黑盒调用参数。动态库或黑盒选择 0：动态库 1：黑盒
		// sysImpl.querySysVal("dynamicDepot_or_blackBox");
		sb.append("|").append("0");

		// 新增客户预选的卧铺位置或者座位号
		String chooseSeats = order.getChoose_seats();
		chooseSeats = StringUtils.isBlank(chooseSeats) || chooseSeats.equals("null") ? null : chooseSeats;
		String seatDetailType = order.getSeatDetailType();
		seatDetailType = StringUtils.isBlank(seatDetailType) || seatDetailType.equals("null") ? null : seatDetailType;
		logger.info("{}选座 chooseSeats:{}， seatDetailType:{}", logid, chooseSeats, seatDetailType);
		sb.append("|").append(seatDetailType);
		sb.append("|").append(chooseSeats);
		ticketJson.put("chooseSeats", chooseSeats);

		maps.put("Param1", sb.toString());

		String channel = order.getChannel();
		logger.info("{}渠道商:{}", logid, channel);
		// 新版java参数
		JSONArray passengers = new JSONArray();
		List<String> cps = order.getOrderCps();
		try {
			for (int i = 0; i < cps.size(); i++) {
				String param = cps.get(i);
				if (!"tongcheng".equals(channel)) {
					param = param + "|" + order.getSeatType();
				}
				String[] paramArr = param.split("\\|");
				String seatType = paramArr[2];
				// 学生票处理
				if ("3".equals(seatType)) {
					service.queryStudentInfo(orderId, paramArr[0], param);
					param = service.getParam();
				}
				logger.info("{}param{}:{}", logid, i + 2, param);
				// 普通:TN_a1b55ee59cc5a72c|王金利|0|2|132930197705154318|2|2
				// 学生票:TN_006f163bee30866c|周桐|3|2|12011219980811043X|8|8|河北|13|12796|河北工程技术学院|17080703004|4|2017|石家庄|365|天津|359
				// passengerNo|姓名|乘客类型|证件类型|证件号|坐席|多余参数
				maps.put("Param" + (i + 2), param);

				// 处理新版java 参数
				JSONObject passengerJson = new JSONObject();
				paramArr = param.split("\\|");
				passengerJson.put("passengerNo", paramArr[0]);
				String name = paramArr[1].toUpperCase().trim();
				// 新疆人
				name = name.replaceAll("\\.", "·");
				name = name.replaceAll("。", "·");
				passengerJson.put("name", name);
				passengerJson.put("ticketType", paramArr[2].toUpperCase());
				passengerJson.put("cardType", paramArr[3].toUpperCase());
				passengerJson.put("cardNo", paramArr[4].toUpperCase().trim());
				passengerJson.put("seatType", paramArr[5].toUpperCase());
				// 学生票信息
				if ("3".equals(seatType)) {
					// 河北|13|12796|河北工程技术学院|17080703004|4|2017|石家庄|365|天津|359
					// 所在省份名称
					passengerJson.put("provinceName", paramArr[7]);
					// 省份代码
					passengerJson.put("provinceCode", paramArr[8]);
					// 学校代码
					passengerJson.put("schoolCode", paramArr[9]);
					// 学校名称
					passengerJson.put("schoolName", paramArr[10]);
					// 学号
					passengerJson.put("studentNo", paramArr[11]);
					// 学制
					passengerJson.put("system", paramArr[12]);
					// 入学年份
					passengerJson.put("enterYear", paramArr[13]);
					// 优惠区间出发站名
					passengerJson.put("limitBeginName", paramArr[14].trim());
					// 优惠区间出发站简码
					passengerJson.put("limitBeginCode", paramArr[15]);
					// 优惠区间到达站名
					passengerJson.put("limitEndName", paramArr[16].trim());
					// 优惠区间到达站简码
					passengerJson.put("limitEndCode", paramArr[17]);
				}
				passengers.add(passengerJson);
			}
		} catch (Exception e) {
			logger.info("{}处理乘客信息出现异常", logid, e);
		}
		ticketJson.put("passengers", passengers);
		requestJson.put("data", ticketJson);

		String param = null;
		try {
			param = UrlFormatUtil.createUrl("", maps, "UTF-8");
		} catch (Exception e) {
			logger.info("{}URL 编码参数异常", logid, e);
		}

		// 代理IP配置
		sysImpl = new SysSettingServiceImpl();
		int proxyFlag = 1; // 0关闭, 1 打开
		try {
			proxyFlag = sysImpl.querySysVal("proxyIP_switch");
			String sysVal = sysImpl.getSysVal();
			proxyFlag = Integer.valueOf(sysVal);
		} catch (Exception e) {
			logger.info("{}获取代理ip开关异常", logid, e);
		}
		logger.info("{}代理ip开关:{} [0: 关闭, 1:打开]", logid, proxyFlag);

		logger.info(logid + ">>> =========================开始发送HTTP请求,调用机器人=======================");
		String workerName = worker.getWorkerName();
		logger.info("{}>>> 机器人名称:{}, 机器人路径:{}", logid, workerName, workerExt);
		// logger.info("{}>>> 【KEY-VALUE 参数】:" + param);

		String reqResult = null;
		String newIp = workerExt;
		Integer ipId = 0;
		if (new Integer(2).equals(workerLangType)) {
			String url = "";
			// 判断 是 JAVA app 还是 pc
			if (workerName.contains("dll")) {
				logger.info("{}>>> JAVA APP !!!!", logid);
				url = "http://" + workerExt + ":18010/robot/book/pc";
				requestJson.put("publicIp", workerExt);
			} else {
				if (proxyFlag == 1) {
					try {
						// 获取新IP
						// TODO
						String httpResult = HttpUtil.sendByGet(ConfigUtil.getValue("getIpInfo"), "UTF-8", 5000, 5000);
						logger.info("{}代理IP获取结果:{}", logid, httpResult);
						JSONObject httpJson = JSONObject.parseObject(httpResult);
						// {"success":false,"msg":"没有可用IP"}
						// {"success":true,"msg":"操作成功","data":{"ipId":1,"ipType":2,"ipExt":"103.37.159.7:808:wxg:wxg","requestNum":1,"ifSupportChange":"0"}}
						boolean success = httpJson.getBoolean("success");
						String msg = httpJson.getString("msg");
						if (!success) {
							logger.info("{}获取代理IP失败:{}", logid, msg);
							result.setRetValue(Result.MANUAL);
							result.setErrorInfo("获取代理IP失败:" + msg);
							return result;
						}
						JSONObject ipData = httpJson.getJSONObject("data");
						ipId = ipData.getInteger("ipId");
						newIp = ipData.getString("ipExt").split(":")[0];
						logger.info("{}切换IP, 切换后新的IP:{}", logid, newIp);
						url = "http://" + newIp + ":18010/robot/book/pc";
						requestJson.put("publicIp", newIp);
					} catch (Exception e) {
						logger.info("{}获取代理IP异常", logid, e);
						result.setRetValue(Result.MANUAL);
						result.setErrorInfo("获取代理IP异常:" + e.getClass().getSimpleName());
						return result;
					}
				} else {
					url = "http://" + workerExt + ":18010/robot/book/pc";
					requestJson.put("publicIp", workerExt);
				}

				logger.info("{}>>> JAVA PC !!!!", logid);
			}

			param = requestJson.toJSONString();
			logger.info(logid + ">>> JAVA请求路径:" + url + ":" + param);
			reqResult = PostRequestUtil.postJson(url, "UTF-8", param);
			logger.info(logid + "JAVA返回结果:" + reqResult);

			// 这里将新版JAVA返回参数转换成兼容旧版本的json串
			try {

				JSONObject jsonBody = new JSONObject();
				jsonBody.put("ErrorCode", 0);
				JSONArray errorInfo = new JSONArray();
				JSONObject resultJson = JSONObject.parseObject(reqResult);
				if (resultJson.getString("status").equals("0000")) {
					JSONObject body = resultJson.getJSONObject("body");
					JSONObject data = new JSONObject();
					data.put("refundOnline", "N");
					data.put("robotNum", 3);
					data.put("contactsnum", body.getInteger("passengerCount"));
					data.put("outTicketBillno", body.getString("sequence"));
					data.put("orderId", body.getString("orderId"));
					data.put("waitTime", "");
					data.put("chooseSeatType", "");
					data.put("arrivetime", body.getString("arrivalTime"));
					data.put("queueNumber", 0);
					data.put("from", body.getString("fromStationName"));
					data.put("seattime", body.getString("departureTime"));
					data.put("trainno", body.getString("trainCode"));
					data.put("retValue", "success");
					data.put("to", body.getString("toStationName"));

					BigDecimal total = new BigDecimal(0);
					JSONArray newPassengerArray = new JSONArray();
					JSONArray passengerArray = body.getJSONArray("passengers");
					for (int j = 0; j < passengerArray.size(); j++) {
						JSONObject newPassenger = new JSONObject();
						JSONObject passenger = passengerArray.getJSONObject(j);
						BigDecimal price = new BigDecimal(passenger.getDouble("price"));
						newPassenger.put("cpId", passenger.getString("passengerNo"));
						newPassenger.put("trainbox", passenger.getString("boxName"));
						newPassenger.put("subOutTicketBillNo", passenger.getString("subSequence"));
						newPassenger.put("paymoney", price);
						newPassenger.put("seatType", passenger.getString("seatType"));
						newPassenger.put("seatNo", passenger.getString("seatName"));

						newPassengerArray.add(newPassenger);
						total = total.add(price);
					}
					data.put("cps", newPassengerArray);
					data.put("summoney", String.valueOf(total));
					errorInfo.add(data);
				} else {
					JSONObject j = new JSONObject();
					j.put("contactsnum", 0);
					j.put("outTicketBillno", "");
					j.put("orderId", orderId);
					j.put("chooseSeatType", "");
					j.put("queueNumber", 0);
					j.put("waitTime", "");
					j.put("robotNum", 5);
					j.put("retValue", "failure");
					j.put("retInfo", resultJson.getString("message"));
					errorInfo.add(j);
				}
				jsonBody.put("ErrorInfo", errorInfo);
				reqResult = jsonBody.toString();
			} catch (Exception e) {
				logger.info("{}JAVA返回参数转换出现异常", logid, e);
				result.setRetValue(Result.MANUAL);
				result.setErrorInfo("【JAVA返回结果转JSON出现异常，转人工！】");
				return result;
			}

			// 释放代理IP
			if (!workerName.contains("dll") && proxyFlag == 1) {
				service = new TrainServiceImpl();
				try {
					service.updateIpStatus(ipId);
				} catch (Exception e) {
					logger.info("{}释放的IP异常 newIp:{}", logid, newIp, e);
				}
			}
		} else {
			logger.info("{}>>> LUA !!!!", logid);
			reqResult = PostRequestUtil.getPostRes("UTF-8", workerExt, param);
			reqResult = reqResult.replace("[\"{", "[{");
			reqResult = reqResult.replace("}\"]", "}]");
			reqResult = reqResult.replace("\\", "");
		}
		logger.info("{}>>> 机器人响应结果:{}", logid, reqResult);

		/**
		 * 本次请求返回结果后，如果后台的机器锁定开关是开启（1）状态， 则需要解锁本次请求的机器人，把worker_lock更新为0
		 * worker_lock 当前机器是否锁定 0、否 1、是
		 */
		String workerLockSwitch = "0";// 1：机器锁定开启 0：机器锁定关闭
		sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.querySysVal("worker_lock_switch");
			if (StringUtils.isNotEmpty(sysImpl.getSysVal())) {
				workerLockSwitch = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("{}机器锁定开关状态workerLockSwitch为:{}", logid, workerLockSwitch);
		if ("1".equals(workerLockSwitch)) {
			// 在此释放本次请求的机器
			App.workerService.releaseWorker(workerId);
			logger.info("{}机器解锁完毕！worker_lock更新为0！", logid);
		}

		if (null == reqResult) {
			logger.info("{}Post Request Result Is Empty", logid);
			result.setErrorInfo("预定错误，没有返回结果！");
			result.setReturnOptlog(ReturnOptlog.NORESULT);
			if ((resendNum + 1 % 3) == 0) {
				result.setRetValue(Result.MANUAL);
				return result;
			} else {
				memcached.setAttribute(resendKey, ++resendNum, 90 * 1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}

		// 请求错误
		if (reqResult.equals(PostRequestUtil.TIME_OUT) || reqResult.equals(PostRequestUtil.CONNECT_ERROR) | reqResult.equals(PostRequestUtil.URL_ERROR)) {
			logger.info("{}Post Request Error, Result Value:{}", logid, reqResult);
			if (reqResult.contains("CONNECTERROR")) {
				result.setReturnOptlog(ReturnOptlog.SOS);
				result.setErrorInfo("机器异常，请尽快进入后台机器管理，针对该机器人进行【短信停用】！");
				result.setRetValue(Result.MANUAL);
				return result;
			} else {
				result.setReturnOptlog(ReturnOptlog.TIMEOUT);
				result.setErrorInfo("机器预定超时，自动重发！【" + reqResult + "】");
			}
			if ((resendNum + 1) % 3 == 0) {
				result.setRetValue(Result.MANUAL);
				return result;
			} else {
				memcached.setAttribute(resendKey, ++resendNum, 90 * 1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}

		// 机器人处理超时
		if (reqResult.contains("\"ErrorCode\":9")) {
			logger.info("{}Robot Response Error, Result Value:", logid, reqResult);
			result.setErrorInfo("机器人处理超时【" + reqResult + "】处理：请核对12306网站是否已经订购！");
			result.setReturnOptlog(ReturnOptlog.TIMEOUT);
			if ((resendNum + 1) % 3 == 0) {
				result.setRetValue(Result.MANUAL);
			} else {
				memcached.setAttribute(resendKey, ++resendNum, 90 * 1000);
				result.setRetValue(Result.RESEND);
			}
			return result;
		}

		// 在此规避 返回结果报姓名格式错误
		if (reqResult.contains("姓名格式错误")) {
			logger.info("{}姓名格式错误，转人工！", logid);
			result.setRetValue(Result.MANUAL);
			result.setErrorInfo("【姓名格式错误，转人工！】");
			return result;
		}

		ReturnVO retObj = null;
		try {
			ObjectMapper map = new ObjectMapper();
			retObj = map.readValue(reqResult, ReturnVO.class);
		} catch (Exception e) {
			logger.info("{}ObjectMapper ReadValue Exception", logid, e);
			result.setErrorInfo("JSON分析异常【" + reqResult + "】");
			result.setReturnOptlog(ReturnOptlog.ROBOTEXCP);
			if ((resendNum + 1) % 3 == 0) {
				result.setRetValue(Result.MANUAL);
				return result;
			} else {
				memcached.setAttribute(resendKey, ++resendNum, 90 * 1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}

		try {
			String spare_thread = retObj.getErrorInfo().get(0).getRobotNum();
			if (Integer.valueOf(spare_thread) > 0) {
				sysImpl.updateWorkerSpareThread(workerId + "", Integer.valueOf(spare_thread));
			}
		} catch (Exception e) {
			logger.info("{}更新机器人空闲进程数异常！", logid, e);
		}

		Integer errorCode = retObj.getErrorCode();
		logger.info("{}ErrorCode:{}", logid, errorCode);
		if (!new Integer(0).equals(errorCode)) {
			result.setRetValue(Result.FAILURE);
			result.setErrorInfo("【" + reqResult + "】");
			return result;
		}

		List<Order> errorInfoList = retObj.getErrorInfo();
		if (errorInfoList.size() > 0) {
			Order val = errorInfoList.get(0);
			// 排队时间改造--只针对途牛
			if ("tuniu".equals(channel)) {
				String waitTime = val.getWaitTime();// 订单排队时间
				Integer queueNumber = val.getQueueNumber();// 当前排队人数
				String msg = "未出票,订单排队中...";
				logger.info("{}排队时间为:{} 排队人数为:{}", logid, waitTime, queueNumber);
				if (null != waitTime && !("".equals(waitTime))) {
					// 插入tuniu_order_queue表一条排队数据
					Map<String, Object> insertMap = new HashMap<String, Object>();
					insertMap.put("order_id", orderId);
					insertMap.put("queue_number", queueNumber);
					insertMap.put("wait_time", waitTime);
					insertMap.put("msg", msg);
					insertMap.put("notify_status", 1);// 通知状态 1：等待通知，
														// 2：正在通知，3：通知成功，
														// 4：通知失败， 5：重新通知
					try {
						service.insertTuniuOrderQueue(insertMap);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			// 更新客户可以选座的坐席类型（可能有多个）
			String chooseSeatType = val.getChooseSeatType();
			logger.info("{}客户可以选座的坐席类型chooseSeatType为:{}", logid, chooseSeatType);
			try {
				service.updateOrderChooseSeatType(orderId, chooseSeatType);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String retValue = val.getRetValue();
			String retInfo = val.getRetInfo();
			if (Order.CHANNEL_GROUP_1.equals(order.getChannelGroup())) {
				String formatStr_new = "yyyy-MM-dd HH:mm:ss";
				try {
					if (StringUtils.isBlank(val.getSeattime()) || val.getSeattime().equals("null")) {
						order.setSeattime("");
					} else {
						order.setSeattime(DateUtil.stringToString(val.getSeattime() + ":00", formatStr_new));
					}
					if (StringUtils.isBlank(val.getArrivetime()) || val.getArrivetime().equals("null")) {
						order.setArrivetime("");
					} else {
						order.setArrivetime(DateUtil.stringToString(val.getArrivetime() + ":00", formatStr_new));
					}
				} catch (Exception e) {
					logger.info("{}时间异常！", logid, e);
				}
			}
			logger.info("{}Arrive Time:{}", logid, val.getArrivetime());

			String loginFailNum = String.valueOf(memcached.getAttribute("robot" + workerId));
			// 成功
			if (StringUtils.equals(retValue, "success")) {
				memcached.setAttribute("robot" + workerId, 0);
				result.setSelfId(val.getOrderId());
				result.setSheId(val.getOutTicketBillNo());
				result.setSelfId(val.getOrderId());
				result.setSheId(val.getOutTicketBillNo());
				result.setFromCity(val.getFrom());
				result.setToCity(val.getTo());
				result.setTrainNo(val.getTrainno());
				result.setPayMoney(val.getBuymoney());
				result.setOrderCps(val.getCps());
				result.setSeattime(val.getSeattime());
				result.setBuyMoney(val.getBuymoney());
				result.setRefundOnline(val.getRefundOnline());
				result.setRetValue(Result.SUCCESS);
				result.setManual(false);

				logger.info(logid + "contacts num: " + val.getContactsnum());
				result.setContactsNum(val.getContactsnum());

				// 常用联系人添加成功需要添加到账号过滤表中
				result.setInsertFilter(true);
				result.setFilterScope(Result.FILTER_ALL);
				int isPay = 0;
				try {
					isPay = service.orderIsPay(orderId);
				} catch (Exception e) {
					logger.info("{}常用联系人添加至过滤表异常", logid, e);
				}
				logger.info("{}是否支付标识:{} [0:未支付,1:已支付]", logid, isPay);

				String robotPaymoney = val.getBuymoney();
				String orderPaymoney = order.getPaymoney();
				logger.info("{}订单金额robotPaymoney:{}，orderPaymoney:{}", logid, robotPaymoney, orderPaymoney);
				if (StringUtils.isNotBlank(robotPaymoney) && isPay == 1) {
					if (!new Double(robotPaymoney).equals(new Double(orderPaymoney))) {
						result.setErrorInfo("价格不相同，请确认价格【儿童票、特价票、卧铺票】！");
					}
					if (new Double(robotPaymoney).doubleValue() > new Double(orderPaymoney).doubleValue()) {
						logger.info("{}Price Not Same, setPriceModify=true, Need Update Base Price!", logid);
						double buyMoney = Double.parseDouble(robotPaymoney);
						double payMoney = Double.parseDouble(orderPaymoney);
						double diffMoney = buyMoney - payMoney;
						if (StringUtils.isNotEmpty(channel) && "19e".equals(channel)) {
							if (diffMoney <= 10) {
								logger.info("{}19e channel price differ={}<=10, can out ticket!", logid, diffMoney);
								result.setErrorInfo("<b>价格不相同，报价接口报价错误，19e渠道10元误差可以出票！真实价格" + buyMoney + "</b>");
							} else {
								logger.info("{}19e channel price differ={}>10, out ticket fail!", logid, diffMoney);
								result.setErrorInfo("出票失败，请确定错误！【价格不相同，报价接口报价错误，19e渠道大于10元误差，出票失败！真实价格" + buyMoney + "】");
								result.setFailReason(ErrorInfo.TICKET_PRICE_WRONG);// 票价和12306不符
								result.setRetValue(Result.CANCEL);
								result.setPriceModify(true);// 需要更新车票表
								return result;
							}
						} else {
							logger.info("{}other channel price differ={}, out ticket fail!", logid, diffMoney);
							result.setErrorInfo("出票失败，请确定错误！【价格不相同，报价接口报价错误，非19e渠道出票失败！真实价格" + buyMoney + "】");
							result.setFailReason(ErrorInfo.TICKET_PRICE_WRONG);// 票价和12306不符
							result.setPriceModify(true);// 需要更新车票表
							result.setRetValue(Result.CANCEL);
							return result;
						}
					}
				}

				// 没有支付
				if (isPay == 0) {
					// 美团

				}

				String channelGroup = order.getChannelGroup();
				String formatStr = "yyyy-MM-dd HH:mm";
				logger.info("{}订单组同程、去哪、美团 group-1,其他group-2{}", logid, channelGroup);
				// 同程不需要比较发车时间
				if (!Order.CHANNEL_GROUP_1.equals(channelGroup)) {
					Date oSeattime = DateUtil.stringToDate(order.getSeattime(), formatStr);
					Date rSeattime = DateUtil.stringToDate(val.getSeattime(), formatStr);
					logger.info("{} Robot SeatTime():{}， Order SeatTime:{}", logid, val.getSeattime(), order.getSeattime());
					if (oSeattime != null && rSeattime != null && !oSeattime.equals(rSeattime)) {
						long diffms = 0L;
						if (oSeattime.after(rSeattime)) {
							diffms = oSeattime.getTime() - rSeattime.getTime();
						} else {
							diffms = rSeattime.getTime() - oSeattime.getTime();
						}
						long diffm = diffms / 1000 / 60;
						if (diffm >= 10L) {
							logger.info("{}time diff={}>=20min,out ticket fail!", logid, diffm);
							result.setErrorInfo("出票失败，请确定错误！【乘车时间异常，相差10分钟以上，以无票处理！真实日期:" + val.getSeattime() + "】");
							result.setFailReason(ErrorInfo.RIDING_TIME_EXCEPTION);// 乘车时间异常
							result.setRetValue(Result.CANCEL);
							result.setPriceModify(true);// 需要更新车票表
							return result;
						} else {
							logger.info("{}time diff={}<20min,can out ticket!", logid, diffm);
							result.setErrorInfo("<b>乘车时间异常，处理：相差10分钟以内可以开始出票！真实日期:" + val.getSeattime() + "</b>");
						}
					}
				}

				String level = order.getLevel();
				logger.info("{}等级:{}", logid, level);
				if (StringUtils.isNotBlank(level) && "10".equals(level)) {
					if (reqResult.contains("无座") && !StringUtils.equals(order.getSeatType(), "9")) {
						if (!order.isWea_wz()) {
							logger.info("{}订到无票坐席，并且备选坐席没有选择\"无座\"", logid);
							result.setErrorInfo("出票失败，请确定错误！订到无票坐席，并且备选坐席没有选择\"无座\"");
							result.setFailReason(ErrorInfo.WITHOUT_TICKET);// 失败原因为：无票
							result.setRetValue(Result.CANCEL);
							result.setPriceModify(true);// 需要更新车票表
							return result;
						}
					}
					result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>" + retInfo);
					result.setManual(true);
					return result;
				}

				if (reqResult.contains("无座") && !StringUtils.equals(order.getSeatType(), "9")) {
					if (!order.isWea_wz()) {
						logger.info("{}订到无票坐席，并且备选坐席没有选择\"无座\"", logid);
						result.setErrorInfo("出票失败，请确定错误！订到无票坐席，并且备选坐席没有选择\"无座\"");
						result.setFailReason(ErrorInfo.WITHOUT_TICKET);// 失败原因为：无票
						result.setRetValue(Result.CANCEL);
						result.setPriceModify(true);// 需要更新车票表
						return result;
					} else {
						result.setErrorInfo("订到无票坐席，并且备选坐席选择无座，直接预订成功！</b>" + retInfo);
						result.setRetValue(Result.SUCCESS);
						return result;
					}
				}

				/**
				 * 一人多单判断 TODO </br>
				 * 这里数据库并不是立马跟新订单信息，可能延迟3-10秒处理其他操作。在12306登录不互相踢，
				 * 并且一秒多单同一乘客订票的情况下 </br>
				 * 第二单可能在这个时间差内没有查询到相同的12306订单号，通过redis分布式锁实现
				 */
				try {
					String sequence = val.getOutTicketBillNo();
					SingleJedisClient jedisClient = App.jedisClient;
					String setResult = jedisClient.set(sequence, orderId, "NX", "EX", 60 * 30);
					if (!"OK".equals(setResult)) {
						// 判断value是否与订单号一致，如果一致，则默认成功
						String getValue = jedisClient.get(sequence);
						logger.info("{}{}一人多单查询结果:{}-{}", logid, sequence, orderId, getValue);
						if (!orderId.equals(getValue)) {
							logger.info("{}可能存在一人多单，或者往返票赔款的情况，转入人工处理", logid);
							result.setErrorInfo("可能存在一人多单，或者往返票赔款的情况，转入人工处理");
							result.setRetValue(Result.MANUAL);
							return result;
						}
					}
				} catch (Exception e) {
					logger.info("{}查询一人多单异常", logid, e);
				}

				return result;
			} else if (retValue.equals("manual")) {// 人工处理
				memcached.setAttribute("robot" + workerId, 0);
				result.setErrorInfo("出票失败，请确定错误,转人工！【" + retInfo + "】");
				result.setRetValue(Result.MANUAL);
				return result;
			} else if (StringUtils.equals(retValue, "failure")) {// 失败
				result.setSelfId(val.getOrderId());
				logger.info("{}" + result.getJdOrderId() + "work-id:" + result.getWorker().getWorkerId(), logid);
				result.setErrorInfo("出票失败，请确定错误！【" + retInfo + "】");
				if (StringUtils.isEmpty(retInfo)) {
					result.setRetValue(Result.MANUAL);
					memcached.setAttribute("robot" + workerId, 0);
					return result;
				}
				if (StrUtil.containRegex(retInfo, "waitTime")) {// 12306排队
					result.setRetValue(Result.QUEUE);
					return result;
				}
				if (retInfo.contains("用户信息是否真实、完整、准确")) {
					result.setErrorInfo("STOP");
					result.setRetValue(Result.END);
					return result;
				}
				if (retInfo.contains("账号状态为：待核验")) {
					result.setErrorInfo("WAITCHECK");
					result.setRetValue(Result.END);
					return result;
				}
				// 出票系统日志
				int ifResend = 0;
				for (ReturnOptlog optlog : returnLogList) {
					String returnName = optlog.getReturn_name();
					String returnType = optlog.getReturn_type();
					String returnJoin = optlog.getReturn_join();
					String returnId = optlog.getReturn_id();
					String failReason = optlog.getFail_reason();

					logger.info("{}returnName:{}", logid, returnName);
					logger.info("{}returnType:{}", logid, returnType);
					logger.info("{}returnJoin:{}", logid, returnJoin);
					logger.info("{}returnId:{}", logid, returnId);
					logger.info("{}failReason:{}", logid, failReason);
					if (retInfo.contains(returnName)) {
						logger.info("{}针对错误日志:{}/{}", logid, returnName, returnId);
						if ("00".equals(returnType)) {// 重发
							result.setReturnOptlog(returnId);
							if ("1".equals(returnJoin)) {// 是否参数重发
								ifResend = 1; // 参与重发次数
							} else {
								ifResend = 2; // 直接重发
							}
							break;
						} else if ("11".equals(returnType)) { // 直接失败
							result.setReturnOptlog(returnId);
							// 因为接口返回的报错信息不一致，所以把||
							// "elong".equals(channel)这个条件去掉 modify by wangsf
							// 2016.4.27
							if ("8".equals(failReason) && StringUtils.isNotEmpty(channel) && "qunar".equals(channel)) {
								result.setFailReason(ErrorInfo.CANCEL_ORDER);
							} else if ("12".equals(failReason) && StringUtils.isNotEmpty(channel) && "qunar".equals(channel)) {
								result.setFailReason(ErrorInfo.NOPASS_REALNAME_VERIFY);
							} else {
								result.setFailReason(failReason);
							}

							if ("1".equals(returnJoin)) {// 是否参数重发
								ifResend = 3; // 参与失败次数
							} else {
								ifResend = 4; // 直接失败
							}
							break;
						} else if ("33".equals(returnType)) { // 按时重发
							result.setReturnOptlog(returnId);
							// 因为接口返回的报错信息不一致，所以把||
							// "elong".equals(channel)这个条件去掉 modify by wangsf
							// 2016.4.27
							if ("8".equals(failReason) && StringUtils.isNotEmpty(channel) && "qunar".equals(channel)) {
								result.setFailReason(ErrorInfo.CANCEL_ORDER);
							} else {
								result.setFailReason(failReason);
							}
							if ("1".equals(returnJoin)) {// 是否参数重发
								ifResend = 5; // 参与失败次数
							} else {
								ifResend = 6; // 直接失败
							}
							break;
						} else if ("22".equals(returnType)) { // 人工
							result.setReturnOptlog(returnId);
							if ("1".equals(returnJoin)) {// 是否参数重发
								ifResend = 7; // 参与失败次数
							} else {
								ifResend = 8; // 直接失败
							}
							break;
						}
					}
				}

				logger.info("{}ifResend:{}", logid, ifResend);
				logger.info("{}错误信息为:{}", logid, retInfo);

				// 自动切换账号 TODO
				// 封停账号
				if (retInfo.contains("密码输入错误。") //
						|| retInfo.contains("密码输入错误次数过多") //
						|| retInfo.contains("账号状态为：未通过") //
						|| StrUtil.containRegex(retInfo, "手机.*核验")//
						|| StrUtil.containRegex(retInfo, "重新.*注册") // 账号重新注册
						|| retInfo.contains("未通过核验，不能添加常用联系人")// 账号未通过核验
				) {
					logger.info("{}{}密码输入错误,封停账号:{}", logid, orderId, retInfo);
					Integer fromWay = order.getAccountFromWay();
					if (fromWay != null && fromWay.intValue() == 1) {
						result.setRetValue(Result.STOP);
					} else {
						result.setRetValue(Result.END);
					}
					result.setErrorInfo("【" + retInfo + "】");
					return result;
				}
				// 临时停用
				if (retInfo.contains("您取消次数过多") // 取消次数过多
						|| StrUtil.containRegex(retInfo, "联系人.*上限")// 联系人达上限
						|| retInfo.contains("通过后即可在网上购票") // 身份核验未通过
						|| retInfo.contains("未完成订单信息不匹配,")// 未完成订单信息不匹配,
				) {
					logger.info("{}{}错误信息为,临时停用账号:{}", logid, orderId, retInfo);
					memcached.setAttribute("robot" + workerId, 0);
					result.setRetValue(Result.STOP);
					result.setErrorInfo("【" + retInfo + "】");
					return result;

				}

				// TODO 重发操作
				if (retInfo.indexOf("网络可能存在问题") >= 0 || retInfo.indexOf("12306服务器压力太大") >= 0) {
					ifResend = 1;
				}
				if (ifResend != 0) {
					if (ifResend == 1) {
						logger.info("{}预定重发1", logid);
						memcached.setAttribute("robot" + workerId, 0);
						if ((resendNum + 1) % 3 == 0) {
							result.setRetValue(Result.MANUAL);
							return result;
						} else {
							memcached.setAttribute(resendKey, ++resendNum, 90 * 1000);
							result.setRetValue(Result.RESEND);
							return result;
						}
					} else if (ifResend == 2) {
						logger.info("{}预定重发2", logid);
						result.setRetValue(Result.RESEND);
						return result;
					} else if (ifResend == 3) {// 出票失败
						logger.info("{}出票失败超时重发3", logid);
						memcached.setAttribute("robot" + workerId, 0);
						if (failNum >= 3) {
							if (StringUtils.isNotEmpty(order.getLevel()) && "10".equals(order.getLevel())) {
								result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>" + retInfo);
								result.setRetValue(Result.MANUAL);
								return result;
							} else {
								if (!Result.NOPASS.equals(result.getRetValue())) {
									result.setRetValue(Result.FAILURE);
								}
								return result;
							}
						} else {
							memcached.setAttribute("fail_times_" + orderId, ++failNum, 90 * 1000);
							result.setRetValue(Result.RESEND);
							return result;
						}
					} else if (ifResend == 4) {
						logger.info("{}出票失败4", logid);
						if (StringUtils.isNotEmpty(order.getLevel()) && "10".equals(order.getLevel())) {
							result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>" + retInfo);
							result.setRetValue(Result.MANUAL);
							return result;
						} else {
							if (!Result.NOPASS.equals(result.getRetValue())) {
								result.setRetValue(Result.FAILURE);
							}
							return result;
						}
					} else if (ifResend == 5) { // 按时重发
						logger.info("{}按时重发5", logid);
						memcached.setAttribute("robot" + workerId, 0);
						if (waitNum >= 3) {
							if (!Result.NOPASS.equals(result.getRetValue())) {
								result.setRetValue(Result.FAILURE);
							}
							return result;
						} else {
							memcached.setAttribute("wait_times_" + orderId, ++waitNum, 4 * 60 * 1000);
							result.setRetValue(Result.WAIT);
							return result;
						}
					} else if (ifResend == 6) {
						logger.info("{}按时重发6", logid);
						if (StringUtils.isNotEmpty(order.getLevel()) && "10".equals(order.getLevel())) {
							result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>" + retInfo);
							result.setRetValue(Result.MANUAL);
							return result;
						} else {
							result.setRetValue(Result.FAILURE);
							return result;
						}
					} else if (ifResend == 7) {
						logger.info("{}预定人工1", logid);
						memcached.setAttribute("robot" + workerId, 0);
						if (manualNum >= 2) {
							result.setRetValue(Result.MANUAL);
							return result;
						} else {
							memcached.setAttribute("manual_times_" + orderId, ++manualNum, 90 * 1000);
							result.setRetValue(Result.RESEND);
							return result;
						}
					} else if (ifResend == 8) {
						logger.info("{}预定人工2", logid);
						result.setRetValue(Result.MANUAL);
						return result;
					}
				}

				if (retInfo.contains("登录失败")) {
					logger.info("{}错误信息为:{}", logid, retInfo);

					if (StringUtils.isNotBlank(loginFailNum)) {
						int loginFailNumber = Integer.parseInt(loginFailNum);
						loginFailNumber += 1;
						memcached.setAttribute("robot" + workerId, loginFailNumber);
						if (loginFailNumber >= 10) {
							try {
								sysImpl.updateRobot(workerId + "");
								sysImpl.insertWarning(worker.getWorkerName());
							} catch (Exception e) {
								logger.info("{}登录失败更新数据库发生异常", logid, e);
							}
							memcached.setAttribute("robot" + workerId, 0);
						}
						if (loginFailNumber == 1) {
							logger.info("{}转重发...", logid);
							result.setRetValue(Result.RESEND);
						} else {
							logger.info("{}转人工...", logid);
							result.setRetValue(Result.MANUAL);
						}
						result.setErrorInfo("【" + retInfo + "】");
						return result;
					} else {
						memcached.setAttribute("robot" + workerId, "1");
						result.setRetValue(Result.MANUAL);
						return result;

					}
				} else {
					memcached.setAttribute("robot" + workerId, 0);
					result.setRetValue(Result.MANUAL);
					return result;
				}
			} else if (retValue.equals("nopass")) {// 用户未通过
				result.setSelfId(val.getOrderId());
				result.setErrorInfo(retInfo);

				memcached.setAttribute("robot" + workerId, 0);
				logger.info("{}订票渠道:{} 错误信息:{}", logid, channel, retInfo + "修改订单状态为出票失败（身份信息待审核）");
				// 因为接口返回的报错信息不一致，所以把|| "elong".equals(channel)这个条件去掉 modify by
				// wangsf 2016.4.27
				if (StringUtils.isNotEmpty(channel) && ("qunar".equals(channel))) {
					result.setFailReason(ErrorInfo.CANCEL_ORDER);
					result.setRetValue(Result.NOPASS);
					return result;
				} else {
					result.setFailReason(ErrorInfo.PASSENGER_NO_VERIFY);
					result.setRetValue(Result.NOPASS);
					return result;
				}
			} else {// 查询异常
				logger.info("{}returncode:{}", logid, retValue);
				result.setErrorInfo("出票异常，请检查机器人！【" + retInfo + "】");
				result.setRetValue(Result.MANUAL);
				return result;
			}
		} else {
			logger.info("{}RobotOrderRequest orderid:{} ERROR", logid, orderId);
			result.setErrorInfo("出票异常，请检查机器人！【" + reqResult + "】");
			result.setRetValue(Result.MANUAL);
			return result;
		}
	}

	/**
	 * 时间间隔参数json串
	 * 
	 * @return
	 */
	private String timeIntervalParam(TrainServiceImpl service) {
		MemcachedUtil mUtil = MemcachedUtil.getInstance();

		Map<String, Object> paramMap = new HashMap<String, Object>();
		long signInInterval = 5;
		long checkAccountInterval = 5;
		long unfinishedOrderInterval = 5;
		long bookButtonInterval = 5;
		long getContactInterval = 5;
		long addContactInterval = 5;
		long submitBookInterval = 5;

		try {
			// 登录间隔时间
			if (mUtil.getAttribute("sign_in_time_interval") == null) {
				signInInterval = Long.valueOf(service.getSysSettingValue("sign_in_time_interval"));
				mUtil.setAttribute("sign_in_time_interval", signInInterval, 30 * 1000);
			} else {
				signInInterval = (Long) mUtil.getAttribute("sign_in_time_interval");
			}

			// 检测账号间隔时间
			if (mUtil.getAttribute("check_account_time_interval") == null) {
				checkAccountInterval = Long.valueOf(service.getSysSettingValue("check_account_time_interval"));
				mUtil.setAttribute("check_account_time_interval", checkAccountInterval, 30 * 1000);
			} else {
				checkAccountInterval = (Long) mUtil.getAttribute("check_account_time_interval");
			}

			// 未完成订单查询间隔时间
			if (mUtil.getAttribute("unfinished_order_time_interval") == null) {
				unfinishedOrderInterval = Long.valueOf(service.getSysSettingValue("unfinished_order_time_interval"));
				mUtil.setAttribute("unfinished_order_time_interval", unfinishedOrderInterval, 30 * 1000);
			} else {
				unfinishedOrderInterval = (Long) mUtil.getAttribute("unfinished_order_time_interval");
			}

			// 点击预订按钮间隔时间
			if (mUtil.getAttribute("book_button_time_interval") == null) {
				bookButtonInterval = Long.valueOf(service.getSysSettingValue("book_button_time_interval"));
				mUtil.setAttribute("book_button_time_interval", bookButtonInterval, 30 * 1000);
			} else {
				bookButtonInterval = (Long) mUtil.getAttribute("book_button_time_interval");
			}

			// 获取联系人间隔时间
			if (mUtil.getAttribute("get_contact_time_interval") == null) {
				getContactInterval = Long.valueOf(service.getSysSettingValue("get_contact_time_interval"));
				mUtil.setAttribute("get_contact_time_interval", getContactInterval, 30 * 1000);
			} else {
				getContactInterval = (Long) mUtil.getAttribute("get_contact_time_interval");
			}

			// 添加联系人间隔时间
			if (mUtil.getAttribute("add_contact_time_interval") == null) {
				addContactInterval = Long.valueOf(service.getSysSettingValue("add_contact_time_interval"));
				mUtil.setAttribute("add_contact_time_interval", addContactInterval, 30 * 1000);
			} else {
				addContactInterval = (Long) mUtil.getAttribute("add_contact_time_interval");
			}

			// 提交预订信息间隔时间
			if (mUtil.getAttribute("submit_book_time_interval") == null) {
				submitBookInterval = Long.valueOf(service.getSysSettingValue("submit_book_time_interval"));
				mUtil.setAttribute("submit_book_time_interval", submitBookInterval, 30 * 1000);
			} else {
				submitBookInterval = (Long) mUtil.getAttribute("submit_book_time_interval");
			}

			paramMap.put("login_time", signInInterval * 1000);
			paramMap.put("account_time", checkAccountInterval * 1000);
			paramMap.put("order_time", unfinishedOrderInterval * 1000);
			paramMap.put("order_button_time", bookButtonInterval * 1000);
			paramMap.put("getuser_time", getContactInterval * 1000);
			paramMap.put("adduser_time", addContactInterval * 1000);
			paramMap.put("submit_time", submitBookInterval * 1000);

			ObjectMapper mapper = new ObjectMapper();
			String result = mapper.writeValueAsString(paramMap);
			logger.info("time_interval : " + result);
			return result;
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (RepeatException e) {
			e.printStackTrace();
		} catch (DatabaseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) {
		String ss = "{\"body\":{\"departureDate\":\"20180124\",\"fromStationCode\":\"IZQ\",\"fromStationName\":\"广州南\",\"passengers\":[{\"boxName\":\"05\",\"boxNo\":\"05\",\"cardNo\":\"440104198310115313\",\"cardType\":\"1\",\"loseTime\":\"\",\"name\":\"刘嘉辉\",\"price\":74.5,\"seatName\":\"12B号\",\"seatNo\":\"012B\",\"seatType\":\"O\",\"subSequence\":\"\",\"ticketType\":\"1\"}],\"sequence\":\"EC36469299\",\"toStationCode\":\"IOQ\",\"toStationName\":\"深圳北\",\"trainCode\":\"G1005\"},\"endMillis\":1516709495503,\"message\":\"success\",\"startMillis\":1516709482706,\"status\":\"0000\"}";
		JSONObject resultJson = JSONObject.parseObject(ss);
		System.out.println(resultJson.getString("status"));
		JSONObject body = resultJson.getJSONObject("body");
		JSONObject data = new JSONObject();
		data.put("refundOnline", "N");
		data.put("robotNum", 3);
		data.put("contactsnum", body.getInteger("passengerCount"));
		System.out.println(body.getInteger("passengerCount"));
		data.put("outTicketBillno", body.getString("sequence"));
		System.out.println(body.getString("sequence"));
		data.put("orderId", body.getString("orderId"));
		System.out.println(body.getString("orderId"));
		data.put("waitTime", "");
		data.put("chooseSeatType", "");
		data.put("arrivetime", body.getString("arrivalTime"));
		data.put("queueNumber", 0);
		data.put("from", body.getString("fromStationName"));
		data.put("seattime", body.getString("departureTime"));
		data.put("trainno", body.getString("trainCode"));
		data.put("retValue", "success");
		data.put("to", body.getString("toStationName"));
	}
}
