package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.UserStatToUserinfoDao;
import com.l9e.transaction.service.UserStatToUserinfoService;
@Service
public class UserStatToUserinfoServiceImpl implements UserStatToUserinfoService{
	@Resource
	private UserStatToUserinfoDao userStatToUserinfoDao ;
	public List<Map<String, String>> queryStatList() {
		
		return userStatToUserinfoDao.queryStatList();
	}
	public String queryUser(String queryUser_id) {
		return userStatToUserinfoDao.queryUser(queryUser_id);
	}
	public void addUserStat(Map<String, String> map) {
		userStatToUserinfoDao.addUserStat(map);
	}
	public void updateUserStat(Map<String, String> map) {
		userStatToUserinfoDao.updateUserStat(map);
	}

}
