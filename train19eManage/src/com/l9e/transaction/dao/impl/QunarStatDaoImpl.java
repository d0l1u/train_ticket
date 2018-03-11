package com.l9e.transaction.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.QunarStatDao;
@Repository("qunarStatDao")
public class QunarStatDaoImpl extends BaseDao implements QunarStatDao {

	//统计全部订单信息
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getStatInfo() {
		return this.getSqlMapClientTemplate().queryForList("qunarstat.querystatinfo");
	}
	
	//统计某一时间段内的天数
	public int getStatInfoCount(HashMap<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("qunarstat.querystatcount", map);
	}
	
	//统计某一时间段内的每天订单信息列表
	@SuppressWarnings("unchecked")
	public List<HashMap<String, String>> getDaysStatInfo(
			HashMap<String, Object> map) {
		return this.getSqlMapClientTemplate().queryForList("qunarstat.querydaystatinfo", map);
	}

	//总体小时报表
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAllHour() {
		return this.getSqlMapClientTemplate().queryForList("qunarstat.queryAllHour");
	}

	//查询日小时报表
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryDateTimeDetail(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("qunarstat.queryDateTimeDetail", create_time);
	}

	//15日内交易报表
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryPictureLineParam() {
		return this.getSqlMapClientTemplate().queryForList("qunarstat.queryPictureLineParam");
	}

	//当日小时报表
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryThisDayHour(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("qunarstat.queryThisDayHour", create_time);
	}
}
