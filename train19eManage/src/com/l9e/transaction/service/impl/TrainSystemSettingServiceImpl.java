package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.TrainSystemSettingDao;
import com.l9e.transaction.service.TrainSystemSettingService;
import com.l9e.transaction.vo.SystemSettingVo;
@Service("trainSystemSettingService")
public class TrainSystemSettingServiceImpl implements TrainSystemSettingService {
	@Resource
	private TrainSystemSettingDao trainSystemSettingDao;
	
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		trainSystemSettingDao.deleteInterface12306URL(systemSettingVo);
		trainSystemSettingDao.addSystemLog(log);

	}

	public List<SystemSettingVo> getSystemSetting() {
		// TODO Auto-generated method stub
		return trainSystemSettingDao.getSystemSetting();
	}

	public void addInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		// TODO Auto-generated method stub
		trainSystemSettingDao.insertInterface12306URL(systemSettingVo);
		trainSystemSettingDao.addSystemLog(log);
	}

	public void updateInterface12306URL(SystemSettingVo systemSettingVo, Map<String, String> log) {
		// TODO Auto-generated method stub
		trainSystemSettingDao.updateInterface12306URL(systemSettingVo);
		trainSystemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceChannel(SystemSettingVo systemSettingVo, Map<String, String> log) {
		trainSystemSettingDao.updateInterfaceChannel(systemSettingVo);
		trainSystemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo, Map<String, String> log) {
		trainSystemSettingDao.updateInterfaceConTimeout(systemSettingVo);
		trainSystemSettingDao.addSystemLog(log);
	}

	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo, Map<String, String> log) {
		trainSystemSettingDao.updateInterfaceReadTimeout(systemSettingVo);
		trainSystemSettingDao.addSystemLog(log);
	}
	
	public void updateURLStatus(SystemSettingVo systemSettingVo, Map<String, String> log){
		trainSystemSettingDao.changeURLStatus(systemSettingVo);
		trainSystemSettingDao.addSystemLog(log);
	}
	
	public SystemSettingVo getSystemSettingById(String setting_id) {
		return trainSystemSettingDao.getSystemSettingById(setting_id);
	}

	public void updatePayCtrl(SystemSettingVo systemSettingVo, Map<String, String> log) {
		trainSystemSettingDao.updatePayCtrl(systemSettingVo);
		trainSystemSettingDao.addSystemLog(log);
	}

	public void addSystemLog(Map<String, String> log) {
		trainSystemSettingDao.addSystemLog(log);
	}

	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo){
		trainSystemSettingDao.updateChannel(systemSettingVo);
	}
	
	//切换渠道
	public void updateChannelRh(SystemSettingVo systemSettingVo){
		trainSystemSettingDao.updateChannelRh(systemSettingVo);
	}


	@Override
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return trainSystemSettingDao.querySystemSetList(paramMap);
	}

	@Override
	public int querySystemSetListCount() {
		return trainSystemSettingDao.querySystemSetListCount();
	}

	@Override
	public Object querySystemRefundAndAlert(String string) {
		return trainSystemSettingDao.querySystemRefundAndAlert(string);
	}

	@Override
	public void addtrain_return_optlog(Map<String, Object> paramMap) {
		trainSystemSettingDao.addtrain_return_optlog(paramMap);
		
	}

	@Override
	public void deletetrain_return_optlog(Map<String, Object> paramMap) {
		trainSystemSettingDao.deletetrain_return_optlog(paramMap);
		
	}

	@Override
	public List<Map<String,Object>> querytrain_return_optlogList(Map<String, Object> paramMap) {
		return trainSystemSettingDao.querytrain_return_optlogList(paramMap);
	}
	@Override
	public List<Map<String,Object>> querytrain_return_optlog() {
		return trainSystemSettingDao.querytrain_return_optlog();
	}

	@Override
	public int querytrain_return_optlogCount(Map<String, Object> paramMap) {
		return trainSystemSettingDao.querytrain_return_optlogCount(paramMap);
	}

	
	@Override
	public void updatetrain_return_optlog(Map<String, Object> paramMap) {
		trainSystemSettingDao.updatetrain_return_optlog(paramMap);
	}

	@Override
	public String queryreturn_optlogById(String return_id) {
		return trainSystemSettingDao.queryreturn_optlogById(return_id);
	}

	@Override
	public void addSetting(SystemSettingVo systemSettingVo,
			Map<String, String> log) {
		trainSystemSettingDao.addSetting(systemSettingVo);
		trainSystemSettingDao.addSystemLog(log);		
	}

	@Override
	public Map<String, String> queryCodeInfo() {
		return trainSystemSettingDao.queryCodeInfo();
	}

	@Override
	public void updateCodeInfo(Map<String, Object> paramMap,
			Map<String, String> log) {
		trainSystemSettingDao.updateCodeInfo(paramMap);
		trainSystemSettingDao.addSystemLog(log);		
	}
}
