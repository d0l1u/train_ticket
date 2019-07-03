package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.PictureDao;
@Repository("pictureDao")
public class PictureDaoImpl extends BaseDao implements PictureDao {

	public int queryPictureCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("picture.queryPictureCount", paramMap);
	}

	public List<Map<String, String>> queryPictureList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("picture.queryPictureList", paramMap);
	}

	@Override
	public int queryCodeHourCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("picture.queryCodeHourCount", paramMap);
	}

	@Override
	public List<Map<String, String>> queryCodeHourList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("picture.queryCodeHourList", paramMap);
	}

	@Override
	public int queryUserHourCount(Map<String, Object> paramMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("picture.queryUserHourCount", paramMap);
	}

	@Override
	public List<Map<String, String>> queryUserHourList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("picture.queryUserHourList", paramMap);
	}

	@Override
	public List<Map<String, String>> queryCodeHourEveryDayList(
			String paramMap) {
		return this.getSqlMapClientTemplate().queryForList("picture.queryCodeHourEveryDayList", paramMap);
	}

	@Override
	public List<Map<String, String>> queryUserHourEveryDayList(String string) {
		return this.getSqlMapClientTemplate().queryForList("picture.queryUserHourEveryDayList", string);
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

	@Override
	public List<Map<String, String>> queryFailCodeHourEveryDayList(String string) {
		return this.getSqlMapClientTemplate().queryForList("picture.queryFailCodeHourEveryDayList", string);
	}

	@Override
	public List<Map<String, String>> queryFailCodeHourList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("picture.queryFailCodeHourList", paramMap);
	}

	@Override
	public List<Map<String, String>> querySuccessCodeHourEveryDayList(
			String string) {
		return this.getSqlMapClientTemplate().queryForList("picture.querySuccessCodeHourEveryDayList", string);
	}

	@Override
	public List<Map<String, String>> querySuccessCodeHourList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("picture.querySuccessCodeHourList", paramMap);
	}

}
