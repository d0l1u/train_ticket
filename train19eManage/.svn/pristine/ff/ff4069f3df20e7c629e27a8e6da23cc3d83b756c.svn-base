package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_UserInfoDao;
@Repository("tj_UserInfoDao")
public class Tj_UserInfoDaoImpl extends BaseDao implements Tj_UserInfoDao{

	public int queryTable_count() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjUserInfo.queryTable_count");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAllArea_nameAndArea_no() {
		return this.getSqlMapClientTemplate().queryForList("tjUserInfo.queryAllArea_nameAndArea_no");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryAll_date() {
		return this.getSqlMapClientTemplate().queryForList("tjUserInfo.queryAll_date");
	}

	public int queryUser_total(Map<String, String> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjUserInfo.queryUser_total",query_Map);
	}

	public int queryActiveUserNow(Map<String, String> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjUserInfo.queryActiveUserNow",query_Map);
	}

	public int queryUser_total_Add(Map<String, String> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjUserInfo.queryUser_total_Add",query_Map);
	}

	public int queryActiveUserAll(Map<String, String> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjUserInfo.queryActiveUserAll",query_Map);
	}

	public int queryActiveUser_Add(Map<String, String> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjUserInfo.queryActiveUser_Add",query_Map);
	}

	public void addTj_User(Map<String, Object> add_Map) {
		this.getSqlMapClientTemplate().insert("tjUserInfo.addTj_User",add_Map);
	}

}
