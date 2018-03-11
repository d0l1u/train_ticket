package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.SystemSettingDao;
import com.l9e.transaction.vo.SystemSettingVo;

@Repository("systemSettingDao")
public class SystemSettingDaoImpl extends BaseDao implements SystemSettingDao {

	@SuppressWarnings("unchecked")
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().delete("systemsetting.deleteURL",
				systemSettingVo);

	}

	@SuppressWarnings("unchecked")
	public List<SystemSettingVo> getSystemSetting() {
		// TODO Auto-generated method stub
		return this.getSqlMapClientTemplate().queryForList(
				"systemsetting.querySystemSetting");
	}

	@SuppressWarnings("unchecked")
	public void insertInterface12306URL(SystemSettingVo systemSettingVo) {
		// TODO Auto-generated method stub
		this.getSqlMapClientTemplate().insert("systemsetting.insertURL",
				systemSettingVo);
	}

	@SuppressWarnings("unchecked")
	public void updateInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("systemsetting.updateURL",
				systemSettingVo);
	}

	@SuppressWarnings("unchecked")
	public void updateInterfaceChannel(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("systemsetting.updateChannel",
				systemSettingVo);

	}

	@SuppressWarnings("unchecked")
	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("systemsetting.updateConTimeout",
				systemSettingVo);

	}

	@SuppressWarnings("unchecked")
	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"systemsetting.updateReadTimeout", systemSettingVo);

	}
	
	public void changeURLStatus(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"systemsetting.changeURLStatus", systemSettingVo);
	}
	
	public SystemSettingVo getSystemSettingById(String setting_id) {
		try {
			return (SystemSettingVo)(this.getSqlMapClient().queryForObject("systemsetting.getSystemSettingById", setting_id));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public void updatePayCtrl(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"systemsetting.updatePayCtrl", systemSettingVo);
	}

	public void addSystemLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("systemsetting.addSystemLog", log);
	}
	
	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo){
		this.getSqlMapClientTemplate().update("systemsetting.updateChannels", systemSettingVo);
	}

	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("systemsetting.querySystemSetList",paramMap);
	}

	public int querySystemSetListCount() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("systemsetting.querySystemSetListCount");
	}

	@Override
	public Object querySystemRefundAndAlert(String string) {
		return this.getSqlMapClientTemplate().queryForObject("systemsetting.querySystemRefundAndAlert", string);
	}
}
