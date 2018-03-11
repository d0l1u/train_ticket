package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ChangeDao;
import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLogVO;
import com.l9e.transaction.vo.ChangePassengerInfo;
@Repository("changeDao")
public class ChangeDaoImpl extends BaseDao implements ChangeDao{
	private static final String NAMESPACE="change.";
	@Override
	public ChangeInfo selectChangeInfoByReqtoken(String reqtoken) {
		return (ChangeInfo)this.getSqlMapClientTemplate().queryForObject(NAMESPACE+"selectChangeInfoByReqtoken", reqtoken);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ChangePassengerInfo> selectChangeCp(Map <String,Object>map) {
		return this.getSqlMapClientTemplate().queryForList(NAMESPACE+"selectChangeCp", map);
	}
	@Override
	public int insertChangeInfo(ChangeInfo changeInfo) {
		return (Integer)this.getSqlMapClientTemplate().insert(NAMESPACE+"addChangeInfo", changeInfo);
	}
	@Override
	public void insertChangeCp(ChangePassengerInfo passenger) {
		 this.getSqlMapClientTemplate().insert(NAMESPACE+"addChangePassenger", passenger);
	}
	@Override
	public void insertChangeLog(ChangeLogVO log) {
		this.getSqlMapClientTemplate().insert(NAMESPACE+"addChangeLog", log);//addChangeLog
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<ChangeInfo> selectNoticeChangeInfo(String merchantId) {
		return this.getSqlMapClientTemplate().queryForList(NAMESPACE+"selectNotifyList",merchantId);
	}
	@Override
	public int updateChangeInfo(ChangeInfo changeInfo) {
		return this.getSqlMapClientTemplate().update(NAMESPACE+"updateChangeInfo", changeInfo);
	}
	@Override
	public int updateChangeCp(ChangePassengerInfo changePassengerInfo) {
		
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
