package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.common.SystemConfInfo;
import com.l9e.transaction.dao.CommonDao;
import com.l9e.transaction.vo.ProductVo;
import com.l9e.transaction.vo.SuitVo;

@Repository("commonDao")
public class CommonDaoImpl extends BaseDao implements CommonDao {

	@SuppressWarnings("unchecked")
	public List<ProductVo> queryProductInfoList(ProductVo product) {
		return this.getSqlMapClientTemplate().queryForList("common.queryProductInfoList", product);
	}

	public SystemConfInfo querySysConf(String provinceId) {
		return (SystemConfInfo) this.getSqlMapClientTemplate().queryForObject("common.querySysConf", provinceId);
	}

	public String queryProvinceName(String provinceId) {
		return (String) this.getSqlMapClientTemplate().queryForObject("common.queryProvinceName", provinceId);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryNoticeList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryNoticeList");
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryNoticeInfo(String noticeId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("common.queryNoticeInfo", noticeId);
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> queryOldAreaInfo(String provinceId) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject("common.queryOldAreaInfo", provinceId);
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
	
	@SuppressWarnings("unchecked")
	public List<Map<String, String>> queryNoticeAllList() {
		return this.getSqlMapClientTemplate().queryForList("common.queryNoticeAllList");
	}

	public String querySysSettingByKey(String key) {
		return (String)this.getSqlMapClientTemplate().queryForObject("common.querySysSettingByKey", key);
	}

	//是否投诉
	public SuitVo querySuit(String agentId) {
		return (SuitVo) this.getSqlMapClientTemplate().queryForObject("common.querySuitDate", agentId);
	}
	//更新投诉
	public void updateSuit(SuitVo suit){
		this.getSqlMapClientTemplate().update("common.updateSuit", suit);
	}

}
