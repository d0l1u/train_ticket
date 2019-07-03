package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ExtSettingDao;
import com.l9e.transaction.vo.ExtSettingVo;
@Repository("extSettingDao")
public class ExtSettingDaoImpl extends BaseDao implements ExtSettingDao {

	@Override
	public List<ExtSettingVo> queryExtSettingList(Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("extSetting.queryExtSettingList", paramMap);
	}

	@Override
	public int queryExtSettingListCount(Map<String, Object> paramMap) {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("extSetting.queryExtSettingListCount", paramMap);
	}

	@Override
	public void updateMerchantStatus(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("extSetting.updateMerchantStatus", paramMap);
	}

	@Override
	public void addMarchantInfo(Map<String, Object> mapAdd) {
		this.getSqlMapClientTemplate().insert("extSetting.addMarchantInfo", mapAdd);
	}

	@Override
	public String queryMarchantId(String merchantId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("extSetting.queryMarchantId", merchantId);
	}

	@Override
	public ExtSettingVo queryMerchantInfo(String merchantId) {
		return (ExtSettingVo) this.getSqlMapClientTemplate().queryForObject("extSetting.queryMerchantInfo", merchantId);
	}

	@Override
	public void updateMarchantInfo(Map<String, Object> paramMap) {
		this.getSqlMapClientTemplate().update("extSetting.updateMarchantInfo", paramMap);
	}

	@Override
	public void addMarchantLog(Map<String, String> logMap) {
		this.getSqlMapClientTemplate().insert("extSetting.addMarchantLog", logMap);
	}

	@Override
	public List<Map<String, Object>> queryMarchantLogList(
			Map<String, Object> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("extSetting.queryMarchantLogList", paramMap);
	}

	@Override
	public int queryMarchantLogListCount() {
		return (Integer)this.getSqlMapClientTemplate().queryForObject("extSetting.queryMarchantLogListCount");
	}

}
