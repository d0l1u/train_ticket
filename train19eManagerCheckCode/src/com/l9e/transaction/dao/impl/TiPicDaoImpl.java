package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TjPicDao;
import com.l9e.transaction.vo.CodeRateVo;

@Repository("tjPicDao")
public class TiPicDaoImpl extends BaseDao implements TjPicDao {

	public void addTjPic(Map<String, Object> addMap) {
		this.getSqlMapClientTemplate().insert("tjPic.addTjPic", addMap);
	}

	public String queryChannel(String optRen) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tjPic.queryChannel", optRen);
	}

	public List<String> queryDate_List() {
		return this.getSqlMapClientTemplate().queryForList("tjPic.queryDate_List");
	}

	public List<Map<String, String>> queryOptrenAndPiccount(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("tjPic.queryOptrenAndPiccount", createTime);
	}

	public Integer queryPicSuccess(Map<String, String> queryMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("tjPic.queryPicSuccess", queryMap);
	}

	public int queryTable_Count() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("tjPic.queryTable_Count");
	}

	public Integer queryPicFail(Map<String, String> queryMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("tjPic.queryPicFail", queryMap);
	}

	public String queryOptname(String optRen) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tjPic.queryOptname",optRen);
	}

	public Integer queryPicUnknown(Map<String, String> queryMap) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("tjPic.queryPicUnknown", queryMap);
	}

	@Override
	public void clearRh_picture() {
		this.getSqlMapClientTemplate().delete("tjPic.clearRh_picture");
	}

	@Override
	public int queryRhPicHourTable_Count() {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("tjPic.queryRhPicHourTable_Count");
	}

	@Override
	public void addToTj_rhPic_hour(Map<String, String> pramaMap) {
		this.getSqlMapClientTemplate().insert("tjPic.addToTj_rhPic_hour",pramaMap);
	}

	@Override
	public List<Map<String, String>> queryRhPicHour(String createTime) {
		return this.getSqlMapClientTemplate().queryForList("tjPic.queryRhPicHour", createTime);
	}

	@Override
	public String queryRhPicHourCodeFail(Map<String, String> pramaMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tjPic.queryRhPicHourCodeFail",pramaMap);
	}

	@Override
	public String queryRhPicHourCodeSuccess(Map<String, String> pramaMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tjPic.queryRhPicHourCodeSuccess",pramaMap);
	}

	@Override
	public String queryRhPicHourCodeUnknown(Map<String, String> pramaMap) {
		return (String)this.getSqlMapClientTemplate().queryForObject("tjPic.queryRhPicHourCodeUnknown",pramaMap);
	}

	@Override
	public Map<String, Object> queryNullCodeCount(String createTime) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("tjPic.queryNullCodeCount", createTime);
	}

	@Override
	public Integer queryPicNotOpt(Map<String, String> queryMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjPic.queryPicNotOpt", queryMap);
	}

	@Override
	public Integer queryNullAndNotoptCodeCount(String createTime) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjPic.queryNullAndNotoptCodeCount", createTime);
	}

	@Override
	public Integer queryNullAndUnknownCodeCount(String createTime) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjPic.queryNullAndUnknownCodeCount", createTime);
	}

	@Override
	public int queryTodayCount(Map<String, Object> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjPic.queryTodayCount", map);
	}

	@Override
	public void updateTjPic(Map<String, Object> updateMap) {
		this.getSqlMapClientTemplate().update("tjPic.updateTjPic", updateMap);
	}

	@Override
	public int queryTodayHourCount(Map<String, String> map) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("tjPic.queryTodayHourCount", map);
	}

	@Override
	public void updateToTj_rhPic_hour(Map<String, String> updateMap) {
		this.getSqlMapClientTemplate().update("tjPic.updateToTj_rhPic_hour", updateMap);
	}

	@Override
	public List<CodeRateVo> queryCodeUserList(Map<String, String> param) {
		return this.getSqlMapClientTemplate().queryForList("tjPic.queryCodeUserList", param);
	}

	@Override
	public void addTotj_code_rate(Map<String, String> map) {
		this.getSqlMapClientTemplate().insert("tjPic.addTotj_code_rate", map);
	}

	@Override
	public String queryCodeFail(Map<String, String> map) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tjPic.queryCodeFail", map);
	}

	@Override
	public int queryCodeHourCount(Map<String, String> map) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject("tjPic.queryCodeHourCount", map);
	}

	@Override
	public String queryCodeOver(Map<String, String> map) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tjPic.queryCodeOver", map);
	}

	@Override
	public String queryCodeSuccess(Map<String, String> map) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tjPic.queryCodeSuccess", map);
	}

	@Override
	public void updateTotj_code_rate(Map<String, String> map) {
		this.getSqlMapClientTemplate().update("tjPic.updateTotj_code_rate", map);
	}

	@Override
	public String queryCodeCount(Map<String, String> map) {
		return (String) this.getSqlMapClientTemplate().queryForObject("tjPic.queryCodeCount", map);
	}
	
}
