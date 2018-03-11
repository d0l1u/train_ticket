package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ComplainDao;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.ComplainVo;

@Repository("complainDao")
public class ComplainDaoImpl extends BaseDao
							 implements ComplainDao{
	@SuppressWarnings("unchecked")
	public int queryComplainListCount(Map<String, Object> paramMap) {
		return getTotalRows("complain.queryComplainListCount", paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryComplainList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("complain.queryComplainList",paramMap);
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> queryComplainParticularInfo(String complain_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("complain.queryComplainParticularInfo", complain_id);
	}

	public void updateComplainInfo(ComplainVo complain) {
		this.getSqlMapClientTemplate().update("complain.updateComplainParticularInfo",complain);
	}
	@SuppressWarnings("unchecked")
	public void deleteComplain(String complain_id) {
		this.getSqlMapClientTemplate().delete("complain.deleteComplain",complain_id) ;
		
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("complain.getProvince");
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList("complain.getCity", provinceid);
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList("complain.getArea", cityid);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryComplainStatCount() {
		return this.getSqlMapClientTemplate().queryForList("complain.queryComplainStatCount");
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> querySupervise_nameToArea_no(String string) {
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("book.querySupervise_nameToArea_no",string);
	}
	@SuppressWarnings("unchecked")
	@Override
	public void addComplainHistoryInfo(ComplainVo complain) {
		this.getSqlMapClientTemplate().insert("complain.addComplainHistoryInfo",complain);		
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryHistroyByComplainId(String complainId) {
		return this.getSqlMapClientTemplate().queryForList("complain.queryHistroyByComplainId", complainId);
	}




}
