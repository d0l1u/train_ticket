package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AppSystemSettingDao;
import com.l9e.transaction.dao.SystemSettingDao;
import com.l9e.transaction.service.AppSystemSettingService;
import com.l9e.transaction.vo.SystemSettingVo;
@Service("appSystemSettingService")
public class AppSystemSettingServiceImpl implements AppSystemSettingService {
	@Resource
	private AppSystemSettingDao appSystemSettingDao;
	@Override
	public List<SystemSettingVo> getSystemSetting() {
		return appSystemSettingDao.getSystemSetting();
	}

	@Override
	public SystemSettingVo getSystemSettingById(String settingId) {
		return appSystemSettingDao.getSystemSettingById(settingId);
	}

	@Override
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return appSystemSettingDao.querySystemSetList(paramMap);
	}

	@Override
	public int querySystemSetListCount() {
		return appSystemSettingDao.querySystemSetListCount();
	}

	@Override
	public void updateChannel(SystemSettingVo bxChannel) {
		appSystemSettingDao.updateChannel(bxChannel);
	}

	@Override
	public void addSystemLog(Map<String, String> log) {
		appSystemSettingDao.addSystemLog(log);
	}

	@Override
	public void addInterface12306URL(SystemSettingVo systemSettingVo,
			Map<String, String> log) {
		appSystemSettingDao.insertInterface12306URL(systemSettingVo);
		appSystemSettingDao.addSystemLog(log);
	}

	@Override
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo,
			Map<String, String> log) {
		appSystemSettingDao.deleteInterface12306URL(systemSettingVo);
		appSystemSettingDao.addSystemLog(log);
	}

	@Override
	public void updateInterface12306URL(SystemSettingVo systemSettingVo,
			Map<String, String> log) {
		appSystemSettingDao.updateInterface12306URL(systemSettingVo);
		appSystemSettingDao.addSystemLog(log);
	}

	@Override
	public void updateURLStatus(SystemSettingVo systemSettingVo,
			Map<String, String> log) {
		appSystemSettingDao.changeURLStatus(systemSettingVo);
		appSystemSettingDao.addSystemLog(log);
	}

}
