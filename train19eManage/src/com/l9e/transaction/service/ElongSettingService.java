package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

public interface ElongSettingService {
	
	// 查询艺龙通知设定
	public List<Map<String, String>> querySetting();

	// 更新艺龙通知设定
	public void updateQunarSetting(Map<String, String> log);

	//根据settingId查询属性
	public Map<String, String> querySettingById(String settingId);
	//日志
	public void addSystemLog(Map<String, String> log);
	public int querySystemSetListCount();
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap);
}
