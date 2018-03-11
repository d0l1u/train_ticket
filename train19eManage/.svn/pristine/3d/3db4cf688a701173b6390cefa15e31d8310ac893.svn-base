package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.SystemSettingDao;
import com.l9e.transaction.service.SystemSettingService;
import com.l9e.transaction.vo.SystemSettingVo;

@Service("systemSettingService")
public class SystemSettingServiceImpl implements SystemSettingService {
	
	@Resource
	private SystemSettingDao systemSettingDao;

	public void deleteInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		systemSettingDao.deleteInterface12306URL(systemSettingVo);
		systemSettingDao.addSystemLog(log);

	}

	public List<SystemSettingVo> getSystemSetting() {
		// TODO Auto-generated method stub
		return systemSettingDao.getSystemSetting();
	}

	public void addInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		// TODO Auto-generated method stub
		systemSettingDao.insertInterface12306URL(systemSettingVo);
		systemSettingDao.addSystemLog(log);
	}

	public void updateInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		// TODO Auto-generated method stub
		systemSettingDao.updateInterface12306URL(systemSettingVo);
		systemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceChannel(SystemSettingVo systemSettingVo, Map<String, String> log) {
		systemSettingDao.updateInterfaceChannel(systemSettingVo);
		systemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo, Map<String, String> log) {
		systemSettingDao.updateInterfaceConTimeout(systemSettingVo);
		systemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo, Map<String, String> log) {
		systemSettingDao.updateInterfaceReadTimeout(systemSettingVo);
		systemSettingDao.addSystemLog(log);
	}
	
	public void updateURLStatus(SystemSettingVo systemSettingVo, Map<String, String> log){
		systemSettingDao.changeURLStatus(systemSettingVo);
		systemSettingDao.addSystemLog(log);
	}
	
	public SystemSettingVo getSystemSettingById(String setting_id) {
		return systemSettingDao.getSystemSettingById(setting_id);
	}

	public void updatePayCtrl(SystemSettingVo systemSettingVo, Map<String, String> log) {
		systemSettingDao.updatePayCtrl(systemSettingVo);
		systemSettingDao.addSystemLog(log);
	}

	public void addSystemLog(Map<String, String> log) {
		systemSettingDao.addSystemLog(log);
	}

	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo){
		systemSettingDao.updateChannel(systemSettingVo);
	}


	@Override
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return systemSettingDao.querySystemSetList(paramMap);
	}

	@Override
	public int querySystemSetListCount() {
		return systemSettingDao.querySystemSetListCount();
	}

	@Override
	public Object querySystemRefundAndAlert(String string) {
		return systemSettingDao.querySystemRefundAndAlert(string);
	}
}
