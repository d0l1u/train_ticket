package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.DepartDao;
@Repository("departDao")
public class DepartDaoImpl extends BaseDao implements DepartDao {

	@Override
	public Map<String, Object> queryDepartCodeCount(String department) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("depart.queryDepartCodeCount", department);
	}

	@Override
	public Map<String, Object> queryDepartCodeCountMonth(String department) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("depart.queryDepartCodeCountMonth", department);
	}

	@Override
	public int queryDepartCodeCountToday(String department) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryDepartCodeCountToday",department);
	}

	@Override
	public Map<String, Object> queryDepartCodeCountWeek(String department) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("depart.queryDepartCodeCountWeek", department);
	}

	@Override
	public Map<String, Object> queryDepartCodeCountYesterday(String department) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("depart.queryDepartCodeCountYesterday", department);
	}

	@Override
	public int queryDepartCodeFailToday(String department) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryDepartCodeFailToday",department);
	}

	@Override
	public int queryDepartCodeOvertimeToday(String department) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryDepartCodeOvertimeToday",department);
	}

	@Override
	public int queryDepartCodeToday(String department) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryDepartCodeToday",department);
	}

	@Override
	public int queryDepartCurrentNameCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryDepartCurrentNameCount", paramMap);
	}

	@Override
	public int queryAdminCodeTodayCount(String optRen) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryAdminCodeTodayCount", optRen);
	}

	@Override
	public int queryAdminCodeTodayFail(String optRen) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryAdminCodeTodayFail", optRen);
	}

	@Override
	public int queryAdminCodeTodayOvertime(String optRen) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryAdminCodeTodayOvertime", optRen);
	}

	@Override
	public int queryAdminCodeTodaySuccess(String optRen) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryAdminCodeTodaySuccess", optRen);
	}

	@Override
	public List<Map<String, Object>> queryAdminCurrentName(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("depart.queryAdminCurrentName", paramMap);
	}

	@Override
	public int queryPictureCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryPictureCount", paramMap);
	}

	@Override
	public List<Map<String, String>> queryPictureList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("depart.queryPictureList", paramMap);
	}

	@Override
	public int queryDayPageCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryDayPageCount", paramMap);
	}

	@Override
	public List<Map<String, String>> queryDayPageList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("depart.queryDayPageList", paramMap);
	}

	@Override
	public List<Map<String, String>> queryAdminCodeList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("depart.queryAdminCodeList", paramMap);
	}

	@Override
	public int queryByAdminPageCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryByAdminPageCount", paramMap);
	}

	@Override
	public void addAdmin(Map<String, String> addMap) {
		this.getSqlMapClientTemplate().insert("depart.addAdmin", addMap);
	}

	@Override
	public List<Map<String, Object>> queryDepartAdminUserList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("depart.queryDepartAdminUserList", paramMap);
	}

	@Override
	public int queryDepartAdminUserListPageCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("depart.queryDepartAdminUserListPageCount", paramMap);
	}

	@Override
	public void deleteDepartAdmin(String optRen) {
		this.getSqlMapClientTemplate().delete("depart.deleteDepartAdmin", optRen);
	}

	@Override
	public String queryDepartment(String optRen) {
		return (String)this.getSqlMapClientTemplate().queryForObject("depart.queryDepartment", optRen);
	}
	
}
