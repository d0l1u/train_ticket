package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.Tj_hc_halfHourDao;
@Repository("Tj_hc_halfHourDao")
public class Tj_hc_halfHourDaoImpl extends BaseDao implements Tj_hc_halfHourDao {

	public void addToTj_hc_halfHour(Map<String, Object> map) {
		this.getSqlMapClientTemplate().insert("tj_hc_halfHour.addToTj_Hc_orderInfo",map);
	}

	public List<Map<String, String>> queryDayTimeAfter(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("tj_hc_halfHour.queryDayTimeAfter", createTime);
	}

	public List<Map<String, String>> queryDayTimeBefore(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("tj_hc_halfHour.queryDayTimeBefore", createTime);
	}

	public int queryTable_Count() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_hc_halfHour.queryTable_Count");
	}

	public List<String> queryDate_List() {
		return this.getSqlMapClientTemplate().queryForList("tj_hc_halfHour.queryDate_List");
	}

	public int queryCount(Map map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tj_hc_halfHour.queryCount", map);
	}

	public void updateTj_hc_halfHour(Map map) {
		this.getSqlMapClientTemplate().update("tj_hc_halfHour.updateTj_hc_halfHour", map);
	}

}
