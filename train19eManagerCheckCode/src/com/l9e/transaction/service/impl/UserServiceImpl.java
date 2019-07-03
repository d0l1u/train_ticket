package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.JfreeDao;
import com.l9e.transaction.dao.UserDao;
import com.l9e.transaction.service.UserService;
@Service("userService")
public class UserServiceImpl implements UserService {
	@Resource
	private UserDao userDao;

	@Override
	public Map<String, String> queryUserInfo(String optRen) {
		return userDao.queryUserInfo(optRen);
	}

	@Override
	public void updateUser(Map<String, String> map) {
		userDao.updateUser(map);
	}

	@Override
	public int queryDayUserCount(Map<String, Object> paramMap) {
		return userDao.queryDayUserCount(paramMap);
	}

	@Override
	public List<Map<String, String>> queryDayUserList(
			Map<String, Object> paramMap) {
		return userDao.queryDayUserList(paramMap);
	}

	@Override
	public void updateLogined(Map<String, String> map) {
		userDao.updateLogined(map);
	}
	
	
}
