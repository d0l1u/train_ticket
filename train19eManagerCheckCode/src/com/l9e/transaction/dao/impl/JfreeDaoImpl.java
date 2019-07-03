package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.JfreeDao;
@Repository("jfreeDao")
public class JfreeDaoImpl extends BaseDao implements JfreeDao {

	@Override
	public List<Map<String, Object>> query15dayUserCode(String optRen) {
		return this.getSqlMapClientTemplate().queryForList("jfree.query15dayUserCode", optRen);
	}

	@Override
	public int query15dayUserCodeCount(String optRen) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("jfree.query15dayUserCodeCount", optRen);
	}

	@Override
	public List<Map<String, String>> query15dayCodePicture() {
		return this.getSqlMapClientTemplate().queryForList("jfree.query15dayCodePicture");
	}

	@Override
	public List<Map<String, Object>> query3dayCodeSuccess(String date2) {
		return this.getSqlMapClientTemplate().queryForList("jfree.query3dayCodeSuccess", date2);
	}

	@Override
	public List<Map<String, Object>> queryTodayCodeSuccess(String today) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryTodayCodeSuccess", today);
	}

	@Override
	public int queryUserCodeSuccessCount(String optRen) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("jfree.queryUserCodeSuccessCount", optRen);
	}

	@Override
	public List<Map<String, Object>> queryUserCodeSuccessList(Map<String,String> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryUserCodeSuccessList", paramMap);
	}

	@Override
	public List<Map<String, String>> query15dayDepartCodePicture(String department) {
		return this.getSqlMapClientTemplate().queryForList("jfree.query15dayDepartCodePicture",department);
	}

	@Override
	public List<Map<String, Object>> queryDepartCodeSuccessPicture(
			Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryDepartCodeSuccessPicture", paramMap);
	}

}
