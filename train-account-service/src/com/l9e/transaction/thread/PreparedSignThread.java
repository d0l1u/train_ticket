package com.l9e.transaction.thread;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.HttpUtil;
import com.l9e.util.UrlFormatUtil;

/**
 * 预登录任务
 * 
 * @author licheng
 * 
 */
public class PreparedSignThread implements Runnable {

	private static final Logger logger = Logger.getLogger(PreparedSignThread.class);

	/**
	 * 预登录账号
	 */
	private Account account;
	/**
	 * 预登录机器人
	 */
	private Worker worker;
	/**
	 * 打码方式
	 */
	private String randCodeType = "0";

	public PreparedSignThread(Account account, Worker worker, String randCodeType) {
		super();
		this.account = account;
		this.worker = worker;
		this.randCodeType = randCodeType;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public Worker getWorker() {
		return worker;
	}

	public void setWorker(Worker worker) {
		this.worker = worker;
	}

	public String getRandCodeType() {
		return randCodeType;
	}

	public void setRandCodeType(String randCodeType) {
		this.randCodeType = randCodeType;
	}

	@Override
	public void run() {
		if (worker == null) {
			logger.info("预登录任务终止，没有注入机器人");
			return;
		}

		if (account == null) {
			logger.info("预登录终止，没有注入12306账号");
			return;
		}

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("ScriptPath", "keepCookie.lua");// 执行的脚本路径
		paramMap.put("SessionID", String.valueOf(System.currentTimeMillis()));// 任务完成后，机器人回应该请求时携带
		paramMap.put("Timeout", "240000");// 超时时间
		paramMap.put("Sync", "0");// 0异步请求/1同步请求
		paramMap.put("ParamCount", "1");
		StringBuilder param1 = new StringBuilder();
		param1.append(account.getUsername()).append("|").append(account.getPassword()).append("|").append(randCodeType);
		paramMap.put("Param1", param1.toString());

		/* pc预登录 */
		String params = UrlFormatUtil.createUrl("", paramMap);
		logger.info("预登录机器人请求参数pc,params : " + params);
		long start = System.currentTimeMillis();
		String result = HttpUtil.sendByPost(worker.getWorkerExt(), params, "utf-8");
		long end = System.currentTimeMillis();
		logger.info("pc预登录请求耗时" + (end - start) + "ms,result : " + result);

		/* app预登录 */
		// paramMap.put("ScriptPath", "appKeepCookie.lua");// 执行的脚本路径
		// params = UrlFormatUtil.createUrl("", paramMap);
		// logger.info("预登录机器人请求参数app,params : " + params);
		// start = System.currentTimeMillis();
		// result = HttpUtil.sendByPost(worker.getWorkerExt(), params, "utf-8");
		// end = System.currentTimeMillis();
		// logger.info("app预登录请求耗时" + (end - start) + "ms,result : " + result);
	}

}
