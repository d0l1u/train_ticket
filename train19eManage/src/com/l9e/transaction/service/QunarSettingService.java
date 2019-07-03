package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface QunarSettingService {
	// 查询去哪儿通知设定
	public List<Map<String, String>> queryQunarSetting();

	// 更新去哪儿通知设定
	public void updateQunarSetting(Map<String, String> log);
	//日志
	public void addSystemLog(Map<String, String> log);
	public int querySystemSetListCount();
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap);
}
