package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderStatDao;
@Repository("orderStatDao")
public class OrderStatDaoImpl extends BaseDao implements OrderStatDao{

	public int queryOrderStatListCount(Map<String, Object> paramMap) {
		return getTotalRows("orderStat.queryOrderStatListCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryOrderStatList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderStat.queryOrderStatList",paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryPictureLineParam() {
		return this.getSqlMapClientTemplate().queryForList("orderStat.queryPictureLineParam");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryAllHour() {
		return this.getSqlMapClientTemplate().queryForList("orderStat.queryAllHour");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryThisDayHour(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("orderStat.queryThisDayHour",create_time);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryDateTimeDetail(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("orderStat.queryDateTimeDetail",create_time);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryProvinceCount(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderStat.queryProvinceCount",paramMap);
	}

	public int queryProvinceCountPagein(Map<String, Object> paramMap) {
		return getTotalRows("orderStat.queryProvinceCountPagein",paramMap);
	}

	public int queryProvinceTicket(Map<String, String> ticket_map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderStat.queryProvinceTicket",ticket_map);
	}

	@SuppressWarnings("unchecked")
	public List<String> queryEstateCount(String area_no) {
		return this.getSqlMapClientTemplate().queryForList("orderStat.queryEstateCount",area_no);
	}

	public int queryProvinceActiveAgent(Map<String, String> query_map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderStat.queryProvinceActiveAgent",query_map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> showProvinceSellChart(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("orderStat.showProvinceSellChart",query_Map);
	}

	public int queryActiveUser(String create_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderStat.queryActiveUser",create_time);
	}

	@SuppressWarnings("unchecked")
	public List<String> queryJoinList(String create_time) {
		return this.getSqlMapClientTemplate().queryForList("orderStat.queryJoinList",create_time);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHc_statInfo_provinceList(
			String create_time) {
		return this.getSqlMapClientTemplate().queryForList("orderStat.queryHc_statInfo_provinceList",create_time);
	}

	@SuppressWarnings("unchecked")
	public List<String> queryOrderCount(Map<String, String> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("orderStat.queryOrderCount",query_Map);
	}

	public int queryTicketCount(String area_no) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderStat.queryTicketCount",area_no);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> querySupervise_nameToArea_no(String area_name) {
		return (Map<String, String>)this.getSqlMapClientTemplate().queryForObject("orderStat.querySupervise_nameToArea_no",area_name);
	}

	public int querySuperviseAreaCount(Map<String, Object> paramMap) {
		return this.getTotalRows("orderStat.querySuperviseAreaCount", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querySuperviseAreaList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderStat.querySuperviseAreaList",paramMap);
	}

}
