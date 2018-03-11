package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.AppComplainDao;
import com.l9e.transaction.vo.AreaVo;
import com.l9e.transaction.vo.ComplainVo;

@Repository("appComplainDao")
public class AppComplainDaoImpl extends BaseDao implements AppComplainDao {
	@SuppressWarnings("unchecked")
	public int queryComplainListCount(Map<String, Object> paramMap) {
		return getTotalRows("appComplain.queryComplainListCount", paramMap);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryComplainList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList(
				"appComplain.queryComplainList", paramMap);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryComplainParticularInfo(String complain_id) {
		return (Map<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("appComplain.queryComplainParticularInfo",
						complain_id);
	}

	public void updateComplainInfo(ComplainVo complain) {
		this.getSqlMapClientTemplate().update(
				"appComplain.updateComplainParticularInfo", complain);
	}

	@SuppressWarnings("unchecked")
	public void deleteComplain(String complain_id) {
		this.getSqlMapClientTemplate().delete("appComplain.deleteComplain",
				complain_id);

	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getProvince() {
		return this.getSqlMapClientTemplate().queryForList(
				"appComplain.getProvince");
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getCity(String provinceid) {
		return this.getSqlMapClientTemplate().queryForList(
				"appComplain.getCity", provinceid);
	}

	@SuppressWarnings("unchecked")
	public List<AreaVo> getArea(String cityid) {
		return this.getSqlMapClientTemplate().queryForList(
				"appComplain.getArea", cityid);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryComplainStatCount() {
		return this.getSqlMapClientTemplate().queryForList(
				"appComplain.queryComplainStatCount");
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> querySupervise_nameToArea_no(String string) {
		return (Map<String, String>) this.getSqlMapClientTemplate()
				.queryForObject("book.querySupervise_nameToArea_no", string);
	}

}
