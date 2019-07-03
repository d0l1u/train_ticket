package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.HcStatDao;
import org.springframework.stereotype.Repository;


@Repository("hcStatDao")
public class HcStatDaoImpl extends BaseDao implements HcStatDao{

	public int queryHcStatCount(Map<String, Object> query_Map) {
		return this.getTotalRows("hcStat.queryHcStatCount", query_Map);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHcStatList(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("hcStat.queryHcStatList",query_Map);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryHcStatExcel(
			Map<String, Object> query_Map) {
		return this.getSqlMapClientTemplate().queryForList("hcStat.queryHcStatExcel",query_Map);
	}
	
	public int querytongjiList(
			Map<String, Object> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.querytongjiList",query_Map);
	}
	public int querytongjiListQunar(
			Map<String, Object> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.querytongjiListQunar",query_Map);
	}
	public int querytongjiListInner(
			Map<String, Object> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.querytongjiListInner",query_Map);
	}
	public int querytongjiListExt(
			Map<String, Object> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.querytongjiListExt",query_Map);
	}

	public int queryActiveUser(String order_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.queryActiveUser",order_time);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryPictureLineParam() {
		return this.getSqlMapClientTemplate().queryForList("hcStat.queryPictureLineParam");
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> queryHc_statInfo_provinceList(
			String create_time) {
		return this.getSqlMapClientTemplate().queryForList("hcStat.queryHc_statInfo_provinceList",create_time);
	}
	@SuppressWarnings("unchecked")
	public Map<String,String> queryProvinceTotal_Ticket(Map<String,String>query_Map) {
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("hcStat.queryProvinceTotal_Ticket",query_Map);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String, String> querySupervise_nameToArea_no(String area_name) {
		return (Map<String, String>)this.getSqlMapClientTemplate().queryForObject("hcStat.querySupervise_nameToArea_no",area_name);
	}
	
	public int querySuperviseAreaCount(Map<String, Object> paramMap) {
		return this.getTotalRows("hcStat.querySuperviseAreaCount", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> querySuperviseAreaList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("hcStat.querySuperviseAreaList", paramMap);
	}

	public int queryVipLose(String order_time) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.queryVipLose",order_time);
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> queryHistory() {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("hcStat.queryHistory");
	}
	@SuppressWarnings("unchecked")
	public Map<String, Object> queryYesterdayTicketMoney(String createTime) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("hcStat.queryYesterdayTicketMoney", createTime);
	}

	public int check_num(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.check_num",map);
	}
	public int success_num(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.success_num",map);
	}
	public int fail_num(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.fail_num",map);
	}
	public int order_num_elong(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.order_num_elong",map);
	}
	
	public int order_num_qunar(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.order_num_qunar",map);
	}

	@Override
	public int add_user_num(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.add_user_num",map);
	}


	@Override
	public int regist_num(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.regist_num",map);
	}

	
	public int querytongjiListOrder(
			Map<String, Object> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.querytongjiListOrder",query_Map);
	}
	public int querytongjiListQunarOrder(
			Map<String, Object> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.querytongjiListQunarOrder",query_Map);
	}
	public int querytongjiListInnerOrder(
			Map<String, Object> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.querytongjiListInnerOrder",query_Map);
	}
	public int querytongjiListExtOrder(
			Map<String, Object> query_Map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.querytongjiListExtOrder",query_Map);
	}

	//在此新增查询同程和美团订单数接口 
	@Override
	public int order_num_meituan(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.order_num_meituan",map);
	}

	@Override
	public int order_num_tongcheng(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.order_num_tongcheng",map);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> queryTjCode(String date){
		return this.getSqlMapClientTemplate().queryForList("hcStat.queryTjCode", date);
	}

	@Override
	public int order_num_gt(Map<String, Object> map) {
		// TODO Auto-generated method stub
		return (Integer)this.getSqlMapClientTemplate().queryForObject("hcStat.order_num_gt", map);
	}
	
	

}
