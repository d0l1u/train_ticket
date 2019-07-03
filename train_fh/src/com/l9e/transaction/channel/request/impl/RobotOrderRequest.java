
package com.l9e.transaction.channel.request.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.common.Consts;
import com.l9e.transaction.service.impl.SysSettingServiceImpl;
import com.l9e.transaction.service.impl.TrainServiceImpl;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.ErrorInfo;
import com.l9e.transaction.vo.Order;
import com.l9e.transaction.vo.Result;
import com.l9e.transaction.vo.ReturnOptlog;
import com.l9e.transaction.vo.ReturnVO;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.ApplicationContextUtil;
import com.l9e.util.ConfigUtil;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MD5;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.PostRequestUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.UrlFormatUtil;
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

	private Logger logger = Logger.getLogger(this.getClass());

	public RobotOrderRequest(Account account, Worker worker) {
		super(account, worker);
	}

	@Override
	public Result request(Order order, String weight) {

		String orderId = order.getOrderId();
		String uuid = order.getUuid();
		TrainServiceImpl service = new TrainServiceImpl();

		if (StringUtils.isBlank(uuid)) {
			uuid = "[";
			Random random = new Random();
			for (int i = 0; i < 6; i++) {
				uuid = uuid + random.nextInt(9);
			}
			uuid = uuid + "] ";
		}

		logger.info(uuid + "------- Robot Request:" + orderId + " -------");

		// 生成一个随机的16位的字符串(设备号）
		String deviceNo = StrUtil.getRandomString(16);
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil.stringToDate(datePre + "060000", "yyyyMMddHHmmss");// 6:00
		Date end = DateUtil.stringToDate(datePre + "230000", "yyyyMMddHHmmss");// 23:00
		if (date.before(begin) || date.after(end)) {
			logger.info(uuid + "【向机器人发送预订请求】发送时间在23:00~06:00之间，不予发送！");
			result.setRetValue(Result.MANUAL);
			return result;
		}

		// LUA 参数
		Map<String, String> maps = new HashMap<String, String>();

		// 新版java 预订机器人，发送post请求，参数格式 json
		JSONObject requestJson = new JSONObject();
		requestJson.put("orderId", orderId);

		// 在此先判断订单是否自带12306账号 如果订单中没带12306的账号密码，则跳过下面一步的流程 账号来源： 0：公司自有账号
		// ；1：12306自带账号
		/*
		 * Integer accountFromWay = order.getAccountFromWay();
		 * logger.info(orderId + "账号来源为: " + accountFromWay); if (null !=
		 * accountFromWay && 1 == accountFromWay) {
		 * 
		 * List<Passenger> passengers =
		 * App.orderService.getPassengersByOrderId(orderId); logger.info(orderId
		 * + "自带12306账号下的联系人个数为:" + passengers.size()); String errorCode = "1";
		 * // 登录验证返回的错误码信息。 默认核验成功 1：成功 if (passengers != null &&
		 * passengers.size() > 0) {
		 * 
		 * // 把订单下面的多个身份证号用","分隔，拼成一个全新的字符串。 StringBuilder passportNos = new
		 * StringBuilder(); String passportNo = null; for (int i = 0; i <
		 * passengers.size(); i++) { passportNo =
		 * passengers.get(i).getPassportNo(); passportNos.append(passportNo);
		 * passportNos.append(","); } errorCode =
		 * App.accountService.handleBindAccErrorCode(account.getUsername(),
		 * account.getPassword(), passportNos.toString()); logger.info(orderId +
		 * "自带12306账号登录核验结果为 : " + errorCode); }
		 * 
		 * if ("0".equals(errorCode)) { logger.info("登录核验发生未知错误，预定转人工！");
		 * result.setErrorInfo("自带12306账号订单，登录核验发生未知错误！");
		 * result.setFailReason("登录核验发生未知错误，预定转人工！");
		 * result.setRetValue(Result.MANUAL); return result; } else if
		 * ("21".equals(errorCode)) { logger.info("传入的12306账号未进行手机核验，预定失败！");
		 * result.setErrorInfo("自带12306账号订单，传入的12306账号未进行手机核验！");
		 * result.setFailReason(ErrorInfo.MOBILEPHONE_NO_VERIFY);
		 * result.setRetValue(Result.FAILURE); return result; } else if
		 * ("22".equals(errorCode)) { logger.info("传入的12306账号 用户达上限，预定失败！");
		 * result.setErrorInfo("自带12306账号订单，传入的12306账号用户达上限！");
		 * result.setFailReason(ErrorInfo.USERNUMBERS_OVER_TOPLIMIT);
		 * result.setRetValue(Result.FAILURE); return result; } else if
		 * ("25".equals(errorCode)) { logger.info("传入的12306账号待核验，预定失败！");
		 * result.setErrorInfo("自带12306账号订单，传入的12306账号待核验！");
		 * result.setFailReason(ErrorInfo.ACCOUNT_WAIT_VERIFY);
		 * result.setRetValue(Result.FAILURE); return result; } else if
		 * ("26".equals(errorCode)) { logger.info("传入的12306账号不存在，预定失败！");
		 * result.setErrorInfo("自带12306账号订单，传入的12306账号不存在！");
		 * result.setFailReason(ErrorInfo.ACCOUNT_NO_EXIST);
		 * result.setRetValue(Result.FAILURE); return result; } else if
		 * ("27".equals(errorCode)) { logger.info("传入的12306账号登录密码错误，预定失败！");
		 * result.setErrorInfo("自带12306账号订单，传入的12306账号登录密码错误！");
		 * result.setFailReason(ErrorInfo.PASSWORD_WRONG);
		 * result.setRetValue(Result.FAILURE); return result; } }
		 */

		// 查询系统设置的预订机器人版本
		SysSettingServiceImpl sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.queryLogList();
		} catch (Exception e) {
			logger.info(uuid + "获取出票系统日志异常！", e);
		}
		List<ReturnOptlog> systemLogList = sysImpl.getList_return();
		// logger.info(uuid + "出票系统日志:" + JSONObject.toJSONString(listReturn));

		int resendNum = 0;
		MemcachedUtil memcache = MemcachedUtil.getInstance();
		if (null != memcache.getAttribute("resend_times_" + orderId)) {
			resendNum = Integer.valueOf(String.valueOf(memcache.getAttribute("resend_times_" + orderId)));
		}
		logger.info(uuid + "resendNum(重发次数):" + resendNum);

		int failNum = 0;
		if (null != memcache.getAttribute("fail_times_" + orderId)) {
			failNum = Integer.valueOf(String.valueOf(memcache.getAttribute("fail_times_" + orderId)));
		}
		logger.info(uuid + "failNum(失败次数):" + failNum);

		int manualNum = 0;
		if (null != memcache.getAttribute("manual_times_" + orderId)) {
			manualNum = Integer.valueOf(String.valueOf(memcache.getAttribute("manual_times_" + orderId)));
		}
		logger.info(uuid + "manualNum(人工次数):" + manualNum);

		int waitNum = 0;
		if (null != memcache.getAttribute("wait_times_" + orderId)) {
			waitNum = Integer.valueOf(String.valueOf(memcache.getAttribute("wait_times_" + orderId)));
		}
		logger.info(uuid + "waitNum(等待次数):" + waitNum);

		Integer workerLangType = worker.getWorkerLangType();
		logger.info(uuid + " 机器脚本语言类型为:" + workerLangType + " [1:lua脚本语言, 2:java脚本语言, 3:Python]");
		String workerExt = worker.getWorkerExt();

		if ("0".equals(String.valueOf(workerLangType)) || "1".equals(String.valueOf(workerLangType))) {
			logger.info(uuid + "执行LUA脚本12306new.lua");
			// String weight = deviceWeight(service);
			String scriptPath = "12306new.lua";
			logger.info(uuid + "出票模式权重:" + weight);
			maps.put("ScriptPath", scriptPath);
			try {
				// LUA 全部PC出票
				service.updateDevice(orderId, 0);
				service.insertHistory(orderId, "本次预订使用设备:LUA PC");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		// 打码方式：0、手动打码 1、机器识别
		String randCodeType = "0";
		sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.querySysVal("rand_code_type");
			if (StringUtils.isNotEmpty(sysImpl.getSysVal())) {
				randCodeType = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info(uuid + "打码方式:" + randCodeType + " [0:手动打码, 1:机器识别]");

		maps.put("commond", "trainOrder");
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));
		maps.put("Timeout", "240000");
		// 乘客数量+账号信息
		maps.put("ParamCount", String.valueOf(order.getOrderCps().size() + 1));

		// 机器人返回信息改造
		StringBuffer sb = new StringBuffer();
		String username = account.getUsername();
		String password = account.getPassword();
		// 各个操作的睡眠时间JSON串
		String timeIntervalParam = timeIntervalParam(service);
		// tn17101342036412|香坊|海林|2017-10-16|K1451
		String orderstr = order.getOrderstr();
		sb.append(username).append("|").append(password).append("|").append(orderstr).append("|").append(randCodeType).append("|").append(worker.getWorkerId())
				.append("|").append(timeIntervalParam).append("|").append(deviceNo);
		String[] orderArr = orderstr.split("\\|");

		// 账号信息
		JSONObject accountJson = new JSONObject();
		accountJson.put("username", username);
		accountJson.put("password", password);
		requestJson.put("account", accountJson);

		// 车票信息
		JSONObject ticketJson = new JSONObject();
		ticketJson.put("departureDate", orderArr[3]);
		ticketJson.put("trainCode", orderArr[4]);

		// 在此获取订单中出发城市和到达城市的三字码 add by wangsf
		String fromCity = order.getFrom();
		logger.info(uuid + " 订单中出发城市为：" + fromCity);
		String toCity = order.getTo();
		logger.info(uuid + " 订单中到达城市为：" + toCity);

		String fromCity3c = order.getFromCity_3c();
		logger.info(uuid + " 订单中出发城市三字码为：" + fromCity3c);
		String toCity3c = order.getToCity_3c();
		logger.info(uuid + " 订单中到达城市三字码为：" + toCity3c);
		if (StringUtils.isNotBlank(fromCity3c) && StringUtils.isNotBlank(toCity3c)) {
			sb.append("|").append(fromCity3c).append("|").append(toCity3c);
		} else {
			if (StringUtils.isNotBlank(fromCity) && StringUtils.isNotBlank(toCity)) {
				Object fromCity3cOb = null;
				Object toCity3cOb = null;

				logger.info(uuid + "从Redis缓存中获取车站简码");
				try {
					fromCity3cOb = Consts.redisDao.getCacheVal(MD5.getMd5_UTF8(fromCity));
					if (null != fromCity3cOb) {
						fromCity3c = fromCity3cOb.toString(); // redis缓存中出发城市三字码
					}
				} catch (Exception e) {
					logger.info("fromCity3cOb Exception", e);
				}
				logger.info(uuid + " redis缓存中出发城市三字码为：" + fromCity3c);

				try {
					toCity3cOb = Consts.redisDao.getCacheVal(MD5.getMd5_UTF8(toCity));
					if (null != toCity3cOb) {
						toCity3c = toCity3cOb.toString(); // redis缓存中到达城市三字码
					}
				} catch (Exception e) {
					logger.info(uuid + "toCity3cOb Exception", e);
				}
				logger.info(uuid + " redis缓存中到达城市三字码为：" + toCity3c);

				if (StringUtils.isNotBlank(fromCity3c) && StringUtils.isNotBlank(toCity3c)) {
					sb.append("|").append(fromCity3c);
					sb.append("|").append(toCity3c);
				} else {
					logger.info(uuid + "从数据库缓存中获取车站简码");
					Order orderCity3c = new Order();
					try {
						service.queryOrderCity3c(fromCity, toCity);
					} catch (Exception e) {
						logger.info(uuid + " queryOrderCity3c2：", e);
					}
					orderCity3c = service.getOrder3c();
					if (null != orderCity3c) {
						fromCity3c = orderCity3c.getFromCity_3c();
						toCity3c = orderCity3c.getToCity_3c();
						logger.info(uuid + " 从数据库中取出的出发三字码:" + fromCity3c);
						logger.info(uuid + " 从数据库中取出的到达三字码:" + toCity3c);
						if (StringUtils.isNotBlank(fromCity3c)) {
							Consts.redisDao.setCacheVal(MD5.getMd5_UTF8(fromCity), fromCity3c);
							sb.append("|").append(fromCity3c);
						}
						if (StringUtils.isNotBlank(toCity3c)) {
							Consts.redisDao.setCacheVal(MD5.getMd5_UTF8(toCity), toCity3c);
							sb.append("|").append(toCity3c);
						}
					}
				}
			}
		}
		logger.info(uuid + "最终的出发城市的三字码为：" + fromCity3c);
		logger.info(uuid + "最终的到达城市的三字码为：" + toCity3c);

		ticketJson.put("fromStationName", fromCity.trim());
		ticketJson.put("fromStationCode", fromCity3c);
		ticketJson.put("toStationName", toCity.trim());

		// 新增动态库或黑盒调用参数
		String dynamicDepotOrBlackBox = "0";
		sysImpl = new SysSettingServiceImpl();
		try {
			sysImpl.querySysVal("dynamicDepot_or_blackBox");
			if (StringUtils.isNotEmpty(sysImpl.getSysVal())) {
				dynamicDepotOrBlackBox = sysImpl.getSysVal();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb.append("|").append(dynamicDepotOrBlackBox);
		logger.info(uuid + "动态库类型:" + dynamicDepotOrBlackBox + " [0:动态库, 1:黑盒]");

		// 新增客户预选的卧铺位置或者座位号
		String chooseSeats = order.getChoose_seats();
		chooseSeats = StringUtils.isBlank(chooseSeats) || chooseSeats.equals("null") ? null : chooseSeats;
		String seatDetailType = order.getSeatDetailType();
		seatDetailType = StringUtils.isBlank(seatDetailType) || seatDetailType.equals("null") ? null : seatDetailType;
		logger.info(uuid + "选座 chooseSeats：" + chooseSeats + "， seatDetailType：" + seatDetailType);
		sb.append("|").append(seatDetailType);
		sb.append("|").append(chooseSeats);
		ticketJson.put("chooseSeats", chooseSeats);

		maps.put("Param1", sb.toString());

		int i = 2;
		String channel = order.getChannel();
		logger.info(uuid + "渠道商:" + channel);
		// 新版java参数
		JSONArray passengers = new JSONArray();
		try {
			for (String param : order.getOrderCps()) {
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
				logger.info(uuid + "param" + i + ":" + param);
				// 普通:TN_a1b55ee59cc5a72c|王金利|0|2|132930197705154318|2|2
				// passengerNo|姓名|乘客类型|证件类型|证件号|坐席|多余参数
				maps.put("Param" + i, param);
				i++;

				// 处理新版java 参数
				JSONObject passenger = new JSONObject();
				paramArr = param.split("\\|");
				passenger.put("passengerNo", paramArr[0]);
				passenger.put("name", paramArr[1].toUpperCase().trim());
				passenger.put("ticketType", paramArr[2].toUpperCase());
				passenger.put("cardType", paramArr[3].toUpperCase());
				passenger.put("cardNo", paramArr[4].toUpperCase().trim());
				passenger.put("seatType", paramArr[5].toUpperCase());
				// 学生票信息
				if ("3".equals(seatType)) {
					// 河北|13|12796|河北工程技术学院|17080703004|4|2017|石家庄|365|天津|359
					// 所在省份名称
					passenger.put("provinceName", paramArr[7]);
					// 省份代码
					passenger.put("provinceCode", paramArr[8]);
					// 学校代码
					passenger.put("schoolCode", paramArr[9]);
					// 学校名称
					passenger.put("schoolName", paramArr[10]);
					// 学号
					passenger.put("studentNo", paramArr[11]);
					// 学制
					passenger.put("system", paramArr[12]);
					// 入学年份
					passenger.put("enterYear", paramArr[13]);
					// 优惠区间出发站名
					passenger.put("limitBeginName", paramArr[14].trim());
					// 优惠区间出发站简码
					passenger.put("limitBeginCode", paramArr[15]);
					// 优惠区间到达站名
					passenger.put("limitEndName", paramArr[16].trim());
					// 优惠区间到达站简码
					passenger.put("limitEndCode", paramArr[17]);
				}
				passengers.add(passenger);
			}
		} catch (Exception e) {
			logger.info(uuid + "处理乘客信息出现异常", e);
		}
		ticketJson.put("passengers", passengers);
		requestJson.put("data", ticketJson);

		String param = null;
		try {
			param = UrlFormatUtil.createUrl("", maps, i);
		} catch (Exception e) {
			logger.info(uuid + "URL 编码参数异常", e);
		}

		// 代理IP配置
		sysImpl = new SysSettingServiceImpl();
		int proxyFlag = 1; // 0关闭, 1 打开
		try {
			proxyFlag = sysImpl.querySysVal("proxyIP_switch");
		} catch (Exception e) {
			logger.info(uuid + "获取代理ip开关异常", e);
		}
		logger.info(uuid + " 代理ip开关:" + proxyFlag + " [0: 关闭, 1:打开]");

		logger.info(uuid + ">>> =========================开始发送HTTP请求,调用机器人=======================");

		String reqResult = null;
		String newIp = workerExt;
		Integer ipId = 0;
		if (new Integer(2).equals(workerLangType)) {
			String url = "";
			// 判断 是 JAVA app 还是 pc
			String workerName = worker.getWorkerName();
			if (workerName.contains("dll")) {
				logger.info(uuid + ">>> JAVA APP !!!!");
				url = "http://" + workerExt + ":18010/robot/book/pc";
				requestJson.put("publicIp", workerExt);
			} else {
				if (proxyFlag == 1) {
					try {
						// 获取新IP
						// TODO
						String httpResult = HttpUtil.sendByGet(ConfigUtil.getValue("getIpInfo"), "UTF-8", 5000, 5000);
						logger.info(uuid + "代理IP获取结果:" + httpResult);
						JSONObject httpJson = JSONObject.parseObject(httpResult);
						// {"success":false,"msg":"没有可用IP"}
						// {"success":true,"msg":"操作成功","data":{"ipId":1,"ipType":2,"ipExt":"103.37.159.7:808:wxg:wxg","requestNum":1,"ifSupportChange":"0"}}
						boolean success = httpJson.getBoolean("success");
						String msg = httpJson.getString("msg");
						if (!success) {
							logger.info(uuid + "获取代理IP失败:" + msg);
							result.setRetValue(Result.MANUAL);
							result.setErrorInfo("获取代理IP失败:" + msg);
							return result;
						}
						JSONObject ipData = httpJson.getJSONObject("data");
						ipId = ipData.getInteger("ipId");
						newIp = ipData.getString("ipExt").split(":")[0];
						logger.info(uuid + "切换IP, 切换后新的IP:" + newIp);

						url = "http://" + newIp + ":18010/robot/book/pc";
						requestJson.put("publicIp", newIp);
					} catch (Exception e) {
						logger.info(uuid + "获取代理IP异常", e);
						result.setRetValue(Result.MANUAL);
						result.setErrorInfo("获取代理IP异常:" + e.getClass().getSimpleName());
						return result;
					}
				} else {
					url = "http://" + workerExt + ":18010/robot/book/pc";
					requestJson.put("publicIp", workerExt);
				}
				logger.info(uuid + ">>> JAVA PC !!!!");
			}

			param = requestJson.toJSONString();
			logger.info(uuid + ">>> JAVA请求路径:" + url + ":" + param);
			reqResult = PostRequestUtil.postJson(url, "UTF-8", param);
			logger.info(uuid + "JAVA返回结果:" + reqResult);

			// 释放代理IP
			if (!workerName.contains("dll") && proxyFlag == 1) {
				service = new TrainServiceImpl();
				int insertResult = 0;
				try {
					insertResult = service.updateIpStatus(ipId);
				} catch (Exception e) {
					logger.info(uuid + "释放的IP异常 newIp:", e);
				}
				logger.info(uuid + "释放的IP结果:" + insertResult);
			}
		} else {
			logger.info(uuid + ">>> LUA !!!!");
			reqResult = PostRequestUtil.getPostRes("UTF-8", workerExt, param);
			reqResult = reqResult.replace("[\"{", "[{");
			reqResult = reqResult.replace("}\"]", "}]");
			reqResult = reqResult.replace("\\", "");
			logger.info(uuid + "LUA返回结果:" + reqResult);
		}
		logger.info(uuid + ">>> 机器人最终响应结果:" + reqResult);

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
		logger.info(uuid + "机器锁定开关状态workerLockSwitch为:" + workerLockSwitch);

		if ("1".equals(workerLockSwitch)) {
			// 在此释放本次请求的机器
			Consts.workerService.releaseWorker(worker.getWorkerId());
			logger.info(uuid + "机器解锁完毕！worker_lock更新为0！");
		}

		if (null == reqResult) {
			logger.info(uuid + "Post Request Result Is Empty");
			result.setErrorInfo("预定错误，没有返回结果！");
			result.setReturnOptlog(ReturnOptlog.NORESULT);
			if (resendNum >= 5) {
				result.setRetValue(Result.MANUAL);
				return result;
			} else {
				memcache.setAttribute("resend_times_" + orderId, ++resendNum, 90 * 1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}

		// 请求错误
		int resendMax = 3;
		if (reqResult.equals(PostRequestUtil.TIME_OUT) || reqResult.equals(PostRequestUtil.CONNECT_ERROR) || reqResult.equals(PostRequestUtil.URL_ERROR)) {
			logger.info(uuid + " Post Request Error, Result Value:" + reqResult);
			if (reqResult.contains("CONNECTERROR")) {
				logger.info(uuid + "机器异常，请尽快进入后台机器管理，针对该机器人进行【短信停用】！");
				result.setReturnOptlog(ReturnOptlog.SOS);
				result.setErrorInfo("机器异常，请尽快进入后台机器管理，针对该机器人进行【短信停用】！");
				result.setRetValue(Result.MANUAL);
				return result;
			} else {
				logger.info(uuid + "机器预定超时，自动重发！");
				result.setReturnOptlog(ReturnOptlog.TIMEOUT);
				result.setErrorInfo("机器预定超时，自动重发！【" + reqResult + "】");
			}
			if (resendNum >= resendMax) {
				logger.info(uuid + "重发超过" + resendMax + "次,转人工");
				result.setRetValue(Result.MANUAL);
				return result;
			} else {
				logger.info(uuid + "机器问题，自动重发-1");
				memcache.setAttribute("resend_times_" + orderId, ++resendNum, 90 * 1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}

		// 机器人处理超时
		if (reqResult.contains("\"ErrorCode\":9")) {
			logger.info(uuid + "Robot Response Error, Result Value:" + reqResult);
			result.setErrorInfo("机器人处理超时【" + reqResult + "】处理：请核对12306网站是否已经订购！");
			result.setReturnOptlog(ReturnOptlog.TIMEOUT);
			if (resendNum >= resendMax) {
				result.setRetValue(Result.MANUAL);
			} else {
				logger.info(uuid + "机器问题，自动重发-2");
				memcache.setAttribute("resend_times_" + orderId, ++resendNum, 90 * 1000);
				result.setRetValue(Result.RESEND);
			}
			return result;
		}

		// 在此规避 返回结果报姓名格式错误
		if (reqResult.contains("姓名格式错误")) {
			logger.info(uuid + "姓名格式错误，转人工！");
			result.setRetValue(Result.MANUAL);
			result.setErrorInfo("【姓名格式错误，转人工！】");
			return result;
		}

		// 这里将新版JAVA返回参数转换成兼容旧版本的json串
		try {
			if (new Integer(2).equals(workerLangType)) {
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
				logger.info(uuid + "New Java Response Value:" + reqResult);
			}
		} catch (Exception e) {
			logger.info(uuid + "JAVA返回参数转换出现异常", e);
			result.setRetValue(Result.MANUAL);
			result.setErrorInfo("【JAVA返回结果转JSON出现异常，转人工！】");
			return result;
		}

		ReturnVO retObj = null;
		try {
			ObjectMapper map = new ObjectMapper();
			retObj = map.readValue(reqResult, ReturnVO.class);
		} catch (Exception e) {
			logger.info(uuid + "ObjectMapper ReadValue Exception", e);
			result.setErrorInfo("JSON分析异常【" + reqResult + "】");
			result.setReturnOptlog(ReturnOptlog.ROBOTEXCP);
			if (resendNum >= manualNum) {
				logger.info(uuid + "JSON分析异常,自动重发");
				result.setRetValue(Result.MANUAL);
				return result;
			} else {
				logger.info(uuid + "JSON分析异常,转人工处理");
				MemcachedUtil.getInstance().setAttribute("resend_times_" + orderId, ++resendNum, 90 * 1000);
				result.setRetValue(Result.RESEND);
				return result;
			}
		}

		try {
			String spare_thread = retObj.getErrorInfo().get(0).getRobotNum();
			if (Integer.valueOf(spare_thread) > 0) {
				sysImpl.updateWorkerSpareThread(worker.getWorkerId() + "", Integer.valueOf(spare_thread));
			}
		} catch (Exception e) {
			logger.info(uuid + "更新机器人空闲进程数异常！", e);
		}

		Integer errorCode = retObj.getErrorCode();
		logger.info(uuid + "ErrorCode:" + errorCode);

		if (!new Integer(0).equals(errorCode)) {
			logger.info(uuid + "ErrorCode 不为0，转失败");
			result.setRetValue(Result.FAILURE);
			result.setErrorInfo("【" + reqResult + "】");
			return result;
		}

		Order resultOrder = retObj.getErrorInfo().get(0);
		if (resultOrder == null) {
			logger.info(uuid + "retValue 为空，转失败");
			result.setRetValue(Result.FAILURE);
			result.setErrorInfo("【retValue 为空，转失败】");
			return result;
		}
		logger.info(uuid + "retValue:" + JSONObject.toJSONString(resultOrder));

		// 排队时间改造--只针对途牛
		if ("tuniu".equals(channel)) {
			logger.info(uuid + "途牛订单排队时间改造");
			String waitTime = resultOrder.getWaitTime();// 订单排队时间
			Integer queueNumber = resultOrder.getQueueNumber();// 当前排队人数
			String msg = "未出票,订单排队中...";
			logger.info(uuid + "排队时间为：" + waitTime + ", 排队人数为：" + queueNumber);
			if (StringUtils.isNotBlank(waitTime)) {
				// 插入tuniu_order_queue表一条排队数据
				Map<String, Object> insertMap = new HashMap<String, Object>();
				insertMap.put("order_id", order.getOrderId());
				insertMap.put("queue_number", queueNumber);
				insertMap.put("wait_time", waitTime);
				insertMap.put("msg", msg);
				insertMap.put("notify_status", 1);// 通知状态
													// 1:等待通知；2:正在通知；3:通知成功；4:通知失败；
													// 5:重新通知
				try {
					service.insertTuniuOrderQueue(insertMap);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		// 更新客户可以选座的坐席类型（可能有多个）
		String chooseSeatType = resultOrder.getChooseSeatType();
		logger.info(uuid + " 客户可以选座的坐席类型chooseSeatType为：" + chooseSeatType);
		try {
			service.updateOrderChooseSeatType(orderId, chooseSeatType);
		} catch (Exception e) {
			e.printStackTrace();
		}

		String retValue = resultOrder.getRetValue();
		String retInfo = resultOrder.getRetInfo();
		String channelGroup = order.getChannelGroup();
		logger.info(uuid + "channelGroup:" + channelGroup);
		if (Order.CHANNEL_GROUP_1.equals(channelGroup)) {
			String formatStr_new = "yyyy-MM-dd HH:mm:ss";
			try {
				String seattime = resultOrder.getSeattime();
				if (StringUtils.isBlank(seattime)) {
					order.setSeattime("");
				} else {
					order.setSeattime(DateUtil.stringToString(seattime + ":00", formatStr_new));
				}

				String arrivetime = resultOrder.getArrivetime();
				if (StringUtils.isBlank(arrivetime)) {
					order.setArrivetime("");
				} else {
					order.setArrivetime(DateUtil.stringToString(arrivetime + ":00", formatStr_new));
				}
			} catch (Exception e) {
				logger.info(uuid + "时间异常！", e);
			}
		}
		logger.info(uuid + "tongcheng arrive:" + resultOrder.getArrivetime());

		String loginFailNum = String.valueOf(memcache.getAttribute("robot" + worker.getWorkerId()));
		logger.info(uuid + "loginFailNum:" + loginFailNum);

		if (StringUtils.equals(retValue, "success")) {// 成功
			memcache.setAttribute("robot" + worker.getWorkerId(), 0);
			result.setSelfId(resultOrder.getOrderId());
			result.setSheId(resultOrder.getOutTicketBillNo());
			result.setSelfId(resultOrder.getOrderId());
			result.setSheId(resultOrder.getOutTicketBillNo());
			result.setFromCity(resultOrder.getFrom());
			result.setToCity(resultOrder.getTo());
			result.setTrainNo(resultOrder.getTrainno());
			result.setPayMoney(resultOrder.getBuymoney());
			result.setOrderCps(resultOrder.getCps());
			result.setSeattime(resultOrder.getSeattime());
			result.setBuyMoney(resultOrder.getBuymoney());
			result.setRefundOnline(resultOrder.getRefundOnline());
			result.setRetValue(Result.SUCCESS);
			result.setManual(false);

			logger.info(uuid + "contacts num:" + resultOrder.getContactsnum());
			result.setContactsNum(resultOrder.getContactsnum());

			// 常用联系人添加成功需要添加到账号过滤表中
			result.setInsertFilter(true);
			result.setFilterScope(Result.FILTER_ALL);
			int isPay = 0;
			try {
				isPay = service.orderIsPay(orderId);
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.info(uuid + "isPay:" + isPay);
			String orderPaymoney = order.getPaymoney();
			String resultBuyMoney = resultOrder.getBuymoney();
			logger.info(uuid + "orderPaymoney:" + orderPaymoney);
			logger.info(uuid + "resultBuyMoney:" + resultBuyMoney);
			if (StringUtils.isNotBlank(orderPaymoney) && isPay == 1) {
				if (!new Double(resultBuyMoney).equals(new Double(orderPaymoney))) {
					result.setErrorInfo("价格不相同，请确认价格【儿童票、特价票、卧铺票】！");
					logger.info(uuid + "价格不相同，请确认价格【儿童票、特价票、卧铺票】！");
				}

				if (new Double(resultBuyMoney).doubleValue() > new Double(orderPaymoney).doubleValue()) {
					logger.info(uuid + "price not same, setPriceModify=true,need update base price!");
					double buyMoney = Double.parseDouble(resultBuyMoney);
					double payMoney = Double.parseDouble(orderPaymoney);
					double diffMoney = buyMoney - payMoney;
					if (StringUtils.isNotEmpty(channel) && "19e".equals(channel)) {
						if (diffMoney <= 10) {
							logger.info(uuid + "19e channel price differ=" + diffMoney + "<=10, can out ticket!");
							result.setErrorInfo("<b>价格不相同，报价接口报价错误，19e渠道10元误差可以出票！真实价格" + buyMoney + "</b>");
						} else {
							logger.info(uuid + "19e channel price differ=" + diffMoney + ">10, out ticket fail!");
							result.setErrorInfo("出票失败，请确定错误！【价格不相同，报价接口报价错误，19e渠道大于10元误差，出票失败！真实价格" + buyMoney + "】");
							result.setFailReason(ErrorInfo.TICKET_PRICE_WRONG);// 票价和12306不符
							result.setRetValue(Result.CANCEL);
							result.setPriceModify(true);// 需要更新车票表
							return result;
						}
					} else {
						logger.info(uuid + "other channel price differ =" + diffMoney + ", out ticket fail!");
						result.setErrorInfo("出票失败，请确定错误！【价格不相同，报价接口报价错误，非19e渠道出票失败！真实价格" + buyMoney + "】");
						result.setFailReason(ErrorInfo.TICKET_PRICE_WRONG);// 票价和12306不符
						result.setPriceModify(true);// 需要更新车票表
						result.setRetValue(Result.CANCEL);
						return result;
					}
				}
			}

			String formatStr = "yyyy-MM-dd HH:mm";
			// 同程不需要比较发车时间
			if (!Order.CHANNEL_GROUP_1.equals(channelGroup)) {
				String orderSeattime = order.getSeattime();
				String resultSeattime = resultOrder.getSeattime();
				Date oSeattime = DateUtil.stringToDate(orderSeattime, formatStr);
				Date rSeattime = DateUtil.stringToDate(resultSeattime, formatStr);

				logger.info(uuid + "oSeattime:" + oSeattime);
				logger.info(uuid + "rSeattime:" + rSeattime);

				if (!oSeattime.equals(rSeattime)) {
					long diffms = 0L;
					if (oSeattime.after(rSeattime)) {
						diffms = oSeattime.getTime() - rSeattime.getTime();
					} else {
						diffms = rSeattime.getTime() - oSeattime.getTime();
					}
					long diffm = diffms / 1000 / 60;
					if (diffm >= 10L) {
						logger.info(uuid + "time diff=" + diffm + ">=20min,out ticket fail!");
						result.setErrorInfo("出票失败，请确定错误！【乘车时间异常，相差10分钟以上，以无票处理！真实日期：" + resultOrder.getSeattime() + "】");
						result.setFailReason(ErrorInfo.RIDING_TIME_EXCEPTION);// 乘车时间异常
						result.setRetValue(Result.CANCEL);
						result.setPriceModify(true);// 需要更新车票表
						return result;
					} else {
						logger.info(uuid + "time diff=" + diffm + "<20min,can out ticket!");
						result.setErrorInfo("<b>乘车时间异常，处理：相差10分钟以内可以开始出票！真实日期：" + resultOrder.getSeattime() + "</b>");
					}
				}
			}

			String level = order.getLevel();
			String seatType = order.getSeatType();
			logger.info(uuid + "level:" + level);
			logger.info(uuid + "seatType:" + seatType);
			if (StringUtils.isNotEmpty(level) && "10".equals(level)) {
				if (reqResult.contains("无座") && !StringUtils.equals(seatType, "9")) {
					if (!order.isWea_wz()) {
						logger.info(uuid + "订到无票坐席，并且备选坐席没有选择\"无座\"");
						result.setErrorInfo("出票失败，请确定错误！订到无票坐席，并且备选坐席没有选择\"无座\"");
						result.setFailReason(ErrorInfo.WITHOUT_TICKET);// 失败原因为：无票
						result.setRetValue(Result.CANCEL);
						result.setPriceModify(true);// 需要更新车票表
						return result;
					}
				}
				logger.info(uuid + "<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>" + retInfo);
				result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>" + retInfo);
				result.setManual(true);
				return result;
			}

			if (reqResult.contains("无座") && !StringUtils.equals(seatType, "9")) {
				if (!order.isWea_wz()) {
					logger.info(uuid + "订到无票坐席，并且备选坐席没有选择\"无座\"");
					result.setErrorInfo("出票失败，请确定错误！订到无票坐席，并且备选坐席没有选择\"无座\"");
					result.setFailReason(ErrorInfo.WITHOUT_TICKET);// 失败原因为：无票
					result.setRetValue(Result.CANCEL);
					result.setPriceModify(true);// 需要更新车票表
					return result;
				} else {
					result.setErrorInfo(uuid + "订到无票坐席，并且备选坐席选择无座，直接预订成功！</b>" + retInfo);
					result.setRetValue(Result.SUCCESS);
					return result;
				}
			}

			/**
			 * 一人多单判断 TODO </br>
			 * 这里数据库并不是立马跟新订单信息，可能延迟3-10秒处理其他操作。在12306登录不互相踢，并且一秒多单同一乘客订票的情况下
			 * </br>
			 * 第二单可能在这个时间差内没有查询到相同的12306订单号，通过redis分布式锁实现
			 */
			try {
				String sequence = resultOrder.getOutTicketBillNo();
				SingleJedisClient jedisClient = ApplicationContextUtil.context.getBean(SingleJedisClient.class);
				String setResult = jedisClient.set(sequence, orderId, "NX", "EX", 60 * 30);
				if (!"OK".equals(setResult)) {
					// 判断value是否与订单号一致，如果一致，则默认成功
					String getValue = jedisClient.get(sequence);
					logger.info(uuid + sequence + "一人多单查询结果:" + orderId + "-" + getValue);
					if (!orderId.equals(getValue)) {
						logger.info(uuid + "可能存在一人多单，或者往返票赔款的情况，转入人工处理");
						result.setErrorInfo("可能存在一人多单，或者往返票赔款的情况，转入人工处理");
						result.setRetValue(Result.MANUAL);
						return result;
					}
				}
			} catch (Exception e) {
				logger.info(uuid + "查询一人多单异常", e);
			}

			return result;
		} else if (StringUtils.equals(retValue, "manual")) {
			logger.info(uuid + "人工处理");
			memcache.setAttribute("robot" + worker.getWorkerId(), 0);
			result.setErrorInfo("出票失败，请确定错误,转人工！【" + retInfo + "】");
			result.setRetValue(Result.MANUAL);
			return result;
		} else if (StringUtils.equals(retValue, "failure")) {// 失败
			logger.info(uuid + "失败处理");
			result.setSelfId(resultOrder.getOrderId());
			result.setErrorInfo("出票失败，请确定错误！【" + retInfo + "】");

			if (StringUtils.isEmpty(retInfo)) {
				result.setRetValue(Result.MANUAL);
				memcache.setAttribute("robot" + worker.getWorkerId(), 0);
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
			for (ReturnOptlog systemLog : systemLogList) {
				if (retInfo.contains(systemLog.getReturn_name())) {
					String logName = systemLog.getReturn_name();
					String logId = systemLog.getReturn_id();
					String logJoin = systemLog.getReturn_join();
					String logType = systemLog.getReturn_type();
					String logFail = systemLog.getFail_reason();
					logger.info(uuid + "针对错误日志:" + logName + ", logId:" + logId + ", logType:" + logType + ", logJoin:" + logJoin);

					result.setReturnOptlog(logId);
					if ("00".equals(logType)) {// 重发
						if ("1".equals(logJoin)) {// 是否参数重发
							ifResend = 1; // 参与重发次数
						} else {
							ifResend = 2; // 直接重发
						}
						break;
					} else if ("11".equals(logType)) { // 直接失败
						// 因为接口返回的报错信息不一致，所以把||
						// "elong".equals(channel)这个条件去掉 modify by wangsf
						// 2016.4.27
						if ("8".equals(logFail) && StringUtils.isNotEmpty(channel) && "qunar".equals(channel)) {
							result.setFailReason(ErrorInfo.CANCEL_ORDER);
						} else if ("12".equals(logFail) && StringUtils.isNotEmpty(channel) && "qunar".equals(channel)) {
							result.setFailReason(ErrorInfo.NOPASS_REALNAME_VERIFY);
						} else {
							result.setFailReason(logFail);
						}

						if ("1".equals(logJoin)) {// 是否参数重发
							ifResend = 3; // 参与失败次数
						} else {
							ifResend = 4; // 直接失败
						}
						break;
					} else if ("33".equals(logType)) { // 按时重发
						// 因为接口返回的报错信息不一致，所以把||
						// "elong".equals(channel)这个条件去掉 modify by wangsf
						// 2016.4.27
						if ("8".equals(logFail) && StringUtils.isNotEmpty(channel) && "qunar".equals(channel)) {
							result.setFailReason(ErrorInfo.CANCEL_ORDER);
						} else {
							result.setFailReason(logFail);
						}
						if ("1".equals(logJoin)) {// 是否参数重发
							ifResend = 5; // 参与失败次数
						} else {
							ifResend = 6; // 直接失败
						}
						break;
					} else if ("22".equals(logType)) { // 人工
						if ("1".equals(logJoin)) {// 是否参数重发
							ifResend = 7; // 参与失败次数
						} else {
							ifResend = 8; // 直接失败
						}
						break;
					}
				}
			}

			logger.info(uuid + "ifResend:" + ifResend);
			logger.info(uuid + "错误信息为:" + retInfo);

			// 自动切换账号 TODO
			// 封停账号
			if (retInfo.contains("密码输入错误。") || retInfo.contains("密码输入错误次数过多") || retInfo.contains("账号状态为：未通过") || StrUtil.containRegex(retInfo, "手机.*核验")
					|| StrUtil.containRegex(retInfo, "重新.*注册") // 账号重新注册
					|| retInfo.contains("未通过核验，不能添加常用联系人")// 账号未通过核验
			) {
				logger.info(uuid + orderId + "密码输入错误,封停账号:" + retInfo);
				result.setRetValue(Result.END);
				result.setErrorInfo("【" + retInfo + "】");
				return result;
			}
			// 临时停用
			if (retInfo.contains("您取消次数过多") // 取消次数过多
					|| StrUtil.containRegex(retInfo, "联系人.*上限")// 联系人达上限
					|| retInfo.contains("通过后即可在网上购票") // 身份核验未通过
					|| retInfo.contains("未完成订单信息不匹配,")// 未完成订单信息不匹配,
			) {
				logger.info(uuid + orderId + "错误信息为,临时停用账号:" + retInfo);
				memcache.setAttribute("robot" + worker.getWorkerId(), 0);
				result.setRetValue(Result.STOP);
				result.setErrorInfo("【" + retInfo + "】");
				return result;

			}

			if (retInfo.indexOf("网络可能存在问题") >= 0 || retInfo.indexOf("12306服务器压力太大") >= 0) {
				ifResend = 1;
			}
			if (ifResend != 0) {
				if (ifResend == 1) {
					logger.info(uuid + "预定重发1");
					MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					if (resendNum >= 5) {
						result.setRetValue(Result.MANUAL);
						return result;
					} else {
						MemcachedUtil.getInstance().setAttribute("resend_times_" + order.getOrderId(), ++resendNum, 90 * 1000);
						result.setRetValue(Result.RESEND);
						return result;
					}
				} else if (ifResend == 2) {
					logger.info(uuid + "预定重发2");
					result.setRetValue(Result.RESEND);
					return result;
				} else if (ifResend == 3) {// 出票失败
					logger.info(uuid + "出票失败超时重发3");
					MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
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
						MemcachedUtil.getInstance().setAttribute("fail_times_" + order.getOrderId(), ++failNum, 90 * 1000);
						result.setRetValue(Result.RESEND);
						return result;
					}
				} else if (ifResend == 4) {
					logger.info(uuid + "出票失败4");
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
					logger.info(uuid + "按时重发5");
					MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					if (waitNum >= 3) {
						if (!Result.NOPASS.equals(result.getRetValue())) {
							result.setRetValue(Result.FAILURE);
						}
						return result;
					} else {
						MemcachedUtil.getInstance().setAttribute("wait_times_" + order.getOrderId(), ++waitNum, 4 * 60 * 1000);
						result.setRetValue(Result.WAIT);
						return result;
					}
				} else if (ifResend == 6) {
					logger.info(uuid + "按时重发6");
					if (StringUtils.isNotEmpty(order.getLevel()) && "10".equals(order.getLevel())) {
						result.setErrorInfo("<b>本订单为联程订单，处理：请确认联程订单的相关订单都预订成功后再出票！</b>" + retInfo);
						result.setRetValue(Result.MANUAL);
						return result;
					} else {
						result.setRetValue(Result.FAILURE);
						return result;
					}
				} else if (ifResend == 7) {
					logger.info(uuid + "预定人工1");
					MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					if (manualNum >= 2) {
						result.setRetValue(Result.MANUAL);
						return result;
					} else {
						MemcachedUtil.getInstance().setAttribute("manual_times_" + order.getOrderId(), ++manualNum, 90 * 1000);
						result.setRetValue(Result.RESEND);
						return result;
					}
				} else if (ifResend == 8) {
					logger.info(uuid + "预定人工2");
					result.setRetValue(Result.MANUAL);
					return result;
				}
			}
			if (retInfo.contains("登录失败")) {
				logger.info(uuid + orderId + "错误信息为:" + retInfo);

				if (loginFailNum != null && !"".equals(loginFailNum)) {
					int loginFailNumber = Integer.parseInt(loginFailNum);
					loginFailNumber += 1;
					MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), loginFailNumber);
					if (loginFailNumber >= 10) {
						try {
							sysImpl.updateRobot(worker.getWorkerId() + "");
							sysImpl.insertWarning(worker.getWorkerName());
						} catch (Exception e) {
							logger.info(uuid + "登录失败更新数据库发生异常:" + e.getClass().getSimpleName(), e);
						}
						MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
					}
					if (loginFailNumber == 1 && !StrUtil.containRegex(retInfo, "联系人.*上限")) {
						result.setRetValue(Result.RESEND);
					} else {
						result.setRetValue(Result.MANUAL);
					}
					result.setErrorInfo("【" + retInfo + "】");
					return result;
				} else {
					MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), "1");
					result.setRetValue(Result.MANUAL);
					return result;

				}
			} else {
				MemcachedUtil.getInstance().setAttribute("robot" + worker.getWorkerId(), 0);
				result.setRetValue(Result.MANUAL);
				return result;
			}
		} else if (StringUtils.equals(retValue, "nopass")) {
			logger.info(uuid + "用户未通过处理");
			result.setSelfId(resultOrder.getOrderId());
			result.setErrorInfo(retInfo);

			memcache.setAttribute("robot" + worker.getWorkerId(), 0);
			logger.info(uuid + "订票渠道：" + channel + "错误信息：" + retInfo + "修改订单状态为出票失败（身份信息待审核）");

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
			logger.error("returncode=" + retValue);
			result.setErrorInfo("出票异常，请检查机器人！【" + retInfo + "】");
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
				mUtil.setAttribute("check_account_time_interval", signInInterval, 30 * 1000);
			} else {
				checkAccountInterval = (Long) mUtil.getAttribute("check_account_time_interval");
			}

			// 未完成订单查询间隔时间
			if (mUtil.getAttribute("unfinished_order_time_interval") == null) {
				unfinishedOrderInterval = Long.valueOf(service.getSysSettingValue("unfinished_order_time_interval"));
				mUtil.setAttribute("unfinished_order_time_interval", signInInterval, 30 * 1000);
			} else {
				unfinishedOrderInterval = (Long) mUtil.getAttribute("unfinished_order_time_interval");
			}

			// 点击预订按钮间隔时间
			if (mUtil.getAttribute("book_button_time_interval") == null) {
				bookButtonInterval = Long.valueOf(service.getSysSettingValue("book_button_time_interval"));
				mUtil.setAttribute("book_button_time_interval", signInInterval, 30 * 1000);
			} else {
				bookButtonInterval = (Long) mUtil.getAttribute("book_button_time_interval");
			}

			// 获取联系人间隔时间
			if (mUtil.getAttribute("get_contact_time_interval") == null) {
				getContactInterval = Long.valueOf(service.getSysSettingValue("get_contact_time_interval"));
				mUtil.setAttribute("get_contact_time_interval", signInInterval, 30 * 1000);
			} else {
				getContactInterval = (Long) mUtil.getAttribute("get_contact_time_interval");
			}

			// 添加联系人间隔时间
			if (mUtil.getAttribute("add_contact_time_interval") == null) {
				addContactInterval = Long.valueOf(service.getSysSettingValue("add_contact_time_interval"));
				mUtil.setAttribute("add_contact_time_interval", signInInterval, 30 * 1000);
			} else {
				addContactInterval = (Long) mUtil.getAttribute("add_contact_time_interval");
			}

			// 提交预订信息间隔时间
			if (mUtil.getAttribute("submit_book_time_interval") == null) {
				submitBookInterval = Long.valueOf(service.getSysSettingValue("submit_book_time_interval"));
				mUtil.setAttribute("submit_book_time_interval", signInInterval, 30 * 1000);
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

}
