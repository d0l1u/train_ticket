package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.LoginDao;
import com.l9e.transaction.service.LoginService;
@Service("loginService")
public class LoginServiceImpl implements LoginService{
	@Resource
	private LoginDao loginDao;

	public Map<String, Object> queryLogin_UserInfo(Map<String, String> query_Map) {
		return loginDao.queryLogin_UserInfo(query_Map);
	}

	public void updateLoginTimeAndIp(Map<String, String> add_Map) {
		loginDao.updateLoginTimeAndIp(add_Map);
	}

	public int queryCodeCountToday(Map<String,Object>paramMap) {
		return loginDao.queryCodeCountToday(paramMap);
	}

	public int queryCodeToday(Map<String,Object>paramMap) {
		return loginDao.queryCodeToday(paramMap);
	}

	@Override
	public int queryCode(int i) {
		return loginDao.queryCode(i);
	}

	@Override
	public Map<String,Object> queryCodeCount(Map<String,Object>paramMap) {
		return loginDao.queryCodeCount(paramMap);
	}

	@Override
	public Map<String,Object> queryCodeCountMonth(Map<String,Object>paramMap) {
		return loginDao.queryCodeCountMonth(paramMap);
	}

	@Override
	public Map<String,Object> queryCodeCountWeek(Map<String,Object>paramMap) {
		return loginDao.queryCodeCountWeek(paramMap);
	}

	@Override
	public int queryCodeMonth(int i) {
		return loginDao.queryCodeMonth(i);
	}

	@Override
	public int queryCodeWeek(int i) {
		return loginDao.queryCodeWeek(i);
	}

	public void addAdmin(Map<String, String> addMap) {
		loginDao.addAdmin(addMap);
	}

	@Override
	public int queryRealname(String realName) {
		return loginDao.queryRealname(realName);
	}

	@Override
	public String queryUsername(String userName) {
		return loginDao.queryUsername(userName);
	}

	@Override
	public List<String> queryDate_List() {
		return loginDao.queryDate_List();
	}

	@Override
	public int queryDayPageCount(Map<String, Object> paramMap) {
		return loginDao.queryDayPageCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryDayPageList(Map<String, Object> paramMap) {
		return loginDao.queryDayPageList(paramMap);
	}

	@Override
	public int queryByAdminPageCount(Map<String, Object> paramMap) {
		return loginDao.queryByAdminPageCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryAdminCodeList(
			Map<String, Object> paramMap) {
		return loginDao.queryAdminCodeList(paramMap);
	}

	@Override
	public int queryAdminCodeTodayCount(String optRen) {
		return loginDao.queryAdminCodeTodayCount(optRen);
	}

	@Override
	public int queryAdminCodeTodayFail(String optRen) {
		return loginDao.queryAdminCodeTodayFail(optRen);
	}

	@Override
	public int queryAdminCodeTodayOvertime(String optRen) {
		return loginDao.queryAdminCodeTodayOvertime(optRen);
	}

	@Override
	public int queryAdminCodeTodaySuccess(String optRen) {
		return loginDao.queryAdminCodeTodaySuccess(optRen);
	}

	@Override
	public Map<String, Object> queryAdminCodeInfoSum(String optRen) {
		return loginDao.queryAdminCodeInfoSum(optRen);
	}

	@Override
	public Map<String, Object> queryAdminCodeMonth(String optRen) {
		return loginDao.queryAdminCodeMonth(optRen);
	}

	@Override
	public Map<String, Object> queryAdminCodeWeek(String optRen) {
		return loginDao.queryAdminCodeWeek(optRen);
	}

	@Override
	public List<Map<String, Object>> queryAdminCurrentName(Map<String,Object>paramMap) {
		return loginDao.queryAdminCurrentName(paramMap);
	}

	@Override
	public int queryAdminCodeCurrentCount(Map<String, Object> paramMap) {
		return loginDao.queryAdminCodeCurrentCount(paramMap);
	}

	@Override
	public int queryAdminCurrentNameCount(Map<String,Object>paramMap) {
		return loginDao.queryAdminCurrentNameCount(paramMap);
	}

	@Override
	public String queryAdminDepartment(String username) {
		return loginDao.queryAdminDepartment(username);
	}

	@Override
	public int queryCodeFailToday(Map<String,Object>paramMap) {
		return loginDao.queryCodeFailToday(paramMap);
	}

	@Override
	public int queryCodeOvertimeToday(Map<String,Object>paramMap) {
		return loginDao.queryCodeOvertimeToday(paramMap);
	}

	@Override
	public String queryOptname(String optRen) {
		return loginDao.queryOptname(optRen);
	}

	@Override
	public Map<String, Object> queryCodeCountYesterday(Map<String,Object>paramMap) {
		return loginDao.queryCodeCountYesterday(paramMap);
	}

	@Override
	public Map<String, Object> queryAdminCodeYesterday(String optRen) {
		return loginDao.queryAdminCodeYesterday(optRen);
	}

	@Override
	public void deleteAdmin(String optRen) {
		loginDao.deleteAdmin(optRen);
	}

	@Override
	public List<Map<String, Object>> queryAdminUserList(
			Map<String, Object> paramMap) {
		return loginDao.queryAdminUserList(paramMap);
	}

	@Override
	public int queryByAdminUserListPageCount(Map<String, Object> paramMap) {
		return loginDao.queryByAdminUserListPageCount(paramMap);
	}

	@Override
	public Integer queryAdminCodeMonthMoney(String username) {
		return loginDao.queryAdminCodeMonthMoney(username);
	}

	@Override
	public int querySuccessPreCount(String optRen) {
		return loginDao.querySuccessPreCount(optRen);
	}

	@Override
	public int queryWaitCodeCount() {
		return loginDao.queryWaitCodeCount();
	}

	@Override
	public int queryCodeRateCount(Map<String, Object> paramMap) {
		return loginDao.queryCodeRateCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryCodeRateList(
			Map<String, Object> paramMap) {
		return loginDao.queryCodeRateList(paramMap);
	}

	@Override
	public Map<String, String> querySystemInfo() {
		return loginDao.querySystemInfo();
	}

	@Override
	public int queryWaitCodeQueenCount(String string) {
		return loginDao.queryWaitCodeQueenCount(string);
	}

	@Override
	public void updateSystemInfo(Map<String, Object> paramMap) {
		loginDao.updateSystemInfo(paramMap);
	}

	@Override
	public int queryAdminCurrentOtherCount(Map<String, Object> paramMap1) {
		return loginDao.queryAdminCurrentOtherCount(paramMap1);
	}





}
