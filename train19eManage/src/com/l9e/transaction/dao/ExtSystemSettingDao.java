package com.l9e.transaction.dao;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.SystemSettingVo;

public interface ExtSystemSettingDao {
	
	/*
	 * 获取系统设置信息
	 */
	public List<SystemSettingVo> getSystemSetting();
	
	public void updateInterfaceChannel(SystemSettingVo systemSettingVo);
	
	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo);
	
	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo);
	
	public void updateInterface12306URL(SystemSettingVo systemSettingVo);
	
	public void insertInterface12306URL(SystemSettingVo systemSettingVo);
	
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo);
	
	public void changeURLStatus(SystemSettingVo systemSettingVo);
	
	public SystemSettingVo getSystemSettingById(String setting_id);

	public void updatePayCtrl(SystemSettingVo systemSettingVo);

	public void addSystemLog(Map<String, String> log);
	
	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo);

	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap);

	public int querySystemSetListCount();
	
	public void updateNoticeSetting(SystemSettingVo systemSettingVo);
}
