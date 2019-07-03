package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ElongChangeDao;
import com.l9e.transaction.vo.ElongChangeInfo;
import com.l9e.transaction.vo.ElongChangeLogVO;
import com.l9e.transaction.vo.ElongChangePassengerInfo;
@Repository("elongChangeDao")
public class ElongChangeDaoImpl  extends BaseDao implements ElongChangeDao {

	private static final String NAMESPACE="elongChange.";
	@Override
	public ElongChangeInfo selectChangeInfoByReqtoken(String reqtoken) {
		return (ElongChangeInfo)this.getSqlMapClientTemplate().queryForObject(NAMESPACE+"selectChangeInfoByReqtoken", reqtoken);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ElongChangePassengerInfo> selectChangeCp(Map <String,Object>map) {
		return this.getSqlMapClientTemplate().queryForList(NAMESPACE+"selectChangeCp", map);
	}
	@Override
	public int insertChangeInfo(ElongChangeInfo changeInfo) {
		return (Integer)this.getSqlMapClientTemplate().insert(NAMESPACE+"addChangeInfo", changeInfo);
	}
	@Override
	public void insertChangeCp(ElongChangePassengerInfo passenger) {
		 this.getSqlMapClientTemplate().insert(NAMESPACE+"addChangePassenger", passenger);
	}
	@Override
	public void insertChangeLog(ElongChangeLogVO log) {
		this.getSqlMapClientTemplate().insert(NAMESPACE+"addChangeLog", log);//addChangeLog
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ElongChangeInfo> selectNoticeChangeInfo(String merchantId) {
		return this.getSqlMapClientTemplate().queryForList(NAMESPACE+"selectNotifyList",merchantId);
	}
	@Override
	public int updateChangeInfo(ElongChangeInfo changeInfo) {
		return this.getSqlMapClientTemplate().update(NAMESPACE+"updateChangeInfo", changeInfo);
	}
	@Override
	public int updateChangeCp(ElongChangePassengerInfo changePassengerInfo) {
		
		return this.getSqlMapClientTemplate().update(NAMESPACE+"updateChangeCp", changePassengerInfo);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getCpListByOrderId(String orderId) {
		return this.getSqlMapClientTemplate().queryForList(NAMESPACE+"getCpListByOrderId",orderId);
	}
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryChangeCpInfo(Map<String, Object> param) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject(NAMESPACE+"queryChangeCpInfo", param);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Map<String, String> querySinfo(Map<String, String> map) {
		return (Map<String, String>) this.getSqlMapClientTemplate().queryForObject(NAMESPACE+"querySinfo", map);
	}



}
