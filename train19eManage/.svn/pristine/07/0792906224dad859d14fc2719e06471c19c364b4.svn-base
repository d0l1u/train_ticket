package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.RobotSetNewDao;
@Repository("robotSetNewDao")
public class RobotSetNewDaoImpl extends BaseDao implements RobotSetNewDao{

	@Override
	public int queryRobotSetCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("robotSetNew.queryRobotSetCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> queryRobotSetList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("robotSetNew.queryRobotSetList",paramMap);
	}

	@Override
	public int changeStatus(Map<String, String> worker) {
		return (Integer)this.getSqlMapClientTemplate().update("robotSetNew.changeStatus",worker);
	}

	@Override
	public int changeAlipayAccount(Map<String, String> worker) {
		return (Integer)this.getSqlMapClientTemplate().update("robotSetNew.changeAlipayAccount",worker);
	}
	
	@Override
	public int changeAlipayAccountType(Map<String, String> worker) {
		return (Integer)this.getSqlMapClientTemplate().update("robotSetNew.changeAlipayAccountType",worker);
	}
	
	@Override
	public void insertLog(Map<String, String> log) {
		this.getSqlMapClientTemplate().insert("robotSetNew.insertLog",log);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryWorkerLog(String workerId) {
		return this.getSqlMapClientTemplate().queryForList("robotSetNew.queryWorkerLog",workerId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryRobotSetInfo(String workerId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("robotSetNew.queryRobotSetInfo",workerId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryReportLog(String workerId) {
		return this.getSqlMapClientTemplate().queryForList("robotSetNew.queryReportLog",workerId);
	}

	@Override
	public void deleteByWorkId(Map<String, String> worker) {
		this.getSqlMapClientTemplate().delete("robotSetNew.deleteByWorkId",worker);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryZhanghaoList() {
		return  this.getSqlMapClientTemplate().queryForList("robotSetNew.queryZhanghaoList");
	}

	@Override
	public void addVerificationCode(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().insert("robotSetNew.addVerificationCode",paramMap);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryVerificationCode(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("robotSetNew.queryVerificationCode",paramMap);
	}
}
