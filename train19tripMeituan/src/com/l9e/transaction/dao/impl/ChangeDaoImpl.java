package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.ChangeDao;
import com.l9e.transaction.vo.ChangeInfo;
import com.l9e.transaction.vo.ChangeLog;
import com.l9e.transaction.vo.ChangePassengerInfo;
@Repository("changeDao")
public class ChangeDaoImpl extends BaseDao implements ChangeDao{

	@Override
	public void addChangeLog(ChangeLog log) {
		this.getSqlMapClientTemplate().insert("change.insertChangeLog", log);
	}

	@Override
	public void insertChangeCp(ChangePassengerInfo passenger) {
		 this.getSqlMapClientTemplate().insert("change.insertChangePassenger", passenger);
	}

	@Override
	public int insertChangeInfo(ChangeInfo change) {
		return (Integer)this.getSqlMapClientTemplate().insert("change.insertChangeInfo", change);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChangePassengerInfo> selectChangeCp(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().queryForList("change.selectChangeCp", param);
	}

	@Override
	public ChangeInfo selectChangeInfo(Map<String, Object> param) {
		return (ChangeInfo) this.getSqlMapClientTemplate().queryForObject("change.selectChangeInfoByParam", param);

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ChangeInfo> selectNoticeChangeInfo(String merchantId) {
		return this.getSqlMapClientTemplate().queryForList("change.selectNotifyList", merchantId);
	}

	@Override
	public int updateChangeCp(ChangePassengerInfo passenger) {
		return this.getSqlMapClientTemplate().update("change.updateChangeCp", passenger);

	}

	@Override
	public int updateChangeInfo(ChangeInfo change) {
		return this.getSqlMapClientTemplate().update("change.updateChangeInfo", change);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> queryChangeCpInfo(Map<String, Object> param) {
		return (Map<String, Object>) this.getSqlMapClientTemplate().queryForObject("change.queryChangeCpInfo", param);
	}

}
