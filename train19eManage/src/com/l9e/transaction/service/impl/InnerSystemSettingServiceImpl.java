package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.InnerSystemSettingDao;
import com.l9e.transaction.service.InnerSystemSettingService;
import com.l9e.transaction.vo.SystemSettingVo;
@Service("innerSystemSettingService")
public class InnerSystemSettingServiceImpl implements InnerSystemSettingService {
	@Resource
	private InnerSystemSettingDao innerSystemSettingDao;
	
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		innerSystemSettingDao.deleteInterface12306URL(systemSettingVo);
		innerSystemSettingDao.addSystemLog(log);

	}

	public List<SystemSettingVo> getSystemSetting() {
		// TODO Auto-generated method stub
		return innerSystemSettingDao.getSystemSetting();
	}

	public void addInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		// TODO Auto-generated method stub
		innerSystemSettingDao.insertInterface12306URL(systemSettingVo);
		innerSystemSettingDao.addSystemLog(log);
	}

	public void updateInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		// TODO Auto-generated method stub
		innerSystemSettingDao.updateInterface12306URL(systemSettingVo);
		innerSystemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceChannel(SystemSettingVo systemSettingVo, Map<String, String> log) {
		innerSystemSettingDao.updateInterfaceChannel(systemSettingVo);
		innerSystemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo, Map<String, String> log) {
		innerSystemSettingDao.updateInterfaceConTimeout(systemSettingVo);
		innerSystemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo, Map<String, String> log) {
		innerSystemSettingDao.updateInterfaceReadTimeout(systemSettingVo);
		innerSystemSettingDao.addSystemLog(log);
	}
	
	public void updateURLStatus(SystemSettingVo systemSettingVo, Map<String, String> log){
		innerSystemSettingDao.changeURLStatus(systemSettingVo);
		innerSystemSettingDao.addSystemLog(log);
	}
	
	public SystemSettingVo getSystemSettingById(String setting_id) {
		return innerSystemSettingDao.getSystemSettingById(setting_id);
	}

	public void updatePayCtrl(SystemSettingVo systemSettingVo, Map<String, String> log) {
		innerSystemSettingDao.updatePayCtrl(systemSettingVo);
		innerSystemSettingDao.addSystemLog(log);
	}

	public void addSystemLog(Map<String, String> log) {
		innerSystemSettingDao.addSystemLog(log);
	}

	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo){
		innerSystemSettingDao.updateChannel(systemSettingVo);
	}


	@Override
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return innerSystemSettingDao.querySystemSetList(paramMap);
	}

	@Override
	public int querySystemSetListCount() {
		return innerSystemSettingDao.querySystemSetListCount();
	}

	@Override
	public Object querySystemRefundAndAlert(String string) {
		return innerSystemSettingDao.querySystemRefundAndAlert(string);
	}
}
