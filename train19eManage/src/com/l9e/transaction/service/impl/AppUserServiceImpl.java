package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.AppUserDao;
import com.l9e.transaction.service.AppUserService;
@Service("appUserService")
public class AppUserServiceImpl implements AppUserService {
	@Resource
	private AppUserDao appUserDao;

	@Override
	public List<Map<String, Object>> queryAppUserList(Map<String, Object> paramMap) {
		return appUserDao.queryAppUserList(paramMap);
	}

	@Override
	public int queryAppUserListCount(Map<String, Object> paramMap) {
		return appUserDao.queryAppUserListCount(paramMap);
	}

	@Override
	public Integer queryLinkerNum(String userId) {
		return appUserDao.queryLinkerNum(userId);
	}

	@Override
	public Integer queryRefereeAccountNum(String userId) {
		return appUserDao.queryRefereeAccountNum(userId);
	}

	@Override
	public Map<String, String> queryAppUserInfo(String userId) {
		return appUserDao.queryAppUserInfo(userId);
	}

	@Override
	public List<Map<String, Object>> queryAppUserLinkerList(
			Map<String, Object> paramMap) {
		return appUserDao.queryAppUserLinkerList(paramMap);
	}

	@Override
	public int queryAppUserLinkerListCount(String userId) {
		return appUserDao.queryAppUserLinkerListCount(userId);
	}

	@Override
	public void updateAppUser(Map<String, String> paramMap) {
		appUserDao.updateAppUser(paramMap);
	}

	@Override
	public void deleteAppUser(String userId) {
		appUserDao.deleteAppUser(userId);
	}

	@Override
	public void updateAppUserStop(Map<String, String> paramMap) {
		appUserDao.updateAppUserStop(paramMap);
	}
	
}
