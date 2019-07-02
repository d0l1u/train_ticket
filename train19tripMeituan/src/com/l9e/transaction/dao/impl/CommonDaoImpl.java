package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.vo.Station;

@Repository("commonDao")
public class CommonDaoImpl extends BaseDao implements CommonDao {

	@SuppressWarnings("unchecked")
	public List<String> querySysSettingValue(Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"common.querySysSettingValue", paramMap);
	}

	public String querySysSettingByKey(String key) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"common.querySysSettingByKey", key);
	}

	@Override
	public String queryMeituanSysValueByName(String settingName) {
		return (String) this.getSqlMapClientTemplate().queryForObject(
				"common.queryMeituanSysValueByName", settingName);
	}

	@Override
	public Station selectOneStation(Map<String, Object> queryParam) {
		return (Station) this.getSqlMapClientTemplate().queryForObject(
				"common.selectStation", queryParam);
	}
}
