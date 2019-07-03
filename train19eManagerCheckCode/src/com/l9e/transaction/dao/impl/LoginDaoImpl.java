package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.LoginDao;
@Repository("loginDao")
public class LoginDaoImpl extends BaseDao implements LoginDao{

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryLogin_UserInfo(Map<String, String> query_Map) {
		return (Map<String,Object>)this.getSqlMapClientTemplate().queryForObject("login.queryLogin_UserInfo",query_Map);
	}

	public void updateLoginTimeAndIp(Map<String, String> add_Map) {
		this.getSqlMapClientTemplate().update("login.updateLoginTimeAndIp",add_Map);
	}

	public int queryCodeCountToday(Map<String,Object>paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("login.queryCodeCountToday",paramMap);
	}

	public int queryCodeToday(Map<String,Object>paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("login.queryCodeToday",paramMap);
	}

	public int queryCode(int i) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("login.queryCode",i);
	}

	@Override
	public Map<String,Object> queryCodeCount(Map<String,Object>paramMap) {
		return (Map<String,Object>) this.getSqlMapClientTemplate().queryForObject("login.queryCodeCount",paramMap);
	}

	@Override
	public Map<String,Object> queryCodeCountMonth(Map<String,Object>paramMap) {
		return (Map<String,Object>) this.getSqlMapClientTemplate().queryForObject("login.queryCodeCountMonth",paramMap);
	}

	@Override
	public Map<String,Object> queryCodeCountWeek(Map<String,Object>paramMap) {
		return (Map<String,Object>) this.getSqlMapClientTemplate().queryForObject("login.queryCodeCountWeek",paramMap);
	}

	@Override
	public int queryCodeMonth(int i) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("login.queryCodeMonth",i);
	}

	@Override
	public int queryCodeWeek(int i) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("login.queryCodeWeek",i);
	}

	@Override
	public void addAdmin(Map<String, String> addMap) {
		this.getSqlMapClientTemplate().insert("login.addAdmin", addMap);
	}

	@Override
	public int queryRealname(String realName) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("login.queryRealname", realName);
	}

	@Override
	public String queryUsername(String userName) {
		return (String) this.getSqlMapClientTemplate().queryForObject("login.queryUsername", userName);
	}

	@Override
	public List<String> queryDate_List() {
		return this.getSqlMapClientTemplate().queryForList("login.queryDate_List");
	}

	@Override
	public int queryDayPageCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("login.queryDayPageCount", paramMap);
	}

	@Override
	public List<Map<String, String>> queryDayPageList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("login.queryDayPageList",paramMap);
	}

	@Override
	public int queryByAdminPageCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryByAdminPageCount", paramMap);
	}

	@Override
	public List<Map<String, String>> queryAdminCodeList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("login.queryAdminCodeList", paramMap);
	}


	@Override
	public int queryAdminCodeTodayCount(String optRen) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryAdminCodeTodayCount", optRen);
	}

	@Override
	public int queryAdminCodeTodayFail(String optRen) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryAdminCodeTodayFail", optRen);
	}

	@Override
	public int queryAdminCodeTodayOvertime(String optRen) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryAdminCodeTodayOvertime", optRen);
	}

	@Override
	public int queryAdminCodeTodaySuccess(String optRen) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryAdminCodeTodaySuccess", optRen);
	}

	@Override
	public Map<String, Object> queryAdminCodeInfoSum(String optRen) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("login.queryAdminCodeInfoSum",optRen);
	}

	@Override
	public Map<String, Object> queryAdminCodeMonth(String optRen) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("login.queryAdminCodeMonth",optRen);
	}

	@Override
	public Map<String, Object> queryAdminCodeWeek(String optRen) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("login.queryAdminCodeWeek",optRen);
	}

	@Override
	public List<Map<String, Object>> queryAdminCurrentName(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("login.queryAdminCurrentName",paramMap);
	}

	@Override
	public int queryAdminCodeCurrentCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryAdminCodeCurrentCount", paramMap);
	}

	@Override
	public int queryAdminCurrentNameCount(Map<String,Object>paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryAdminCurrentNameCount",paramMap);
	}

	@Override
	public String queryAdminDepartment(String username) {
		return (String) this.getSqlMapClientTemplate().queryForObject("login.queryAdminDepartment", username);
	}

	@Override
	public int queryCodeFailToday(Map<String,Object>paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryCodeFailToday",paramMap);
	}

	@Override
	public int queryCodeOvertimeToday(Map<String,Object>paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryCodeOvertimeToday",paramMap);
	}

	@Override
	public String queryOptname(String optRen) {
		return (String) this.getSqlMapClientTemplate().queryForObject("login.queryOptname", optRen);
	}

	@Override
	public Map<String, Object> queryCodeCountYesterday(Map<String,Object>paramMap) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("login.queryCodeCountYesterday",paramMap);
	}

	@Override
	public Map<String, Object> queryAdminCodeYesterday(String optRen) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("login.queryAdminCodeYesterday", optRen);
	}

	@Override
	public void deleteAdmin(String optRen) {
		this.getSqlMapClientTemplate().delete("login.deleteAdmin", optRen);
	}

	@Override
	public List<Map<String, Object>> queryAdminUserList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("login.queryAdminUserList", paramMap);
	}

	@Override
	public int queryByAdminUserListPageCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryByAdminUserListPageCount", paramMap);
	}

	@Override
	public Integer queryAdminCodeMonthMoney(String username) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryAdminCodeMonthMoney", username);
	}

	@Override
	public int querySuccessPreCount(String optRen) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.querySuccessPreCount", optRen);
	}

	@Override
	public int queryWaitCodeCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryWaitCodeCount");
	}

	@Override
	public int queryCodeRateCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("login.queryCodeRateCount", paramMap);
	}

	@Override
	public List<Map<String, String>> queryCodeRateList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("login.queryCodeRateList", paramMap);
	}

	@Override
	public Map<String, String> querySystemInfo() {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("login.querySystemInfo");
	}

	@Override
	public int queryWaitCodeQueenCount(String string) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("login.queryWaitCodeQueenCount", string);
	}

	@Override
	public void updateSystemInfo(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("login.updateSystemInfo", paramMap);
	}

	@Override
	public int queryAdminCurrentOtherCount(Map<String, Object> paramMap1) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("login.queryAdminCurrentOtherCount", paramMap1);
	}




}
