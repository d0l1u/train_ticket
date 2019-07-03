package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Through_the_auditDao;
@Repository("through_the_auditDao")
public class Through_the_auditDaoImpl extends BaseDao implements Through_the_auditDao{

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryDoes_not_pass_the_examination(
			Map<String, String> queryList) {
		return this.getSqlMapClientTemplate().queryForList("through_the_audit.queryDoes_not_pass_the_examination", queryList);
	}

	public void updateWaitPassToPass(Map<String,String>update_Map) {
		this.getSqlMapClientTemplate().update("through_the_audit.updateWaitPassToPass", update_Map);
	}
	
}
