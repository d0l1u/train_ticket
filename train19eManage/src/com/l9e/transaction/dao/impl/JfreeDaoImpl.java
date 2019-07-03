package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.JfreeDao;

@Repository("jfreeDao")
public class JfreeDaoImpl extends BaseDao implements JfreeDao{

	public int queryActiveUser(String create_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("jfree.queryActiveUser",create_time);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryPictureLineParam() {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryPictureLineParam");
	}
//	@SuppressWarnings("unchecked")
//	public List<Map<String, String>> queryDateTimeDetail(String create_time) {
//		return this.getSqlMapClientTemplate().queryForList("jfree.queryDateTimeDetail",create_time);
//	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryQunar15DayPic() {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryQunar15DayPic");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryThisDayHour(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryThisDayHour",create_time);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryThisDayHourQunar(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryThisDayHourQunar",create_time);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryThisDayHour19pay(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryThisDayHour19pay",create_time);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryThisDayHourCmpay(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryThisDayHourCmpay",create_time);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> showProvinceSellChart(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("jfree.showProvinceSellChart",query_Map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryDateTimeBefore(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryDateTimeBefore", create_time);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryDateTimeAfter(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryDateTimeAfter", create_time);
	}

	public List<Map<String, String>> query15DaysActive(String province_id) {
		return this.getSqlMapClientTemplate().queryForList("jfree.query15DaysActive", province_id);
	}


	public List<Map<String, String>> queryDayTimeBefore(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryDayTimeBefore", createTime);
	}

	@Override
	public List<Map<String, String>> queryThisDayHourApp(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryThisDayHourApp", createTime);
	}

	@Override
	public List<Map<String, String>> queryThisDayHourCBB(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryThisDayHourCBB", createTime);
	}

	@Override
	public List<Map<String, String>> queryThisDayHourWeixin(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryThisDayHourWeixin", createTime);
	}

	@Override
	public List<Map<String, String>> queryOutTicketSbl(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryOutTicketSbl", createTime);
	}

	@Override
	public List<Map<String, String>> queryThisDayHourExt(Map<String, String> map) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryThisDayHourExt", map);
	}

	@Override
	//查询本省十五日销售统计
	public List<Map<String, String>> query15DaysActiveInfo(String province_id) {
		return this.getSqlMapClientTemplate().queryForList("jfree.query15DaysActiveInfo", province_id);
	}

	@Override
	public List<Map<String, String>> queryThisDayHourElong(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("jfree.queryThisDayHourElong", createTime);
	}

}
