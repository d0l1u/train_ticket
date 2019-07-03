package com.l9e.transaction.dao.impl;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.SystemDao;

@Repository("systemDao")
public class SystemDaoImpl extends BaseDao implements SystemDao {

	@Override
	public String selectSettingValue(String settingName) {
		return (String) getSqlMapClientTemplate().queryForObject("system.selectSettingValue", settingName);
	}

}
