package com.l9e.transaction.service.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.l9e.transaction.dao.Tj_hc_halfHourDao;
import com.l9e.transaction.service.Tj_hc_halfHourService;

@Service("Tj_hc_halfHourService")
public class Tj_hc_halfHourServiceImpl implements Tj_hc_halfHourService {
	@Resource
	private Tj_hc_halfHourDao tj_hc_halfHourDao;

	public void addToTj_hc_halfHour(Map<String, Object> map) {
		tj_hc_halfHourDao.addToTj_hc_halfHour(map);
	}

	public List<Map<String, String>> queryDayTimeAfter(String createTime) {
		return tj_hc_halfHourDao.queryDayTimeAfter(createTime);
	}

	public List<Map<String, String>> queryDayTimeBefore(String createTime) {
		return tj_hc_halfHourDao.queryDayTimeBefore(createTime);
	}

	public int queryTable_Count() {
		return tj_hc_halfHourDao.queryTable_Count();
	}
	
	public List<String> queryDate_List() {
		return tj_hc_halfHourDao.queryDate_List();
	}

	public int queryCount(Map map) {
		return tj_hc_halfHourDao.queryCount(map);
	}

	public void updateTj_hc_halfHour(Map map) {
		tj_hc_halfHourDao.updateTj_hc_halfHour(map);
	}
	
	
}
