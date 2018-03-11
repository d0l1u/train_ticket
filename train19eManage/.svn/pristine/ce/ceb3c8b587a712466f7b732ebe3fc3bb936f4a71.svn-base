package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.QunarSettingDao;
@Repository("qunarSettingDao")
public class QunarSettingDaoImpl extends BaseDao implements QunarSettingDao {

	//查询去哪儿通知设定
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryQunarSetting(){
		return this.getSqlMapClientTemplate().queryForList("qunarsetting.querySetting");
	}
	
	//更新去哪儿通知设定
	public void updateQunarSetting(Map<String, String> log){
		this.getSqlMapClientTemplate().update("qunarsetting.updateSetting", log);
	}


	public void addSystemLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("qunarsetting.addSystemLog", log);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querySystemSetList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("qunarsetting.querySystemSetList",paramMap);
	}

	@Override
	public int querySystemSetListCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("qunarsetting.querySystemSetListCount");
	}
}
