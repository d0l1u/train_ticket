package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.OrderInfoToOrder_provinceDao;
@Repository("orderInfoToOrder_provinceDao")
public class OrderInfoToOrder_provinceDaoImpl extends BaseDao implements OrderInfoToOrder_provinceDao{  


	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryProvinceCount(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("orderInfoToOrder_province.queryProvinceCount",paramMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, Object> queryProvinceTicket(Map<String, String> query_map) {
		return (Map<String, Object>)this.getSqlMapClientTemplate().queryForObject("orderInfoToOrder_province.queryProvinceTicket",query_map);
	}

	@SuppressWarnings("unchecked")
	public List<String> queryEstateCount(String area_no) {
		return this.getSqlMapClientTemplate().queryForList("orderInfoToOrder_province.queryEstateCount",area_no);
	}

	public int queryProvinceActiveAgent(Map<String, String> query_map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToOrder_province.queryProvinceActiveAgent",query_map);
	}

	@SuppressWarnings("unchecked")
	public void addOrder_province(Map insert_Map) {
		this.getSqlMapClientTemplate().insert("orderInfoToOrder_province.addOrder_province",insert_Map);
	}

	public int queryHc_statInfo_provinceCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToOrder_province.queryHc_statInfo_provinceCount");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryDate() {
		return this.getSqlMapClientTemplate().queryForList("orderInfoToOrder_province.queryDate");
	}

	public double query_Province_Bx_Money_Sum(Map<String, String> query_map) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("orderInfoToOrder_province.query_Province_Bx_Money_Sum",query_map);
	}

	public int query_Province_Bx_count(Map<String, String> query_map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("orderInfoToOrder_province.query_Province_Bx_count",query_map);
	}

	public double query_Province_Succeed_money(Map<String, String> query_map) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("orderInfoToOrder_province.query_Province_Succeed_money",query_map);
	}

	public double query_Province_defeated_money(Map<String, String> query_map) {
		return (Double)this.getSqlMapClientTemplate().queryForObject("orderInfoToOrder_province.query_Province_defeated_money",query_map);
	}

	@SuppressWarnings("unchecked")
	public List<String> query_Province_Succeed_count(
			Map<String, String> query_map) {
		return this.getSqlMapClientTemplate().queryForList("orderInfoToOrder_province.query_Province_Succeed_count",query_map);
	}

	@Override
	public long query_Province_Last_mount_count(Map<String, String> query_map) {
		return (Long)this.getSqlMapClientTemplate().queryForObject("orderInfoToOrder_province.query_Province_Last_mount_count",query_map);
	}

	@Override
	public long query_Province_Mount_count(Map<String, String> query_map) {
		return (Long)this.getSqlMapClientTemplate().queryForObject("orderInfoToOrder_province.query_Province_Mount_count",query_map);
	}

}
