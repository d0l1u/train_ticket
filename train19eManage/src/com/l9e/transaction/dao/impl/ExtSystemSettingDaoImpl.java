package com.l9e.transaction.dao.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ExtSystemSettingDao;
import com.l9e.transaction.vo.SystemSettingVo;
@Repository("extSystemSettingDao")
public class ExtSystemSettingDaoImpl extends BaseDao implements	ExtSystemSettingDao {

	@SuppressWarnings("unchecked")
	public void deleteInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().delete("extSystemsetting.deleteURL",
				systemSettingVo);

	}

	@SuppressWarnings("unchecked")
	public List<SystemSettingVo> getSystemSetting() {
		return this.getSqlMapClientTemplate().queryForList(
				"extSystemsetting.querySystemSetting");
	}

	@SuppressWarnings("unchecked")
	public void insertInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().insert("extSystemsetting.insertURL",
				systemSettingVo);
	}

	@SuppressWarnings("unchecked")
	public void updateInterface12306URL(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("extSystemsetting.updateURL",
				systemSettingVo);
	}

	@SuppressWarnings("unchecked")
	public void updateInterfaceChannel(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("extSystemsetting.updateChannel",
				systemSettingVo);

	}

	@SuppressWarnings("unchecked")
	public void updateInterfaceConTimeout(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update("extSystemsetting.updateConTimeout",
				systemSettingVo);

	}

	@SuppressWarnings("unchecked")
	public void updateInterfaceReadTimeout(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"extSystemsetting.updateReadTimeout", systemSettingVo);

	}
	
	public void changeURLStatus(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"extSystemsetting.changeURLStatus", systemSettingVo);
	}
	
	public SystemSettingVo getSystemSettingById(String setting_id) {
		try {
			return (SystemSettingVo)(this.getSqlMapClient().queryForObject("extSystemsetting.getSystemSettingById", setting_id));
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void updatePayCtrl(SystemSettingVo systemSettingVo) {
		this.getSqlMapClientTemplate().update(
				"extSystemsetting.updatePayCtrl", systemSettingVo);
	}

	public void addSystemLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("extSystemsetting.addSystemLog", log);
	}
	
	//切换渠道
	public void updateChannel(SystemSettingVo systemSettingVo){
		this.getSqlMapClientTemplate().update("extSystemsetting.updateChannels", systemSettingVo);
	}

	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("extSystemsetting.querySystemSetList",paramMap);
	}

	public int querySystemSetListCount() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("extSystemsetting.querySystemSetListCount");
	}

	@Override
	public void updateNoticeSetting(SystemSettingVo systemSettingVo){
		this.getSqlMapClientTemplate().update("extSystemsetting.updateNoticeSetting", systemSettingVo);
	}
}
