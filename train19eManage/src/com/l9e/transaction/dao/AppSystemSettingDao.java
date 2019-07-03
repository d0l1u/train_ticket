package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.SystemSettingVo;

public interface AppSystemSettingDao {

	List<SystemSettingVo> getSystemSetting();

	SystemSettingVo getSystemSettingById(String settingId);

	List<Map<String, Object>> querySystemSetList(Map<String, Object> paramMap);

	int querySystemSetListCount();

	void updateChannel(SystemSettingVo bxChannel);

	void addSystemLog(Map<String, String> log);

	void insertInterface12306URL(SystemSettingVo systemSettingVo);

	void deleteInterface12306URL(SystemSettingVo systemSettingVo);

	void updateInterface12306URL(SystemSettingVo systemSettingVo);

	void changeURLStatus(SystemSettingVo systemSettingVo);
	
}
