package com.l9e.transaction.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.l9e.transaction.dao.AccountDao;
import com.l9e.transaction.dao.RedisDao;
import com.l9e.transaction.dao.SystemDao;
import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.thread.AccountThreadPool;
import com.l9e.transaction.thread.PreparedSignThread;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.ConfigUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.JacksonUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.StrUtil;
import com.l9e.util.UrlFormatUtil;

@Service("accountService")
public class AccountServiceImpl implements AccountService {

	private static final Logger logger = Logger.getLogger(AccountServiceImpl.class);

	@Resource
	private RedisDao redisDao;

	@Resource
	private AccountDao accountDao;

	@Resource
	private SystemDao systemDao;

	@Override
	public Account getAccount(String channel, Integer passengerSize) {
		logger.info("开始获取账号，channel：" + channel + " 订单中联系人个数passengerSize为:" + passengerSize);
		String key = StrUtil.getAccountQueue(channel, passengerSize);
		Long lLen = redisDao.LLEN(key);
		logger.info("key:" + key + "; length:" + lLen);
		Account account = null;
		try {
			if (lLen > 0) {
				Object obj = redisDao.RPOP(key);
				if (obj != null) {
					Integer accountId = (Integer) obj;
					account = queryAccountById(accountId);
				}
			}
		} catch (Exception e) {
			logger.info("【RPOP Exception】", e);
		}
		return account;
	}

	@Override
	public Account queryAccountById(Integer accountId) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", accountId);
		return accountDao.selectOneAccount(params);
	}

	@Override
	public void setAccount(String channel, Integer passengerSize, String uuid, int limit) {
		Integer noOrderDay = 5;
		Integer bookNumber = 3;
		Integer contactNumber = 10; 
		String settingKey = null;

		// 未下单天数
		settingKey = "robot_no_order_day";
		Object noOrderDayValue = redisDao.getCacheVal(settingKey);
		if (noOrderDayValue == null) {
			noOrderDayValue = systemDao.selectSettingValue(settingKey);
			redisDao.setCacheVal(settingKey, noOrderDayValue);
			redisDao.setCacheExpire(settingKey, 5 * 60);
		}
		logger.info(uuid + "noOrderDayValue(未下单天数):" + noOrderDayValue);

		// 下单次数
		settingKey = "robot_app_book_num";
		Object bookNumberValue = redisDao.getCacheVal(settingKey);
		if (bookNumberValue == null) {
			bookNumberValue = systemDao.selectSettingValue(settingKey);
			redisDao.setCacheVal(settingKey, bookNumberValue);
			redisDao.setCacheExpire(settingKey, 5 * 60);
		}
		logger.info(uuid + "bookNumberValue(下单次数):" + bookNumberValue);

		// 联系人数量
		settingKey = "contact_num";
		Object contactNumberValue = redisDao.getCacheVal(settingKey);
		if (contactNumberValue == null) {
			contactNumberValue = systemDao.selectSettingValue(settingKey);
			redisDao.setCacheVal(settingKey, contactNumberValue);
			redisDao.setCacheExpire(settingKey, 60);
		}
		logger.info(uuid + "contactNumberValue(联系人数量):" + contactNumberValue);

		// 预登录开关
		settingKey = "is_not_login";
		Object isNotLoginValue = redisDao.getCacheVal(settingKey);
		if (isNotLoginValue == null) {
			isNotLoginValue = systemDao.selectSettingValue(settingKey);
			redisDao.setCacheVal(settingKey, isNotLoginValue);
			redisDao.setCacheExpire(settingKey, 60);
		}
		logger.info(uuid + "isNotLoginValue(预登录开关):" + isNotLoginValue);

		try {
			noOrderDay = Integer.valueOf((String) noOrderDayValue);
			bookNumber = Integer.valueOf((String) bookNumberValue);
			contactNumber = Integer.valueOf((String) contactNumberValue);
		} catch (Exception e) {
			logger.info(uuid + "系统参数转换异常", e);
		}
		StringBuilder log = new StringBuilder();
		log.append("noOrderDay : ").append(noOrderDay).append(", bookNumber : ").append(bookNumber)
				.append(", contactNumber : ").append(contactNumber);
		logger.info(uuid + log.toString());

		/* 查询符合条件的账号填充账号队列 */
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("status", Account.STATUS_FREE);
		// params.put("bookNum", bookNumber);
		// params.put("priority", noOrderDay);
		params.put("contact", 15 - passengerSize);
		params.put("channel", channel);
		params.put("limit", limit);// 单次查询记录20条
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.set(Calendar.DAY_OF_MONTH, -2);
		Date optionTime = cal.getTime();
		params.put("option_time", optionTime);

		List<Account> accounts = null;
		/**
		 * passengerSize为0的情况下取联系人个数小于10的12306账号；
		 * 其它情况下取联系人个数加上要添加的联系人个数等于15的12306账号
		 */
		
		logger.info(uuid + "查询参数:" + JSONObject.toJSONString(params));
		if (0 == passengerSize.intValue()) {
			logger.info(uuid + "selectLess10Accounts...");
			accounts = accountDao.selectLess10Accounts(params);
		} else {
			logger.info(uuid + "selectAccounts-" + passengerSize + "...");
			accounts = accountDao.selectAccounts(params);
		}
		try {
			if (accounts != null && accounts.size() > 0) {
				logger.info(uuid + "queue-" + passengerSize + "-查询结果:" + accounts.size());
				String queueKey = StrUtil.getAccountQueue(channel, passengerSize);
				logger.info(uuid + "queueKey：" + queueKey);
				for (Account account : accounts) {
					account.setStatus(Account.STATUS_QUEUE);
					int rows = accountDao.updateAccount(account);
					logger.info(uuid + "更新结果:" + rows);
					if (rows > 0) {
						Long lpush = redisDao.LPUSH(queueKey, account.getId());
						logger.info(uuid + (15 - passengerSize) + "-队列结果:" + lpush);

						// if (StringUtils.equals("1", (String)
						// isNotLoginValue)) {
						// Integer workerId = preparedSign(account);
						// if (workerId != null) {
						// logger.info(startWith + "账号预登录,account : " + account
						// + ", workerId : " + workerId);
						// redisDao.setCacheVal(account.getUsername(),
						// workerId);
						// redisDao.setCacheExpire(account.getUsername(), 25 *
						// 60);
						// releaseBookWorker(workerId);
						// }
						// }
						logger.info(uuid + "账号进入 " + (15 - passengerSize) + " 队列 account:[" + account.getId()
								+ ", " + account.getUsername() + ", " + account.getPassword() + "]");
					}
				}
			} else {
				logger.info(uuid + "查询结果为空...");
			}
		} catch (Exception e) {
			logger.info(uuid + "【系统异常】", e);
		}

	}

	@Override
	public void updateAccount(Account account) {
		accountDao.updateAccount(account);
	}

	@Override
	public Account stopAccount(Integer accountId, String reason) {
		if (accountId == null || StringUtils.isEmpty(reason))
			return null;
		Account stopAccount = null;

		stopAccount = queryAccountById(accountId);
		if (stopAccount != null) {
			stopAccount.setStatus(Account.STATUS_STOP);
			stopAccount.setStopReason(reason);
			stopAccount.setStopTime("stopTime");

			logger.info("停用账号 account : " + stopAccount);
			int rows = accountDao.updateAccount(stopAccount);
			if (rows == 0)
				stopAccount = null;
		}

		return stopAccount;
	}

	@Override
	@Deprecated
	public Integer preparedSign(Account account) {

		try {
			/* 打码方式 */
			String settingKey = "rand_code_type";
			Object randCodeTypeValue = MemcachedUtil.getInstance().getAttribute(settingKey);
			if (randCodeTypeValue == null) {
				randCodeTypeValue = systemDao.selectSettingValue(settingKey);
				MemcachedUtil.getInstance().setAttribute(settingKey, randCodeTypeValue, 60 * 1000);
			}

			/* 获取一个预定机器人 */
			Worker worker = getBookWorker();
			if (worker == null) {
				logger.info("账号预登录，没有获取到可用预定机器人，预登录失败,accountName : " + account.getUsername());
				return null;
			}

			logger.info("获取到的预登录机器人worker : " + worker);
			logger.info("预登录账号account : " + account.getUsername());
			Runnable task = new PreparedSignThread(account, worker, (String) randCodeTypeValue);
			AccountThreadPool.submitPreparedSignTask(task);
			return worker.getWorkerId();
		} catch (Exception e) {
			logger.info("预登录异常, e : " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 从机器人服务获得预订机器人
	 * 
	 * @return
	 */
	private Worker getBookWorker() {
		Worker worker = null;
		try {

			int count = 0;
			do {
				String resultJson = HttpUtil.sendByPost(ConfigUtil.getConfig("getWorker"), "type=1", "utf-8");
				logger.info("获取机器人返回结果 result : " + resultJson);
				if (resultJson != null && !"".equals(resultJson)) {
					if (JacksonUtil.getValue(resultJson, "success", Boolean.class)) {
						worker = (Worker) JacksonUtil.getValue(resultJson, "data", Worker.class);
						break;
					}
				}

				count++;
			} while (count < 5);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return worker;
	}

	/**
	 * 白名单更改账号表信息
	 * 
	 * @param id:账号ID
	 *            realCheckStatus：账号核验状态 realReceive：手机核验状态
	 *            realContactNum：真实联系人个数 status:账号状态 contact：常用联系人个数
	 */
	@Override
	public void updateAccountInfo(Integer id, Byte realCheckStatus, String realReceive, Integer realContactNum,
			String status, Integer contact) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("id", id);
		params.put("realCheckStatus", realCheckStatus);
		params.put("realReceive", realReceive);
		params.put("realContactNum", realContactNum);
		params.put("status", status);
		params.put("contact", contact);
		accountDao.updateAccountInfo(params);

	}

	/**
	 * 匹配历史适用账号 换成白名单表 实现多人匹配，找出匹配度最高的那个账号 add by wangsf
	 * 
	 * @param passportNus
	 *            乘客证件号码List集合
	 * @return
	 */
	@Override
	public Account filterAccount(List<String> passportNus, String channel) {
		// TODO Auto-generated method stub
		if (passportNus.size() == 0) {
			return null;
		}

		// 匹配历史账户信息(换成cp_pass_whitelist白名单表）
		// Integer accountId = accountDao.selectFilterAccount(passportNo);
		// Integer accountId = accountDao.selectFilterAccountList(passportNo);
		Account account = null;
		Account accountInfo = null;
		Integer accountId = null;// 账号ID
		Integer paramNum = passportNus.size();// 传入参数的个数
		logger.info("入参传入的身份证号总个数 : " + paramNum);
		Integer machingNum = null;// 匹配到的总次数
		Integer maxNum = 15;// 一个账号所能添加的最大联系人个数
		Integer realContactNum = null;// 一个账号下面的真实联系人个数
		Integer compareNum = null;// 入参个数与匹配个数的差值
		List<Account> accIdNuList = new ArrayList<Account>();
		// 先取得各个账号ID匹配传入身份证号的成功总个数的list集合（ID--匹配到的个数）
		accIdNuList = accountDao.selectFilterAccountList(passportNus);

		// cp_match表插入记录
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("channel", channel);
		paramMap.put("param_num", paramNum);
		paramMap.put("param_cardno", passportNus.toString());

		// 遍历accIdNuList,找出最优的那个账号ID
		if (accIdNuList != null) {
			for (int i = 0; i < accIdNuList.size(); i++) {
				accountInfo = accIdNuList.get(i);
				accountId = accountInfo.getId();
				machingNum = accountInfo.getContact();
				logger.info("账号ID匹配到的次数 : " + machingNum);

				paramMap.put("match_num", machingNum);
				if (machingNum > 0) {
					Map<String, Object> certMap = new HashMap<String, Object>();
					certMap.put("passportNus", passportNus);
					certMap.put("accountId", accountId);
					List<String> certnoList = accountDao.queryCertnoList(certMap);
					paramMap.put("match_cardno", certnoList.toString());
				}

				if (paramNum.equals(machingNum)) {
					paramMap.put("match_status", "00");// 匹配状态：00完全匹配
														// 11部分匹配(可添加剩余证件)
														// 22部分匹配(不可添加剩余证件)
														// 33不匹配
					break;
				} else {
					account = queryAccountById(accountId);
					realContactNum = account.getContact();
					logger.info("账号ID下的真实联系人个数 : " + realContactNum);
					compareNum = paramNum - machingNum;
					logger.info("入参与匹配到个数的差值 : " + compareNum);

					// 如果真实联系人个数加上compareNum后大于15，证明这个账号没法再添加联系人，就继续下一次循环匹配。
					if ((compareNum + realContactNum) <= maxNum) {
						logger.info("匹配到的账号ID : " + accountId);
						paramMap.put("match_status", "11");// 匹配状态：00完全匹配
															// 11部分匹配(可添加剩余证件)
															// 22部分匹配(不可添加剩余证件)
															// 33不匹配

						break;
					} else {
						accountId = null;
						account = null;
						paramMap.put("match_status", "22");// 匹配状态：00完全匹配
															// 11部分匹配(可添加剩余证件)
															// 22部分匹配(不可添加剩余证件)
															// 33不匹配
						logger.info("因为要添加的联系人个数与真实联系人个数相加大于15，匹配失败！");
					}
				}

			}
		} else {
			paramMap.put("match_status", "33");// 匹配状态：00完全匹配 11部分匹配(可添加剩余证件)
												// 22部分匹配(不可添加剩余证件) 33不匹配
		}

		paramMap.put("match_account_id", accountId);
		paramMap.put("create_time", "now()");
		accountDao.insertCpMatch(paramMap);
		logger.info("-----------------插入cp_match表一条记录");

		if (accountId != null) {
			account = queryAccountById(accountId);
			if (account != null) {
				if ((Account.STATUS_QUEUE).equals(account.getStatus())) {
					redisDao.LREM(StrUtil.getAccountQueue(account.getChannel()), 0, account.getId());
				} else if ((Account.STATUS_STOP).equals(account.getStatus())
						&& (("1").equals(account.getStopReason()) || ("7").equals(account.getStopReason()))) {
					account = null;
				}
			}
		}
		return account;
	}

	/**
	 * 白名单表匹配到历史账号的ID和次数
	 * 
	 * @param passportNus
	 *            乘客证件号码List集合
	 * @return add by wangsf
	 */
	@Override
	public Account queryAccountMappingNum(List<String> passportNus) {
		// TODO Auto-generated method stub
		if (passportNus.size() == 0) {
			return null;
		}
		Account account = null;
		Account accountInfo = null;
		Integer accountId = null;// 账号ID
		Integer paramNum = passportNus.size();// 传入参数的个数
		logger.info("入参传入的身份证号总个数 : " + paramNum);
		Integer machingNum = null;// 匹配到的总次数
		Integer maxNum = 15;// 一个账号所能添加的最大联系人个数
		Integer realContactNum = null;// 一个账号下面的真实联系人个数
		Integer compareNum = null;// 入参个数与匹配个数的差值
		List<Account> accIdNuList = new ArrayList<Account>();
		// 先取得各个账号ID匹配传入身份证号的成功总个数的list集合（ID--匹配到的个数）
		accIdNuList = accountDao.selectFilterAccountList(passportNus);

		// 遍历accIdNuList,找出最优的那个账号ID
		if (accIdNuList != null) {
			for (int i = 0; i < accIdNuList.size(); i++) {
				accountInfo = accIdNuList.get(i);
				accountId = accountInfo.getId();
				machingNum = accountInfo.getContact();
				logger.info("账号ID匹配到的次数 : " + machingNum);

				if (paramNum.equals(machingNum)) {
					break;
				} else {
					account = queryAccountById(accountId);
					realContactNum = account.getContact();
					logger.info("账号ID下的真实联系人个数 : " + realContactNum);
					compareNum = paramNum - machingNum;
					logger.info("入参与匹配到个数的差值 : " + compareNum);

					// 如果真实联系人个数加上compareNum后大于15，证明这个账号没法再添加联系人，就继续下一次循环匹配。
					if ((compareNum + realContactNum) <= maxNum) {
						logger.info("匹配到的账号ID : " + accountId);
						break;
					} else {
						accountInfo = null;
						logger.info("因为要添加的联系人个数与真实联系人个数相加大于15，匹配失败！");
					}
				}
			}

		}
		return accountInfo;
	}

	/**
	 * add by wangsf 对自带12306账号订单的错误信息处理
	 * 
	 * @return 错误码
	 */
	@SuppressWarnings("rawtypes")
	@Override
	public String bindAccountErrorCode(String userName, String pass, List<String> passportNus) {

		String errorCode = "1";// 返回的错误码 默认核验成功 1：核验成功
		Integer paramNum = passportNus.size();// 传入参数的个数
		logger.info("自带12306账号的处理，入参传入的身份证号总个数 : " + paramNum);
		Integer machingNum = 0;// 匹配到的总次数
		Integer maxNum = 15;// 一个账号所能添加的最大联系人个数
		Integer realContactNum = 0;// 一个账号下面的真实联系人个数
		Integer compareNum = 0;// 入参个数与匹配个数的差值

		Map<String, String> params = new HashMap<String, String>();
		params.put("command", "verifyAccount");
		params.put("channel", "19e");
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("username", userName);
		logger.info("自带12306账号的处理，账号名为 : " + userName);
		jsonObject.put("pass", pass);
		logger.info("自带12306账号的处理，账号密码为 : " + pass);
		JSONArray jsonArray = new JSONArray();
		jsonArray.add(jsonObject);
		params.put("data", jsonArray.toString());
		String uri = UrlFormatUtil.createUrl("", params, "utf-8");

		String result = HttpUtil.sendByPost(ConfigUtil.getProperty("identityURL"), uri, "UTF-8"); // 调用登录验证接口

		logger.info("自带12306账号的处理，账号登录验证的返回结果result为 : " + result);
		// // {"ErrorCode":0,"ErrorInfo":[{"accId":"","robotNum":9,
		// // "retValue":"","retInfo":{“user_status”:”1”}}]}
		// // retValue为success时，帐号登陆成功，retInfo为帐号状态，如下：
		// // retValue为failure时，账号登陆失败，retInfo为原因
		// {"ErrorCode":7,"ErrorInfo":"脚本执行异常。"}
		JSONObject strObject = JSON.parseObject(result);
		String flag = strObject.getString("ErrorInfo").replace("\\", "").replace("[\"{", "[{").replace("}\"]", "}]");
		logger.info("自带12306账号的处理，对返回结果中的ErrorInfo进行整理后为 : " + flag);
		if (flag.startsWith("脚本")) {
			return "0";
		} else {
			JSONArray array = JSON.parseArray(flag);
			for (int i = 0; i < array.size(); i++) {
				Map maps = (Map) array.get(i);
				String results = maps.get("retValue").toString();
				String retInfo = maps.get("retInfo").toString();
				logger.info("自带12306账号的处理，返回结果中的retValue为 : " + results);
				logger.info("自带12306账号的处理，返回结果中的retInfo为 : " + retInfo);

				if (null != retInfo && "" != retInfo && null != results && "" != results) {
					if ("failure".equals(results)) {

						if (retInfo.contains("app登录名不存在")) {
							errorCode = "26"; // 26 : 传入12306账号不存在
							return errorCode;
						} else if (retInfo.contains("app密码输入错误")) {
							errorCode = "27"; // 27 : 传入12306账号登录密码错误
							return errorCode;
						} else if (retInfo.contains("app您的手机号码尚未进行核验")) {
							errorCode = "21"; // 21 : 传入12306账号未进行手机核验
							return errorCode;
						} else {
							errorCode = "0"; // 其它异常
						}

						logger.info("自带12306账号的处理，登录验证失败时errorCode为 : " + errorCode);

					} else if ("success".equals(results)) {

						JSONObject retInfoObject = JSON.parseObject(retInfo);
						Map retMap = (Map) retInfoObject;
						logger.info("自带12306账号的处理，登录验证成功时retInfoMap对象为 : " + retMap);
						String isRecive = retMap.get("is_receive").toString();
						logger.info("自带12306账号的处理，登录验证成功时手机核验状态为 : " + isRecive);
						String userStatus = retMap.get("user_status").toString();
						logger.info("自带12306账号的处理，登录验证成功时账号核验状态为 : " + userStatus);

						if (null != isRecive && "" != isRecive) {
							if ("X".equalsIgnoreCase(isRecive)) {
								errorCode = "21"; // 21 : 传入12306账号未进行手机核验
								return errorCode;
							}
						}

						if (null != userStatus && "" != userStatus) {
							if ("2".equals(userStatus) || "6".equals(userStatus)) {
								errorCode = "25"; // 25 : 传入12306账号待核验
								return errorCode;
							}
						}

						String cardId = ""; // 身份证号
						String contacts = maps.get("contacts").toString();// 返回结果中的联系人列表
						logger.info("自带12306账号的处理，登录验证成功时contacts为 : " + contacts);

						if (null != contacts && "" != contacts) {
							JSONArray contactsArray = JSON.parseArray(contacts);
							for (int j = 0; j < contactsArray.size(); j++) {
								Map contactMap = (Map) contactsArray.get(j);
								logger.info("自带12306账号的处理，登录验证成功时联系人的Map对象为 : " + contactMap);
								cardId = contactMap.get("identy").toString();
								logger.info("自带12306账号的处理，联系人的身份证号为 : " + cardId);
								if (null != cardId && "" != cardId) {
									if (passportNus.contains(cardId)) {
										machingNum++;
									}
								}
							}
							logger.info("自带12306账号的处理，传入的身份证号匹配到的总个数为 : " + machingNum);
							realContactNum = contactsArray.size();
							logger.info("自带12306账号的处理，传入的账号下面真实联系人个数为 : " + realContactNum);
							compareNum = paramNum - machingNum;
							logger.info("自带12306账号的处理，匹配过后还需要添加的联系人个数为 : " + compareNum);

							if ((realContactNum + compareNum) > maxNum) {
								errorCode = "22"; // 22 : 传入12306账号 用户达上限
								return errorCode;
							}
						}

						logger.info("自带12306账号的处理，登录验证成功时errorCode为  : " + errorCode);
					}

				}

			}
		}
		logger.info("自带12306账号的处理，最终返回的错误码为 : " + errorCode);
		return errorCode;

	}

	@Override
	public void updateAccountPwd(String userName, String passWord) {
		// TODO Auto-generated method stub
		// 先通过账号名和channel查询一个账号并得到账号ID
		Account account = null;
		Integer accId = null;

		Map<String, Object> queryMap = new HashMap<String, Object>();
		queryMap.put("userName", userName);
		queryMap.put("channel", "12306");
		account = accountDao.selectAccountByName(queryMap);
		logger.info("根据账号名查询得到的 Account为: " + account);
		if (account != null) {
			accId = account.getId();
			logger.info("根据账号名查询得到的 Account实体中 主键accId为: " + accId + " 账号密码为: " + account.getPassword() + " 传进来的密码参数为: "
					+ passWord);
		}

		// 根据账号ID更新账号密码
		if (null != accId && 0 != accId) {
			Map<String, Object> updateMap = new HashMap<String, Object>();
			updateMap.put("id", accId);
			updateMap.put("userPassWord", passWord);
			accountDao.updateAccountPwd(updateMap);
		}

	}

	@Override
	public int reset2Free() {
		return accountDao.reset2Free();
	}

}
