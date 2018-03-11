package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.QunarSettingDao;
import com.l9e.transaction.service.QunarSettingService;
@Service("qunarSettingService")
public class QunarSettingServiceImpl implements QunarSettingService {

	@Resource
	private QunarSettingDao qunarSettingDao;
	
	//查询去哪儿通知设定
	public List<Map<String, String>> queryQunarSetting(){
		return qunarSettingDao.queryQunarSetting();
	}
	
	//更新去哪儿通知设定
	public void updateQunarSetting(Map<String, String> log){
		qunarSettingDao.updateQunarSetting(log);
	}

	@Override
	public void addSystemLog(Map<String, String> log) {
		qunarSettingDao.addSystemLog(log);
		
	}

	@Override
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return qunarSettingDao.querySystemSetList(paramMap);
	}

	@Override
	public int querySystemSetListCount() {
		return qunarSettingDao.querySystemSetListCount();
	}
}
