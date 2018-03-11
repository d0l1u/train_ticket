package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.SystemSettingVo;

public interface SystemSettingService {
	
	/*
	 * 获取系统设置信息
	 */
	public List<SystemSettingVo> getSystemSetting();
	
	public void updateInterfaceChannel(SystemSettingVo systemSettingVo, Map<String, String> log);
	
	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo, Map<String, String> log);
	
	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo, Map<String, String> log);
	
	public void updateInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log);
	
	public void addInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log);
	
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log);
	
	public void updateURLStatus(SystemSettingVo systemSettingVo, Map<String, String> log);
	
	public SystemSettingVo getSystemSettingById(String setting_id);

	public void updatePayCtrl(SystemSettingVo systemSettingVo, Map<String, String> log);

	public void addSystemLog(Map<String, String> log);
	
	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo);

	public int querySystemSetListCount();

	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap);

	public Object querySystemRefundAndAlert(String string);

}
