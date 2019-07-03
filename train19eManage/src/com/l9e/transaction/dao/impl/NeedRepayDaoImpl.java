package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.NeedRepayDao;

@Repository("needRepayDao")
public class NeedRepayDaoImpl extends BaseDao implements NeedRepayDao{
	@SuppressWarnings("unchecked")
	public List<String> queryNeedRepay() {
		return this.getSqlMapClientTemplate().queryForList("repay.queryNeedRepay");
	}
	@SuppressWarnings("unchecked")
	
	public void updateNeedRepay(String user_id) {
		this.getSqlMapClientTemplate().update("repay.updateNeedRepay",user_id);
		
	}

}
