package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.vo.ExternalProductVo;

@Repository("commonDao")
public class CommonDaoImpl extends BaseDao implements CommonDao {

	@Override
	@SuppressWarnings("unchecked")
	public List<ExternalProductVo> queryForeignProductInfo(
			ExternalProductVo product) {
		return this.getSqlMapClientTemplate().queryForList("common.queryForeignProductInfo", product);
	}
	
	public String queryInterfaceChannel() {
		return (String) this.getSqlMapClientTemplate().queryForObject("common.queryInterfaceChannel");
	}

	@SuppressWarnings("unchecked")
	public List<String> querySysInterfaceUrl() {
		return this.getSqlMapClientTemplate().queryForList("common.querySysInterfaceUrl");
	}
	
	@SuppressWarnings("unchecked")
	public List<String> querySysSettingValue(Map<String, String> paramMap) {
		return this.getSqlMapClientTemplate().queryForList("common.querySysSettingValue", paramMap);
	}
	
	public String querySysSettingByKey(String key) {
		return (String)this.getSqlMapClientTemplate().queryForObject("common.querySysSettingByKey", key);
	}

	@Override
	public Map<String, String> queryMerchantInfo(String merchant_id) {
		return (Map<String,String>)this.getSqlMapClientTemplate().queryForObject("common.queryMerchantInfo", merchant_id);
	}

	@Override
	public void updateMerchantStatus(Map<String, String> paramMap) {
		 this.getSqlMapClientTemplate().update("common.updateMerchantStatus", paramMap);
	}

	@Override
	public String getTrainSysSettingValue(String key) {
		return (String)this.getSqlMapClientTemplate().queryForObject("common.getTrainSysSettingValue", key);
	}

	@Override
	public void updateMerchantVerifyNum(String merchantId) {
		 this.getSqlMapClientTemplate().update("common.updateMerchantVerifyNum", merchantId);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> queryCheciPrice(Map<String, String> paramMap) {
		return  (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("common.queryCheciPrice", paramMap);
	}
}
