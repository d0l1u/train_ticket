package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.InnerSystemSettingDao;
import com.l9e.transaction.vo.SystemSettingVo;

@Repository("innerSystemSettingDao")
public class InnerSystemSettingDaoImpl extends BaseDao implements InnerSystemSettingDao{

	@SuppressWarnings("unchecked")
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().delete("innerSystemsetting.deleteURL",
				systemSettingVo);

	}

	@SuppressWarnings("unchecked")
	public List<SystemSettingVo> getSystemSetting() {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList(
				"innerSystemsetting.querySystemSetting");
	}

	@SuppressWarnings("unchecked")
	public void insertInterface12306URL(SystemSettingVo systemSettingVo) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("innerSystemsetting.insertURL",
				systemSettingVo);
	}

	@SuppressWarnings("unchecked")
	public void updateInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("innerSystemsetting.updateURL",
				systemSettingVo);
	}

	@SuppressWarnings("unchecked")
	public void updateInterfaceChannel(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("innerSystemsetting.updateChannel",
				systemSettingVo);

	}

	@SuppressWarnings("unchecked")
	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("innerSystemsetting.updateConTimeout",
				systemSettingVo);

	}

	@SuppressWarnings("unchecked")
	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"innerSystemsetting.updateReadTimeout", systemSettingVo);

	}
	
	public void changeURLStatus(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"innerSystemsetting.changeURLStatus", systemSettingVo);
	}
	
	public SystemSettingVo getSystemSettingById(String setting_id) {
		try {
			return (SystemSettingVo)(this.getSqlMapClient().queryForObject("innerSystemsetting.getSystemSettingById", setting_id));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void updatePayCtrl(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"innerSystemsetting.updatePayCtrl", systemSettingVo);
	}

	public void addSystemLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("innerSystemsetting.addSystemLog", log);
	}
	
	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo){
		this.getSqlMapClientTemplate().update("innerSystemsetting.updateChannels", systemSettingVo);
	}

	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("innerSystemsetting.querySystemSetList",paramMap);
	}

	public int querySystemSetListCount() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("innerSystemsetting.querySystemSetListCount");
	}

	@Override
	public Object querySystemRefundAndAlert(String string) {
		return this.getSqlMapClientTemplate().queryForObject("innerSystemsetting.querySystemRefundAndAlert", string);
	}
}
