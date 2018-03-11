package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.ExtSystemSettingDao;
import com.l9e.transaction.service.ExtSystemSettingService;
import com.l9e.transaction.vo.SystemSettingVo;
@Service("extSystemSettingService")
public class ExtSystemSettingServiceImpl implements ExtSystemSettingService {
	
	@Resource
	private ExtSystemSettingDao extSystemSettingDao;

	public void deleteInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		extSystemSettingDao.deleteInterface12306URL(systemSettingVo);
		extSystemSettingDao.addSystemLog(log);

	}

	public List<SystemSettingVo> getSystemSetting() {
		return extSystemSettingDao.getSystemSetting();
	}

	public void addInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		extSystemSettingDao.insertInterface12306URL(systemSettingVo);
		extSystemSettingDao.addSystemLog(log);
	}

	public void updateInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		extSystemSettingDao.updateInterface12306URL(systemSettingVo);
		extSystemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceChannel(SystemSettingVo systemSettingVo, Map<String, String> log) {
		extSystemSettingDao.updateInterfaceChannel(systemSettingVo);
		extSystemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo, Map<String, String> log) {
		extSystemSettingDao.updateInterfaceConTimeout(systemSettingVo);
		extSystemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo, Map<String, String> log) {
		extSystemSettingDao.updateInterfaceReadTimeout(systemSettingVo);
		extSystemSettingDao.addSystemLog(log);
	}
	
	public void updateURLStatus(SystemSettingVo systemSettingVo, Map<String, String> log){
		extSystemSettingDao.changeURLStatus(systemSettingVo);
		extSystemSettingDao.addSystemLog(log);
	}
	
	public SystemSettingVo getSystemSettingById(String setting_id) {
		return extSystemSettingDao.getSystemSettingById(setting_id);
	}

	public void updatePayCtrl(SystemSettingVo systemSettingVo, Map<String, String> log) {
		extSystemSettingDao.updatePayCtrl(systemSettingVo);
		extSystemSettingDao.addSystemLog(log);
	}

	public void addSystemLog(Map<String, String> log) {
		extSystemSettingDao.addSystemLog(log);
	}

	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo){
		extSystemSettingDao.updateChannel(systemSettingVo);
	}


	@Override
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return extSystemSettingDao.querySystemSetList(paramMap);
	}

	@Override
	public int querySystemSetListCount() {
		return extSystemSettingDao.querySystemSetListCount();
	}

	@Override
	public void updateNoticeSetting(SystemSettingVo systemSettingVo) {
		extSystemSettingDao.updateNoticeSetting(systemSettingVo);
	}

}
