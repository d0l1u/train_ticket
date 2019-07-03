package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.Account;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

@Component("modifyPasswordJob")
@Deprecated
public class ModifyPasswordJob {
	private static final Logger logger = Logger.getLogger(ModifyPasswordJob.class);

	@Resource
	private AccountService accountService;

	@Resource
	private CommonService commonService;

	public void modifyPassword() {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		// paramMap.put("modify_password_time", new Date());
		paramMap.put("modify_status", "00");
		// 查询等待更改的账号
		List<Account> accountList = accountService.queryAccount(paramMap);
		paramMap.put("modify_status", "01");
		// 查询准备更改的账号
		List<Account> queryedList = accountService.queryAccount(paramMap);
		// 查询正在更改的账号
		paramMap.put("modify_status", "02");

		List<Account> modifyingList = accountService.queryAccount(paramMap);

		if (accountList.size() <= 0 && modifyingList.size() <= 0
				&& queryedList.size() <= 0) {
			logger.info("[更改密码task]没有查询到符合条件的账号！");
			return;
		}
		Account account = accountList.get(0);
		if (account != null) {
			requestRobot(account);
		}
		account = queryedList.get(0);
		if (account != null) {
			requestRobot(account);
		}
		account = modifyingList.get(0);
		if (account != null) {
			requestRobot(account);
		}

	}

	// 获取随机字符串
	public String getRandomString(int length) {
		String str = "";
		Random random = new Random();
		for (int i = 0; i < length; i++) {
			boolean b = random.nextBoolean();
			if (b) {
				str += (char) (65 + random.nextInt(26));
			} else {
				str += String.valueOf(random.nextInt(10));
			}
		}
		return str;
	}

	/**
	 * 请求机器人保持账号唤醒
	 * 
	 * @param account
	 */
	private void requestRobot(Account account) {
		// 打码方式：0、手动打码 1、机器识别
		String randCodeType = commonService
				.querySysSettingByKey("rand_code_type");

		String newPassword = getRandomString(8);
		JSONObject accountJson = new JSONObject();
		accountJson.put("acc_id", account.getAccId());
		accountJson.put("acc_username", account.getAccUsername());
		accountJson.put("acc_password", account.getAccPassword());
		accountJson.put("acc_newPassword", newPassword);
		logger.info("[更改密码task]发送的json：：：" + accountJson.toString());

		Map<String, String> maps = new HashMap<String, String>();
		maps.put("ScriptPath", "modifyPass.lua");
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));
		maps.put("Sync", "0");// 0异步请求/1同步请求
		maps.put("Timeout", "160000");
		maps.put("ParamCount", "1");
		maps.put("Param1", accountJson.toString());

		logger.info("maps=" + maps);

		String param = null;
		try {
			param = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
			logger.info("【修改密码task】发送的账户信息为：" + param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * Worker worker = commonService.queryRandomWorker();
		 * logger.info("current worker is " + worker.workerName + ",worker_ext
		 * is " + worker.workerExt + "参数为:" + param);
		 * 
		 * String reqResult = HttpUtil.sendByPost(worker.getWorkerExt(), param,
		 * "UTF-8");
		 */

		String reqResult = HttpUtil.sendByPost("http://10.12.12.151:8091/RunScript", param, "UTF-8");
		logger.info("reqResult=" + reqResult);

		JSONObject jsobj = JSONObject.fromObject(reqResult);

		if (0 == jsobj.getInt("ErrorCode")) {// 请求成功
			logger.info("发送更改账号请求完成，" + account.getAccUsername());
			account.setAccPassword(null);
			account.setModifyStatus("02");
			accountService.updateModifyStatus(account);
		} else {
			logger.info("发送更改账号请求失败，" + account.getAccUsername() + ", 原因为："
					+ jsobj.toString());
			String acc_id = account.getAccId();
			account.setAccPassword(null);
			account.setModifyStatus("00");
			accountService.updateModifyStatus(account);
		}
	}

}
