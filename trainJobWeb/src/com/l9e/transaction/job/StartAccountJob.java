package com.l9e.transaction.job;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.l9e.transaction.service.AccountService;

/**
 * 账号启用job
 * 
 * @author zhangjc
 * 
 */
@Component("startAccountJob")
public class StartAccountJob {
	@Resource
	private AccountService accountService;

	static String status = "start";

	private static final Logger logger = Logger.getLogger(StartAccountJob.class);

	public void StartAccount() {
		logger.info("status:" + status);
		if (status == "start" || status == "finish") {
			this.openAccuntBegin();
		} else {
			logger.info("上次执行未结束，本次不做处理!!!");
		}
	}

	// 开启账号begin
	private void openAccuntBegin() {
		logger.info("#######开启账号Job####begin##############");
		status = "running";
		Map<String, Object> setMap = new HashMap<String, Object>();
		try {
			Map<String, String> map = accountService.querySettingMap();// 获取开启账号条件
																		// train_system_setting
																		// 表中配置（setting_value格式
																		// 用@@分隔开）
			if (map == null || map.isEmpty()) {
				if ("1".equals(map.get("setting_status"))) {
					String setting_value = map.get("setting_value");
					logger.info("setting_value：" + setting_value + StringUtils.isNotEmpty(setting_value));
					if (StringUtils.isNotEmpty(setting_value)) {
						String[] setting = setting_value.split("@@");
						for (String s : setting) {
							if ("limit".equals(s.split("=")[0])) {
								setMap.put("limit", 10);
							} else {
								setMap.put(s.split("=")[0], s.split("=")[1]);
							}
						}
						logger.info("Map：" + setMap.toString());
						if (!"".equals(setMap.get("acc_status")) && !"".equals(setMap.get("opt_person")) && !"".equals(setMap.get("stop_reason"))
								&& !"".equals(setMap.get("option_time")) && !"".equals(setMap.get("limit"))) {
							logger.info("获取开启账号条件Map：" + setMap.toString());
						} else {
							this.getDefaultMap(setMap);// 取不到值则取默认值
							logger.info("获取开启账号条件失败，给予默认值");
							status = "finish";
						}
					} else {
						this.getDefaultMap(setMap);
						logger.info("获取开启账号条件失败，给予默认值");
						status = "finish";
					}
					List<String> accountList = accountService.queryWaitOpenAccountList(setMap);// 获取符合条件的账号acc_id
																								// 主键
					int limitNum = Integer.parseInt(String.valueOf(setMap.get("limit")));
					if (accountList.size() == 0) {// 获取不到时，不做处理
						logger.info("符合开启账号条件的个数为0 ，不做处理");
						status = "finish";
					} else if (accountList.size() <= limitNum) {
						logger.info("符合开启账号条件的个数为" + accountList.size());
						int updateSuccess = 0;
						int updateFailure = 0;
						for (int m = 0; m < accountList.size(); m++) {
							int update_count = accountService.openAccountByAccId(accountList.get(m));// 根据acc_id
																										// 开启账号
							if (update_count > 0) {
								logger.error("开启账号success,acc_id：" + accountList.get(m));
								updateSuccess += 1;
							} else {
								logger.error("开启账号异常fail：" + accountList.get(m));
								updateFailure += 1;
							}
						}
						if (updateSuccess + updateFailure == accountList.size()) {
							status = "finish";
							logger.info("开启账号正常结束，等待下次运行！updateSuccess：" + updateSuccess + ",updateFailure:" + updateFailure);
						}
					}
				} else {
					status = "finish";
					logger.error("开启账号功能未【启用】,不做处理。");
				}
			} else {
				status = "finish";
				logger.error("开启账号功能在系统配置为空，停止运行。");
			}
		} catch (Exception e) {
			logger.error("未知错误：" + e);
			status = "finish";
		}
	}

	// 查询符合开启条件的默认参数
	private Map<String, Object> getDefaultMap(Map<String, Object> setMap) {
		setMap.put("acc_status", "22");
		setMap.put("opt_person", "pay app");
		setMap.put("stop_reason", "5");
		setMap.put("option_time", "2015-03-01");
		setMap.put("limit", 10);
		return setMap;
	}

}
