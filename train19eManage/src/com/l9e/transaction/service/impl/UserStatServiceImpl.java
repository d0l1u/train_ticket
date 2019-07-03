package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.UserStatDao;
import com.l9e.transaction.service.UserStatService;
import com.l9e.transaction.vo.AreaVo;

@Service("userStatService")
public class UserStatServiceImpl implements UserStatService{
	@Resource
	private UserStatDao userStatDao;

	public List<Map<String, String>> queryUserStatList(
			Map<String, Object> query_Map) {
		return userStatDao.queryUserStatList(query_Map);
	}

	public int queryOrderStatListCount(Map<String, Object> query_Map) {
		return userStatDao.queryOrderStatListCount(query_Map);
	}

	public List<Map<String, String>> queryUserStatPictureLine() {
		return userStatDao.queryUserStatPictureLine();
	}

	public List<AreaVo> getProvince() {
		return userStatDao.getProvince();
	}

	public List<Map<String, String>> queryThisDayUserStat(
			Map<String, String> query_Map) {
		return userStatDao.queryThisDayUserStat(query_Map);
	}

	public List<Map<String, String>> queryThisDayUserStatActive(
			Map<String, String> query_Map) {
		return userStatDao.queryThisDayUserStatActive(query_Map);
	}

	public List<Map<String, String>> queryAllPrivateUserBar(String date) {
		return userStatDao.queryAllPrivateUserBar(date);
	}

	public int queryPreProvinceCount(Map<String, String> queryMap) {
		return userStatDao.queryPreProvinceCount(queryMap);
	}

	public int queryThisProvinceCount(Map<String, String> queryMap) {
		return userStatDao.queryThisProvinceCount(queryMap);
	}

	public int queryAWeekAgoCount(Map<String, String> queryMap) {
		return userStatDao.queryAWeekAgoCount(queryMap);
	}
}
