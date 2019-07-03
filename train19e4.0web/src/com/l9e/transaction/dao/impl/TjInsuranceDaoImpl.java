package com.l9e.transaction.dao.impl;

import java.util.HashMap;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TjInsuranceDao;
@Repository("tjInsuranceDao")
public class TjInsuranceDaoImpl extends BaseDao implements TjInsuranceDao{

	@Override
	public void clickNumAdd(HashMap<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tjInsurance.clickNumAdd", map);
	}

	@Override
	public int clickNumUpdate(HashMap<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().update("tjInsurance.clickNumUpdate", map);
	}

	@Override
	public int queryclickNum(HashMap<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjInsurance.queryclickNum", map);
	}

}
