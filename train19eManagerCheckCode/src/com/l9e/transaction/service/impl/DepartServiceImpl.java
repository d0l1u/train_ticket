package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.DepartDao;
import com.l9e.transaction.dao.LoginDao;
import com.l9e.transaction.service.DepartService;
@Service("departService")
public class DepartServiceImpl implements DepartService {
	@Resource
	private DepartDao departDao;

	@Override
	public Map<String, Object> queryDepartCodeCount(String department) {
		return departDao.queryDepartCodeCount(department);
	}

	@Override
	public Map<String, Object> queryDepartCodeCountMonth(String department) {
		return departDao.queryDepartCodeCountMonth(department);
	}

	@Override
	public int queryDepartCodeCountToday(String department) {
		return departDao.queryDepartCodeCountToday(department);
	}

	@Override
	public Map<String, Object> queryDepartCodeCountWeek(String department) {
		return departDao.queryDepartCodeCountWeek(department);
	}

	@Override
	public Map<String, Object> queryDepartCodeCountYesterday(String department) {
		return departDao.queryDepartCodeCountYesterday(department);
	}

	@Override
	public int queryDepartCodeFailToday(String department) {
		return departDao.queryDepartCodeFailToday(department);
	}

	@Override
	public int queryDepartCodeOvertimeToday(String department) {
		return departDao.queryDepartCodeOvertimeToday(department);
	}

	@Override
	public int queryDepartCodeToday(String department) {
		return departDao.queryDepartCodeToday(department);
	}

	@Override
	public int queryDepartCurrentNameCount(Map<String, Object> paramMap) {
		return departDao.queryDepartCurrentNameCount(paramMap);
	}

	@Override
	public int queryAdminCodeTodayCount(String optRen) {
		return departDao.queryAdminCodeTodayCount(optRen);
	}

	@Override
	public int queryAdminCodeTodayFail(String optRen) {
		return departDao.queryAdminCodeTodayFail(optRen);
	}

	@Override
	public int queryAdminCodeTodayOvertime(String optRen) {
		return departDao.queryAdminCodeTodayOvertime(optRen);
	}

	@Override
	public int queryAdminCodeTodaySuccess(String optRen) {
		return departDao.queryAdminCodeTodaySuccess(optRen);
	}

	@Override
	public List<Map<String, Object>> queryAdminCurrentName(
			Map<String, Object> paramMap) {
		return departDao.queryAdminCurrentName(paramMap);
	}

	@Override
	public int queryPictureCount(Map<String, Object> paramMap) {
		return departDao.queryPictureCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryPictureList(
			Map<String, Object> paramMap) {
		return departDao.queryPictureList(paramMap);
	}

	@Override
	public int queryDayPageCount(Map<String, Object> paramMap) {
		return departDao.queryDayPageCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryDayPageList(
			Map<String, Object> paramMap) {
		return departDao.queryDayPageList(paramMap);
	}

	@Override
	public List<Map<String, String>> queryAdminCodeList(
			Map<String, Object> paramMap) {
		return departDao.queryAdminCodeList(paramMap);
	}

	@Override
	public int queryByAdminPageCount(Map<String, Object> paramMap) {
		return departDao.queryByAdminPageCount(paramMap);
	}

	@Override
	public void addAdmin(Map<String, String> addMap) {
		departDao.addAdmin(addMap);
	}

	@Override
	public List<Map<String, Object>> queryDepartAdminUserList(
			Map<String, Object> paramMap) {
		return departDao.queryDepartAdminUserList(paramMap);
	}

	@Override
	public int queryDepartAdminUserListPageCount(Map<String, Object> paramMap) {
		return departDao.queryDepartAdminUserListPageCount(paramMap);
	}

	@Override
	public void deleteDepartAdmin(String optRen) {
		departDao.deleteDepartAdmin(optRen);
	}

	@Override
	public String queryDepartment(String optRen) {
		return departDao.queryDepartment(optRen);
	}
	
	
}
