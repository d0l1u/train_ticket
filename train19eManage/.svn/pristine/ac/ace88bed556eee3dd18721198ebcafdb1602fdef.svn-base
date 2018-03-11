package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.UserStatDao;
import com.l9e.transaction.vo.AreaVo;

@Repository("userStatDao")
public class UserStatDaoImpl extends BaseDao implements UserStatDao{

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryUserStatList(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("userStat.queryUserStatList",query_Map);
	}

	public int queryOrderStatListCount(Map<String, Object> query_Map) {
		return this.getTotalRows("userStat.queryOrderStatListCount", query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryUserStatPictureLine() {
		return this.getSqlMapClientTemplate().queryForList("userStat.queryUserStatPictureLine");
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("userStat.getProvince");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryThisDayUserStat(
			Map<String, String> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("userStat.queryThisDayUserStat",query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryThisDayUserStatActive(Map<String, String> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("userStat.queryThisDayUserStatActive",query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAllPrivateUserBar(String date) {
		return this.getSqlMapClientTemplate().queryForList("userStat.queryAllPrivateUserBar",date);
	}

	public int queryPreProvinceCount(Map<String, String> queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("userStat.queryPreProvinceCount",queryMap);
	}

	public int queryThisProvinceCount(Map<String, String> queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("userStat.queryThisProvinceCount",queryMap);
	}

	public int queryAWeekAgoCount(Map<String, String> queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("userStat.queryAWeekAgoCount",queryMap);
	}
	
}
