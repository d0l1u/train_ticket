package com.l9e.transaction.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.l9e.common.BaseDao;
import com.l9e.transaction.dao.TuniuChangeDao;
import com.l9e.transaction.vo.TuniuChangeInfo;
import com.l9e.transaction.vo.TuniuChangeLogVO;
import com.l9e.transaction.vo.TuniuChangePassengerInfo;
@Repository("tuniuChangeDao")
public class TuniuChangeDaoImpl extends BaseDao implements TuniuChangeDao{

	@Override
	public int insertChangeInfo(TuniuChangeInfo change) {
		return (Integer)this.getSqlMapClientTemplate().insert("change.insertChangeInfo", change);
	}
	@Override
	public void insertChangeCp(TuniuChangePassengerInfo passenger) {
		this.getSqlMapClientTemplate().insert("change.insertChangePassenger", passenger);
	}
	@Override
	public TuniuChangeInfo selectChangeInfo(Map<String, Object> param) {
		return (TuniuChangeInfo) this.getSqlMapClientTemplate().queryForObject("change.selectChangeInfoByParam", param);
	}

	@Override
	public int updateChangeInfo(TuniuChangeInfo change) {
		return this.getSqlMapClientTemplate().update("change.updateChangeInfo", change);
	}
	@Override
	public void addChangeLog(TuniuChangeLogVO log) {
		this.getSqlMapClientTemplate().insert("change.insertChangeLog", log);

	}
	@SuppressWarnings("unchecked")
	@Override
	public List<TuniuChangeInfo> selectNoticeChangeInfo(String merchantId) {
		return this.getSqlMapClientTemplate().queryForList("change.selectNotifyList", merchantId);
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<TuniuChangePassengerInfo> selectChangeCp(Map<String, Object> param) {
		return this.getSqlMapClientTemplate().queryForList("change.selectChangeCp", param);
	}
	@Override
	public int updateChangeCp(TuniuChangePassengerInfo passenger) {
		return this.getSqlMapClientTemplate().update("change.updateChangeCp", passenger);
	}

}
