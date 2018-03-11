package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AllComplainDao;
import com.l9e.transaction.vo.AllComplainVo;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.ComplainVo;
@Repository("allComplainDao")
public class AllComplainDaoImpl extends BaseDao implements AllComplainDao {
	@SuppressWarnings("unchecked")
	public int queryComplainListCount(Map<String, Object> paramMap) {
		return getTotalRows("allComplain.queryComplainListCount", paramMap);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryComplainList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("allComplain.queryComplainList",paramMap);
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> queryComplainParticularInfo(String complain_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("allComplain.queryComplainParticularInfo", complain_id);
	}

	public void updateComplainInfo(AllComplainVo complain) {
		this.getSqlMapClientTemplate().update("allComplain.updateComplainParticularInfo",complain);
	}
	@SuppressWarnings("unchecked")
	public void deleteComplain(String complain_id) {
		this.getSqlMapClientTemplate().delete("allComplain.deleteComplain",complain_id) ;
		
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList("allComplain.getProvince");
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList("allComplain.getCity", provinceid);
	}
	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList("allComplain.getArea", cityid);
	}
	@SuppressWarnings("unchecked")
	public List<Map<String,String>> queryComplainStatCount() {
		return this.getSqlMapClientTemplate().queryForList("allComplain.queryComplainStatCount");
	}
	@SuppressWarnings("unchecked")
	public Map<String, String> querySupervise_nameToArea_no(String string) {
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("book.querySupervise_nameToArea_no",string);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> queryHistroyByComplainId(String complainId) {
		return this.getSqlMapClientTemplate().queryForList("allComplain.queryHistroyByComplainId", complainId);
	}

	@Override
	public void addComplainHistoryInfo(AllComplainVo complain) {
		this.getSqlMapClientTemplate().insert("allComplain.addComplainHistoryInfo",complain);		
		
	}
	@Override
	public void insertLog(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("allComplain.insertLog", map);
	}

}
