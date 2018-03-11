package com.l9e.transaction.service;

import java.util.List;
import java.util.Map;

import com.l9e.transaction.vo.AreaVo;

public interface UserStatService {

	List<Map<String, String>> queryUserStatList(Map<String, Object> query_Map);

	int queryOrderStatListCount(Map<String, Object> query_Map);

	List<Map<String, String>> queryUserStatPictureLine();

	/**
	 * 获取省份
	 * @return
	 */
	public List<AreaVo> getProvince();

	List<Map<String, String>> queryThisDayUserStat(Map<String, String> query_Map);

	List<Map<String, String>> queryThisDayUserStatActive(
			Map<String, String> query_Map);

	List<Map<String, String>> queryAllPrivateUserBar(String date);

	int queryThisProvinceCount(Map<String, String> queryMap);

	int queryPreProvinceCount(Map<String, String> queryMap);

	int queryAWeekAgoCount(Map<String, String> queryMap);

}
