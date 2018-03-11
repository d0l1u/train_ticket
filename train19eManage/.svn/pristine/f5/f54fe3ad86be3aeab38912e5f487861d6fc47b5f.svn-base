package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ElongSettingDao;

@Repository("elongSettingDao")
public class ElongSettingDaoImpl extends BaseDao implements ElongSettingDao {

	//查询艺龙通知设定
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> querySetting(){
		return this.getSqlMapClientTemplate().queryForList("elongSetting.querySetting");
	}
	
	//更新艺龙通知设定
	public void updateQunarSetting(Map<String, String> log){
		this.getSqlMapClientTemplate().update("elongSetting.updateSetting", log);
	}

	public Map<String, String> querySettingById(String settingId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("elongSetting.querySettingById", settingId);
	}

	public void addSystemLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("elongSetting.addSystemLog", log);
	}
	
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("elongSetting.querySystemSetList",paramMap);
	}

	public int querySystemSetListCount() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("elongSetting.querySystemSetListCount");
	}
}
