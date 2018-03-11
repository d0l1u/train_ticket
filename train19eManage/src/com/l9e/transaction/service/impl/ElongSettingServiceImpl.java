package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ElongSettingDao;
import com.l9e.transaction.service.ElongSettingService;

@Service("elongSettingService")
public class ElongSettingServiceImpl implements ElongSettingService {

	@Resource
	private ElongSettingDao elongSettingDao;
	
	//查询去哪儿通知设定
	public List<Map<String, String>> querySetting(){
		return elongSettingDao.querySetting();
	}
	
	//更新去哪儿通知设定
	public void updateQunarSetting(Map<String, String> log){
		elongSettingDao.updateQunarSetting(log);
	}

	@Override
	public Map<String, String> querySettingById(String settingId) {
		return elongSettingDao.querySettingById(settingId);
	}
	
	public void addSystemLog(Map<String, String> log) {
		elongSettingDao.addSystemLog(log);
	}

	@Override
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return elongSettingDao.querySystemSetList(paramMap);
	}

	@Override
	public int querySystemSetListCount() {
		return elongSettingDao.querySystemSetListCount();
	}
}
