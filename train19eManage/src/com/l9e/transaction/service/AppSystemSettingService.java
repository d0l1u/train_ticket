package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.SystemSettingVo;

public interface AppSystemSettingService {

	public List<SystemSettingVo> getSystemSetting() ;

	SystemSettingVo getSystemSettingById(String settingId);

	void addSystemLog(Map<String, String> log);

	void updateChannel(SystemSettingVo bxChannel);

	int querySystemSetListCount();

	List<Map<String, Object>> querySystemSetList(Map<String, Object> paramMap);

	public void addInterface12306URL(SystemSettingVo systemSettingVo,
			Map<String, String> log);

	public void updateInterface12306URL(SystemSettingVo systemSettingVo,
			Map<String, String> log);

	public void updateURLStatus(SystemSettingVo systemSettingVo,
			Map<String, String> log);

	public void deleteInterface12306URL(SystemSettingVo systemSettingVo,
			Map<String, String> log);
	

}
