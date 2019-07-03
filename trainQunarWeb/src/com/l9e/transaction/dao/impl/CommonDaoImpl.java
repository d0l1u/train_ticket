package com.l9e.transaction.dao.impl;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CommonDao;

@Repository("commonDao")
public class CommonDaoImpl extends BaseDao implements CommonDao {

	@Override
	public String queryQunarSysValue(String setting_name) {
		return (String)this.getSqlMapClientTemplate().queryForObject("common.queryQunarSysValue", setting_name);
	}

}
