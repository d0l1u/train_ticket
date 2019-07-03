package com.l9e.transaction.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.AccountService;
import com.l9e.transaction.service.CommonService;
import com.l9e.transaction.vo.Account;
import com.l9e.transaction.vo.ChannelAlive;
import com.l9e.transaction.vo.Worker;
import com.l9e.util.DateUtil;
import com.l9e.util.HttpUtil;
import com.l9e.util.MemcachedUtil;
import com.l9e.util.UrlFormatUtil;

import net.sf.json.JSONObject;

/**
 * 账号唤醒job
 * 
 * @author zhangjun
 * 
 */
@Component("accountAliveJob")
public class AccountAliveJob {

	private static final Logger logger = Logger
			.getLogger(AccountAliveJob.class);

	@Resource
	private AccountService accountService;

	@Resource
	private CommonService commonService;

	public void live() throws Exception {
		Date date = new Date();
		String datePre = DateUtil.dateToString(date, "yyyyMMdd");
		Date begin = DateUtil
				.stringToDate(datePre + "063000", "yyyyMMddHHmmss");// 7:00
		Date end = DateUtil.stringToDate(datePre + "225000", "yyyyMMddHHmmss");// 23:00
		if (date.before(begin) || date.after(end)) {
			logger.info("6:30--22：50点方可唤醒账号机器人！");
			return;
		}

		int live_num = 0;
		int limit_num = 10;
		long live_time = 0;
		// if(live_time == 0 || live)
		try {
			live_time = Long.parseLong((String) MemcachedUtil.getInstance()
					.getAttribute("live_num"));
			if (new Date().getTime() >= 5 * 60 * 1000) {
				List<ChannelAlive> alives = getChannelAliveNum();
				for (ChannelAlive alive : alives) {
					live_num = live_num
							+ Integer.parseInt(alive.getAlive_num());
				}
				MemcachedUtil.getInstance().setAttribute("live_time",
						new Date().getTime());
				MemcachedUtil.getInstance().setAttribute("live_num", live_num);
			} else {
				live_num = Integer.parseInt((String) MemcachedUtil
						.getInstance().getAttribute("live_num"));
			}
		} catch (Exception e) {
			List<ChannelAlive> alives = getChannelAliveNum();

			for (ChannelAlive alive : alives) {
				live_num = live_num + Integer.parseInt(alive.getAlive_num());
			}
		}
		if (live_num == 0) {
			List<ChannelAlive> alives = getChannelAliveNum();

			for (ChannelAlive alive : alives) {
				live_num = live_num + Integer.parseInt(alive.getAlive_num());
			}
		}
		int alive_num = accountService.queryAliveAccoutsCount(null);
		limit_num = live_num / 60;
		if (alive_num < 100) {
			limit_num = 40;
		}
		limit_num = limit_num < 10 ? 10 : limit_num;

		Map<String, Object> map = new HashMap<String, Object>();
		map.put("limit_time", "true");
		map.put("limit_num", limit_num);
		List<Account> accounts = accountService.queryAliveAccouts(map);
		if (accounts.size() == 0 || live_num < 100) {
			logger.info("已唤醒所有唤醒池中帐号，自动扩张唤醒池");
			map.put("limit_time", null);
			accounts = accountService.queryAliveAccouts(map);
		}

		logger.info("待唤醒帐号数量" + accounts.size());
		for (Account account : accounts) {
			this.requestRobot(account);
		}
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

		Map<String, String> maps = new HashMap<String, String>();
		maps.put("ScriptPath", "keepCookie.lua");
		maps.put("SessionID", String.valueOf(System.currentTimeMillis()));
		maps.put("Sync", "0");// 0异步请求/1同步请求
		maps.put("Timeout", "160000");
		maps.put("ParamCount", "1");
		maps.put("Param1", account.getAccUsername() + "|"
				+ account.getAccPassword() + "|" + randCodeType + "|"
				+ account.getAccId());

		logger.info("maps=" + maps);

		String param = null;
		try {
			param = UrlFormatUtil.CreateUrl("", maps, "", "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		Worker worker = commonService.queryRandomWorker();
		logger.info("current worker is " + worker.getWorkerName()
				+ ",worker_ext is " + worker.getWorkerExt() + "参数为:" + param);
		String reqResult = HttpUtil.sendByPost(worker.getWorkerExt(), param,
				"UTF-8");

		logger.info("reqResult=" + reqResult);

		JSONObject jsobj = JSONObject.fromObject(reqResult);

		if (0 == jsobj.getInt("ErrorCode")) {// 请求成功
			logger.info("发送robot唤醒账号请求完成，" + account.getAccUsername());
		} else {
			logger.info("发送robot唤醒账号请求失败，" + account.getAccUsername()
					+ ", 原因为：" + jsobj.toString());
			String acc_id = account.getAccId();
			Map<String, Object> failMap = new HashMap<String, Object>();
			failMap.put("acc_id", acc_id);
			failMap.put("status", "03");
			accountService.updateAccountActive(failMap);
		}
	}

	/**
	 * 获取各渠道配置唤醒账号数
	 * 
	 * @return
	 */
	private List<ChannelAlive> getChannelAliveNum() {
		List<ChannelAlive> alives = new ArrayList<ChannelAlive>();
		ChannelAlive alive = null;
		// 加载系统设置值
		String alive_setting = commonService
				.querySysSettingByKey("account_alive_setting");
		try {
			if (StringUtils.isNotEmpty(alive_setting)) {
				String[] settings = alive_setting.split(",");
				String[] part = null;
				for (String element : settings) {
					part = element.split(":");
					if (part != null && part.length == 2) {
						alive = new ChannelAlive();
						alive.setChannel(part[0]);
						alive.setAlive_num(part[1]);
						alives.add(alive);
					}
				}
			}
		} catch (Exception e) {
			logger.error("解析各渠道唤醒账号数设置异常", e);
		}
		// 加载失败，则设置默认值
		if (alives == null || alives.size() == 0) {
			alive = new ChannelAlive();
			alive.setChannel("19e");
			alive.setAlive_num("50");
			alives.add(alive);

			alive = new ChannelAlive();
			alive.setChannel("qunar");
			alive.setAlive_num("200");
			alives.add(alive);
		}

		return alives;
	}

	public static void main(String[] args) {
		while (true) {
			long time = new Date().getTime();
			System.out.println(time / (60 * 5 * 1000));
			System.out.println(time % (60 * 5 * 1000));
		}
	}

}
