package com.l9e.transaction.dao.impl;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.UpdateCheckAutoDao;
@Repository("updateCheckAutoDao")
public class UpdateCheckAutoDaoImpl extends BaseDao implements UpdateCheckAutoDao {

	@Override
	public String querySettingStatus(String string) {
		return (String) this.getSqlMapClientTemplate().queryForObject("updateCheckAuto.querySettingStatus" ,string);
	}

	@Override
	public void updateSettingStatus(String string) {
		 this.getSqlMapClientTemplate().update("updateCheckAuto.updateSettingStatus" ,string);
	}

}
