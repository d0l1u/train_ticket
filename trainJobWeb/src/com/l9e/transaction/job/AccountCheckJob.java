package com.l9e.transaction.job;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.service.WorkerService;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.DateUtil;
import com.train.commons.worm.client.HttpWormClient;
import com.train.commons.worm.client.HttpWormResponse;

/**
 * 账号核验job
 */
@Component("accountCheckJob")
public class AccountCheckJob {

	private static final Logger logger = Logger.getLogger(AccountCheckJob.class);

	@Resource
	private AccountService accountService;

	@Resource
	private CommonService commonService;

	@Resource
	private WorkerService workerService;

	/**
	 * 白名单更新
	 * 
	 * @author: taoka
	 * @date: 2017年11月27日 上午9:45:06 void
	 */
	public void accountCheck() {
		String tempUuid = "[白名单-";
		Random random = new Random();
		for (int i = 0; i < 5; i++) {
			tempUuid = tempUuid + random.nextInt(9);
		}
		tempUuid = tempUuid + "] ";

		logger.info(tempUuid + "============= update white list =============");

		try {
			Date date = new Date();
			String datePre = DateUtil.dateToString(date, "yyyyMMdd");
			Date begin = DateUtil.stringToDate(datePre + "065000", "yyyyMMddHHmmss");// 7:15
			Date end = DateUtil.stringToDate(datePre + "233000", "yyyyMMddHHmmss");// 22:50
			if (date.before(begin) || date.after(end)) {
				logger.info(tempUuid + "6:50-23:30点方可检查账号是否可用！");
				return;
			}

			// 前20分钟的时间
			Calendar theCa = Calendar.getInstance();
			theCa.setTime(date);
			theCa.add(Calendar.MINUTE, -20);
			Date date20 = theCa.getTime();
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			String queryDate = df.format(date20);

			Map<String, Object> parameter = new HashMap<String, Object>();
			parameter.put("use_status", 00);
			parameter.put("begin_time", queryDate.concat(":00"));
			// paramMap.put("end_time", querydate.concat(":59"));
			List<Map<String, Object>> accountList = accountService.queryAccInfoList(parameter);
			if (accountList == null) {
				logger.info(tempUuid + "查询待更新账号结果集为空,不做处理...");
				return;
			} else {
				logger.info(tempUuid + "待更新账号结果集 size:" + accountList.size());
			}

			// 预订机器人，状态空闲
			logger.info(tempUuid + "获取白名单机器人列表...");
			parameter.clear();
			parameter.put("worker_type", 1);
			parameter.put("worker_status", "00");
			parameter.put("worker_language_type", 2);
			List<Worker> workerList = workerService.selectWorker(parameter);
			if (workerList == null) {
				logger.info(tempUuid + "查询机器人结果集为空,不做处理...");
				return;
			} else {
				logger.info(tempUuid + "机器人结果集 size:" + workerList.size());
			}

			HttpWormClient httpClient = new HttpWormClient().setCharset("UTF-8").setTimeout(1000 * 90);
			HashMap<String, String> header = new HashMap<String, String>();
			Map<String, Object> updateMap = new HashMap<String, Object>();

			for (Map<String, Object> accountMap : accountList) {

				String uuid = "";
				uuid = tempUuid.substring(0, tempUuid.length() - 2);
				uuid = "-" + random.nextInt(9) + random.nextInt(9) + random.nextInt(9) + "] ";

				Integer accountId = Integer.valueOf(accountMap.get("acc_id").toString());
				String username = (String) accountMap.get("acc_username");
				username = username.replaceAll("\\s", "");
				String password = (String) accountMap.get("acc_password");
				password = password.replaceAll("\\s", "");
				Integer verifyNum = (Integer) accountMap.get("verify_num");
				verifyNum = verifyNum + 1;
				logger.info(uuid + "---accountId:" + accountId + ", username:" + username + ", password=" + password);

				try {
					// 更新cp_pass_acc表的状态为00---->11
					updateMap.clear();
					updateMap.put("use_status", 11);
					updateMap.put("use_time", "now()");
					updateMap.put("acc_id", accountId);
					int updateFlag = accountService.updateAccInfo(updateMap);
					logger.info(uuid + "更新状态-11更新结果:" + updateFlag);

					// 从空闲的预订机器人中，随机抽取一个机器人处理请求. 调用java PC 版
					int nextInt = random.nextInt(workerList.size() - 1) + 1;
					Worker worker = workerList.get(nextInt);
					String publicIp = worker.getPublicIp();
					logger.info(uuid + "机器人公网IP:" + publicIp);

					// 发起http请求核验
					String url = "http://" + publicIp + ":18010/robot/passenger/pc/" + username + "/" + password;
					logger.info(uuid + "请求路径:" + url);

					String result = "";
					HttpWormResponse response = httpClient.doGet(url, header);
					result = response.getResponseContent();
					logger.info(uuid + "机器人处理结果:-1" + result);

					// 解析机器人返回结果。
					// result = result.substring(1, result.length() - 1);
					// result = result.replaceAll("\\\\\"", "\"");
					logger.info(uuid + "机器人处理结果:" + result);
					if (!result.startsWith("{")) {
						logger.info(uuid + "返回结果不符合JSON格式，重置状态");
						throw new RuntimeException("返回结果不符合JSON格式，重置状态");
					}

					// 判断执行结果。
					com.alibaba.fastjson.JSONObject resultJson = com.alibaba.fastjson.JSONObject.parseObject(result);
					String jsonStatus = resultJson.getString("status");
					String message = resultJson.getString("message");
					logger.info(uuid + accountId + "返回结果码 [" + jsonStatus + ":" + message + "]");
					if (!jsonStatus.equals("0000")) {
						// 判断状态码
						String messageCode = "";
						boolean isStop = true;
						if (jsonStatus.equals("1101")) {
							// 账号不存在：1101
							messageCode = "6";
						} else if (jsonStatus.equals("1102") || jsonStatus.equals("1103") || jsonStatus.equals("1104")) {
							// 用户名或密码为空：1102, 密码错误：1103, 账号锁定：1104
							messageCode = "7";
						} else if (jsonStatus.equals("1201")) {
							// 身份信息冒用：1201
							messageCode = "4";
						} else if (jsonStatus.equals("1208")) {
							// 账号核验未通过：1208
							messageCode = "11";
						} else if (jsonStatus.equals("1209")) {
							// 手机核验未通过：1209
							messageCode = "8";
						} else {
							isStop = false;
						}

						if (isStop) {
							try {
								logger.info(uuid + "停用账号...");
								Map<String, Object> updateAccountMap = new HashMap<String, Object>();
								updateAccountMap.put("acc_status", "22");
								updateAccountMap.put("username", username);
								updateAccountMap.put("password", password);
								updateAccountMap.put("reason", messageCode);
								updateAccountMap.put("acc_id", accountId);
								accountService.updateAccountInfo(updateAccountMap);

								logger.info(uuid + "删除该账号下所有白名单...");
								accountService.deleteWhiteListByAccount(accountId);

								// 更新状态为33 核验失败
								logger.info(uuid + "更新状态为33 核验失败...");
								updateMap.clear();
								updateMap.put("use_status", 33);
								updateMap.put("use_time", "now()");
								updateMap.put("acc_id", accountId);
								accountService.updateAccInfo(updateMap);
							} catch (Exception e) {
								logger.info(uuid + "停用账号异常", e);
							}
							return;
						} else {
							logger.info(uuid + "非账号封停状态,重置更新状态...");
							throw new RuntimeException("非账号封停状态，重置状态");
						}
					}

					// TODO
					JSONArray jsonArray = resultJson.getJSONArray("body");
					Integer passengerCount = jsonArray.size();
					logger.info(uuid + accountId + "常用联系人数量:" + passengerCount);
					// 更新cp_accountinfo常用联系人数量。
					logger.info(uuid + "更新cp_accountinfo常用联系人数量...");
					Map<String, Object> updateAccountMap = new HashMap<String, Object>();
					updateAccountMap.put("acc_id", accountId);
					updateAccountMap.put("contactsNumber", passengerCount);
					accountService.updateAccountInfo(updateAccountMap);

					ArrayList<String> cardNoList = new ArrayList<>();
					for (int i = 0; i < jsonArray.size(); i++) {
						parameter.clear();
						com.alibaba.fastjson.JSONObject jsonObject = jsonArray.getJSONObject(i);
						String name = jsonObject.getString("name");
						String cardNo = jsonObject.getString("cardNo");
						Date deleteDate = jsonObject.getDate("deleteDate");
						String userSelf = jsonObject.getString("userSelf");
						String cardType = jsonObject.getString("cardType");
						String ticketType = jsonObject.getString("ticketType");

						// 参数转换
						if (cardType.equals("1")) {
							// 二代身份证
							cardType = "2";
						} else if (cardType.equals("G")) {
							// 台湾通行证
							cardType = "4";
						} else if (cardType.equals("B")) {
							// 护照
							cardType = "5";
						} else if (cardType.equals("C")) {
							// 港澳通行证
							cardType = "3";
						}

						if (ticketType.equals("1")) {
							// 成人
							ticketType = "0";
						} else if (ticketType.equals("2")) {
							// 儿童
							ticketType = "1";
						} else if (ticketType.equals("3")) {
							// 学生
							ticketType = "3";
						}

						// 插入或者更新 cp_pass_whitelist 表数据
						parameter.put("accountId", accountId);
						parameter.put("name", name);
						parameter.put("cardNo", cardNo);
						parameter.put("cardType", cardType);
						parameter.put("ticketType", ticketType);
						parameter.put("username", username);
						parameter.put("userSelf", userSelf);
						parameter.put("deleteDate", deleteDate);
						// 更新白名单
						logger.info(uuid + "更新白名单 name:" + name + ", cardNo:" + cardNo);
						accountService.addAccWhiteInfo(parameter);

						cardNoList.add(cardNo);
					}

					logger.info(uuid + "删除多余白名单...");
					Integer delete = accountService.deleteWhiteList(accountId, cardNoList);
					logger.info(uuid + "多余白名单删除结果:" + delete);

					// 将状态置为 22-核验成功
					logger.info(uuid + "更新状态为22 核验成功...");
					updateMap.clear();
					updateMap.put("use_status", 22);
					updateMap.put("use_time", "now()");
					updateMap.put("acc_id", accountId);
					try {
						accountService.updateAccInfo(updateMap);
					} catch (Exception e) {
						logger.info(uuid + "更新状态：成功发生异常", e);
					}
				} catch (Exception e) {
					logger.info(uuid + "发生异常，重置状态", e);
					// 状态 00-待核验 11-核验中 22-核验成功 33-核验失败 44-3次核验均返回空
					updateMap.clear();
					if (verifyNum > 3) {
						updateMap.put("use_status", 44);
						logger.info(uuid + "重置状态:" + 44);
					} else {
						updateMap.put("use_status", 00);
						logger.info(uuid + "重置状态:" + 00);
					}
					updateMap.put("use_time", "now()");
					updateMap.put("acc_id", accountId);
					try {
						int updateFlag = accountService.updateAccInfo(updateMap);
						logger.info(uuid + "状态重置结果:" + updateFlag);
					} catch (Exception e2) {
						logger.info(uuid + "状态重置发生异常", e2);
					}
				}
			}
			logger.info(tempUuid + " end ~~~~~~");
		} catch (Exception e) {
			logger.info(tempUuid + "白名单核验流程异常", e);
		}
	}

}
